package com.springboot.handler;

import com.springboot.service.impl.ChatServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;


/**
 * @author xiaomai
 * @description 文本消息处理类
 * @date 2023/6/13 15:12
 */
@Component
public class TextHandler implements WxMpMessageHandler {

    @Resource
    private ChatServiceImpl chatService;

    /**
     * @param wxMessage
     * @param map              上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param wxMpService
     * @param wxSessionManager
     * @description 方法描述
     * @author xiaomai
     * @date 2023/6/13 15:26
     * @retuen WxMpXmlOutMessage
     */
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        // 获取用户发送的文本消息
        String content = wxMessage.getContent();
        //TODO: 如果需要做微信消息日志存储，可以在这里进行日志存储到数据库，这里省略不写。

        String chat = chatService.chat(content);
        // 获取当前时间戳
        long currentTime = System.currentTimeMillis() / 1000;
        // 判断收到消息的时间是否超过5秒
        if (currentTime - wxMessage.getCreateTime() > 5) {
            // 如果超过5秒，则使用客服消息主动发送信息
            WxMpKefuMessage message = WxMpKefuMessage.TEXT()
                    .toUser(wxMessage.getFromUser())
                    .content(chat)
                    .build();
            wxMpService.getKefuService().sendKefuMessage(message);
            return null;
        }else {
            return WxMpXmlOutMessage.TEXT()
                    .content(chat)
                    .fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser())
                    .build();
        }
    }

}