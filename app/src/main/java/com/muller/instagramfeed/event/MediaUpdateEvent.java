package com.muller.instagramfeed.event;

import com.muller.instagramfeed.model.Media;

import java.util.List;

public class MediaUpdateEvent {
	private List<Media> mMediaList;

	public MediaUpdateEvent(List<Media> mediaList) {
		mMediaList = mediaList;
	}

	public List<Media> getMediaList() {
		return mMediaList;
	}
}
