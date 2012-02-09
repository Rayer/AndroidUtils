package com.rayer.utilviews;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class FlipView extends FrameLayout {

	private View image1;
	private View image2;

	private boolean isFirstImage = true;

	public FlipView(Context context) {
		super(context);


	}
	
	public void initWithView(View p1, View p2) {
		
		removeAllViews();
		
		image1 = p1;
		image2 = p2;
		
		image2.setVisibility(View.GONE);
		image1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if (isFirstImage) {
					applyRotation(0, 90);
					isFirstImage = !isFirstImage;

				} else {
					applyRotation(0, -90);
					isFirstImage = !isFirstImage;
				}
			}
		});
		
		addView(image1);
		addView(image2);
	}


	private void applyRotation(float start, float end) {
		// Find the center of image
		final float centerX = image1.getWidth() / 2.0f;
		final float centerY = image1.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Flip3dAnimation rotation = new Flip3dAnimation(start, end,
				centerX, centerY);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(isFirstImage, image1, image2));

		if (isFirstImage) {
			image1.startAnimation(rotation);
		} else {
			image2.startAnimation(rotation);
		}

	}

	public final class DisplayNextView implements Animation.AnimationListener {
		private boolean mCurrentView;
		View image1;
		View image2;

		public DisplayNextView(boolean currentView, View image1,
				View image2) {
			mCurrentView = currentView;
			this.image1 = image1;
			this.image2 = image2;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			image1.post(new SwapViews(mCurrentView, image1, image2));
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	public final class SwapViews implements Runnable {
		private boolean mIsFirstView;
		View image1;
		View image2;

		public SwapViews(boolean isFirstView, View image1, View image2) {
			mIsFirstView = isFirstView;
			this.image1 = image1;
			this.image2 = image2;
		}

		public void run() {
			final float centerX = image1.getWidth() / 2.0f;
			final float centerY = image1.getHeight() / 2.0f;
			Flip3dAnimation rotation;

			if (mIsFirstView) {
				image1.setVisibility(View.GONE);
				image2.setVisibility(View.VISIBLE);
				image2.requestFocus();

				rotation = new Flip3dAnimation(-90, 0, centerX, centerY);
			} else {
				image2.setVisibility(View.GONE);
				image1.setVisibility(View.VISIBLE);
				image1.requestFocus();

				rotation = new Flip3dAnimation(90, 0, centerX, centerY);
			}

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			if (mIsFirstView) {
				image2.startAnimation(rotation);
			} else {
				image1.startAnimation(rotation);
			}
		}
	}

}
