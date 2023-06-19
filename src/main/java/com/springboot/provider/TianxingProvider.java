package com.springboot.provider;

import com.springboot.entity.vo.TianXingVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xiaomai
 * @description 第三方天行数据接口
 * @date 2023/6/14 13:55
 */
@Component
@FeignClient(value = "TianxingData", url = "${tianxing.server}")
public interface TianxingProvider {

    /**
     * 获取彩虹屁
     */
    @GetMapping(value = "/caihongpi/index",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    TianXingVo queryCaihongpi(@RequestParam("key") String key);

    /**
     * 获取英语一句话
     */
    @GetMapping(value = "/ensentence/index",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    TianXingVo queryEnsentence(@RequestParam("key") String key);

    /**
     * 获取ONE一个
     */
    @GetMapping(value = "/one/index",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    TianXingVo queryOne(@RequestParam("key") String key);

    /**
     * 获取土味情话
     */
    @GetMapping(value = "/saylove/index",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    TianXingVo querySaylove(@RequestParam("key") String key);

}
