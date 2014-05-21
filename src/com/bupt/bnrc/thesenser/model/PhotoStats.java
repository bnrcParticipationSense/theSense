package com.bupt.bnrc.thesenser.model;

public class PhotoStats {
	private Float m_xDirect = null;
	private Float m_yDirect = null;
	private Float m_zDirect = null;
	private Float m_longitude = null;
	private Float m_latitude = null;
	private Integer m_exposureValue = null;
	private Float m_focalDistance = null;
	private Float m_aperture = null;
	
	public PhotoStats(Float xDirect, Float yDirect, Float zDirect, Float longitude, 
			Float latitude, Integer exposureValue, Float focalDistance, Float aperture) {
		this.m_xDirect = xDirect;
		this.m_yDirect = yDirect;
		this.m_zDirect = zDirect;
		this.m_latitude = latitude;
		this.m_longitude = longitude;
		this.m_exposureValue = exposureValue;
		this.m_focalDistance = focalDistance;
		this.m_aperture = aperture;
	}
	public Float getXDirect() {
		return m_xDirect;
	}
	
	public Float getYDirect() {
		return m_yDirect;
	}
	
	public Float getZDirect() {
		return m_zDirect;
	}
	
	public Float getLongitude() {
		return m_longitude;
	}
	
	public Float getLatitude() {
		return m_latitude;
	}
	
	public Integer getExposureValue() {
		return m_exposureValue;
	}
	
	public Float getFocalDistance() {
		return m_focalDistance;
	}
	
	public Float getAperture() {
		return m_aperture;
	}

}
