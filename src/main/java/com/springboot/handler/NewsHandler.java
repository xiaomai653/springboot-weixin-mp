package com.springboot.handler;

import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author xiaomai
 * @description 图文消息处理类
 * @date 2023/6/13 15:53
 */
@Component
public class NewsHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) {
        // 构建图文消息
        WxMpXmlOutNewsMessage.Item item1 = new WxMpXmlOutNewsMessage.Item();
        item1.setTitle("图文消息标题1");
        item1.setDescription("图文消息描述1");
        item1.setPicUrl("图片URL1");
        item1.setUrl("链接URL1");

        WxMpXmlOutNewsMessage.Item item2 = new WxMpXmlOutNewsMessage.Item();
        item2.setTitle("图文消息标题2");
        item2.setDescription("图文消息描述2");
        item2.setPicUrl("图片URL2");
        item2.setUrl("链接URL2");

        WxMpXmlOutNewsMessage newsMessage = WxMpXmlOutMessage.NEWS()
                .fromUser(wxMessage.getToUser())
                .toUser(wxMessage.getFromUser())
                .addArticle(item1)
                .addArticle(item2)
                .build();

        return newsMessage;
    }

}