package in.co.skynetserver.skynet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.List;

import static in.co.skynetserver.skynet.R.string.ssid;


public class DeviceSetup extends AppCompatActivity {

    protected TextView mSSID;
    protected WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setup);
        Intent intent = getIntent();
        //mSSID.setText(R.string.no_device_found);
        connectToWifi();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        webView = (WebView) findViewById(R.id.autoconfig);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://192.168.4.1/");

//        mSSID.setText(R.string.ssid);

    }

    protected void onStart() {

        super.onStart();
        /*while(!(wifiManager.getConnectionInfo().getSSID() == getString(ssid))) {
            wifiManager.reconnect();
            mSSID.setText("Connecting");
        }
        if (wifiManager.getConnectionInfo().getSSID() == getString(ssid)){
            mSSID.setText(wifiManager.getConnectionInfo().getSSID());

        }*/
    }
    public void connectToWifi(){
        try{
            WifiManager wifiManager = (WifiManager) super.getApplicationContext().getSystemService(android.content.Context.WIFI_SERVICE);
            WifiConfiguration wc = new WifiConfiguration();
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            wc.SSID = "\"SkyNet-AutoConfig\"";
            //wc.preSharedKey = "\"PASSWORD\"";
            wc.status = WifiConfiguration.Status.ENABLED;
            wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            //wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);

            wifiManager.setWifiEnabled(true);
            int netId = wifiManager.addNetwork(wc);
            if (netId == -1) {
                netId = getExistingNetworkId(wc.SSID);
                mSSID.setText(netId);
            }
            wifiManager.disconnect();
            wifiManager.enableNetwork(netId, true);
            wifiManager.reconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int getExistingNetworkId(String SSID) {
        WifiManager wifiManager = (WifiManager) super.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            for (WifiConfiguration existingConfig : configuredNetworks) {
                if (existingConfig.SSID.equals(SSID)) {
                    return existingConfig.networkId;
                }
            }
        }
        return -1;
    }
    protected void conn(){
        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

        wifiConfig.SSID = getString(ssid);
        //wifiConfig.preSharedKey = getString(R.string.key);
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiConfig.status = WifiConfiguration.Status.ENABLED;

//        int wifiState = wifiManager.getWifiState();
//
//        while(!(wifiManager.getConnectionInfo().getSSID() == getString(ssid)))
//        {
        //mSSID.setText("Disconnecting");
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();

    //}
    }
    public String getCurrentSSID(WifiManager wifiManager) {
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        return ssid;
    }


}
