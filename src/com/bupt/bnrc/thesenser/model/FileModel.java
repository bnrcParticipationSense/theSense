package com.bupt.bnrc.thesenser.model;

import java.io.File;
import java.util.Date;

import com.bupt.bnrc.thesenser.dao.DAOFactory;
import com.bupt.bnrc.thesenser.dao.FileDAO;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.content.Context;

public class FileModel {
	private Long m_id = null;
	private String m_fileName = null;
	private Date m_createTime = null;
	private PhotoStats m_photoStats = null;
	
	private static DAOFactory daoFactory = DAOFactory.getInstance();
	
	public FileModel(String fileName, Date createTime) {
		if(fileName == null) {
			throw new IllegalArgumentException("File name must not be null");
		}
		this.setFileName(fileName);
		
		if(createTime == null) {
			throw new IllegalArgumentException("File create time must not be null");
		}
		this.setCreateTime(createTime);
	}
	
	public FileModel(String fileName, Date createTime, PhotoStats photostats) {
		this(fileName, createTime);
		this.setPhotoStats(photostats);
	}
	
	public FileModel(String fileName, Date createTime, Float xDirect, Float yDirect, Float zDirect, 
			Float longitude, Float latitude, Integer exposureValue, Float focalDistance, Float aperture) {
		this(fileName, createTime);
		this.setPhotoStats(new PhotoStats(xDirect, yDirect, zDirect, longitude, latitude, exposureValue, focalDistance, aperture));
	}

	public FileModel(Long id, FileModel file) {
		this.m_id = id;
		this.m_fileName = file.getFileName();
		this.m_createTime = file.getCreateTime();
		this.m_photoStats = file.getPhotoStats();
	}
	
	public FileModel save(Context context) {
		FileDAO dao = null;
		FileModel file = null;
		try {
			dao = daoFactory.getFileDAO(context);
			file = dao.save(this);
		} catch (Exception e) {
			// TODO: handle exception
			Logger.e(e.getMessage());
		} finally {
			dao.close();
		}
		
		return file;
	}
	
	
	
	public Long getId() {
		return m_id;
	}

	public Date getCreateTime() {
		return m_createTime;
	}

	public void setCreateTime(Date m_createTime) {
		this.m_createTime = m_createTime;
	}

	public String getFileName() {
		return m_fileName;
	}

	public void setFileName(String m_fileName) {
		this.m_fileName = m_fileName;
	}

	public PhotoStats getPhotoStats() {
		return m_photoStats;
	}

	public void setPhotoStats(PhotoStats m_photoStats) {
		this.m_photoStats = m_photoStats;
	}

	
	
}
