package com.springboot.handler;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMusicMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaomai
 * @description 音乐消息处理类
 * @date 2023/6/13 15:53
 */
@Component
public class MusicHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) {
        // 构建音乐消息
        WxMpXmlOutMusicMessage musicMessage = WxMpXmlOutMessage.MUSIC()
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .title("音乐标题")
                .description("音乐描述")
                .musicUrl("音乐URL")
                .hqMusicUrl("高质量音乐URL")
                .thumbMediaId("缩略图媒体ID")
                .build();

        return musicMessage;
    }

}