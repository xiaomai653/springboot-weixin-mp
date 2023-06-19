package com.springboot.service;

import java.util.Map;

/**
 * @author xiaomai
 * @description gpt 接口类
 * @date 2023/6/16 11:15
 */
public interface IChatService {

    /**
     *  聊天
     *
     * @param question 问题
     * @return 结果
     */
    String chat(String question);

    /**
     *  聊天（流）
     *
     * @param question 问题
     * @return 结果
     */
    void chatStream(String question);

    /**
     *  生成图片
     *
     * @param information 图片信息
     * @return 结果
     */
    Map<String, String> createImage(String information);

    /**
     *  文本补全
     *
     * @param prompt 文本
     * @return 结果
     */
    String completions(String prompt);

}
