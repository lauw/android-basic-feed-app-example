package com.muller.instagramfeed.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muller.instagramfeed.R;
import com.muller.instagramfeed.model.Media;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MediaLikesFragment extends Fragment {
	@InjectView(R.id.fragment_likes_count) TextView mDataTextView;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_likes, container, false);
		ButterKnife.inject(this, view);
		return view;
	}

	public void updateLikes(List<Media> mediaList) {
		mDataTextView.setText(getAmountOfLikes(mediaList) + "");
	}

	private int getAmountOfLikes(List<Media> mediaList) {
		int count = 0;

		for (Media media : mediaList)
			count += media.getLikes();

		return count;
	}
}
