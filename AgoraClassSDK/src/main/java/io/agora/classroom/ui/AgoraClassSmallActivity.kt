package io.agora.classroom.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.google.gson.Gson
import io.agora.agoraclasssdk.databinding.ActivityAgoraClassSmallBinding
import io.agora.agoraeducore.core.context.*
import io.agora.agoraeducore.core.internal.base.ToastManager
import io.agora.agoraeducore.core.internal.education.impl.Constants.AgoraLog
import io.agora.agoraeducore.core.internal.framework.impl.handler.RoomHandler
import io.agora.agoraeducore.core.internal.framework.impl.handler.StreamHandler
import io.agora.agoraeducore.core.internal.framework.proxy.RoomType
import io.agora.agoraeduuikit.R
import io.agora.agoraeduuikit.component.dialog.AgoraUICustomDialogBuilder
import io.agora.agoraeduuikit.component.toast.AgoraUIToast
import io.agora.classroom.common.AgoraEduClassActivity
import io.agora.classroom.presenter.AgoraClassVideoPresenter

/**
 * author : hefeng
 * date : 2022/1/24
 * description : 小班课（200）
 */
class AgoraClassSmallActivity : AgoraEduClassActivity() {
    private val TAG = "AgoraClassSmallActivity"
    private var agoraClassVideoPresenter: AgoraClassVideoPresenter? = null
    var localStreamInfo: AgoraEduContextStreamInfo? = null
    lateinit var binding: ActivityAgoraClassSmallBinding

    private val roomHandler = object : RoomHandler() {
        override fun onJoinRoomSuccess(roomInfo: EduContextRoomInfo) {
            super.onJoinRoomSuccess(roomInfo)
            AgoraLog?.d("$TAG->classroom ${roomInfo.roomUuid} joined success")
            setRecordProperties()
//            initSystemDevices()
        }

        override fun onJoinRoomFailure(roomInfo: EduContextRoomInfo, error: EduContextError) {
            super.onJoinRoomFailure(roomInfo, error)
            AgoraUIToast.error(context = this@AgoraClassSmallActivity, text = error.msg)
            AgoraLog?.e("$TAG->classroom ${roomInfo.roomUuid} joined fail:${Gson().toJson(error)}")
        }

        override fun onClassStateUpdated(state: AgoraEduContextClassState) {
            super.onClassStateUpdated(state)
            AgoraLog?.d("$TAG->class state updated: ${state.name}")
        }
    }

    private val streamHandler = object : StreamHandler() {
        override fun onStreamJoined(streamInfo: AgoraEduContextStreamInfo, operator: AgoraEduContextUserInfo?) {
            eduCore()?.eduContextPool()?.userContext()?.getLocalUserInfo()?.let { localUser ->
                eduCore()?.eduContextPool()?.userContext()?.getLocalUserInfo()?.let { localUser ->
                    if (localUser.userUuid == streamInfo.owner.userUuid) {
                        localStreamInfo = streamInfo
                        openSystemDevices()
                    }
                }
            }
        }

        override fun onStreamUpdated(streamInfo: AgoraEduContextStreamInfo, operator: AgoraEduContextUserInfo?) {
            super.onStreamUpdated(streamInfo, operator)
            localStreamInfo = streamInfo
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAgoraClassSmallBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 创建了教室对象
        createEduCore(object : EduContextCallback<Unit> {
            override fun onSuccess(target: Unit?) {
                // 教室资源加载完成后
                joinClassRoom()
            }

            override fun onFailure(error: EduContextError?) {
                error?.let {
                    ToastManager.showShort(it.msg)
                }
                finish()
            }
        })
    }

    private fun joinClassRoom() {
        runOnUiThread {
            eduCore()?.eduContextPool()?.let { context ->
                context.roomContext()?.addHandler(roomHandler)
                context.streamContext()?.addHandler(streamHandler)
                binding.teachAidContainer.initView(this)
                binding.agoraClassHead.initView(this)
                binding.agoraClassHead.setTitleToRight()
                binding.agoraEduWhiteboard.initView(uuid, this)
                agoraClassVideoPresenter = AgoraClassVideoPresenter(binding.agoraClassTeacherVideo, binding.agoraClassUserListVideo)
                agoraClassVideoPresenter?.initView(RoomType.SMALL_CLASS, this, uiController)
                binding.agoraEduOptions.initView(uuid, binding.root, binding.agoraEduOptionsItemContainer, this)
                launchConfig?.roleType?.let {
                    binding.agoraEduOptions.setShowOption(RoomType.SMALL_CLASS, it)
                }
            }
            join()
        }
    }
}