package com.springboot.rest;

import com.springboot.service.IMessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpNewsArticle;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * @author xiaomai
 * @description 消息处理接口
 * @date 2023/6/13 15:42
 */
@RestController
@RequestMapping("/wechat")
@Slf4j
@Api(value = "消息处理接口", tags = "消息处理接口")
public class MessageController {

    @Resource
    private WxMpService wxMpService;

    @Resource
    private WxMpMessageRouter wxMpMessageRouter;

    @Resource
    private IMessageService messageService;

    /**
     * 开发者接入校验
     */
    @GetMapping("/message")
    @ApiOperation("开发者接入校验")
    public String validate(@RequestParam(name = "signature") String signature,
                           @RequestParam(name = "timestamp") String timestamp,
                           @RequestParam(name = "nonce") String nonce,
                           @RequestParam(name = "echostr") String echostr) {
        if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }
        return "非法请求";
    }

    /**
     * 处理微信交互信息
     */
    @PostMapping("/message")
    @ApiOperation("处理微信交互信息")
    public String handleMessage(@RequestBody String requestBody,
                                @RequestParam("signature") String signature,
                                @RequestParam("timestamp") String timestamp,
                                @RequestParam("nonce") String nonce,
                                @RequestParam("openid") String openid,
                                @RequestParam(name = "encrypt_type", required = false) String encType,
                                @RequestParam(name = "msg_signature", required = false) String msgSignature) {

        // 验证请求的签名
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 签名不匹配，直接返回空串
            log.error("非法请求！");
            return "";
        }

        // 处理微信消息，并返回回复消息
        String out = null;
        if (encType == null) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage != null) {
                // 说明是同步回复的消息
                out = outMessage.toXml();
            }
        } else if ("aes".equalsIgnoreCase(encType)) {
            // AES加密的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxMpService.getWxMpConfigStorage(),
                    timestamp, nonce, msgSignature);
            log.debug("\n消息解密后内容为：\n{}", inMessage.toString());
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            if (outMessage != null) {
                // 说明是同步回复的消息
                out = outMessage.toEncryptedXml(wxMpService.getWxMpConfigStorage());
            }
        }
//        log.debug("\n组装回复信息：{}", out);
        return out != null ? out : "";
    }


    /**
     * 发送模板消息
     */
    @GetMapping("/sendTemplate")
    @ApiOperation("发送模板消息")
    public String sendTemplate() throws WxErrorException {
        messageService.sendTemplateMessage();
        return "success";
    }

    /**
     * 发送客服文本消息
     */
    @GetMapping("/sendKefuText")
    @ApiOperation("发送客服文本消息")
    public String sendKefu(@RequestParam String toUser, @RequestParam String content) throws WxErrorException {
        WxMpKefuMessage message = WxMpKefuMessage
                // 文本消息，还有其他图片、语音、视频等
                .TEXT()
                .toUser(toUser)
                .content(content)
                .build();
        wxMpService.getKefuService().sendKefuMessage(message);
        return "success";
    }

    /**
     * 发送群发文本消息
     */
    @GetMapping("/sendMassText")
    @ApiOperation("发送群发文本消息")
    public WxMpMassSendResult sendMass(@RequestParam List<String> toUsers, @RequestParam String content) throws WxErrorException {
        WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
        // 文本消息，还有其他图片、语音、视频等
        massMessage.setMsgType(WxConsts.MassMsgType.TEXT);
        massMessage.setContent(content);
        // 群发至少两个用户
        massMessage.setToUsers(toUsers);
        WxMpMassSendResult wxMpMassSendResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
        return wxMpMassSendResult;
    }

    /**
     * 发送群发图文消息
     */
    @SneakyThrows
    @GetMapping("/sendMassNews")
    @ApiOperation("发送群发图文消息")
    public WxMpMassSendResult sendMassNews(@RequestParam List<String> toUsers, @RequestParam String content, @RequestParam("file") MultipartFile multipartFile) throws WxErrorException {
        // 创建临时文件
        File file = File.createTempFile("upload-", multipartFile.getOriginalFilename());
        // 将 MultipartFile 转换为 File
        multipartFile.transferTo(file);
        // 上传图文消息的封面图片
        WxMediaUploadResult uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, file);

        // 上传图文消息的正文图片(返回的url拼在正文的<img>标签中)
        WxMediaImgUploadResult imagedMediaRes = wxMpService.getMaterialService().mediaImgUpload(file);
        String url = imagedMediaRes.getUrl();
        log.info("url:{}", url);

        // 创建图文消息对象
        WxMpMassNews news = new WxMpMassNews();
        // 创建第一篇图文文章
        WxMpNewsArticle article1 = new WxMpNewsArticle();
        article1.setTitle("标题1");
        article1.setContent("内容1");
        article1.setThumbMediaId(uploadMediaRes.getMediaId());
        news.addArticle(article1);

        // 创建第二篇图文文章
        WxMpNewsArticle article2 = new WxMpNewsArticle();
        article2.setTitle("标题2");
        article2.setContent("内容2");
        article2.setThumbMediaId(uploadMediaRes.getMediaId());
        article2.setShowCoverPic(true);
        article2.setAuthor("作者2");
        article2.setContentSourceUrl("www.baidu.com");
        article2.setDigest("摘要2");
        news.addArticle(article2);

        WxMpMassUploadResult massUploadResult = wxMpService.getMassMessageService().massNewsUpload(news);

        WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
        massMessage.setMsgType(WxConsts.MassMsgType.MPNEWS);
        massMessage.setMediaId(massUploadResult.getMediaId());
        massMessage.setToUsers(toUsers);

        WxMpMassSendResult massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
        // 删除临时文件
        file.delete();
        return massResult;
    }

}