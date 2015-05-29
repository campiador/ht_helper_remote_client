package com.campiador.trojanhelper;


public class NetModel {
	
	private String mName;
	private String mZeroTime;
	private String mOneTime;
	private String mTransitionCount;
	
	public NetModel(String name, String zero, String one, String transit){
		mName = name;
		mZeroTime = zero;
		mOneTime = one;
		mTransitionCount = transit;
		
	}
	
	public NetModel(String name){
		mName = name;
	}
	
	public String getTransitionCount() {
		return mTransitionCount;
	}

	public void setTransitionCount(String mTransitionCount) {
		this.mTransitionCount = mTransitionCount;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getZeroTime() {
		return mZeroTime;
	}

	public void setZeroTime(String mZeroTime) {
		this.mZeroTime = mZeroTime;
	}

	public String getOneTime() {
		return mOneTime;
	}

	public void setOneTime(String mOneTime) {
		this.mOneTime = mOneTime;
	}
	
	
	
	
}
