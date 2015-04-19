package com.muller.instagramfeed.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.muller.instagramfeed.R;
import com.muller.instagramfeed.adapter.MediaAdapter;
import com.muller.instagramfeed.event.MediaUpdateEvent;
import com.muller.instagramfeed.model.Media;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MediaActivity extends BaseActivity {
	@InjectView(R.id.media_recycler_view) RecyclerView mRecyclerView;
	private List<Media> mPopularMedia = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media);
		ButterKnife.inject(this);

		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		MediaAdapter adapter = new MediaAdapter(mRecyclerView, mPopularMedia);
		mRecyclerView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_media, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_data) {
			showDataActivity(); /** refresh data **/
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void showDataActivity() {
		Intent intent = new Intent(MediaActivity.this, MediaInfoActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (mPopularMedia.isEmpty())
			loadPopularMedia();
	}

	@Subscribe
	public void onMediaUpdate(MediaUpdateEvent event) {
		mPopularMedia.clear();
		mPopularMedia.addAll(event.getMediaList());
		((MediaAdapter)mRecyclerView.getAdapter()).refresh();
	}
}
