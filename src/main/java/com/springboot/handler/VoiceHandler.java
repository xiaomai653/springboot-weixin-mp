package com.springboot.handler;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaomai
 * @description 语音消息处理类
 * @date 2023/6/13 15:33
 */
@Component
public class VoiceHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) {
        // 获取用户发送的语音消息的媒体ID
        String mediaId = wxMessage.getMediaId();
        // TODO: 可以对语音消息进行处理或其他操作，例如保存语音或调用其他接口
        return WxMpXmlOutMessage.VOICE().mediaId(mediaId)
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();
    }

}