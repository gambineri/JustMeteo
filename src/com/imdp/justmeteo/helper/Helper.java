package com.imdp.justmeteo.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Helper {
	
	private Context mContext = null;
	
	public Helper(Context context) {
	  mContext = context;
	}

  /*******************************************
   * Toast helper functions
   **/
  public void tL(String msg_long_duration) {
    doToast(msg_long_duration, Toast.LENGTH_LONG);
  }
  
  public void tS(String msg_short_duration) {
    doToast(msg_short_duration, Toast.LENGTH_SHORT);
  }

  private void doToast(String msg, int duration) {
    if (mContext != null) 
      Toast.makeText(mContext, msg, duration).show();	  
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
