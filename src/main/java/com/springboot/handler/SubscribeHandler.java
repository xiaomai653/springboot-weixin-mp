package com.springboot.handler;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaomai
 * @description 关注处理类
 * @date 2023/6/16 11:25
 */
@Component
@Slf4j
public class SubscribeHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) {
        //TODO: 如果需要对用户做存储操作，可以在这里写数据库。
        log.info("用户关注：{}", wxMessage.getFromUser());
        return WxMpXmlOutMessage.TEXT().content("感谢关注!")
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();

    }
}
