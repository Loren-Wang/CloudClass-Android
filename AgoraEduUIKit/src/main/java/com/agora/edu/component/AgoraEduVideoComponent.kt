package com.agora.edu.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.*
import android.view.View.OnClickListener
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.agora.edu.component.common.AbsAgoraEduComponent
import com.agora.edu.component.common.IAgoraUIProvider
import com.agora.edu.component.helper.AgoraUIDeviceSetting
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import io.agora.agoraeducore.core.context.AgoraEduContextMediaSourceState
import io.agora.agoraeducore.core.context.AgoraEduContextMediaStreamType.Audio
import io.agora.agoraeducore.core.context.AgoraEduContextMediaStreamType.Video
import io.agora.agoraeducore.core.context.AgoraEduContextSystemDevice
import io.agora.agoraeducore.core.context.AgoraEduContextSystemDevice.*
import io.agora.agoraeducore.core.context.AgoraEduContextUserInfo
import io.agora.agoraeducore.core.context.AgoraEduContextUserRole
import io.agora.agoraeducore.core.context.AgoraEduContextUserRole.Teacher
import io.agora.agoraeducore.core.internal.education.impl.Constants.AgoraLog
import io.agora.agoraeducore.core.internal.framework.utils.GsonUtil
import io.agora.agoraeducore.extensions.widgets.bean.AgoraWidgetDefaultId
import io.agora.agoraeduuikit.R
import io.agora.agoraeduuikit.databinding.AgoraEduVideoComponentBinding
import io.agora.agoraeduuikit.impl.video.AgoraEduFloatingControlWindow
import io.agora.agoraeduuikit.impl.video.IAgoraOptionListener2
import io.agora.agoraeduuikit.impl.whiteboard.bean.AgoraBoardGrantData
import io.agora.agoraeduuikit.impl.whiteboard.bean.AgoraBoardInteractionPacket
import io.agora.agoraeduuikit.impl.whiteboard.bean.AgoraBoardInteractionSignal
import io.agora.agoraeduuikit.interfaces.listeners.IAgoraUIVideoListener
import io.agora.agoraeduuikit.provider.AgoraUIUserDetailInfo
import io.agora.agoraeduuikit.util.SvgaUtils
import kotlin.math.min

/**
 * 基础视频组件
 * Basic video component
 */
