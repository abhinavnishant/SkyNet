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
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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


//        mSSID.setText(R.string.ssid);

    }

    protected void onStart() {

        super.onStart();
        connectToWifi();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        webView = (WebView) findViewById(R.id.autoconfig);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://192.168.4.1/");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                return false;
            }
        });
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
                //mSSID.setText(netId);
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
