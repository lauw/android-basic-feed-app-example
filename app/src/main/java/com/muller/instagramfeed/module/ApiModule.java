package com.muller.instagramfeed.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.muller.instagramfeed.BuildConfig;
import com.muller.instagramfeed.activity.MediaActivity;
import com.muller.instagramfeed.activity.MediaInfoActivity;
import com.muller.instagramfeed.api.ApiService;
import com.muller.instagramfeed.model.Media;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

@Module(
		library = true,
		complete = false,
		injects = {
				MediaActivity.class, MediaInfoActivity.class
		}
)
public final class ApiModule {
	private static final String BASE_URL = "https://api.instagram.com";
	private static final String INSTAGRAM_CLIENT_ID = "60a773e3cc6f4b85b68190fac7894137";

	@Provides @Singleton Endpoint provideEndpoint() {
		return Endpoints.newFixedEndpoint(BASE_URL);
	}

	@Provides @Singleton RequestInterceptor provideRequestInterceptor() {
		return new RequestInterceptor() {
			@Override
			public void intercept(RequestFacade request) {
				request.addQueryParam("client_id", INSTAGRAM_CLIENT_ID);
			}
		};
	}

	@Provides @Singleton RestAdapter provideRestAdapter(Endpoint endpoint, RequestInterceptor headers, Gson gson) {
		return new RestAdapter.Builder()
				.setConverter(new GsonConverter(gson))
				.setRequestInterceptor(headers)
				.setEndpoint(endpoint)
				.setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
				.build();
	}

	@Provides @Singleton Gson provideGson() {
		Gson gson = new GsonBuilder().registerTypeAdapter(Media.class, new Media.GsonDeserializer()).create();
		return gson;
	}


	@Provides @Singleton ApiService provideApiService(RestAdapter restAdapter) {
		return restAdapter.create(ApiService.class);
	}
}
