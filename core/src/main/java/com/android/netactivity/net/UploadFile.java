/**
 * UploadFile.java
 * project_name: cn.yoho.yohoevogue.activity.startActivity
 *
 * Created by sunny on 2013-2-22 ����9:49:57
 * Copyright (c) 2013�� NewPower Co. All rights reserved.
 *
 */

package com.android.netactivity.net;

/**
 * @author sunny
 *
 */
public class UploadFile {
    private String mType ;  //image: voice ,file ,video
    private String mFilePath ;
    private String mFile ;
	private byte[] mBytes ;
	public static final String IMAGE = "image";
	public static final String FILE = "file";
	public static final String VOICE = "voice";
	public static final String VOIDE = "video";
	public UploadFile(String mType, String mFilePath)
	{
		this.mType = mType;
		this.mFilePath = mFilePath;
	}
	public UploadFile(String mType, String mFilePath,byte[] mBytes)
	{
		this.mType = mType;
		this.mFilePath = mFilePath;
		this.mBytes=mBytes;
	}

	public String getmType() {
		return mType;
	}
	public void setmType(String mType) {
		this.mType = mType;
	}
	public String getmFilePath() {
		return mFilePath;
	}

	public byte[] getmBytes() {
		return mBytes;
	}

	public void setmBytes(byte[] mBytes) {
		this.mBytes = mBytes;
	}

	public void setmFilePath(String mFilePath) {
		this.mFilePath = mFilePath;
	}
	public String getmFile() {
		return mFile;
	}
	public void setmFile(String mFile) {
		this.mFile = mFile;
	}
    
    
    
    
}
