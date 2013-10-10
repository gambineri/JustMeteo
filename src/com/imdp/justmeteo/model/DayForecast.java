package com.imdp.justmeteo.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 
 *
 */
public class DayForecast {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);

	public Weather weather = new Weather();
	public ForecastTemp forecastTemp = new ForecastTemp();
	public long timestamp;
	
	public class ForecastTemp {
		public float day;
		public float min;
		public float max;
		public float night;
		public float eve;
		public float morning;
	}
	
	public String getStringDate() {
		return sdf.format(new Date(timestamp));
	}
}
