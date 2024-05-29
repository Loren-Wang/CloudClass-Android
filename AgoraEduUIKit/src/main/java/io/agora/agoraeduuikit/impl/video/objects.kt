package io.agora.agoraeduuikit.impl.video

data class AgoraLargeWindowInteractionPacket(val signal: AgoraLargeWindowInteractionSignal, val body: Any)

enum class AgoraLargeWindowInteractionSignal(val value: Int) {

    //开启大窗
    LargeWindowShowed(1),

    //关闭大窗
    LargeWindowClosed(2),

    //开始渲染
    LargeWindowStartRender(3),

    //停止渲染
    LargeWindowStopRender(4)
}


