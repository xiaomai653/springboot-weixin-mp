package com.springboot.service.impl;

import com.alibaba.fastjson2.JSON;
import com.springboot.entity.vo.GaoDeVo;
import com.springboot.entity.vo.TianXingVo;
import com.springboot.provider.GaodeProvider;
import com.springboot.provider.TianxingProvider;
import com.springboot.service.IMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
 * @author xiaomai
 * @description 消息接口实现类
 * @date 2023/6/14 14:54
 */
@Service
@Slf4j
public class MessageServiceImpl implements IMessageService {


    /**
     * 高德 key
     **/
    @Value("${gaode.key}")
    private String gaodeKey;

    /**
     * 天行 key
     **/
    @Value("${tianxing.key}")
    private String tianxingKey;

    @Resource
    private GaodeProvider gaodeProvider;
    @Resource
    private TianxingProvider tianxingProvider;

    @Resource
    private WxMpService wxMpService;


    /**
     今天是：{{now.DATA}}
     今天有点想你，不止有一点，也不止今天！
     当前城市：{{city.DATA}}
     今天的天气：{{weather.DATA}}
     今日温度：{{temperature.DATA}} 度
     空气湿度：{{humidity.DATA}} 度
     风力：{{windPower.DATA}} 级
     今天是我们相恋的第：{{scq_day.DATA}} 天
     距你的生日还有：{{bir_day.DATA}} 天
     今日英文：{{daily_english_en.DATA}}
     译文：{{daily_english_zn.DATA}}
     爱心一言：{{daily_say_love.DATA}}
     备注：爱你哟，发射爱心biubiubiu~
     */

    /**
     * 今天是：{{now.DATA}}
     * 工作一天累了吧，但是也要记得想我哦。
     * 当前城市：{{city.DATA}}
     * 今天的天气：{{weather.DATA}}
     * 今日温度：{{temperature.DATA}} 度
     * 空气湿度：{{humidity.DATA}} 度
     * 风力：{{windPower.DATA}} 级
     * 今天是我们相恋的第：{{scq_day.DATA}} 天
     * 距你的生日还有：{{bir_day.DATA}} 天
     * 今日英文：{{daily_english_en.DATA}}
     * 译文：{{daily_english_zn.DATA}}
     * 爱心一言：{{daily_say_love.DATA}}
     * 备注：爱你哟，发射爱心biubiubiu~
     */
    @Override
    public void sendTemplateMessage() throws WxErrorException {
        // 微信ID, 可以在数据库获取
        String wechatId = "oRV7I5kStAiBPRYbp25owvZfaysU";
        // 模板ID
        String templateId = "AP8AWf75Uq4pCJw085JD0v3jSYnZq3i39ktRVbBin4w";
        // 创建模板信息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                .toUser(wechatId)
                .templateId(templateId)
                .build();

        // 获取高德天气
        GaoDeVo gaoDeVo = gaodeProvider.queryWeather("440106", gaodeKey);

        // 获取彩虹屁
        TianXingVo caihongpi = tianxingProvider.queryCaihongpi(tianxingKey);

        // 获取每日英文
        TianXingVo ensentence = tianxingProvider.queryEnsentence(tianxingKey);

        // 封装模板数据
        templateMessage.addData(new WxMpTemplateData("now", getDayOfWeek(), "#FFB6C1"));
        templateMessage.addData(new WxMpTemplateData("city", "广州市", "#B95EA6"));
        templateMessage.addData(new WxMpTemplateData("weather", gaoDeVo.getLives().get(0).getWeather(), "#173177"));
        templateMessage.addData(new WxMpTemplateData("temperature", gaoDeVo.getLives().get(0).getTemperature(), "#87cefa"));
        templateMessage.addData(new WxMpTemplateData("humidity", gaoDeVo.getLives().get(0).getHumidity(), "#FF6347"));
        templateMessage.addData(new WxMpTemplateData("windPower", gaoDeVo.getLives().get(0).getWindpower(), "#33CCCC"));
        templateMessage.addData(new WxMpTemplateData("scq_day", String.valueOf(countdownToRemembranceDay(LocalDate.of(2023, 10, 01))), "#FF1493"));
        templateMessage.addData(new WxMpTemplateData("bir_day", String.valueOf(countdownToRemembranceDay(LocalDate.of(2000, 06, 01))), "#FF00FF"));
        templateMessage.addData(new WxMpTemplateData("daily_english_en", ensentence.getNewslist().get(0).getEn(), "#FFA500"));
        templateMessage.addData(new WxMpTemplateData("daily_english_zn", ensentence.getNewslist().get(0).getZh(), "#FFA500"));
        templateMessage.addData(new WxMpTemplateData("daily_say_love", caihongpi.getNewslist().get(0).getContent(), "#800080"));

        log.info("发送的消息为：{}", JSON.toJSONString(templateMessage));
        wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);

    }

    /**
     * 计算距离生日的倒计时天数
     *
     * @param anniversary 纪念日
     * @return 距离下一个纪念日的天数
     */
    public int countdownToRemembranceDay(LocalDate anniversary) {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 将生日日期设置为当前年份的日期
        LocalDate nextBirthday = anniversary.withYear(today.getYear());
        // 如果下一个生日已经在当前日期之前或者与当前日期相等，则表示下一个生日在明年
        if (nextBirthday.isBefore(today) || nextBirthday.isEqual(today)) {
            nextBirthday = nextBirthday.plusYears(1);
        }
        // 计算距离下一个生日的天数
        long days = ChronoUnit.DAYS.between(today, nextBirthday);
        // 将天数转换为整数并返回
        return Math.toIntExact(days);
    }

    /**
     * 获取今天是星期几
     *
     * @return 今天的星期几，以文本形式表示（例如：星期一）
     */
    public String getDayOfWeek() {
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 获取今天是星期几
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        // 获取默认的语言环境
        Locale locale = Locale.getDefault();
        // 指定星期几的文本样式为完整的形式（例如：星期一）
        TextStyle textStyle = TextStyle.FULL;
        // 返回今天的星期几的文本表示
        return dayOfWeek.getDisplayName(textStyle, locale);
    }

}
