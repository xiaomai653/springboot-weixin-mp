package com.springboot.rest;

import com.springboot.service.impl.ChatServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author xiaomai
 * @description chat管理接口
 * @date 2023/6/15 11:02
 */
@RestController
@RequestMapping("/chat")
@Api(tags = "chat管理接口")
public class ChatController {

    @Resource
    private ChatServiceImpl openAiChatBiz;

    @GetMapping("/chat")
    @ApiOperation("聊天")
    public String chat(@RequestParam String question) {
        return openAiChatBiz.chat(question);
    }

    @GetMapping("/image")
    @ApiOperation("生成照片")
    public Map<String, String> openAiChat(@RequestParam String information) {
        return openAiChatBiz.createImage(information);
    }

    @GetMapping("/completions")
    @ApiOperation("文本完善")
    public String completions(@RequestParam String prompt) {
        return openAiChatBiz.completions(prompt);
    }
}