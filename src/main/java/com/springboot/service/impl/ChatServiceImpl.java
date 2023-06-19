package com.springboot.service.impl;

import com.springboot.service.IChatService;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.CompletionResult;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.ImageResult;
import com.theokanning.openai.service.OpenAiService;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @author xiaomai
 * @description gpt实现类
 * @date 2023/6/15 11:01
 */
@Service
@Slf4j
public class ChatServiceImpl implements IChatService {

    @Resource
    private OpenAiService openAiService;


    /**
     * @param question
     * @return 结果
     * @description 聊天
     */
    public String chat(String question) {
        log.info("问题：{}", question);
        long start = System.currentTimeMillis();
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 模型名称
                .model("gpt-3.5-turbo")
                // 下面两项数值控制模型输出的随机性，对回答的稳定性要求高时，可设置随机性为最低
                .temperature(0.0D)
                .topP(1.0)
                .maxTokens(200)
                .messages(Arrays.asList(new ChatMessage(ChatMessageRole.SYSTEM.value(), "IMPORTANT: You are a virtual assistant powered by the gpt-3.5-turbo model")
                        , new ChatMessage(ChatMessageRole.USER.value(), "下面我让你来充当翻译家，你的目标是把任何语言翻译成中文，请翻译时不要带翻译腔，而是要翻译得自然、流畅和地道，使用优美和高雅的表达方式。请翻译下面这句话：“how are you ?")
                        , new ChatMessage(ChatMessageRole.USER.value(), question)))
                .build();
        ChatCompletionResult chatCompletion = openAiService.createChatCompletion(chatCompletionRequest);
        long end = System.currentTimeMillis();
        log.info("chat接口调用时长：{} 毫秒", end - start);
        return chatCompletion.getChoices().get(0).getMessage().getContent();
    }

    public void chatStream(String question) {
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                // 模型名称
                .model("gpt-3.5-turbo")
                // 下面两项数值控制模型输出的随机性，对回答的稳定性要求高时，可设置随机性为最低
                .temperature(0.0D)
                .topP(1.0)
                .maxTokens(200)
                .stream(true)
                .messages(Arrays.asList(new ChatMessage(ChatMessageRole.SYSTEM.value(), "IMPORTANT: You are a virtual assistant powered by the gpt-3.5-turbo model")
                        , new ChatMessage(ChatMessageRole.USER.value(), "下面我让你来充当翻译家，你的目标是把任何语言翻译成中文，请翻译时不要带翻译腔，而是要翻译得自然、流畅和地道，使用优美和高雅的表达方式。请翻译下面这句话：“how are you ?")
                        , new ChatMessage(ChatMessageRole.USER.value(), question)))
                .build();
        Flowable<ChatCompletionChunk> chunks = openAiService.streamChatCompletion(chatCompletionRequest);

    }


    /**
     * 生成图片
     */
    public Map<String, String> createImage(String information) {
        long start = System.currentTimeMillis();
        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                //所需图像的文本描述。最大长度为 1000 个字符。
                .prompt(information)
                //生成图像的大小。必须是256x256、512x512或1024x1024
                .size("1024x1024")
                //响应格式,生成的图像返回的格式。必须是url或b64_json,默认为url,url将在一小时后过期。
                .responseFormat("url")
                //要生成的图像数。必须介于 1 和 10 之间。
                .n(1)
                .build();
        ImageResult result = openAiService.createImage(createImageRequest);
        Map<String, String> map = new HashMap<>(4);
        String url = result.getData().get(0).getUrl();
        map.put("图片链接", url);
        long end = System.currentTimeMillis();
        log.info("image接口调用时长：{} 毫秒", end - start);
        return map;
    }


    /**
     * 文本补全
     */
    public String completions(String prompt) {
        long start = System.currentTimeMillis();
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("ada")
                .echo(false)
                .temperature(0.5)
                .topP(1d)
                .frequencyPenalty(0d)
                .presencePenalty(0d)
                .maxTokens(1000)
                .build();
        CompletionResult completionResult = openAiService.createCompletion(completionRequest);
        String text = completionResult.getChoices().get(0).getText();
        long end = System.currentTimeMillis();
        log.info("completions接口调用时长：{} 毫秒", end - start);
        return text;
    }


}