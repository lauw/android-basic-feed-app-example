package com.muller.instagramfeed.api;

import com.muller.instagramfeed.model.MediaResponse;

import retrofit.Callback;
import retrofit.http.GET;

public interface ApiService {
	@GET("/v1/media/popular")
	void getPopularMedia(Callback<MediaResponse> callback);
}
