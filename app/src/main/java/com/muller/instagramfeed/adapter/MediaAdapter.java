package com.muller.instagramfeed.adapter;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.muller.instagramfeed.R;
import com.muller.instagramfeed.model.Media;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	private RecyclerView mRecyclerView;
	private List<Media> mMediaList;
	private int mLastPosition = -1;

	private static final int VIEW_TYPE_IMAGE = 0;
	private static final int VIEW_TYPE_IMAGE_CAPTION = 1;
	private static final int VIEW_TYPE_VIDEO = 2;

    public MediaAdapter(RecyclerView recyclerView, List<Media> mediaList) {
		mRecyclerView = recyclerView;
		mMediaList = mediaList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		 if (viewType == VIEW_TYPE_IMAGE_CAPTION) {
		 	View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_adapter_item_image_caption, parent, false);
			return new ImageCaptionViewHolder(view);
		} else if (viewType == VIEW_TYPE_VIDEO) {
			 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_adapter_item_video, parent, false);
			 return new VideoViewHolder(view);
		 } else {
			 View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_adapter_item_image, parent, false);
			 return new ImageViewHolder(view);
		 }
    }

	@Override
	public int getItemViewType(int position) {
		Media media = mMediaList.get(position);

		if (media.getType() == Media.MediaType.Video)
			return VIEW_TYPE_VIDEO;

		if (media.hasCaption())
			return VIEW_TYPE_IMAGE_CAPTION;

		return VIEW_TYPE_IMAGE;
	}

	@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		Media media = mMediaList.get(position);
		/** set the background color for this view (for a better distinction between items) **/
		holder.itemView.setBackgroundResource(getBackgroundColorResource(position));

		Log.d("BINDING", "type: " + media.getType().toString() + ", position: " + position + ", caption: " + (media.getCaption() != null ? "true" : "false"));

		if (holder.getItemViewType() == VIEW_TYPE_VIDEO)
			bindVideo((VideoViewHolder)holder, position);
		else if (holder.getItemViewType() == VIEW_TYPE_IMAGE_CAPTION)
			bindImageCaption((ImageCaptionViewHolder)holder, position);
		else
			bindImage((ImageViewHolder)holder, position);

		animate(holder.itemView, position); /** animates the view when it is first shown **/
    }

	private void bindVideo(VideoViewHolder holder, int position) {
		Media media = mMediaList.get(position);

		holder.mVideoView.setVideoURI(Uri.parse(media.getUrl()));
		//The image overlay handles all the click events, because VideoView does not support clickListeners
		holder.mVideoOverlayImageView.setOnClickListener(onVideoClickListener);
		showVideoPlayOverlay(holder.itemView);
	}

	private void showVideoPlayOverlay(View itemView) {
		ImageView videoImageOverlay = ButterKnife.findById(itemView, R.id.media_adapter_item_video_image_overlay);

		videoImageOverlay.setImageResource(android.R.drawable.ic_media_play);
		videoImageOverlay.animate().alpha(1f).setDuration(0).start();
	}

	private void showVideoPauseOverlay(View itemView) {
		ImageView videoImageOverlay = ButterKnife.findById(itemView, R.id.media_adapter_item_video_image_overlay);

		videoImageOverlay.setAlpha(1f);
		videoImageOverlay.setImageResource(android.R.drawable.ic_media_pause);
		videoImageOverlay.animate().alpha(0f).setDuration(1000).start();
	}

	private View.OnClickListener onVideoClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			View itemView = (View)view.getParent();
			VideoView videoView = ButterKnife.findById(itemView, R.id.media_adapter_item_video);

			if (videoView.isPlaying()) {
				videoView.pause();
				showVideoPlayOverlay(itemView);
			} else {
				videoView.start();
				showVideoPauseOverlay(itemView);
			}
		}
	};

	private void bindImage(ImageViewHolder holder, int position) {
		Media media = mMediaList.get(position);

		holder.mImageView.setImageResource(R.drawable.placeholder);

		if (media.hasUrl()) {
			Picasso.with(mRecyclerView.getContext()).load(media.getUrl()).placeholder(R.drawable.placeholder).into(holder.mImageView);
		}
	}

	private void bindImageCaption(ImageCaptionViewHolder holder, int position) {
		Media media = mMediaList.get(position);

		holder.mCaptionTextView.setText(media.getCaption());
		holder.mImageView.setImageResource(R.drawable.placeholder);

		if (media.hasUrl()) {
			Picasso.with(mRecyclerView.getContext()).load(media.getUrl()).placeholder(R.drawable.placeholder).into(holder.mImageView);
		}
	}

	public void refresh() {
		mRecyclerView.scrollToPosition(0);
		super.notifyDataSetChanged();
		mLastPosition = -1; //reset animation position
	}

    @Override
    public int getItemCount() {
        return mMediaList.size();
    }

	private void animate(View view, int position) {
		// If the view wasn't previously displayed on screen, it's animated
		if (position > mLastPosition) {
			AnimatorSet animatorSet = new AnimatorSet();
			animatorSet.playTogether(ObjectAnimator.ofFloat(view, "translationY", mRecyclerView.getHeight() / 2, 0), ObjectAnimator.ofFloat(view, "alpha", 0, 1));
			animatorSet.setDuration(250);
			animatorSet.start();
			mLastPosition = position;
		}
	}

	public class ImageViewHolder extends RecyclerView.ViewHolder {
		@InjectView(R.id.media_adapter_item_image) ImageView mImageView;

		public ImageViewHolder(View view) {
			super(view);
			ButterKnife.inject(this, view);
		}
	}

	public class ImageCaptionViewHolder extends RecyclerView.ViewHolder {
		@InjectView(R.id.media_adapter_item_image_caption) TextView mCaptionTextView;
		@InjectView(R.id.media_adapter_item_image) ImageView mImageView;

		public ImageCaptionViewHolder(View view) {
			super(view);
			ButterKnife.inject(this, view);
		}
	}

	public class VideoViewHolder extends RecyclerView.ViewHolder {
		@InjectView(R.id.media_adapter_item_video) VideoView mVideoView;
		@InjectView(R.id.media_adapter_item_video_image_overlay) ImageView mVideoOverlayImageView;

		public VideoViewHolder(View view) {
			super(view);
			ButterKnife.inject(this, view);
		}
	}

	private int getBackgroundColorResource(int position) {
		if (position % 2 == 0)
			return R.color.white;

		return R.color.lightGray;
	}
}