package com.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;

import static com.theokanning.openai.service.OpenAiService.*;


/**
 * @author xiaomai
 * @description openAi 配置类
 * @date 2023/6/15 10:36
 */
@Configuration
public class OpenAiConfig {

    @Value("${open.ai.key}")
    private String openAiKey;
    @Value("${open.ai.request.timeout}")
    private long timeout;
    
    @Bean
    public OpenAiService openAiService(){
        ObjectMapper mapper = defaultObjectMapper();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        OkHttpClient client = defaultClient(openAiKey, Duration.ofSeconds(timeout))
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = defaultRetrofit(client, mapper);
        OpenAiApi api = retrofit.create(OpenAiApi.class);
        OpenAiService service = new OpenAiService(api);
        return service;
    }
}