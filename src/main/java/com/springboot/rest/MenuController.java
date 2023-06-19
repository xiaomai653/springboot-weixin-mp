package com.springboot.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.menu.WxMpMenu;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author xiaomai
 * @description 菜单管理接口
 * @date 2023/6/13 15:56
 */
@RestController
@RequestMapping("/wechat")
@Api(tags = "菜单管理")
public class MenuController {

    @Resource
    private WxMpService wxMpService;

    @GetMapping("/menu")
    @ApiOperation("获取菜单")
    public WxMpMenu getWxMenu() throws WxErrorException {
        WxMpMenu wxMpMenu = wxMpService.getMenuService().menuGet();
        return wxMpMenu;
    }

    @PostMapping("/menu")
    @ApiOperation("新增修改菜单")
    public String createWxMenu(@RequestBody WxMenu menu) throws WxErrorException {
        wxMpService.getMenuService().menuCreate(menu);
        return "success";
    }

    @DeleteMapping("/menu")
    @ApiOperation("删除菜单")
    public String delWxMenu() throws WxErrorException {
        wxMpService.getMenuService().menuDelete();
        return "success";
    }

}
