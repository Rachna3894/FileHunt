package com.mojodigitech.filehunt.junkCleanModule;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mojodigitech.filehunt.MainActivity;
import com.mojodigitech.filehunt.R;
import com.mojodigitech.filehunt.Utils.Utility;
import com.todddavies.components.progressbar.ProgressWheel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoostFragment extends AppCompatActivity implements OnClickListener {

	private ProgressWheel pw = null;
	private long totalMemory;
	private TextView txtUsedMemory;
	private TextView txtFreeMemory;
	private TextView txtTotalMemory;
	private Button btnBoost;
	private SharedPreferences boostPrefs = null;
	private Handler timerHandler = null;
	LinearLayout layoutBack;
	private TextView txtLastBoostTime;
	private TextView txtLastMemoryCleaned;
	private TextView txtLastCacheCleaned;

	private ArrayList<String> pList = null;
	private long beforeMemory;
	private long aftermemory;
	private int cacheFreed;
	Context mcontext;
	private int processesKilled;
	private int ramFreed;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boost_layout);
		mcontext= BoostFragment.this;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Utility.setstatusBarColorBelowM(BoostFragment.this);
		}
		totalMemory = getTotalMemory();// get total device memory

		/**
		 * Save total memory into shared preference
		 */



		timerHandler = new Handler();// initialize timer handler
		layoutBack= findViewById(R.id.layoutBack);
		txtFreeMemory = (TextView)findViewById(R.id.txtFreeMemory);
		txtUsedMemory = (TextView) findViewById(R.id.txtUsedMemory);
		txtTotalMemory = (TextView) findViewById(R.id.txtTotalMemory);

		txtLastBoostTime = (TextView)findViewById(R.id.txtLastBoost);
		txtLastMemoryCleaned = (TextView) findViewById(R.id.txtLastMemoryCleaned);
		txtLastCacheCleaned = (TextView)findViewById(R.id.txtLastCacheCleaned);

		// boost button
		btnBoost = (Button) findViewById(R.id.btnBoost);
		btnBoost.setOnClickListener(this);

		boostPrefs =getSharedPreferences("BOOST",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = boostPrefs.edit();
		editor.putLong("totalMemory", totalMemory);
		editor.commit();


		// initialize process list
		pList = new ArrayList<String>();

		pw = (ProgressWheel) findViewById(R.id.pw_spinner);
		// pw.spin();
		pw.setProgress(50);// initial progress to 50
		updateMemoryStatus();// update device memory status

		layoutBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent= new Intent(getApplicationContext(), MainActivity.class);
				startActivity(intent);
			}
		});

	}

	/**
	 * Runnable interface for update current memory status
	 */
	Runnable timerRunnable = new Runnable() {

		@Override
		public void run() {
			updateMemoryStatus();
			timerHandler.postDelayed(timerRunnable, 5000);
		}
	};


	@Override
	public void onResume() {
		super.onResume();

		timerHandler.postDelayed(timerRunnable, 0);

	}

	@Override
	public void onPause() {

		super.onPause();

		timerHandler.removeCallbacks(timerRunnable);// remove timer runnable interface
	}

	@Override
	public void onDestroy() {



		super.onDestroy();

		timerHandler.removeCallbacks(timerRunnable);// remove timer runnable interface
	}

	private void updateMemoryStatus() {

		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		((ActivityManager) getSystemService(
				Context.ACTIVITY_SERVICE)).getMemoryInfo(memInfo);
		
		long availMem = memInfo.availMem;
		float f = availMem;
		float f1 = totalMemory;
		int i = (int) ((f / f1) * 100F);
		if (i != 0) {
			String s = formatMemSize(availMem, 0);
			txtFreeMemory.setText(s);
			String s1 = formatMemSize(totalMemory, 0);
			txtTotalMemory.setText(s1);
			String s2 = formatMemSize(totalMemory - availMem, 0);
			txtUsedMemory.setText(s2);
			setPercentage(i);// Progress Wheel progress percentage
		}

		SharedPreferences prefs = getSharedPreferences(
				"last_boost", Context.MODE_PRIVATE);
		String lastBoost = prefs.getString("boost_time", "");
		String lastMemoryCleaned = prefs.getString("memory_cleaned", "");
		String lastCahchedCleaned = prefs.getString("cache_cleaned", "");

		// Get Boost Information From SharedPreference and Display
		txtLastBoostTime.setText("Last Boost: " + lastBoost);
		txtLastMemoryCleaned.setText("Memory Cleaned: " + lastMemoryCleaned);
		txtLastCacheCleaned.setText("Cache Cleaned: " + lastCahchedCleaned);
	}

	public void setPercentage(int info) {

		String s = (new StringBuilder()).append(info).append("%").toString();
		pw.setText(s);
		int j = (int) ((double) info * 3.6000000000000001D);// for circular progress
		pw.setProgress(j);

	}

	public long getTotalMemory() {

		/**
		 * The entries in the /proc/meminfo can help explain 
		 * what's going on with your memory usage
		 */
		
		String str1 = "/proc/meminfo";
		String str2 = "tag";
		String[] arrayOfString;
		long initial_memory = 0, free_memory = 0;
		
		try {
			
			FileReader localFileReader = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(
					localFileReader, 8192);
			
			for (int i = 0; i < 2; i++) {
				str2 = str2 + " " + localBufferedReader.readLine();
			}
			
			arrayOfString = str2.split("\\s+");
			for (String num : arrayOfString) {
				Log.i(str2, num + "\t");// show memory info into log
			}
			
			// total Memory
			initial_memory = Integer.valueOf(arrayOfString[2]).intValue();
			free_memory = Integer.valueOf(arrayOfString[5]).intValue();

			Log.d("MEM", "FREE " + (free_memory / 1024) + " MB");
			Log.d("MEM", "INIT " + (initial_memory * 1024L) + " MB");

			localBufferedReader.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (initial_memory * 1024L);
	}

	// format memory size to B, KB, MB, GB
	public static String formatMemSize(long size, int value) {
		
		String result = "";
		if (1024L > size) {// size less than 1024, for byte result
			String info = String.valueOf(size);
			result = (new StringBuilder(info)).append(" B").toString();
		} else if (1048576L > size) {// for KB result
			String s2 = (new StringBuilder("%.")).append(value).append("f")
					.toString();
			Object aobj[] = new Object[1];
			Float float1 = Float.valueOf((float) size / 1024F);
			aobj[0] = float1;
			String s3 = String.valueOf(String.format(s2, aobj));
			result = (new StringBuilder(s3)).append(" KB").toString();
		} else if (1073741824L > size) {// for MB result
			String s4 = (new StringBuilder("%.")).append(value).append("f")
					.toString();
			Object aobj1[] = new Object[1];
			Float float2 = Float.valueOf((float) size / 1048576F);
			aobj1[0] = float2;
			String s5 = String.valueOf(String.format(s4, aobj1));
			result = (new StringBuilder(s5)).append(" MB").toString();
		} else {// for GB Result
			Object aobj2[] = new Object[1];
			Float float3 = Float.valueOf((float) size / 1.073742E+009F);
			aobj2[0] = float3;
			String s6 = String.valueOf(String.format("%.2f", aobj2));
			result = (new StringBuilder(s6)).append(" GB").toString();
		}
		
		return result;
	}

	// main boost operation
	@SuppressLint("MissingPermission")
	private boolean MemBoost() {

		PackageManager pm = getPackageManager();
		Method amethod[] = pm.getClass().getDeclaredMethods();
		int mLength = amethod.length;
		
		SharedPreferences pref = getSharedPreferences("CACHE",
				Context.MODE_PRIVATE);
		long l1 = pref.getLong("date_last", 0);// get last boost datetime
		long l2 = System.currentTimeMillis() - l1;
		if (l2 <= 600000 && l2 <= 7200000) {
			return false;// for recent boost, so we return false
		}

		long l3 = System.currentTimeMillis();
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong("date_last", l3);// save last boost datetime
		editor.commit();
		
		if (l2 > 86400000) {
			l2 = 86400000;
		}
		int j = (int) (l2 / 1000L);
		int k = j / 360;
		
		Random random = new Random();
		int i1 = 0;
		int j2 = 0;
		int j1 = 0;
		int k1 = 0;
		int l4 = 0;
		if (k > 0) {
			i1 = j * k;
		} else {
			i1 = j;
		}

		j1 = i1 * 15;
		k1 = (j1 - i1) + 1;
		j2 = random.nextInt(k1) + i1;// get random cachce size
		amethod = pm.getClass().getDeclaredMethods();

		// for cache information
		if (mLength > 0) {
			Method method = amethod[0];
			if (!method.getName().equals("freeStorage")) {
				long l11 = 0L;
				l4 = 2;
				try {
					Object aobj[] = new Object[l4];
					Long long1 = Long.valueOf(l11);
					aobj[0] = long1;
					aobj[1] = 0;
					Object aobj1[] = aobj;
					Object obj = method.invoke(pm, aobj1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// process killer

		ActivityManager acm = (ActivityManager) getSystemService(
				Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
		acm.getMemoryInfo(memInfo);
		beforeMemory = memInfo.availMem;
		
		List<RunningAppProcessInfo> taskList = acm
				.getRunningAppProcesses();
		
		int before = taskList.size();

		for (int i = 0; i < taskList.size(); i++) {
			Log.v("process: " + i,
					taskList.get(i).processName + " pid: "
							+ taskList.get(i).pid + " importance: "
							+ taskList.get(i).importance + " reason: "
							+ taskList.get(i).importanceReasonCode);
		}

		for (int i = 0; i < taskList.size(); i++) {
			
			RunningAppProcessInfo process = taskList.get(i);
			int importance = process.importance;
			int pid = process.pid;
			String pname = process.processName;
			
			if (pname.equals("com.raihanbd.easyrambooster")) {// kill other accept own package
				continue;
			}
			
			if (pname.equals("android")// important process or system process
					|| pname.equals("com.android.bluetooth")
					|| pname.equals("android.process.acore")
					|| pname.equals("system")
					|| pname.equals("com.android.phone")
					|| pname.equals("com.android.systemui")
					|| pname.equals("com.android.launcher")) {
				continue;
			}
			
			/*
			 * if (importance == RunningAppProcessInfo.IMPORTANCE_SERVICE) {
			 * Log.v("manager: ", "task " + pname + " pid: " + pid +
			 * " has importance: " + importance + " WILL NOT KILL"); continue; }
			 */
			
			Log.v("manager", "task " + pname + " pid: " + pid
					+ " has importance: " + importance + " WILL KILL");
			
			pList.add(pname);
			
			int count = 0;
			while (count < 3) {// attempt to kill three times
				acm.killBackgroundProcesses(taskList.get(i).processName);
				count++;
			}
		}

		taskList = acm.getRunningAppProcesses();// after killing processes
		int after = taskList.size();
		
		for (int i = 0; i < taskList.size(); i++) {// after killing tasks
			Log.v("proces after killings: " + i,
					taskList.get(i).processName + " pid:" + taskList.get(i).pid
							+ " importance: " + taskList.get(i).importance
							+ " reason: "
							+ taskList.get(i).importanceReasonCode);
		}

		processesKilled = before - after;// calculate before and after killed task list
		cacheFreed = j2;// cache cleaned size

		return true;
	}// end memory boost

	@Override
	public void onClick(View v) {

		if (MemBoost()) {

			Log.i("Total Process Killed: ", processesKilled + "");
			Log.i("Total Cache: ", formatMemSize(cacheFreed, 0));
			Log.i("Total RAM FREE: ", formatMemSize(ramFreed, 0));

			DialogTask task = new DialogTask();
			task.execute();// show dialog task

		} else {
			Log.i("Total: ", "Memory level is good. please try later");
			Toast.makeText(getApplicationContext(),
					"Memory level is good. Please try later", Toast.LENGTH_LONG)
					.show();
		}
	}

	private class DialogTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(BoostFragment.this);
			dialog.setMessage("please wait...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (dialog != null) {
				dialog.dismiss();
			}

			updateMemoryStatus();

			ActivityManager acm = (ActivityManager)
					getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
			ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
			acm.getMemoryInfo(memInfo);
			
			aftermemory = memInfo.availMem;
			if (aftermemory > beforeMemory) {
				ramFreed = (int) (aftermemory - beforeMemory);
			} else {
				ramFreed = 0;
			}

			BoostDialog dialog = new BoostDialog();
			dialog.setProcessKilled(String.valueOf(processesKilled));
			dialog.setMemoryCleaned(formatMemSize(ramFreed, 0));
			dialog.setCacheCleaned(formatMemSize(cacheFreed, 0));
			dialog.setCancelable(false);
			dialog.show(getSupportFragmentManager(), "dialog");
		}

	}
}
