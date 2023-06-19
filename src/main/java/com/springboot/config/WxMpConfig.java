package com.springboot.config;

import com.springboot.handler.*;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author xiaomai
 * @description 微信公众号核心配置
 * @date 2023/6/13 15:13
 */

@Configuration
public class WxMpConfig {
    @Value("${wechat.mp.app-id}")
    private String appId;

    @Value("${wechat.mp.app-secret}")
    private String appSecret;

    @Value("${wechat.mp.token}")
    private String token;

    @Resource
    private LogHandler logHandler;
    @Resource
    private TextHandler textHandler;
    @Resource
    private ImageHandler imageHandler;
    @Resource
    private VoiceHandler voiceHandler;
    @Resource
    private VideoHandler videoHandler;
    @Resource
    private MusicHandler musicHandler;
    @Resource
    private NewsHandler newsHandler;
    @Resource
    private MenuHandler menuHandler;
    @Resource
    private SubscribeHandler subscribeHandler;
    @Resource
    private UnsubscribeHandler unsubscribeHandler;
    @Resource
    private ScanHandler scanHandler;

    /**
     * 配置公众号基本信息
     */
    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(appId);
        config.setSecret(appSecret);
        config.setToken(token);
        // 其他配置项...
        return config;
    }

    /**
     * 配置
     */
    @Bean
    public WxMpService wxMpService(WxMpConfigStorage configStorage) {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(configStorage);
        return wxMpService;
    }


    /**
     * 消息路由，通过设置规则可以将消息交给指定的MessageHandler去处理，再经过Controller返回给微信服务器
     */
    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        // 记录所有事件的日志 （异步执行）
        router.rule().handler(logHandler).next();
        // 处理文本消息
        router.rule().async(false).msgType(WxConsts.XmlMsgType.TEXT).handler(textHandler).end();
        // 处理图片消息
        router.rule().async(false).msgType(WxConsts.XmlMsgType.IMAGE).handler(imageHandler).end();
        // 处理语音消息
        router.rule().async(false).msgType(WxConsts.XmlMsgType.VOICE).handler(voiceHandler).end();
        // 处理视频消息
        router.rule().async(false).msgType(WxConsts.XmlMsgType.VIDEO).handler(videoHandler).end();
        // 处理音乐消息
        router.rule().async(false).msgType(WxConsts.XmlMsgType.MUSIC).handler(musicHandler).end();
        // 处理图文消息
        router.rule().async(false).msgType(WxConsts.XmlMsgType.NEWS).handler(newsHandler).end();

        // 自定义菜单事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.CLICK).handler(menuHandler).end();
        // 点击菜单连接事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.VIEW).handler(menuHandler).end();
        // 关注事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(subscribeHandler).end();
        // 取消关注事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(unsubscribeHandler).end();
        // 扫码事件
        router.rule().async(false).msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SCAN).handler(scanHandler).end();
        return router;
    }

}