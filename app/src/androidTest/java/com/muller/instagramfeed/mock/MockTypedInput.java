// Copyright 2013 Square, Inc.
package com.muller.instagramfeed.mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import retrofit.mime.TypedInput;

public class MockTypedInput implements TypedInput {
	private byte[] bytes;

	public MockTypedInput(String body) {
		try {
			this.bytes = body.getBytes("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String mimeType() {
		return "application/unknown";
	}

	@Override
	public long length() {
		return bytes.length;
	}

	@Override
	public InputStream in() throws IOException {

		return new ByteArrayInputStream(bytes);
	}
}