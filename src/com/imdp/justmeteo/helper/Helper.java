package com.imdp.justmeteo.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Helper {
	
	private Context mContext = null;
	
	public Helper(Context context) {
		// TODO Auto-generated constructor stub
	}

  /*******************************************
   * Toast helper functions
   **/
  public void tL(String msg_long_duration) {
  	Toast.makeText(mContext, msg_long_duration, Toast.LENGTH_LONG).show();
  }
  
  public void tS(String msg_short_duration) {
  	Toast.makeText(mContext, msg_short_duration, Toast.LENGTH_SHORT).show();
  }
  
  /*******************************************
   * Connectivity
   **/
	public boolean isNetworkAvailable() {
    ConnectivityManager connectivityManager 
          = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }
  
}
