package com.springboot.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;


/**
 * @author xiaomai
 * @description 高德天气实体类
 * @date 2023/6/14 14:48
 */
@Data
@NoArgsConstructor
public class GaoDeVo {


	/** 返回状态 **/
	@ApiModelProperty("status")
	private String status;

	/** 返回结果总数目 **/
	@ApiModelProperty("count")
	private String count;

	/** 返回的状态信息 **/
	@ApiModelProperty("info")
	private String info;

	/** 返回状态说明,10000代表正确 **/
	@ApiModelProperty("infocode")
	private String infocode;

	/** 实时天气 **/
	private List<Lives> lives;

	/** 天气预报 **/
	private List<Forecast> forecast;


	@Data
	@NoArgsConstructor
	public static class Lives{

		/** 省份名 **/
		@ApiModelProperty("province")
		private String province;

		/** 城市名 **/
		@ApiModelProperty("city")
		private String city;

		/**区域编码 **/
		@ApiModelProperty("adcode")
		private String adcode;

		/** 天气现象,例如阴 **/
		@ApiModelProperty("weather")
		private String weather;

		/** 温度（℃） **/
		@ApiModelProperty("temperature")
		private String temperature;

		/** 风向描述 **/
		@ApiModelProperty("winddirection")
		private String winddirection;

		/** 风力等级 **/
		@ApiModelProperty("windpower")
		private String windpower;

		/** 相对湿度(%) **/
		@ApiModelProperty("humidity")
		private String humidity;

		/** 数据发布的时间 **/
		@ApiModelProperty("reporttime")
		@JsonFormat(pattern = "yyyy-HH-dd HH:mm:ss")
		private Date reporttime;

	}

	@Data
	@NoArgsConstructor
	public static class Forecast{

		/** 省份名 **/
		@ApiModelProperty("province")
		private String province;

		/** 城市名 **/
		@ApiModelProperty("city")
		private String city;

		/**区域编码 **/
		@ApiModelProperty("adcode")
		private String adcode;

		/** 数据发布的时间 **/
		@ApiModelProperty("reporttime")
		@JsonFormat(pattern = "yyyy-HH-dd HH:mm:ss")
		private Date reporttime;

		private List<Casts> casts;

		@Data
		@NoArgsConstructor
		public static class Casts{

			/** 日期 **/
			@ApiModelProperty("date")
			@JsonFormat(pattern = "yyyy-HH-dd")
			private Date date;

			/**星期几 **/
			@ApiModelProperty("week")
			private String week;

			/**白天天气现象 **/
			@ApiModelProperty("dayweather")
			private String dayweather;

			/**晚上天气现象 **/
			@ApiModelProperty("nightweather")
			private String nightweather;

			/**白天温度 **/
			@ApiModelProperty("daytemp")
			private String daytemp;

			/**晚上温度 **/
			@ApiModelProperty("nighttemp")
			private String nighttemp;

			/**白天风向 **/
			@ApiModelProperty("daywind")
			private String daywind;

			/**晚上风向 **/
			@ApiModelProperty("nightwind")
			private String nightwind;

			/**白天风力 **/
			@ApiModelProperty("daypower")
			private String daypower;

			/**晚上风力 **/
			@ApiModelProperty("nightpower")
			private String nightpower;

		}

	}

}

