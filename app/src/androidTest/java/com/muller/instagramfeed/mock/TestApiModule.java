package com.muller.instagramfeed.mock;

import com.muller.instagramfeed.activity.MediaActivity;
import com.muller.instagramfeed.activity.MediaInfoActivity;
import com.muller.instagramfeed.api.ApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
		library = true,
		complete = false,
		injects = {
				MediaActivity.class, MediaInfoActivity.class
		}
)
public final class TestApiModule {
	@Provides @Singleton ApiService provideApiService() {
		return new TestApiService();
	}
}
