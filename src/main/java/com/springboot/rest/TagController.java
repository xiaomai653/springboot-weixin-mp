package com.springboot.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.tag.WxUserTag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xiaomai
 * @description 标签管理接口
 * @date 2023/6/14 10:05
 */
@RestController
@RequestMapping("/tag")
@Api(tags = "标签管理接口")
public class TagController {

    @Resource
    private WxMpService wxMpService;

    @PostMapping("/create")
    @ApiOperation("创建标签")
    public WxUserTag create(@RequestParam String tagName) throws WxErrorException {
        WxUserTag res = wxMpService.getUserTagService().tagCreate(tagName);
        return res;
    }

    @GetMapping("/list")
    @ApiOperation("获得标签列表")
    public List<WxUserTag> lsit() throws WxErrorException {
        List<WxUserTag> res = wxMpService.getUserTagService().tagGet();
        return res;
    }

    @PutMapping("/update")
    @ApiOperation("更新标签名")
    public Boolean update(@RequestParam Long tagId, @RequestParam String tagName) throws WxErrorException {
        Boolean res = wxMpService.getUserTagService().tagUpdate(tagId, tagName);
        return res;
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除指定标签")
    public Boolean delete(@RequestParam Long tagId) throws WxErrorException {
        Boolean res = wxMpService.getUserTagService().tagDelete(tagId);
        return res;
    }

}
