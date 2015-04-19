package com.muller.instagramfeed.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.muller.instagramfeed.App;
import com.muller.instagramfeed.R;
import com.muller.instagramfeed.api.ApiService;
import com.muller.instagramfeed.event.MediaUpdateEvent;
import com.muller.instagramfeed.model.MediaResponse;
import com.squareup.otto.Bus;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public abstract class BaseActivity extends ActionBarActivity {
	private MenuItem mRefreshMenuItem;
	@Inject Bus mEventBus;
	@Inject ApiService mApiService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		App.DependencyInjector.inject(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mRefreshMenuItem = menu.findItem(R.id.action_refresh);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_refresh) {
			loadPopularMedia(); /** refresh data **/
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		mEventBus.register(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		mEventBus.unregister(this);
	}

	protected void loadPopularMedia() {
		showProgressBar();

		mApiService.getPopularMedia(new Callback<MediaResponse>() {
			@Override
			public void success(MediaResponse media, Response response) {
				hideProgressBar();
				mEventBus.post(new MediaUpdateEvent(media.getData()));
			}

			@Override
			public void failure(RetrofitError error) {
				error.printStackTrace();
				hideProgressBar();
			}
		});
	}

	protected void showProgressBar() {
		if (mRefreshMenuItem != null)
			mRefreshMenuItem.setActionView(R.layout.actionbar_progress);
	}

	protected void hideProgressBar() {
		if (mRefreshMenuItem != null)
			mRefreshMenuItem.setActionView(null);
	}
}
