package com.springboot.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

/**
 * @author xiaomai
 * @description 二维码管理接口
 * @date 2023/6/18 18:29
 */
@RestController
@RequestMapping("/ticket")
@Api(tags = "二维码管理接口")
public class TicketController {

    @Resource
    private WxMpService wxMpService;

    @PostMapping("/create")
    @ApiOperation("创建标签")
    public WxMpQrCodeTicket create(@RequestParam String scene, @RequestParam(required = false) Integer seconds) throws IOException, WxErrorException {
        WxMpQrCodeTicket ticket;
        if (seconds != null) {
            // 临时ticket
            ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(scene, seconds);
        } else {
            // 永久ticket
            ticket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(scene);
        }

        // 获得在系统临时目录下的文件，需要自己保存使用，注意：临时文件夹下存放的文件不可靠，不要直接使用
        File file = wxMpService.getQrcodeService().qrCodePicture(ticket);

        // 获取 classpath 路径
        String filePath = new ClassPathResource("").getFile().getAbsolutePath() + "/static/" + File.separator + file.getName();

        // 将文件保存到目标路径
        FileCopyUtils.copy(file, new File(filePath));

        return ticket;
    }

}
