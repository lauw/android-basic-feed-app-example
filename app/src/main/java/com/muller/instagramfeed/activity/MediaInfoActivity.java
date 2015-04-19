package com.muller.instagramfeed.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import com.muller.instagramfeed.R;
import com.muller.instagramfeed.event.MediaUpdateEvent;
import com.muller.instagramfeed.fragment.MediaDataFragment;
import com.muller.instagramfeed.fragment.MediaLikesFragment;
import com.muller.instagramfeed.model.Media;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.ButterKnife;

public class MediaInfoActivity extends BaseActivity {
	private MediaDataFragment mDataFragment;
	private MediaLikesFragment mLikesFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_info);
		ButterKnife.inject(this);

		mDataFragment = new MediaDataFragment();
		mLikesFragment = new MediaLikesFragment();

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.media_info_fragment_data, mDataFragment);
		transaction.add(R.id.media_info_fragment_likes, mLikesFragment);

		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_media_info, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Subscribe
	public void onMediaUpdate(MediaUpdateEvent event) {
		updateFragments(event.getMediaList());
	}

	private void updateFragments(List<Media> mediaList) {
		mDataFragment.updateDataCount(mediaList);
		mLikesFragment.updateLikes(mediaList);
	}
}
