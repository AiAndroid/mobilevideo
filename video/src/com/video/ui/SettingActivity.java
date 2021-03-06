package com.video.ui;

import android.app.ActionBar;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.*;
import android.widget.Toast;
import com.video.ui.idata.iDataORM;
import com.video.ui.tinyui.BasePreferenceActivity;

public class SettingActivity extends /*Base*/PreferenceActivity {
	
	private final String TAG = SettingActivity.class.getName();
	public  boolean isExternalSdcardMounted(Context context){
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(miui.R.style.Theme_Light_Settings);
		super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
		if(actionBar != null) {
			actionBar.setTitle(R.string.app_name);
			actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_back_light));
		}

		addPreferencesFromResource(R.xml.miui_video_setting);
		Preference clearCahcePreference = findPreference("clear_cache");
		clearCahcePreference.setOnPreferenceClickListener(mOnPreferenceClickListener);
		
		if (isExternalSdcardMounted(getBaseContext())) {
			CheckBoxPreference checkmPriorityStorage = (CheckBoxPreference) findPreference("priority_storage");
			checkmPriorityStorage.setChecked(iDataORM.isPriorityStorage(getBaseContext()));
			checkmPriorityStorage.setOnPreferenceClickListener(mOnPreferenceClickListener);
		} else {
			final PreferenceScreen screen = getPreferenceScreen();
			removeChildPreference(screen, "priority_storage");
		} 
		
		CheckBoxPreference playHint = (CheckBoxPreference) findPreference("use_cellular_play_hint");
		playHint.setChecked(iDataORM.isOpenCellularPlayHint(this));
		playHint.setOnPreferenceClickListener(mOnPreferenceClickListener);
		
		CheckBoxPreference offDownloadHint = (CheckBoxPreference) findPreference("use_cellular_offlinedownload_hint");
		offDownloadHint.setChecked(iDataORM.isOpenCellularOfflineHint(this));
		offDownloadHint.setOnPreferenceClickListener(mOnPreferenceClickListener);

		CheckBoxPreference receiveMipush = (CheckBoxPreference) findPreference("receive_mipush");
		receiveMipush.setChecked(iDataORM.isMiPushOn());
		receiveMipush.setOnPreferenceClickListener(mOnPreferenceClickListener);

		CheckBoxPreference open_develop = (CheckBoxPreference) findPreference("open_develop");
		open_develop.setChecked(iDataORM.isDevelopmentOn(this));
		open_develop.setOnPreferenceClickListener(mOnPreferenceClickListener);

		CheckBoxPreference ui_style = (CheckBoxPreference) findPreference("ui_style");
		ui_style.setChecked(iDataORM.getBooleanValue(getApplicationContext(), iDataORM.gridview_ui, false));
		ui_style.setOnPreferenceClickListener(mOnPreferenceClickListener);

	}
	
	//UI callback
	private Preference.OnPreferenceClickListener mOnPreferenceClickListener = new Preference.OnPreferenceClickListener() {
		
		@Override
		public boolean onPreferenceClick(Preference preference) {
			String preKey = preference.getKey();
			if(preKey.equals("clear_cache")) {
				clearCache();
				return true;
			}else if(preKey.equals("priority_storage")){
				iDataORM.setPriorityStorage(getBaseContext(), ((CheckBoxPreference) preference).isChecked());
				return true;
			}else if(preKey.equals("use_cellular_play_hint")){
				iDataORM.setOpenCellularPlayHint(getBaseContext(), ((CheckBoxPreference) preference).isChecked());
				return true;
			}else if(preKey.equals("use_cellular_offlinedownload_hint")){
				iDataORM.setOpenCellularOfflineHint(getBaseContext(), ((CheckBoxPreference) preference).isChecked());
				return true;
			}else if(preKey.equals("receive_mipush")){
				iDataORM.setMiPushOn(getBaseContext(), ((CheckBoxPreference) preference).isChecked());
                return true;
            }else if(preKey.equals("open_develop")){
				iDataORM.setDevelopmentOn(getBaseContext(), ((CheckBoxPreference) preference).isChecked());
			}
			else if(preKey.equals("ui_style")) {
				iDataORM.addSetting(getBaseContext(), iDataORM.gridview_ui, ((CheckBoxPreference) preference).isChecked()?"1":"0");
			}
			return false;
		}
	};
	
	//packaged method
    private void clearCache(){
    	new AsyncClearCacheTask().execute();
    }
    
    private class AsyncClearCacheTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			//clear local cache
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			Toast.makeText(getBaseContext(), R.string.clear_cache_success, Toast.LENGTH_SHORT).show();
		}
    }
    
	public  boolean removeChildPreference(PreferenceGroup preferenceGroup, String key) {
        Preference preference = preferenceGroup.findPreference(key);
        if (preference != null) {
            return removeChildPreference(preferenceGroup, preference);
        }
        return false;
    }

    public  boolean removeChildPreference(PreferenceGroup preferenceGroup, Preference preference) {
        if (preferenceGroup.removePreference(preference)) {
            return true;
        }
        final int childCount = preferenceGroup.getPreferenceCount();
        for (int i=0; i<childCount; i++) {
            final Preference childPreference = preferenceGroup.getPreference(i);
            if ((childPreference instanceof PreferenceGroup) &&
                    removeChildPreference((PreferenceGroup) childPreference, preference)){
                return true;
            }
        }
        return false;
    }
}
