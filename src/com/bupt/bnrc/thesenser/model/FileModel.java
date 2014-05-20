package com.bupt.bnrc.thesenser.model;

import java.util.Date;
import java.util.List;

import com.bupt.bnrc.thesenser.dao.DAOFactory;
import com.bupt.bnrc.thesenser.dao.FileDAO;
import com.bupt.bnrc.thesenser.utils.Logger;

import android.content.Context;
import android.content.SharedPreferences;

public class FileModel {
	private Long m_id = null;
	private String m_fileName = null;
	private Date m_createTime = null;
	private PhotoStats m_photoStats = null;
	private Integer m_tag = null; // 用来表示文件类型 ：0为照片文件， 1为视频文件， 2为文本文件，>10为pm2.5训练集，序号相同为一组，3-10为保留
	
	private static DAOFactory daoFactory = DAOFactory.getInstance();
	
	public FileModel(String fileName, Date createTime) {
		if(fileName == null) {
			throw new IllegalArgumentException("File name must not be null");
		}
		this.m_fileName = fileName;
		
		if(createTime == null) {
			throw new IllegalArgumentException("File create time must not be null");
		}
		this.m_createTime = createTime;
		
		this.m_tag = 0;
	}
	
	public FileModel(String fileName, Date createTime, Integer tag) {
		if(fileName == null) {
			throw new IllegalArgumentException("File name must not be null");
		}
		this.m_fileName = fileName;
		
		if(createTime == null) {
			throw new IllegalArgumentException("File create time must not be null");
		}
		this.m_createTime = createTime;
		
		this.m_tag = tag;
	}
	
	public FileModel(String fileName, Date createTime, PhotoStats photostats) {
		this(fileName, createTime);
		this.m_photoStats = photostats;
		this.m_tag = 0;
	}
	
	public FileModel(String fileName, Date createTime, PhotoStats photostats, Integer tag) {
		this(fileName, createTime);
		this.m_photoStats = photostats;
		this.m_tag = tag;
	}
	
	public FileModel(String fileName, Date createTime, Float xDirect, Float yDirect, Float zDirect, 
			Float longitude, Float latitude, Integer exposureValue, Float focalDistance, Float aperture, Integer tag) {
		this(fileName, createTime, tag);
		this.m_photoStats = new PhotoStats(xDirect, yDirect, zDirect, longitude, latitude, exposureValue, focalDistance, aperture);
	}
	
	public FileModel(Long id, String fileName, Date createTime, Float xDirect, Float yDirect, Float zDirect, 
			Float longitude, Float latitude, Integer exposureValue, Float focalDistance, Float aperture, Integer tag) {
		
		this(fileName, createTime, tag);
		this.m_photoStats = new PhotoStats(xDirect, yDirect, zDirect, longitude, latitude, exposureValue, focalDistance, aperture);
		this.m_id = id;
	}

	public FileModel(Long id, FileModel file) {
		this.m_id = id;
		this.m_fileName = file.getFileName();
		this.m_createTime = file.getCreateTime();
		this.m_photoStats = file.getPhotoStats();
		this.m_tag = file.m_tag;
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
	
	public static List<FileModel> findNotUploadFiles(Integer num, Context context) {
		FileDAO dao = null;
		try {
			dao = daoFactory.getFileDAO(context);
			return dao.findNotUploadFiles(num, context);
		} finally {
			if(dao != null) {
				dao.close();
			}
		}
		
	}
	
	public static List<FileModel> findFilesToBeModeled(Integer index, Context context) {
		FileDAO dao = null;
		try {
			dao = daoFactory.getFileDAO(context);
			return dao.findFilesToBeModeled(index, context);
		} finally {
			if(dao != null) {
				dao.close();
			}
		}
	}
	
	public Long getId() {
		return m_id;
	}

	public Date getCreateTime() {
		return m_createTime;
	}

	public String getFileName() {
		return m_fileName;
	}

	public PhotoStats getPhotoStats() {
		return m_photoStats;
	}

	public Integer getTag() {
		return m_tag;
	}

	
	
}
