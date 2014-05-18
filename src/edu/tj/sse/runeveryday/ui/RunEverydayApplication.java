package edu.tj.sse.runeveryday.ui;

import android.app.Application;

public class RunEverydayApplication extends Application {
	private boolean isConnected = false;
	
	public boolean getIsConnected() {
		return isConnected;
	}
	public void setIsConnected(boolean f) {
		isConnected = f;
	}
}
