package com.springboot.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.kefu.result.WxMpKfList;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author xiaomai
 * @description 客服管理接口
 * @date 2023/6/15 15:30
 */
@RestController
@RequestMapping("/customer")
@Api(tags = "客服管理接口")
public class CustomerController {

    @Resource
    private WxMpService wxMpService;

    @PostMapping("/sendText")
    @ApiOperation("发送文本消息")
    public boolean sendText(@RequestParam String toUser, @RequestParam String content) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage
                .TEXT()
                .toUser(toUser)
                .content(content)
                .build();
        return wxMpService.getKefuService().sendKefuMessage(message);
    }

    @PostMapping("/sendImage")
    @ApiOperation("发送图片消息")
    public boolean sendImage(@RequestParam String toUser, @RequestParam String mediaId) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage
                .IMAGE()
                .toUser(toUser)
                .mediaId(mediaId)
                .build();
        return wxMpService.getKefuService().sendKefuMessage(message);
    }

    @PostMapping("/sendVoice")
    @ApiOperation("发送语音消息")
    public boolean sendVoice(@RequestParam String toUser, @RequestParam String mediaId) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage.VOICE()
                .toUser(toUser)
                .mediaId(mediaId)
                .build();
        return wxMpService.getKefuService().sendKefuMessage(message);
    }

    @PostMapping("/sendVideo")
    @ApiOperation("发送视频消息")
    public boolean sendVideo(@RequestParam String toUser, @RequestParam String mediaId) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage.VIDEO()
                .toUser(toUser)
                .title("TITLE")
                .mediaId(mediaId)
                .thumbMediaId("MEDIA_ID")
                .description("DESCRIPTION")
                .build();
        return wxMpService.getKefuService().sendKefuMessage(message);
    }

    @PostMapping("/sendMusic")
    @ApiOperation("发送音乐消息")
    public boolean sendMusic(@RequestParam String toUser) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage.MUSIC()
                .toUser(toUser)
                .title("音乐标题")
                .description("音乐描述")
                .musicUrl("音乐URL")
                .hqMusicUrl("高质量音乐URL")
                .thumbMediaId("缩略图媒体ID")
                .build();
        return wxMpService.getKefuService().sendKefuMessage(message);
    }

    @PostMapping("/sendNews")
    @ApiOperation("发送图文消息")
    public boolean sendNews(@RequestParam String toUser) throws WxErrorException {

        WxMpKefuMessage.WxArticle article1 = new WxMpKefuMessage.WxArticle();
        article1.setTitle("小麦的博客");
        article1.setDescription("这个我个人的博客！");
        article1.setPicUrl("https://mmbiz.qpic.cn/sz_mmbiz_jpg/z5bVicK4Bs9dr76MulzicngAyY8hAAJkibu9M4C3mO10PLPwbCsSfXD42sLgOEwQTC6bCFyw01fLctbUI9C2OV8sA/0?wx_fmt=jpeg");
        article1.setUrl("https://xiaomai653.github.io/");

        WxMpKefuMessage.WxArticle article2 = new WxMpKefuMessage.WxArticle();
        article2.setTitle("Java知音");
        article2.setDescription("SpringBoot内容聚合");
        article2.setUrl("https://mp.weixin.qq.com/s/NyCHUwcSf06LrfQBUZ69ww");

        WxMpKefuMessage message = WxMpKefuMessage.NEWS()
                .toUser(toUser)
                .addArticle(article1)
//                .addArticle(article2)
                .build();
        return wxMpService.getKefuService().sendKefuMessage(message);
    }

    @GetMapping("/list")
    @ApiOperation("客服列表")
    public WxMpKfList list() throws WxErrorException {
        WxMpKfList wxMpKfList = wxMpService.getKefuService().kfList();
        return wxMpKfList;
    }

}
