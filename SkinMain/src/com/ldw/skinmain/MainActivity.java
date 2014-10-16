package com.ldw.skinmain;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import dalvik.system.DexClassLoader;

public class MainActivity extends Activity implements OnItemClickListener {

	private List<ResolveInfo> mRlist;
	private ListView mListView;
	private PackageManager mPm;

	private RelativeLayout mMainLayout;
	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		searchSkin();
		initView();
		initListView();
	}

	private void searchSkin() {
		Intent intent = new Intent("com.ldw.skin", null);
		mPm = getPackageManager();

		mRlist = mPm.queryIntentActivities(intent, 0);
//		mRlist = new ArrayList<ResolveInfo>();
//		mRlist.add(null);
////		mRlist.add(null);
	}

	private void initView() {
		mMainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
		mButton = (Button) findViewById(R.id.button);
	}

	private void initListView() {
		mListView = (ListView) findViewById(R.id.listview);
		mListView.setAdapter(new MyAdapter());
		mListView.setOnItemClickListener(this);
	}
	

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mRlist.size();
		}

		@Override
		public ResolveInfo getItem(int position) {
			return mRlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			ResolveInfo info = getItem(position);
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item, null);
				holder = new ViewHolder();
				holder.iv = (ImageView) convertView.findViewById(R.id.item_iv);
				holder.tv = (TextView) convertView.findViewById(R.id.item_tv);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			IPlugin plugin = getPlugin(info);
//			IPlugin plugin = getPlugin(position);
			holder.iv
					.setBackgroundDrawable(plugin.getDrawable("icon", info.activityInfo.packageName));
			holder.tv.setText(plugin.getString(info.activityInfo.packageName));
//			String packageName;
//			if (position == 0) {
//                packageName = "com.ldw.skincustom1";
//            } else {
//                packageName = "com.ldw.skincustom2";
//            }
//	         holder.iv
//             .setBackgroundDrawable(plugin.getDrawable("icon", packageName));
//     holder.tv.setText(plugin.getString(packageName));

			return convertView;
		}

		class ViewHolder {
			TextView tv;
			ImageView iv;
		}
	}

	@SuppressLint("NewApi")
	private IPlugin getPlugin(ResolveInfo rinfo) {
		ActivityInfo ainfo = rinfo.activityInfo;

		String packageName = ainfo.packageName;
		// Ŀ��������apk��jar���ļ���·��
		String dexPath = ainfo.applicationInfo.sourceDir;
		// ����dex�ļ�����apk�У������װ��Ŀ����֮ǰ��Ҫ�ȴ�apk�н�ѹ��dex�ļ������������ǽ�ѹ���ŵ�·��
		String dexOutputDir = getApplicationInfo().dataDir;
		// ָĿ��������ʹ�õ�C/C++���ŵ�·��
		String libPath = ainfo.applicationInfo.nativeLibraryDir;

		DexClassLoader cl = new DexClassLoader(dexPath, dexOutputDir, libPath,
				this.getClass().getClassLoader());
		try {
			Class<?> clazz = cl.loadClass(packageName + ".SelectSkin");
			// ��ȡ�����PluginClass����
			IPlugin plugin = (IPlugin) clazz.newInstance();
			plugin.setContext(this);
			return plugin;
		} catch (Exception e) {
			Log.i("Host", "error", e);
		}
		return null;
	}
	
	@SuppressLint("NewApi")
	private IPlugin getPlugin(int pos) {

        String dexOutputDir = getApplicationInfo().dataDir;
        String libPath = null;
        String dexPath;
        String className;
        if (pos == 0 ) {
            dexPath = "/mnt/sdcard/SkinCustom1.apk";
            className = "com.ldw.skincustom1.SelectSkin";
        } else {
            dexPath = "/mnt/sdcard/SkinCustom2.apk";
            className = "com.ldw.skincustom2.SelectSkin";
        }
        DexClassLoader cl = new DexClassLoader(dexPath, dexOutputDir, libPath,
                this.getClass().getClassLoader());
        try {
            Class<?> clazz = cl.loadClass(className);
            IPlugin plugin = (IPlugin) clazz.newInstance();
            plugin.setContext(this);
            return plugin;
        } catch (Exception e) {
            Log.i("Host", "error", e);
        }
        return null;
    }

	@SuppressWarnings("deprecation")
	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position,
			long arg3) {
		ResolveInfo info = (ResolveInfo) parent.getItemAtPosition(position);
		IPlugin plugin = getPlugin(info);
		mMainLayout.setBackgroundDrawable(plugin.getDrawable("bg",
				info.activityInfo.packageName));
		mButton.setBackgroundDrawable(plugin.getDrawable("button_selector",
				info.activityInfo.packageName));
	}
}
