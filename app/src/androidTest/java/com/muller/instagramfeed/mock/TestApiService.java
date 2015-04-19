package com.muller.instagramfeed.mock;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muller.instagramfeed.api.ApiService;
import com.muller.instagramfeed.model.Media;
import com.muller.instagramfeed.model.MediaResponse;
import com.muller.instagramfeed.util.TestUtils;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.client.Header;
import retrofit.client.Response;

public class TestApiService implements ApiService {

	@Override
	public void getPopularMedia(Callback<MediaResponse> callback) {
		String json = TestUtils.loadJSONFromFile(TestUtils.getTestCase().getInstrumentation().getContext(), "api/v1/media/popular.json");

		Gson gson = new GsonBuilder().registerTypeAdapter(Media.class, new Media.GsonDeserializer()).create();
		MediaResponse mediaResponse = gson.fromJson(json, MediaResponse.class);

		Response fakeResponse = new Response("/v1/media/popular", 200, "", new ArrayList<Header>(), new MockTypedInput(json));

		callback.success(mediaResponse, fakeResponse);
	}
}
