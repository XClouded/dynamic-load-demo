package com.ldw.skincustom1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.ldw.skinmain.IPlugin;

public class SelectSkin implements IPlugin {
	
	private Context mContext;

	@Override
	public String getString(String packageName) {
		if(mContext == null) {
			throw new IllegalArgumentException("you should invoke setContext first!");
		}
		try {
			PackageManager mPm = mContext.getPackageManager();
			Resources res = mPm.getResourcesForApplication(packageName);
			int id = res.getIdentifier("app_name", "string", packageName);
			return res.getString(id);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public Drawable getDrawable(String name, String packageName) {
		if(mContext == null) {
			throw new IllegalArgumentException("you should invoke setContext first!");
		}
		try {
			PackageManager mPm = mContext.getPackageManager();
			Resources res = mPm.getResourcesForApplication(packageName);
			int id = res.getIdentifier(name, "drawable", packageName);
			return res.getDrawable(id);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void setContext(Context context) {
		this.mContext = context;
	}

}
