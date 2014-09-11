package com.bupt.bnrc.thesenser;

import org.pm4j.task.PMConfig;

import com.bupt.bnrc.thesenser.utils.SendData;
import com.bupt.bnrc.thesenser.utils.SendFile;
import com.bupt.bnrc.thesenser.utils.WifiUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mListTitles;
	
	private WifiUtil mWifiUtil;

	private static Context mContext;
	static public final Intent intent = new Intent();
	
	private int mNowPosition;

	private static FragmentFactory fragmentFactory = FragmentFactory.getInstance();

	static {
		PMConfig.init();
	}
	
	public static Context getContext() {
		return mContext;
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_main);
    	
    	initDatas();
    	initViews();
    	
    	if (savedInstanceState == null) {
            selectItem(mNowPosition);
        }
    	Collection.getCollection(this);
    	//final Intent intent = new Intent();
    	intent.setAction("com.bupt.bnrc.thesenser.collection.forCollection");
    	//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    	//intent.addCategory(Intent.CATEGORY_HOME);
		startService(intent);
		mContext = getApplicationContext();
		mWifiUtil = WifiUtil.getInstance(mContext);
		if(mWifiUtil.getWifiState() == 30) {
		    SendData.send(mContext);
		    SendFile.send(mContext);
		}
    }
    
    protected void onPause() {
    	super.onPause();
    	Log.i("Collection Activity", "onPause()");
    }
    
    protected void onStop() {
    	super.onStop();
    	Log.i("Collection Activity", "onStop()");
    }
    
    protected void onDestroy() {
    	super.onDestroy();
    	Log.i("Collection Activity", "onDestroy()");
    }

	private void initDatas() {
		mNowPosition = 0;
		mTitle = mDrawerTitle = getTitle();

		mListTitles = getResources().getStringArray(R.array.main_list_array);
	}

	private void initViews() {
		mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.main_left_drawer);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.main_drawer_list_item, mListTitles));
		// Set the list's click listener
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open, /* "open drawer" description for accessibility */
				R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
				// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_take_photo).setVisible(!drawerOpen);
		menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		// update the action items again depends on the fragment
		hideActionButton(menu);
		return super.onPrepareOptionsMenu(menu);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}

	}

	private void selectItem(int position) {
		// get tag
		String tag = getTagFromPosition(position);
		// update the main content by replacing fragments
		Fragment fragment = fragmentFactory
				.createFragment(getFragmentType(position));
		Bundle args = new Bundle();
		args.putInt(FragmentFactory.ARG_MAIN_INDEX, position);
		fragment.setArguments(args);
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, tag).commit();
		// update selected item and title, then close the drawer
		mNowPosition = position;
		mDrawerList.setItemChecked(position, true);
		setTitle(mListTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	private String getTagFromPosition(int position) {
		String tag = null;
		switch (position) {
		case 0:
			tag = "collect";
			break;
		case 1:
			tag = "pmtools";
			break;
		case 2:
			tag = "tasks";
			break;
		case 3:
			tag = "user";
			break;
		case 4:
			tag = "settings";
			break;
		case 5:
			tag = "test";
			break;
		default:
			break;
		}
		return tag;
	}

	private void hideActionButton(Menu menu) {
		switch (mNowPosition) {
		case 0:
			menu.findItem(R.id.action_take_photo).setVisible(false);
			break;
		case 1:
			menu.findItem(R.id.action_refresh).setVisible(false);
			break;
		case 2:
		case 3:
		case 4:
			menu.findItem(R.id.action_take_photo).setVisible(false);
			menu.findItem(R.id.action_refresh).setVisible(false);
			break;
		case 5:
			menu.findItem(R.id.action_take_photo).setVisible(false);
			menu.findItem(R.id.action_refresh).setVisible(false);
			break;
		default:
			break;
		}
	}

	private String getFragmentType(int position) {
		String re = null;
		switch (position) {
		case 0:
			re = "collect";
			break;
		case 1:
			re = "pmtools";
			break;
		case 2:
			re = "tasks";
			break;
		case 3:
			re = "user";
			break;
		case 4:
			re = "settings";
			break;
		case 5:
			re = "test";
			break;
		default:
			break;
		}
		return re;
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_take_photo:
			takePhoto();
			return true;
		case R.id.action_refresh:
			refreshDataForInfo();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void refreshDataForInfo() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		InfoCollectParentFragment fragment = (InfoCollectParentFragment) fragmentManager
				.findFragmentByTag("collect");
		fragment.refreshDataView();
	}

	private void takePhoto() {
		 FragmentManager fragmentManager = getSupportFragmentManager();
		 PMToolsParentFragment fragment = (PMToolsParentFragment)fragmentManager.findFragmentByTag("pmtools");
		 fragment.takePhoto();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
