package com.springboot.handler;

import com.springboot.entity.vo.TianXingVo;
import com.springboot.provider.TianxingProvider;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author xiaomai
 * @description 菜单事件处理类
 * @date 2023/6/13 16:25
 */
@Component
@Slf4j
public class MenuHandler implements WxMpMessageHandler {

    @Value("${tianxing.key}")
    private String tianxingKey;

    @Resource
    private TianxingProvider tianxingProvider;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService, WxSessionManager wxSessionManager) {
        // 获取菜单点击事件类型
        String eventType = wxMessage.getEvent();
        // 定义返回值
        StringBuilder stringBuilder = new StringBuilder();
        if (WxConsts.EventType.CLICK.equals(eventType)) {
            // 处理用户点击 CLICK 事件
            String eventKey = wxMessage.getEventKey();
            // 根据eventKey处理不同的菜单点击事件
            if ("V1001_TODAY_WELFARE" .equals(eventKey)) {
                // TODO: 添加处理逻辑
                stringBuilder.append("影视：茶杯狐\n").append("地址：https://cupfox.app \n").append("直接打开很有可能会给微信拦截，请复制后去浏览器打开")
                        .append("\n\n").append("gpt聊天：chat1\n").append("地址：https://chat2.jinshutuan.com \n").append("直接打开很有可能会给微信拦截，请复制后去浏览器打开")
                        .append("\n\n").append("gpt聊天：chat2\n").append("地址：https://chat.aibear.com.cn \n").append("直接打开很有可能会给微信拦截，请复制后去浏览器打开")
                        .append("\n\n").append("问：如何在公众号使用AI问答？\n").append("答：直接在输入框发送问题即可。")
                        .append("\n\n").append("如果公众号没有回复，那大概率就是没有余额了！\n");
            }
            if ("V1001_TODAY_SAYLOVE" .equals(eventKey)) {
                TianXingVo tianXingVo = tianxingProvider.querySaylove(tianxingKey);
                stringBuilder.append(tianXingVo.getNewslist().get(0).getContent());
            }
        } else if (WxConsts.EventType.VIEW.equals(eventType)) {
            // 处理用户点击 VIEW 事件
            String eventKey = wxMessage.getEventKey();
            // 根据eventKey处理不同的菜单点击事件
            if ("https://www.baidu.com/" .equals(eventKey)) {
                // TODO: 添加处理逻辑
            }
        }

        return WxMpXmlOutMessage.TEXT().content(stringBuilder.toString())
                .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .build();
    }
}
