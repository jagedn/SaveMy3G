package com.puravida.savemy3g;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;

public class CheckWifiService extends Service {
    public CheckWifiService() {
    }

    public class LocalBinder extends Binder {
        CheckWifiService getService() {
            return CheckWifiService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

    @Override
    public void onCreate() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "CheckWifi", Toast.LENGTH_SHORT).show();
        if( isWifiEnabled() ){
            Toast.makeText(this, "Wifi Enabled", Toast.LENGTH_LONG).show();
            scheduleNext(60,false);
            return START_STICKY;
        }
        // si no hay wifi, la conectamos y esperamos 1 minuto a ver si pilla cacho
        if( intent.getBooleanExtra("enable", true)){
            powerOnWifi();
            Toast.makeText(this, "Searching Wifi", Toast.LENGTH_LONG).show();
            scheduleNext(1,false);
        }else{
            powerOffWifi();
            Toast.makeText(this, "Wifi not found", Toast.LENGTH_LONG).show();
            scheduleNext(15,true);
        }
        return START_STICKY;
    }

    PendingIntent sender;

    static final int WHAT = 192837;

    void scheduleNext(int minutes, boolean next){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, minutes);

        Intent intent = new Intent(getApplicationContext(),this.getClass());
        intent.putExtra("enable",next);
        sender = PendingIntent.getService(this, WHAT, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), sender);
    }


    boolean isWifiEnabled(){
        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
        return isWiFi;
    }

    void powerOnWifi(){
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(true);
    }

    void powerOffWifi(){
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
    }

}
