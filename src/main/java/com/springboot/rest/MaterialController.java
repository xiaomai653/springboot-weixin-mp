package com.springboot.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @author xiaomai
 * @description 素材管理接口
 * @date 2023/6/16 21:05
 */
@RestController
@RequestMapping("/material")
@Api(tags = "素材管理接口")
public class MaterialController {

    @Resource
    private WxMpService wxMpService;

    @PostMapping("/upload")
    @ApiOperation("上传其他类型素材")
    public WxMpMaterialUploadResult uploadMaterial(@RequestParam("file") MultipartFile file) throws IOException, WxErrorException {
        // 创建临时文件
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        // 将 MultipartFile 转换为 File
        file.transferTo(tempFile);

        WxMpMaterial wxMaterial = new WxMpMaterial();
        wxMaterial.setFile(tempFile);
        wxMaterial.setName(file.getName());
        WxMpMaterialUploadResult wxMpMaterialUploadResult = wxMpService.getMaterialService().materialFileUpload(WxConsts.MaterialType.IMAGE, wxMaterial);
        // 删除临时文件
        tempFile.delete();
        return wxMpMaterialUploadResult;
    }

    @PostMapping("/uploadNews")
    @ApiOperation("上传图文素材")
    public String uploadNewsMaterial(@RequestParam("mediaId") String mediaId) throws WxErrorException {
        // 单图文消息
        WxMpMaterialNews wxMpMaterialNewsSingle = new WxMpMaterialNews();
        WxMpNewsArticle article = new WxMpNewsArticle();
        article.setAuthor("author");
        article.setThumbMediaId(mediaId);
        article.setTitle("single title");
        article.setContent("single content");
        article.setContentSourceUrl("content url");
        article.setShowCoverPic(true);
        article.setDigest("single news");
        wxMpMaterialNewsSingle.addArticle(article);

        // 多图文消息
        WxMpMaterialNews wxMpMaterialNewsMultiple = new WxMpMaterialNews();
        WxMpNewsArticle article1 = new WxMpNewsArticle();
        article1.setAuthor("author1");
        article1.setThumbMediaId(mediaId);
        article1.setTitle("multi title1");
        article1.setContent("content 1");
        article1.setContentSourceUrl("content url");
        article1.setShowCoverPic(true);
        article1.setDigest("");

        WxMpNewsArticle article2 = new WxMpNewsArticle();
        article2.setAuthor("author2");
        article2.setThumbMediaId(mediaId);
        article2.setTitle("multi title2");
        article2.setContent("content 2");
        article2.setContentSourceUrl("content url");
        article2.setShowCoverPic(true);
        article2.setDigest("");

        wxMpMaterialNewsMultiple.addArticle(article1);
        wxMpMaterialNewsMultiple.addArticle(article2);

        WxMpMaterialUploadResult resSingle = wxMpService.getMaterialService().materialNewsUpload(wxMpMaterialNewsSingle);
        String singleNewsMediaId = resSingle.getMediaId();
        WxMpMaterialUploadResult resMulti = wxMpService.getMaterialService().materialNewsUpload(wxMpMaterialNewsMultiple);
        String multiNewsMediaId = resMulti.getMediaId();
        return singleNewsMediaId;
    }

    @GetMapping("/get")
    @ApiOperation("获取图片或声音永久素材")
    public void getMaterial(@RequestParam("mediaId") String mediaId) throws WxErrorException {
        wxMpService.getMaterialService().materialImageOrVoiceDownload(mediaId);
    }

    @GetMapping("/getNews")
    @ApiOperation("获取图文素材")
    public WxMpMaterialNews getNewsMaterial(@RequestParam("mediaId") String mediaId) throws WxErrorException {
        return wxMpService.getMaterialService().materialNewsInfo(mediaId);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除素材")
    public String deleteMaterial(@RequestParam("mediaId") String mediaId) throws WxErrorException {
        wxMpService.getMaterialService().materialDelete(mediaId);
        return "Delete success";
    }

    @GetMapping("/count")
    @ApiOperation("获取素材总数")
    public WxMpMaterialCountResult count() throws WxErrorException {
        return wxMpService.getMaterialService().materialCount();
    }

    @GetMapping("/list")
    @ApiOperation("根据类别分页获取非图文素材列表")
    public WxMpMaterialFileBatchGetResult list(@RequestParam String type, @RequestParam int offset, @RequestParam int count) throws WxErrorException {
        return wxMpService.getMaterialService().materialFileBatchGet(type, offset, count);
    }

    @GetMapping("/listNews")
    @ApiOperation("根据类别分页获取图文素材列表")
    public WxMpMaterialNewsBatchGetResult listNews(@RequestParam int offset, @RequestParam int count) throws WxErrorException {
        return wxMpService.getMaterialService().materialNewsBatchGet(offset, count);
    }

}
