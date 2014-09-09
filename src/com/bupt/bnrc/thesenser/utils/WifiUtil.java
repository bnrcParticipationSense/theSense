/**
 * 获取Wi-Fi相关信息
 */
package com.bupt.bnrc.thesenser.utils;

/**
 * @author zhuzhiyuan
 * 2014-8
 */

import java.util.List;

import android.net.wifi.WifiManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;

import android.content.Context;

public class WifiUtil {

    private static WifiUtil sInstance = null;

    WifiManager mWifiManager = null;
    WifiInfo mWifiInfo = null;
    List<ScanResult> mWifiList = null;
    Context mContext = null;

    public synchronized static WifiUtil getInstance(Context context) {
        if (null == sInstance) {
            sInstance = new WifiUtil(context);
        }
        return sInstance;
    }

    private WifiUtil(Context context) {
        mContext = context;
        mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 获取Wi-Fi状态
     * @return WIFI_STATE_DISABLED, WIFI_STATE_DISABLING, WIFI_STATE_ENABLED, WIFI_STATE_ENABLING, WIFI_STATE_UNKNOWN
     */
    public int getWifiState() {
        int state = mWifiManager.getWifiState();
        //Msg.logMsg("" + state);
        //LogMsgUtil.toastMsg("Wi-Fi状态：" + (state==3?"开":"关"), mContext);
        return state;
    }

    /**
     * 返回当前Wi-Fi连接的信息
     * @return WifiInfo 包含当前连接的SSID、IpAddress、LinkSpeed、MacAddress等信息
     */
    public WifiInfo getWifiInfo() {

        if (mWifiManager.getWifiState() == 1)
            return null;

        mWifiInfo = mWifiManager.getConnectionInfo();
        //Msg.logMsg(mWifiInfo.toString());
        if (mWifiInfo == null) {
            //Msg.logMsg("null");
            // LogMsgUtil.toastMsg("null", mContext);
        } else {
            //Msg.logMsg(mWifiInfo.getSSID());
        }
        return mWifiInfo;
    }
    
    /**
     * 获取当前连接的SSID
     * @return 当前连接的SSID
     */
    public String getWifiSSID() {
        if (mWifiManager.getWifiState() == 1)
            return null;
        return mWifiManager.getConnectionInfo().getSSID();
    }

    private List<ScanResult> getScanResult() {

        if (mWifiManager.getWifiState() == 1)
            return null;

        mWifiList = mWifiManager.getScanResults();
        //Msg.logMsg("Scan Result size is " + mWifiList.size());
        //LogMsgUtil.toastMsg("Scan Result size is " + mWifiList.size(), mContext);
//        for (ScanResult temp : mWifiList) {
//            LogMsgUtil.logMsg(temp.SSID + "," + temp.capabilities + "," + temp.BSSID + "," + temp.frequency + "," + ","
//                    + temp.level);
//        }
        return mWifiList;
    }
    
    /**
     * 获取当前可搜索到的Wi-Fi连接数
     * @return
     */
    public int getScanCount() {
        getScanResult();
        if(null != mWifiList)
            return mWifiList.size();
        else
            return 0;
    }

}
