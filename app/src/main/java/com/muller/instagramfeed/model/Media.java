package com.muller.instagramfeed.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class Media {

	//region Properties
	//-------------------------------------------------------------------------------------------------------------------
	private String url;
	private int width;
	private int height;
	private int comments;
	private int likes;
	private String caption;
	private MediaType type;

	public String getUrl() {
		return url;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getComments() {
		return comments;
	}

	public int getLikes() {
		return likes;
	}

	public String getCaption() {
		return caption;
	}

	public MediaType getType() {
		return type;
	}
	//-------------------------------------------------------------------------------------------------------------------
	//endregion

	//region Methods
	//-------------------------------------------------------------------------------------------------------------------

	public boolean hasUrl() {
		return getUrl() != null && !getUrl().isEmpty();
	}

	public boolean hasCaption() {
		return getCaption() != null && !getCaption().isEmpty();
	}

	//-------------------------------------------------------------------------------------------------------------------
	//endregion

	//region Enums
	//-------------------------------------------------------------------------------------------------------------------

	public enum MediaType {
		Image(0),
		Video(1);

		private final int index;

		MediaType(int index) {
			this.index = index;
		}

		public static MediaType fromInt(int value) {
			if (value == 1) return Video;

			return Image;
		}

		public static MediaType fromString(String value) {
			if (value.equalsIgnoreCase("video")) return Video;

			return Image;
		}

		public int intValue() {
			return index;
		}
	}

	//-------------------------------------------------------------------------------------------------------------------
	//endregion

	public static class GsonDeserializer implements JsonDeserializer<Media> {
		@Override
		public Media deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
			return parseMedia(json.getAsJsonObject());
		}

		private Media parseMedia(JsonObject jsonObject) {
			Media media = new Media();

			String mediaType = jsonObject.get("type").getAsString();
			media.type = MediaType.fromString(mediaType);

			if (media.type == MediaType.Video) {
				JsonObject videoObject = jsonObject.get("videos").getAsJsonObject();
				parseMediaData(media, videoObject);
			} else {
				JsonObject imageObject = jsonObject.get("images").getAsJsonObject();
				parseMediaData(media, imageObject);
			}

			media.comments = jsonObject.get("comments").getAsJsonObject().get("count").getAsInt();
			media.likes = jsonObject.get("likes").getAsJsonObject().get("count").getAsInt();

			if (jsonObject.has("caption") && !jsonObject.get("caption").isJsonNull())
				media.caption = jsonObject.get("caption").getAsJsonObject().get("text").getAsString();

			return media;
		}

		private void parseMediaData(Media media, JsonObject mediaObject) {
			JsonObject standardResolutionObject = mediaObject.get("standard_resolution").getAsJsonObject();
			media.url = standardResolutionObject.get("url").getAsString();
			media.width = standardResolutionObject.get("width").getAsInt();
			media.height = standardResolutionObject.get("height").getAsInt();
		}
	}
}
