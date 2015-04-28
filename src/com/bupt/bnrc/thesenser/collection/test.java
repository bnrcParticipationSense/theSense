/**
 * 
 */
package com.bupt.bnrc.thesenser.collection;

import java.util.Date;

/**
 * @author zhuzhiyuan
 *
 */
public class test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
    @SuppressWarnings("unused")
    
    public class DataModel{
        private Long m_id = null;               //该条DataModel数据在数据库中的位置
                                                //若不在数据库中则为null
        private Float m_lightIntensity = null;  //光照强度
        private Float m_soundIntensity = null;  //声音强度
        private Date m_cteateTime = null;       //该条数据记录的采集时间
        private Integer m_chargeState = null;   //充电状态，0为未充电，1为正在充电
        private Integer m_batteryState = null;  //电池剩余电量百分比
        private Integer m_netState = null;      //网络状况，表示是否开启网络连接
        private Float m_longitude = null;       //经度
        private Float m_latitude = null;        //纬度
    }       

}

