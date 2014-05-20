package com.bupt.bnrc.thesenser.model;

import java.util.Date;
import java.util.List;

import android.content.Context;

import com.bupt.bnrc.thesenser.dao.DAOFactory;
import com.bupt.bnrc.thesenser.dao.DataDAO;
import com.bupt.bnrc.thesenser.dao.FileDAO;
import com.bupt.bnrc.thesenser.utils.Logger;

public class DataModel {
	private Long m_id = null;
	private Float m_lightIntensity = null; //光照
	private Float m_soundIntensity = null; // 声音
	private Date m_createTime = null; // 采集时间
	private Integer m_chargeState = null; // 0：未充电， 1：充电
	private Integer m_batteryState = null; // 电量的百分比
	private Integer m_netState = null; //0:无网络    1:2G 2:3G 3:4G 4：wifi
	private Float m_longitude = null;
	private Float m_latitude = null;
	
	private static DAOFactory daoFactory = DAOFactory.getInstance();
	
	public DataModel(Float lightIntensisy, Float soundIntensity, Date createTime, Integer chargeState, 
			Integer batteryState, Integer netState, Float longtitude, Float latitude) {
		if(lightIntensisy == null && soundIntensity == null) {
			throw new IllegalArgumentException("light or sound must have at least one not null");
		}
		this.m_lightIntensity = lightIntensisy;
		this.m_soundIntensity = soundIntensity;
		
		if(createTime == null) {
			throw new IllegalArgumentException("Date create time must not be null");
		}
		this.m_createTime = createTime;
		
		this.m_chargeState = chargeState;
		this.m_batteryState = batteryState;
		this.m_netState = netState;
		this.m_longitude = longtitude;
		this.m_latitude = latitude;
	}
	
	public DataModel(Long id, Float lightIntensisy, Float soundIntensity, Date createTime, Integer chargeState, 
			Integer batteryState, Integer netState, Float longtitude, Float latitude) {
		if(lightIntensisy == null && soundIntensity == null) {
			throw new IllegalArgumentException("light or sound must have at least one not null");
		}
		this.m_id = id;
		this.m_lightIntensity = lightIntensisy;
		this.m_soundIntensity = soundIntensity;
		
		if(createTime == null) {
			throw new IllegalArgumentException("Date create time must not be null");
		}
		this.m_createTime = createTime;
		
		this.m_chargeState = chargeState;
		this.m_batteryState = batteryState;
		this.m_netState = netState;
		this.m_longitude = longtitude;
		this.m_latitude = latitude;
	}
	
	public DataModel(Long id, DataModel data) {
		this.m_id = id;
		this.m_lightIntensity = data.getLightIntensity();
		this.m_soundIntensity = data.getSoundIntensity();
		this.m_createTime = data.getCreateTime();
		this.m_chargeState = data.getChargeState();
		this.m_batteryState = data.getBatteryState();
		this.m_netState = data.getNetState();
		this.m_latitude = data.getLatitude();
		this.m_longitude = data.getLongitude();
	}
	
	public DataModel save(Context context) {
		DataDAO dao = null;
		DataModel datas = null;
		try {
			dao = daoFactory.getDataDAO(context);
			datas = dao.save(this);
		} catch (Exception e) {
			// TODO: handle exception
			Logger.e(e.getMessage());
		} finally {
			dao.close();
		}
		
		return datas;
	}
	
	public static List<DataModel> findNotUploadDatas(Integer num, Context context) {
		DataDAO dao = null;
		try {
			dao = daoFactory.getDataDAO(context);
			return dao.findNotUploadDatas(num, context);
		} finally {
			if(dao != null) {
				dao.close();
			}
		}
		
	}

	public Long getId() {
		return m_id;
	}

	public Float getLightIntensity() {
		return m_lightIntensity;
	}

	public Float getSoundIntensity() {
		return m_soundIntensity;
	}

	public Integer getChargeState() {
		return m_chargeState;
	}

	public Integer getBatteryState() {
		return m_batteryState;
	}

	public Integer getNetState() {
		return m_netState;
	}

	public Date getCreateTime() {
		return m_createTime;
	}

	public Float getLongitude() {
		return m_longitude;
	}

	public Float getLatitude() {
		return m_latitude;
	}

}
