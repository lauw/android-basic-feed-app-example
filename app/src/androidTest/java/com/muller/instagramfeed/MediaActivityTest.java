package com.muller.instagramfeed;

import android.app.Instrumentation;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.VideoView;

import com.muller.instagramfeed.activity.MediaActivity;
import com.muller.instagramfeed.event.MediaUpdateEvent;
import com.muller.instagramfeed.model.Media;
import com.muller.instagramfeed.util.TestUtils;
import com.squareup.otto.Bus;

import junit.framework.Assert;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MediaActivityTest extends ActivityInstrumentationTestCase2<MediaActivity> {
	@Inject Bus mEventBus;
	private Instrumentation mInstrumentation;
	private RecyclerView mRecyclerView;

	public MediaActivityTest() {
		super(MediaActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		TestUtils.setUp(this);
		App.DependencyInjector.inject(this);

		mInstrumentation = getInstrumentation();
		mRecyclerView = ButterKnife.findById(getActivity(), R.id.media_recycler_view);

		// Espresso will not launch our activity for us, we must launch it via getActivity().
		getActivity();
	}

	@Override
	protected void tearDown() throws Exception {
		TestUtils.tearDown();
		super.tearDown();
	}

	public void testDataShown() {
		Assert.assertTrue(mRecyclerView.getAdapter().getItemCount() > 0); //check if the recyclerview has items
	}

	public void testRefreshButton_refreshesData() {
		//on main thread, post an event with an empty list, so the recyclerview updates itself and becomes empty
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mEventBus.post(new MediaUpdateEvent(new ArrayList<Media>()));
			}
		});

		mInstrumentation.waitForIdleSync(); //wait for it to sync back

		Assert.assertEquals(0, mRecyclerView.getChildCount()); //assert that recyclerview is indeed empty

		onView(withId(R.id.action_refresh)).perform(click()); //click the refresh button
		Assert.assertTrue(mRecyclerView.getChildCount() > 0); //assert that the recyclerview now has data
	}

	public void testIsVideoPlayable() {
		//the second view is a video item
		final View videoItemView = mRecyclerView.getChildAt(1);
		final VideoView videoView = (VideoView)videoItemView.findViewById(R.id.media_adapter_item_video);

		onView(withId(R.id.media_recycler_view)).perform(RecyclerViewActions.actionOnItem(withChild(withId(R.id.media_adapter_item_video_image_overlay)), click()));

		//TODO: get better test data for video
		TestUtils.sleep(1000); //sleep because it may take a second for the video to start

		Assert.assertTrue(videoView.isPlaying()); //assert that video is playing
	}
}
