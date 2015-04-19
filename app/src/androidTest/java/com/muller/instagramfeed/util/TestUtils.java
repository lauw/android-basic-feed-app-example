package com.muller.instagramfeed.util;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

import com.muller.instagramfeed.App;
import com.muller.instagramfeed.mock.TestApiModule;
import com.muller.instagramfeed.mock.TestApplicationModule;

import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
	private static ActivityInstrumentationTestCase2 testCase;

	public static ActivityInstrumentationTestCase2 getTestCase() {
		return testCase;
	}

	public static void setUp(ActivityInstrumentationTestCase2 testCase) {
		TestUtils.testCase = testCase;
		App.initWithModules(new TestApplicationModule(), new TestApiModule());
	}

	public static void tearDown() {
		testCase = null;
	}

	public static String loadJSONFromFile(Context context, String file) {
		String json = null;

		try {
			InputStream is = context.getAssets().open(file);

			int size = is.available();

			byte[] buffer = new byte[size];

			is.read(buffer);

			is.close();

			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

		return json;
	}

	public static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