class AgoraEduVideoComponent : AbsAgoraEduComponent, OnClickListener, IAgoraOptionListener2 {
    private val tag = "AgoraEduVideoComponent"

    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet) : super(context, attr)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr)

    var videoListener: IAgoraUIVideoListener? = null
    var m: AgoraEduFloatingControlWindow? = null
    var localUsrInfo: AgoraEduContextUserInfo? = null

    private val binding = AgoraEduVideoComponentBinding.inflate(
        LayoutInflater.from(context), this,
        true
    )
    private var audioVolumeSize: Pair<Int, Int>? = null

    private var userDetailInfo: AgoraUIUserDetailInfo? = null

    init {
//        binding.videoContainer.layoutParams.width = measuredWidth
//        binding.videoContainer.layoutParams.height = measuredHeight
        binding.cardView.z = 0.0f
//        binding.cardView.cardElevation = 1f
        val radius = context.resources.getDimensionPixelSize(R.dimen.agora_video_view_corner)
        binding.cardView.radius = radius.toFloat()
        binding.nameText.setShadowLayer(
            context.resources.getDimensionPixelSize(R.dimen.shadow_width).toFloat(),
            2.0f, 2.0f, context.resources.getColor(R.color.theme_text_color_black)
        )
        binding.videoContainer.setOnHierarchyChangeListener(object : ViewGroup.OnHierarchyChangeListener {
            override fun onChildViewAdded(parentView: View?, child: View?) {
                child?.let {
                    if (child is TextureView || child is SurfaceView) {
                        setTextureViewRound(child)
                    }
                }
            }

            override fun onChildViewRemoved(p0: View?, p1: View?) {
            }
        })
        binding.audioIc.isEnabled = false
        binding.videoIc.isEnabled = false
        upsertUserDetailInfo(null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(agoraUIProvider: IAgoraUIProvider) {
        super.initView(agoraUIProvider)
        localUsrInfo = eduContext?.userContext()?.getLocalUserInfo()
        binding.cardView.setOnClickListener(this)
        val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                // only teacher can trigger floatingWindow
                if (localUsrInfo?.role != AgoraEduContextUserRole.Teacher) {
                    return super.onSingleTapConfirmed(e)
                }
                m = userDetailInfo?.let {
                    AgoraEduFloatingControlWindow(it, context, this@AgoraEduVideoComponent, eduContext)
                }
                m?.show(binding.root)
                return super.onSingleTapConfirmed(e)
            }
        })
        binding.cardView.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
    }

    override fun onClick(view: View?) {
    }

    private fun setTextureViewRound(view: View) {
//        val radius: Float = view.context.resources.getDimensionPixelSize(R.dimen.agora_video_view_corner).toFloat()
        val radius: Float = view.context.resources.getDimensionPixelSize(R.dimen.corner_smaller).toFloat()
        val textureOutlineProvider = VideoTextureOutlineProvider(radius)
        view.outlineProvider = textureOutlineProvider
        view.clipToOutline = true
    }

    private fun setCameraState(info: AgoraUIUserDetailInfo?) {
        binding.videoIc.isEnabled = isVideoEnable(info)
        binding.videoIc.isSelected = isVideoOpen(info)
    }

    private fun setVideoPlaceHolder(info: AgoraUIUserDetailInfo?) {
        binding.videoContainer.visibility = GONE
        binding.notInLayout.visibility = GONE
        binding.videoOffLayout.visibility = GONE
        binding.offLineLoadingLayout.visibility = GONE
        binding.noCameraLayout.visibility = GONE
        binding.cameraDisableLayout.visibility = GONE
        if (info == null) {
            binding.notInLayout.visibility = VISIBLE
            binding.audioIc.visibility = GONE
            return
        } else {
            binding.audioIc.visibility = VISIBLE //只要人在，则显示音频图标
        }
        if (isVideoEnable(info) && isVideoOpen(info)) {
            binding.videoContainer.visibility = VISIBLE
        } else if (!isVideoEnable(info)) {
            binding.cameraDisableLayout.visibility = VISIBLE
        } else {
            binding.videoOffLayout.visibility = VISIBLE
        }
    }

    private fun setMicroState(info: AgoraUIUserDetailInfo?) {
        binding.audioIc.isEnabled = isAudioEnable(info)
        binding.audioIc.isSelected = isAudioOpen(info)
    }

    fun setNotInText(info: String?) {
        binding.notInTips.text = info
    }

    /**
     * 更新当前用户信息
     * refresh curUser Info
     * @param info
     */
    fun upsertUserDetailInfo(info: AgoraUIUserDetailInfo?) {
        AgoraLog?.e("$tag->upsertUserDetailInfo:${Gson().toJson(info)}")
        this.post {
            if (info == userDetailInfo) {
                // double check duplicate data
                AgoraLog?.i("$tag->new info is same to old, return")
                return@post
            }
            setCameraState(info)
            setMicroState(info)
            setVideoPlaceHolder(info)

            if (info != null) {
                // handle userInfo
                if (info.role == AgoraEduContextUserRole.Student) {
                    val reward = info.reward
                    if (reward > 0) {
                        binding.trophyLayout.visibility = VISIBLE
                        binding.trophyText.text = String.format(
                            context.getString(R.string.fcr_agora_video_reward),
                            min(reward, 99)
                        )
                        binding.trophyText.text = String.format(context.getString(R.string.fcr_agora_video_reward), info.reward)
                    } else {
                        binding.trophyLayout.visibility = GONE
                    }
                    binding.videoIc.visibility = GONE
                } else {
                    binding.trophyLayout.visibility = GONE
                    binding.videoIc.visibility = GONE
                }
                binding.nameText.text = info.userName
                updateGrantedStatus(info.whiteBoardGranted)
                // handle streamInfo
                val currentVideoOpen: Boolean = userDetailInfo?.let {
                    isVideoEnable(it) && isVideoOpen(it)
                } ?: false
                val newVideoOpen = isVideoEnable(info) && isVideoOpen(info)
                if (!currentVideoOpen && newVideoOpen) {
                    videoListener?.onRendererContainer(binding.videoContainer, info.streamUuid)
                } else if (currentVideoOpen && !newVideoOpen) {
                    videoListener?.onRendererContainer(null, info.streamUuid)
                } else {
                    val parent = if (newVideoOpen) binding.videoContainer else null
                    videoListener?.onRendererContainer(parent, info.streamUuid)
                }
            } else {
                binding.nameText.text = ""
                binding.trophyLayout.visibility = GONE
                binding.videoIc.visibility = GONE
                binding.boardGrantedIc.visibility = GONE
                userDetailInfo?.let {
                    videoListener?.onRendererContainer(null, it.streamUuid)
                }
            }
            this.userDetailInfo = info?.copy()
        }
    }

    /**
     * whether audio device is enable
     * */
    private fun isAudioEnable(info: AgoraUIUserDetailInfo?): Boolean {
        if (info == null) {
            return false
        }
        return info.audioSourceState == AgoraEduContextMediaSourceState.Open
    }

    /**
     * whether have permission to send video stream
     * */
    private fun isAudioOpen(info: AgoraUIUserDetailInfo?): Boolean {
        if (info == null) {
            return false
        }
        // whether have permission to send video stream
        return info.hasAudio
    }

    private fun isVideoEnable(info: AgoraUIUserDetailInfo?): Boolean {
        if (info == null) {
            return false
        }
        return info.videoSourceState == AgoraEduContextMediaSourceState.Open
    }

    private fun isVideoOpen(info: AgoraUIUserDetailInfo?): Boolean {
        if (info == null) {
            return false
        }
        // whether have permission to send video stream
        return info.hasVideo
    }

    /**
     * 更新当前用户的音频声音大小
     * refresh curUser audioVolume
     */
    @Volatile
    private var gifRunning = false
    fun updateAudioVolumeIndication(value: Int, streamUuid: String) {
        if (binding.audioIc.isEnabled && binding.audioIc.isSelected && value > 0 && !gifRunning) {
            this.post {
                Glide.with(this).asGif().skipMemoryCache(true)
                    .load(R.drawable.agora_video_ic_audio_on)
                    .placeholder(R.drawable.agora_video_ic_audio_on)
                    .listener(object : RequestListener<GifDrawable?> {
                        override fun onLoadFailed(
                            e: GlideException?, model: Any?, target: Target<GifDrawable?>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                        override fun onResourceReady(
                            resource: GifDrawable?, model: Any?, target: Target<GifDrawable?>?,
                            dataSource: DataSource?, isFirstResource: Boolean
                        ): Boolean {
                            gifRunning = true
                            resource?.setLoopCount(1)
                            resource?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                                override fun onAnimationEnd(drawable: Drawable) {
                                    binding.audioIc.setImageResource(R.drawable.agora_video_ic_audio_bg)
                                    gifRunning = false
                                }
                            })
                            return false
                        }
                    }).into(binding.audioIc)
            }
        }
    }

    private fun updateGrantedStatus(granted: Boolean) {
        binding.boardGrantedIc.post {
            binding.boardGrantedIc.visibility = if (granted) VISIBLE else INVISIBLE
        }
    }

    /**
     * 播放举手动画
     * play wave animation
     */
    fun updateWaveState(waving: Boolean) {
        val svgaUtils = SvgaUtils(context, binding.handWavingImg)
        if (waving) {
            binding.handWavingLayout.visibility = VISIBLE
            binding.handWavingImg.visibility = VISIBLE
            svgaUtils.initAnimator()
            svgaUtils.startAnimator(context?.getString(R.string.fcr_waving_hands))
        } else {
            binding.handWavingLayout.visibility = GONE
            binding.handWavingImg.visibility = GONE
        }
    }

    // implement IAgoraOptionListener2
    override fun onAudioUpdated(item: AgoraUIUserDetailInfo, enabled: Boolean) {
        AgoraLog?.d(tag, "onAudioUpdated")
        switchMedia(item, enabled, Microphone)
    }

    override fun onVideoUpdated(item: AgoraUIUserDetailInfo, enabled: Boolean) {
        AgoraLog?.d(tag, "onVideoUpdated")
        val device = if (AgoraUIDeviceSetting.isFrontCamera()) {
            CameraFront
        } else {
            CameraBack
        }
        switchMedia(item, enabled, device)
    }

    override fun onCohostUpdated(item: AgoraUIUserDetailInfo, isCoHost: Boolean) {
        AgoraLog?.d("$tag->onCohostUpdated-item:${GsonUtil.toJson(item)}, isCoHost:$isCoHost")
        if (isCoHost) {
            // unreachable
            eduContext?.userContext()?.addCoHost(item.userUuid)
        } else {
            if (item.role == Teacher) {
                // remove all cohost from stage
                eduContext?.userContext()?.removeAllCoHosts()
            } else {
                eduContext?.userContext()?.removeCoHost(item.userUuid)
            }
        }

    }

    override fun onGrantUpdated(item: AgoraUIUserDetailInfo, hasAccess: Boolean) {
        AgoraLog?.d(tag, "onGrantUpdated")
        val data = AgoraBoardGrantData(hasAccess, arrayOf(item.userUuid).toMutableList())
        val packet = AgoraBoardInteractionPacket(AgoraBoardInteractionSignal.BoardGrantDataChanged, data)
        eduContext?.widgetContext()?.sendMessageToWidget(Gson().toJson(packet), AgoraWidgetDefaultId.WhiteBoard.id)

    }

    override fun onRewardUpdated(item: AgoraUIUserDetailInfo, count: Int) {
        AgoraLog?.d(tag, "onRewardUpdated")
        eduContext?.userContext()?.rewardUsers(arrayOf(item.userUuid).toMutableList(), count)
    }

    private fun switchMedia(item: AgoraUIUserDetailInfo, enabled: Boolean, device: AgoraEduContextSystemDevice) {
        localUsrInfo?.userUuid?.let {
            if (it == item.userUuid) {
                if (enabled) {
                    eduContext?.mediaContext()?.openSystemDevice(device)
                } else {
                    eduContext?.mediaContext()?.closeSystemDevice(device)
                }
            } else {
                val streamType = if (device == Microphone) Audio else Video
                if (enabled) {
                    eduContext?.streamContext()?.publishStreams(arrayListOf(item.streamUuid), streamType)
                } else {
                    eduContext?.streamContext()?.muteStreams(arrayListOf(item.streamUuid).toMutableList(), streamType)

                }
            }
        }
    }
}

class VideoTextureOutlineProvider(private val mRadius: Float) : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(0, 0, view.width, view.height, mRadius)
    }
}