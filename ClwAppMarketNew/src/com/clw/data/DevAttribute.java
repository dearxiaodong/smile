package com.clw.data;

public class DevAttribute {
	
	private static DevAttribute instance = null;
	
	private int screenWidth;
	private int screenHeight;
	private float density;
	private int topHeight;
	
	private DevAttribute() {
		
	}
	
	public static DevAttribute getInstance() {
		if (instance == null) {
			synchronized (DevAttribute.class) {
				if (instance == null) {
					instance = new DevAttribute();
				}
			}
		}
		
		return instance;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public float getDensity() {
		return density;
	}

	public void setDensity(float density) {
		this.density = density;
	}

	public int getTopHeight() {
		return topHeight;
	}

	public void setTopHeight(int topHeight) {
		this.topHeight = topHeight;
	}

}
