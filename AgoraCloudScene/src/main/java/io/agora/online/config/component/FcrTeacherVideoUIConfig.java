package io.agora.online.config.component;

/**
 * author : felix
 * date : 2022/7/13
 * description : 老师视频
 */
public class FcrTeacherVideoUIConfig {
    public ResetPosition resetPosition = new ResetPosition();
    public OffStage offStage = new OffStage();

    public PrivateChat privateChat = new PrivateChat();

    /**
     * 重置位置
     */
    public static class ResetPosition extends FcrBaseUIConfig {

    }

    /**
     * 下台
     */
    public static class OffStage extends FcrBaseUIConfig {

    }

    /**
     * 私聊
     */
    public static class PrivateChat extends FcrBaseUIConfig {

    }
}