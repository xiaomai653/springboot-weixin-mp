package com.springboot.service;

import me.chanjar.weixin.common.error.WxErrorException;

/**
 * @author xiaomai
 * @description 消息接口
 * @date 2023/6/14 14:53
 */
public interface IMessageService {

    /**
     * 发送模板消息
     */
    void sendTemplateMessage() throws WxErrorException;

}
