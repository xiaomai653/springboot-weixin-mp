package com.springboot.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpMassNews;
import me.chanjar.weixin.mp.bean.WxMpMassOpenIdsMessage;
import me.chanjar.weixin.mp.bean.WxMpMassVideo;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpNewsArticle;
import me.chanjar.weixin.mp.bean.result.WxMpMassSendResult;
import me.chanjar.weixin.mp.bean.result.WxMpMassUploadResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author xiaomai
 * @description 群发消息
 * @date 2023/6/17 21:42
 */
@RestController
@RequestMapping("/mass")
@Slf4j
@Api(tags = "群发管理接口")
public class MassMessageController {

    @Resource
    private WxMpService wxMpService;

    /**
     * 发送群发图文消息
     */
    @SneakyThrows
    @PostMapping("/sendNews")
    @ApiOperation("发送群发图文消息")
    public WxMpMassSendResult sendMassNews(@RequestParam List<String> toUsers, @RequestParam String content,
                                           @RequestParam("file") MultipartFile multipartFile) {
        // 创建临时文件
        File file = File.createTempFile("upload-", multipartFile.getOriginalFilename());
        // 将 MultipartFile 转换为 File
        multipartFile.transferTo(file);
        // 上传图文消息的封面图片（临时素材）
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

    /**
     * 发送群发文本消息
     */
    @PostMapping("/sendText")
    @ApiOperation("发送群发文本消息")
    public WxMpMassSendResult sendText(@RequestParam List<String> toUsers, @RequestParam String content) throws WxErrorException {
        WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
        massMessage.setMsgType(WxConsts.MassMsgType.TEXT);
        massMessage.setContent(content);
        massMessage.setToUsers(toUsers);

        WxMpMassSendResult massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
        return massResult;
    }

    /**
     * 发送群发图片消息
     */
    @PostMapping("/sendImage")
    @ApiOperation("发送群发图片消息")
    public WxMpMassSendResult sendImage(@RequestParam List<String> toUsers, @RequestParam("file") MultipartFile multipartFile)
            throws IOException, WxErrorException {

        // 创建临时文件
        File file = File.createTempFile("upload-", multipartFile.getOriginalFilename());
        // 将 MultipartFile 转换为 File
        multipartFile.transferTo(file);

        WxMediaUploadResult uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MediaFileType.IMAGE, file);

        WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
        massMessage.setMsgType(WxConsts.MassMsgType.IMAGE);
        massMessage.setMediaId(uploadMediaRes.getMediaId());
        massMessage.setToUsers(toUsers);

        WxMpMassSendResult massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
        file.delete();
        return massResult;
    }

    /**
     * 发送群发视频消息
     */
    @PostMapping("/sendVideo")
    @ApiOperation("发送群发视频消息")
    public WxMpMassSendResult sendVideo(@RequestParam List<String> toUsers, @RequestParam("file") MultipartFile multipartFile)
            throws IOException, WxErrorException {

        // 创建临时文件
        File file = File.createTempFile("upload-", multipartFile.getOriginalFilename());
        // 将 MultipartFile 转换为 File
        multipartFile.transferTo(file);

        WxMediaUploadResult uploadMediaRes = wxMpService.getMaterialService().mediaUpload(WxConsts.MediaFileType.VIDEO, file);

        // 把视频变成可被群发的媒体
        WxMpMassVideo video = new WxMpMassVideo();
        video.setTitle("测试标题");
        video.setDescription("测试描述");
        video.setMediaId(uploadMediaRes.getMediaId());
        WxMpMassUploadResult uploadResult = wxMpService.getMassMessageService().massVideoUpload(video);

        WxMpMassOpenIdsMessage massMessage = new WxMpMassOpenIdsMessage();
        massMessage.setMsgType(WxConsts.MassMsgType.MPVIDEO);
        massMessage.setMediaId(uploadResult.getMediaId());
        massMessage.setToUsers(toUsers);

        WxMpMassSendResult massResult = wxMpService.getMassMessageService().massOpenIdsMessageSend(massMessage);
        file.delete();
        return massResult;
    }


}
