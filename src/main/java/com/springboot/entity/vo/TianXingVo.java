package com.springboot.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author xiaomai
 * @description 天行数据返回实体
 * @date 2023/6/14 14:35
 */
@Data
@NoArgsConstructor
public class TianXingVo {

    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("信息")
    private String msg;

    @ApiModelProperty("结果")
    private List<Newslist> newslist;

    @Data
    @NoArgsConstructor
    public static class Newslist {

        @ApiModelProperty("内容")
        private String content;

        @ApiModelProperty("ONE一个ID")
        private Integer oneid;

        @ApiModelProperty("句子")
        private String word;

        @ApiModelProperty("句子来源")
        private String wordfrom;

        @ApiModelProperty("配图")
        private String imgurl;

        @ApiModelProperty("配图作者")
        private String imgauthor;

        @ApiModelProperty("时间")
        private String date;

        @ApiModelProperty("英语句子")
        private String en;

        @ApiModelProperty("中文句子")
        private String zh;
    }
}
