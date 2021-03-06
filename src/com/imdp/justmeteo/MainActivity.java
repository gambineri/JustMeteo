package com.imdp.justmeteo;

import org.json.JSONException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.imdp.justmeteo.adapter.DailyForecastPageAdapter;
import com.imdp.justmeteo.database.SQLiteDB;
import com.imdp.justmeteo.helper.Helper;
import com.imdp.justmeteo.model.Weather;
import com.imdp.justmeteo.model.WeatherForecast;

public class MainActivity extends FragmentActivity {

	private TextView cityText;
	private TextView condDescr;
	private TextView temp;
	private TextView press;
	private TextView windSpeed;
	private TextView windDeg;
	private TextView unitTemp;
	
	private TextView hum;
	private ImageView imgView;
	
	private static String forecastDaysNum = "3";
	private ViewPager pager;

	private Helper mH = null;

	public boolean isNetworkAvailable2() {
    ConnectivityManager connectivityManager 
          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String city = "Roma, IT";
		String lang = "en";

		mH = new Helper(getBaseContext());
		
		cityText = (TextView) findViewById(R.id.cityText);
		temp = (TextView) findViewById(R.id.temp);
		unitTemp = (TextView) findViewById(R.id.unittemp);
		unitTemp.setText("�C");
		condDescr = (TextView) findViewById(R.id.skydesc);
		
		pager = (ViewPager) findViewById(R.id.pager);
		imgView = (ImageView) findViewById(R.id.condIcon);
		/*
		condDescr = (TextView) findViewById(R.id.condDescr);
		
		hum = (TextView) findViewById(R.id.hum);
		press = (TextView) findViewById(R.id.press);
		windSpeed = (TextView) findViewById(R.id.windSpeed);
		windDeg = (TextView) findViewById(R.id.windDeg);
		
		*/
		
		if (mH.isNetworkAvailable()) {
//		if (isNetworkAvailable2()) {
			mH.tL("Ce sta sine che ce stane");
			JSONWeatherTask task = new JSONWeatherTask();
			task.execute(new String[]{city,lang});
			
			JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
			task1.execute(new String[]{city,lang, forecastDaysNum});
		}
		else {
			mH.tL("Non ce sta la connessioneeee");
		}

//*******************************************8		

//		Context mCon;
//		String path = mCon.getDatabasePath("justmeteo").getPath();

		Cursor employees;
		SQLiteDB db;
		
		db = new SQLiteDB(this.getApplicationContext());
		employees = db.getEmployees(); // you would not typically call this on the main thread
		employees.moveToFirst();
		String row = employees.getInt(0) + employees.getString(1) +employees.getString(2); 
  	Toast.makeText(getBaseContext(), row, Toast.LENGTH_LONG).show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();
			String data = ( (new WeatherHttpClient()).getWeatherData(params[0], params[1]));

			try {
				weather = JSONWeatherParser.getWeather(data);
				System.out.println("Weather ["+weather+"]");
				// Let's retrieve the icon
				weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
				
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			return weather;
		}
		
		@Override
		protected void onPostExecute(Weather weather) {			
			super.onPostExecute(weather);
			
			if (weather.iconData != null && weather.iconData.length > 0) {
				Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length); 
				imgView.setImageBitmap(img);
			}
			
			
			cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
			temp.setText("" + Math.round((weather.temperature.getTemp() - 275.15)));
			condDescr.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
			/*
			
			temp.setText("" + Math.round((weather.temperature.getTemp() - 275.15)) + "�C");
			hum.setText("" + weather.currentCondition.getHumidity() + "%");
			press.setText("" + weather.currentCondition.getPressure() + " hPa");
			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
			windDeg.setText("" + weather.wind.getDeg() + "�");
			*/	
		}
	}	// JSONWeatherTask

	private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {
		
		@Override
		protected WeatherForecast doInBackground(String... params) {
			
			String data = ( (new WeatherHttpClient()).getForecastWeatherData(params[0], params[1], params[2]));
			WeatherForecast forecast = new WeatherForecast();
			try {
				forecast = JSONWeatherParser.getForecastWeather(data);
				System.out.println("Weather ["+forecast+"]");
				// Let's retrieve the icon
				//weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
				
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			return forecast;
		}

		@Override
		protected void onPostExecute(WeatherForecast forecastWeather) {			
			super.onPostExecute(forecastWeather);
			DailyForecastPageAdapter adapter = 
					new DailyForecastPageAdapter(Integer.parseInt(forecastDaysNum), 
																			 getSupportFragmentManager(), 
																			 forecastWeather);
			pager.setAdapter(adapter);
		}
  }	// JSONForecastWeatherTask
}	// MainActivity

