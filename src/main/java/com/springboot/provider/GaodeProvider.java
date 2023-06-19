package com.springboot.provider;

import com.springboot.entity.vo.GaoDeVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author xiaomai
 * @description 第三方高德天气接口
 * @date 2023/6/14 13:55
 */
@Component
@FeignClient(value = "GaodeWeather", url = "${gaode.server}")
public interface GaodeProvider {

    /**
     * 查询每日天气
     */
    @GetMapping(value = "/v3/weather/weatherInfo")
    GaoDeVo queryWeather(@RequestParam("city") String city, @RequestParam("key") String key);

}
