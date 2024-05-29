package com.agora.edu.component.whiteboard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.agora.edu.component.common.AbsAgoraEduComponent
import com.agora.edu.component.common.IAgoraUIProvider
import com.agora.edu.component.helper.GsonUtils
import io.agora.agoraeducore.core.context.AgoraEduContextUserRole
import io.agora.agoraeducore.core.context.EduContextRoomInfo
import io.agora.agoraeducore.core.context.EduContextScreenShareState
import io.agora.agoraeducore.core.internal.framework.impl.handler.RoomHandler
import io.agora.agoraeducore.extensions.widgets.bean.AgoraWidgetDefaultId
import io.agora.agoraeducore.extensions.widgets.bean.AgoraWidgetDefaultId.AgoraCloudDisk
import io.agora.agoraeducore.extensions.widgets.bean.AgoraWidgetMessageObserver
import io.agora.agoraeduuikit.databinding.AgoraEduWhiteboardComponetBinding
import io.agora.agoraeduuikit.impl.whiteboard.AgoraWhiteBoardWidget
import io.agora.agoraeduuikit.impl.whiteboard.bean.AgoraBoardInteractionPacket
import io.agora.agoraeduuikit.impl.whiteboard.bean.AgoraBoardInteractionSignal
import io.agora.agoraeduuikit.provider.AgoraUIUserDetailInfo
import io.agora.agoraeduuikit.provider.UIDataProviderListenerImpl

/**
 *  白板
 */
class AgoraEduWhiteBoardComponent : AbsAgoraEduComponent {
    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet) : super(context, attr)
    constructor(context: Context, attr: AttributeSet, defStyleAttr: Int) : super(context, attr, defStyleAttr)

    private var binding: AgoraEduWhiteboardComponetBinding =
        AgoraEduWhiteboardComponetBinding.inflate(LayoutInflater.from(context), this, true)
    private var whiteboardContainer = binding.agoraEduWhiteboardContainer
    private var whiteBoardWidget: AgoraWhiteBoardWidget? = null
    var uuid: String? = null
    var videoDirection: String = "right"

    private val roomHandler = object : RoomHandler() {
        override fun onJoinRoomSuccess(roomInfo: EduContextRoomInfo) {
            super.onJoinRoomSuccess(roomInfo)
            val config = eduContext?.widgetContext()?.getWidgetConfig(AgoraWidgetDefaultId.WhiteBoard.id)
            // transfer roomContext to startClass for teacher
            // todo(cjw) acadsoc not need teacher in mobile
//            config?.extraInfo = eduContext?.roomContext()

            config?.let {
                whiteBoardWidget = eduContext?.widgetContext()?.create(it) as AgoraWhiteBoardWidget?
            }
            whiteBoardWidget?.let {
                eduContext?.widgetContext()?.addWidgetMessageObserver(it.cloudDiskMsgObserver, AgoraCloudDisk.id)
                it.uuid = uuid
                it.init(whiteboardContainer)
                it.setDirection(videoDirection)
            }
            // Check if there is a screen stream is sharing
            uiDataProvider?.notifyScreenShareDisplay()
        }
    }

    fun initView(uuid: String?, agoraUIProvider: IAgoraUIProvider) {
        this.uuid = uuid
        initView(agoraUIProvider)
    }

    fun setDirection(direction: String) {
        videoDirection = direction
    }

    override fun initView(agoraUIProvider: IAgoraUIProvider) {
        super.initView(agoraUIProvider)
        binding.agoraEduScreenShare.initView(agoraUIProvider)
        eduContext?.widgetContext()?.addWidgetMessageObserver(whiteBoardObserver, AgoraWidgetDefaultId.WhiteBoard.id)
        eduContext?.roomContext()?.addHandler(roomHandler)
        this.uiDataProvider?.addListener(object : UIDataProviderListenerImpl() {
            override fun onScreenShareStart(info: AgoraUIUserDetailInfo) {
                binding.agoraEduScreenShare.updateScreenShareState(EduContextScreenShareState.Start, info.streamUuid)
            }

            override fun onScreenShareStop(info: AgoraUIUserDetailInfo) {
                binding.agoraEduScreenShare.updateScreenShareState(EduContextScreenShareState.Stop, info.streamUuid)
            }
        })
    }

    private val whiteBoardObserver = object : AgoraWidgetMessageObserver {
        override fun onMessageReceived(msg: String, id: String) {
            val packet = GsonUtils.mGson.fromJson(msg, AgoraBoardInteractionPacket::class.java)
            when (packet.signal) {
                AgoraBoardInteractionSignal.BoardGrantDataChanged -> {
                    eduContext?.userContext()?.getLocalUserInfo()?.let { localUser ->
                        if (localUser.role == AgoraEduContextUserRole.Student) {
                            val granted = (packet.body as? ArrayList<String>)?.contains(localUser.userUuid) ?: false
                            // 可以显示白板按钮
                            uiHandler.post {
                                whiteBoardWidget?.setWhiteBoardControlView(granted)
                            }
                        }
                    }
                }
            }
        }
    }
}