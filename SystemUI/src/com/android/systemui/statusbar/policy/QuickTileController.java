package com.android.systemui.statusbar.policy;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.util.QuickTileToken;
import android.text.util.QuickTileTokenizer;

public class QuickTileController extends ContentObserver {
	
	Context mContext;
	ContentResolver mResolver;
	QuickTileChangeCallback mCallback;
	
	public interface QuickTileChangeCallback {
		public void onTileChange(List<QuickTileToken> newList);
	}

	public QuickTileController(Context context, Handler handler, QuickTileChangeCallback callback) {
		super(handler);
		
		mContext = context;
		mResolver = context.getContentResolver();
		mCallback = callback;
		
		observe();
	}
	
	public void release(){
		mResolver.unregisterContentObserver(this);
	}
	
	@Override
	public void onChange(boolean selfChange) {
		update();
	}
	
	private void observe(){
		mResolver.registerContentObserver(Settings.System
				.getUriFor(Settings.System.QUICK_SETTINGS_TILES), false,
				this);
	}
	
	private void update(){
		if(mCallback!=null){
			List<QuickTileToken> newList = new ArrayList<QuickTileToken>();
	        QuickTileTokenizer.tokenize(Settings.System.getString(mContext.getContentResolver(), 
	                Settings.System.QUICK_SETTINGS_TILES), newList);
	        
	        if(newList.size()==0){ 
	        	QuickTileTokenizer.tokenize(Settings.System.QUICK_TILES_DEFAULT, newList);
	        }
			mCallback.onTileChange(newList);
		}
	}
	
	

}
