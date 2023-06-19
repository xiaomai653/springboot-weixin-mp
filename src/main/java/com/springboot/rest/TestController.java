package com.springboot.rest;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@RestController
public class TestController {

    @Autowired
    private WxMpService wxMpService;

    static Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/validate")
    public String validate(@RequestParam(name = "signature") String signature,
                           @RequestParam(name = "timestamp") String timestamp,
                           @RequestParam(name = "nonce") String nonce,
                           @RequestParam(name = "echostr") String echostr) {

        // 将三个参数按字典序排序
        String[] arr = {"123456", timestamp, nonce};
        Arrays.sort(arr);
        // 将三个参数拼接成一个字符串
        StringBuilder builder = new StringBuilder();
        for (String s : arr) {
            builder.append(s);
        }
        String str = builder.toString();
        // 对拼接后的字符串进行sha1加密
        String sha1Str = shal(str);
        // 将加密结果与signature进行比较
        if (sha1Str.equals(signature)) {
            return echostr;
        } else {
            return null;
        }
    }

    @PostMapping("/validate")
    public String handleMessage(@RequestBody String requestBody) {

        WxMpXmlMessage wxMessage = WxMpXmlMessage.fromXml(requestBody);

        // 根据消息类型进行处理
        if (wxMessage.getMsgType().equals(WxConsts.XmlMsgType.TEXT)) {
            // 处理文本消息
            String content = "您发送的消息是：" + wxMessage.getContent();
            WxMpXmlOutTextMessage response = WxMpXmlOutMessage.TEXT().content(content)
                    .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
            return response.toXml();
        } else if (wxMessage.getMsgType().equals(WxConsts.XmlMsgType.IMAGE)) {
            // 处理图片消息
            WxMpXmlOutTextMessage response = WxMpXmlOutMessage.TEXT()
                    .content("您发送了一条图片消息").fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser()).build();
            return response.toXml();
        } else if (wxMessage.getMsgType().equals(WxConsts.XmlMsgType.VOICE)) {
            // 处理语音消息
            WxMpXmlOutTextMessage response = WxMpXmlOutMessage.TEXT()
                    .content("您发送了一条语音消息").fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser()).build();
            return response.toXml();
        } else if (wxMessage.getMsgType().equals(WxConsts.XmlMsgType.VIDEO)) {
            // 处理视频消息
            WxMpXmlOutTextMessage response = WxMpXmlOutMessage.TEXT()
                    .content("您发送了一条视频消息").fromUser(wxMessage.getToUser())
                    .toUser(wxMessage.getFromUser()).build();
            return response.toXml();
        } else if (wxMessage.getMsgType().equals(WxConsts.XmlMsgType.EVENT)) {
            // 处理事件消息
            if (wxMessage.getEvent().equals(WxConsts.EventType.SUBSCRIBE)) {
                // 处理订阅事件
                WxMpXmlOutTextMessage response = WxMpXmlOutMessage.TEXT()
                        .content("感谢您的关注！").fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
                return response.toXml();
            } else if (wxMessage.getEvent().equals(WxConsts.EventType.UNSUBSCRIBE)) {
                // 处理取消订阅事件
                // 可以进行相应的业务处理

                // 返回空串表示不进行回复
                return "";
            } else if (wxMessage.getEvent().equals(WxConsts.EventType.CLICK)) {
                // 处理菜单点击事件
                String eventKey = wxMessage.getEventKey();
                WxMpXmlOutTextMessage response = WxMpXmlOutMessage.TEXT()
                        .content("点击了：" + eventKey).fromUser(wxMessage.getToUser())
                        .toUser(wxMessage.getFromUser()).build();
                // 根据eventKey进行不同的处理逻辑
                // 可以返回相应的回复消息

                // 返回空串表示不进行回复
                return response.toXml();
            }
        }
        return "";
    }

    @GetMapping("/access_token")
    public String getAccessToken() {
        try {
            String accessToken = wxMpService.getAccessToken();
            return accessToken;
        } catch (WxErrorException e) {
            // 处理异常
            logger.error(e.getMessage());
            return e.getMessage();
        }
    }

    /**
     * 字符串进行shal加密
     *
     * @param str
     * @return
     */
    public String shal(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
