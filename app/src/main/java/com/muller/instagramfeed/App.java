package com.muller.instagramfeed;

import android.app.Application;

import com.muller.instagramfeed.event.MediaUpdateEvent;
import com.muller.instagramfeed.model.Media;
import com.muller.instagramfeed.module.ApiModule;
import com.muller.instagramfeed.module.ApplicationModule;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

public class App extends Application {
	private static List<Media> mPopularMediaList = new ArrayList<>();
	private static App instance;

	@Inject Bus mEventBus;

	public App() {
		instance = this;
	}
	public static App getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		initWithModules(new ApplicationModule(), new ApiModule());
		DependencyInjector.inject(this);
		mEventBus.register(this);
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		mEventBus.unregister(this);
	}

	@Produce
	public MediaUpdateEvent produceMedia() {
		return new MediaUpdateEvent(mPopularMediaList);
	}

	@Subscribe
	public void onMediaUpdate(MediaUpdateEvent event) {
		mPopularMediaList = new ArrayList<>(event.getMediaList());
	}

	public static void initWithModules(Object... modules) {
		DependencyInjector.initWithModules(modules);
	}

	public static class DependencyInjector {
		private static ObjectGraph objectGraph;

		/**  Init the dagger object graph with production modules **/
		public static void initWithModules(Object... modules) {
			objectGraph = ObjectGraph.create(modules);
			objectGraph.injectStatics();
		}

		public static <T>T get(Class<T> klass) {
			return objectGraph.get(klass);
		}

		/** Dagger convenience method - will inject the fields of the passed in object **/
		public static void inject(Object object) {
			objectGraph.inject(object);
		}
	}
}
