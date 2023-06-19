package com.springboot.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author xiaomai
 * @description 用户管理接口
 * @date 2023/6/14 9:53
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理接口")
public class UserController {

    @Resource
    private WxMpService wxMpService;

    @GetMapping("/info")
    @ApiOperation("获得用户信息")
    public WxMpUser userInfo(@RequestParam String openid) throws WxErrorException {
        //语言默认为中文, 直接传null
        WxMpUser user = wxMpService.getUserService().userInfo(openid, null);
        return user;
    }

    @GetMapping("/list")
    @ApiOperation("获得用户列表")
    public WxMpUserList userList() throws WxErrorException {
        WxMpUserList wxUserList = wxMpService.getUserService().userList(null);
        return wxUserList;
    }

    @PostMapping("/update/remark")
    @ApiOperation("更新用户备注名")
    public void userUpdateRemark(@RequestParam String openid, @RequestParam String remarkName) throws WxErrorException {
        wxMpService.getUserService().userUpdateRemark(openid, remarkName);
    }

    @GetMapping("/tag/list")
    @ApiOperation("查询用户标签列表")
    public List<Long> userTagList(@RequestParam String openid) throws WxErrorException {
        List<Long> tags = wxMpService.getUserTagService().userTagList(openid);
        return tags;
    }

    @PostMapping("/batch/tag")
    @ApiOperation("查询用户标签列表")
    public void batchTagging(@RequestParam String[] openids, @RequestParam long tagid) throws WxErrorException {
        wxMpService.getUserTagService().batchTagging(tagid, openids);
    }

    @DeleteMapping("/batch/untag")
    @ApiOperation("批量给用户移除标签")
    public void batchUntagging(@RequestParam String[] openids, @RequestParam long tagid) throws WxErrorException {
        wxMpService.getUserTagService().batchUntagging(tagid, openids);
    }


}
