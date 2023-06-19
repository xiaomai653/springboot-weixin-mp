package com.springboot.handler;

import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * @author xiaomai
 * @description 图片消息处理类
 * @date 2023/6/13 15:32
 */
@Component
public class ImageHandler implements WxMpMessageHandler {

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) throws WxErrorException {
        // 获取用户发送的图片消息的媒体ID
        String mediaId = wxMessage.getMediaId();
        // TODO: 可以对图片消息进行处理或其他操作，例如保存图片或调用其他接口
        // 下载媒体文件
        File mediaFile = wxMpService.getMaterialService().mediaDownload(mediaId);
        return WxMpXmlOutMessage.IMAGE().mediaId(mediaId)
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();
    }

}