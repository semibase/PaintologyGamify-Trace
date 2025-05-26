package com.paintology.lite.trace.drawing.Youtube.ui;

import android.animation.Animator;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.paintology.lite.trace.drawing.R;
import com.paintology.lite.trace.drawing.Youtube.player.PlayerConstants;
import com.paintology.lite.trace.drawing.Youtube.player.YouTubePlayer;
import com.paintology.lite.trace.drawing.Youtube.player.YouTubePlayerFullScreenListener;
import com.paintology.lite.trace.drawing.Youtube.player.YouTubePlayerListener;
import com.paintology.lite.trace.drawing.Youtube.player.YouTubePlayerView;
import com.paintology.lite.trace.drawing.Youtube.ui.menu.YouTubePlayerMenu;
import com.paintology.lite.trace.drawing.Youtube.ui.menu.defaultMenu.DefaultYouTubePlayerMenu;
import com.paintology.lite.trace.drawing.Youtube.utils.Utils;
import com.paintology.lite.trace.drawing.minipaint.PaintActivity;
import com.paintology.lite.trace.drawing.minipaint.Play_YotubeVideo;

public class DefaultPlayerUIController implements PlayerUIController, YouTubePlayerListener, YouTubePlayerFullScreenListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    @NonNull
    private final YouTubePlayerView youTubePlayerView;

    @NonNull
    public final YouTubePlayer youTubePlayer;

    @NonNull
    private YouTubePlayerMenu youTubePlayerMenu;

    YouTubePlayerListener listener;
    /**
     * View used for for intercepting clicks and for drawing a black background.
     * Could have used controlsRoot view, but in this way I'm able to hide all the control at once by hiding controlsRoot
     */
    private View panel;

    // view containing the controls
    private View controlsRoot;

    private LinearLayout extraViewsContainer;

    private TextView videoTitle;
    private TextView videoCurrentTime;
    private TextView videoDuration;
    private TextView liveVideoIndicator;

    private ProgressBar progressBar;
    private ImageView menuButton;
    private ImageView playPauseButton;
    private ImageView youTubeButton;
    private ImageView fullScreenButton;

    private ImageView customActionLeft;
    private ImageView customActionRight;

    private SeekBar seekBar;

    @Nullable
    private View.OnClickListener onFullScreenButtonListener;
    @Nullable
    private View.OnClickListener onMenuButtonClickListener;

    // view state
    private boolean isPlaying = false;
    private boolean isVisible = true;
    private boolean canFadeControls = false;

    private boolean showUI = true;
    private boolean showPlayPauseButton = true;
    private boolean showBufferingProgress = true;
    Boolean needToHideToggle;


    public DefaultPlayerUIController(@NonNull YouTubePlayerView youTubePlayerView, @NonNull YouTubePlayer youTubePlayer, Boolean needToHideToggle) {
        this.youTubePlayerView = youTubePlayerView;
        this.youTubePlayer = youTubePlayer;

        this.needToHideToggle = needToHideToggle;
        View defaultPlayerUI = View.inflate(youTubePlayerView.getContext(), R.layout.default_player_ui, youTubePlayerView);
        initViews(defaultPlayerUI);

        youTubePlayerMenu = new DefaultYouTubePlayerMenu(youTubePlayerView.getContext());

        listener = this;
    }

    private void initViews(View controlsView) {

        panel = controlsView.findViewById(R.id.panel);

        controlsRoot = controlsView.findViewById(R.id.controls_root);
        extraViewsContainer = controlsView.findViewById(R.id.extra_views_container);
        videoTitle = controlsView.findViewById(R.id.video_title);
        videoCurrentTime = controlsView.findViewById(R.id.video_current_time);
        videoDuration = controlsView.findViewById(R.id.video_duration);
        liveVideoIndicator = controlsView.findViewById(R.id.live_video_indicator);

        progressBar = controlsView.findViewById(R.id.progress);
        menuButton = controlsView.findViewById(R.id.menu_button);
        playPauseButton = controlsView.findViewById(R.id.play_pause_button);
        youTubeButton = controlsView.findViewById(R.id.youtube_button);
        fullScreenButton = controlsView.findViewById(R.id.fullscreen_button);

        customActionLeft = controlsView.findViewById(R.id.custom_action_left_button);
        customActionRight = controlsView.findViewById(R.id.custom_action_right_button);

        seekBar = controlsView.findViewById(R.id.seek_bar);

        seekBar.setOnSeekBarChangeListener(this);
        panel.setOnClickListener(this);
        playPauseButton.setOnClickListener(this);
        menuButton.setOnClickListener(this);
        fullScreenButton.setOnClickListener(this);

    }

    @Override
    public void showVideoTitle(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        videoTitle.setVisibility(visibility);
    }

    @Override
    public void setVideoTitle(@NonNull String title) {
        videoTitle.setText(title);
    }

    @Override
    public void showUI(boolean show) {
        int visibility = show ? View.VISIBLE : View.INVISIBLE;
        controlsRoot.setVisibility(visibility);

        showUI = show;
    }

    @Override
    public void showPlayPauseButton(boolean show) {
//        int visibility = show ? View.VISIBLE : View.GONE;
//        playPauseButton.setVisibility(visibility);

//        showPlayPauseButton = show;
        showPlayPauseButton = true;
    }

    @Override
    public void enableLiveVideoUI(boolean enable) {
        if (enable) {
            videoDuration.setVisibility(View.INVISIBLE);
            seekBar.setVisibility(View.INVISIBLE);
            videoCurrentTime.setVisibility(View.INVISIBLE);

            liveVideoIndicator.setVisibility(View.VISIBLE);
        } else {
            videoDuration.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
            videoCurrentTime.setVisibility(View.VISIBLE);

            liveVideoIndicator.setVisibility(View.GONE);
        }
    }

    /**
     * Set custom action to the left of the Play/Pause button
     */
    @Override
    public void setCustomAction1(@NonNull Drawable icon, View.OnClickListener clickListener) {
        customActionLeft.setImageDrawable(icon);
        customActionLeft.setOnClickListener(clickListener);
        showCustomAction1(clickListener != null);
    }

    /**
     * Set custom action to the right of the Play/Pause button
     */
    @Override
    public void setCustomAction2(@NonNull Drawable icon, View.OnClickListener clickListener) {
        customActionRight.setImageDrawable(icon);
        customActionRight.setOnClickListener(clickListener);
        showCustomAction2(clickListener != null);
    }

    public void showCustomAction1(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        customActionLeft.setVisibility(visibility);
    }

    public void showCustomAction2(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        customActionRight.setVisibility(visibility);
    }

    @Override
    public void showMenuButton(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
//        menuButton.setVisibility(visibility);
    }

    @Override
    public void setCustomMenuButtonClickListener(@NonNull View.OnClickListener customMenuButtonClickListener) {
        this.onMenuButtonClickListener = customMenuButtonClickListener;
    }

    @Override
    public void showCurrentTime(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        videoCurrentTime.setVisibility(visibility);
    }

    @Override
    public void showDuration(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        videoDuration.setVisibility(visibility);
    }

    @Override
    public void showSeekBar(boolean show) {
        int visibility = show ? View.VISIBLE : View.INVISIBLE;
        seekBar.setVisibility(visibility);
    }

    @Override
    public void showBufferingProgress(boolean show) {
        showBufferingProgress = show;
    }

    @Override
    public void showYouTubeButton(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        youTubeButton.setVisibility(visibility);
    }

    @Override
    public void addView(@NonNull View view) {
        extraViewsContainer.addView(view, 0);
    }

    @Override
    public void removeView(@NonNull View view) {
        extraViewsContainer.removeView(view);
    }

    @NonNull
    @Override
    public YouTubePlayerMenu getMenu() {
        return youTubePlayerMenu;
    }

    @Override
    public void setMenu(@NonNull YouTubePlayerMenu youTubePlayerMenu) {
        this.youTubePlayerMenu = youTubePlayerMenu;
    }

    @Override
    public void showFullscreenButton(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        fullScreenButton.setVisibility(visibility);
    }

    @Override
    public void setCustomFullScreenButtonClickListener(@NonNull View.OnClickListener customFullScreenButtonClickListener) {
        this.onFullScreenButtonListener = customFullScreenButtonClickListener;
    }

    @Override
    public void onClick(View view) {
        if (view == panel)
            toggleControlsVisibility();
        else if (view == playPauseButton)
            onPlayButtonPressed();
        else if (view == fullScreenButton)
            onFullScreenButtonPressed();
        else if (view == menuButton)
            onMenuButtonPressed();
    }

    private void onMenuButtonPressed() {
        if (onMenuButtonClickListener == null)
            youTubePlayerMenu.show(menuButton);
        else
            onMenuButtonClickListener.onClick(menuButton);
    }

    private void onFullScreenButtonPressed() {
        if (onFullScreenButtonListener == null)
            youTubePlayerView.toggleFullScreen();
        else
            onFullScreenButtonListener.onClick(fullScreenButton);
    }

    private void onPlayButtonPressed() {
        if (isPlaying)
            youTubePlayer.pause();
        else
            youTubePlayer.play();


    }

    private void updatePlayPauseButtonIcon(boolean playing) {
        int img = playing ? R.drawable.ic_pause_36dp : R.drawable.ic_play_36dp;
        playPauseButton.setImageResource(img);
    }

    private void toggleControlsVisibility() {
        final float finalAlpha = isVisible ? 0f : 1f;
        fadeControls(finalAlpha);
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable fadeOutRunnable = new Runnable() {
        @Override
        public void run() {
            fadeControls(0f);
        }
    };

    private void fadeControls(final float finalAlpha) {
        if (!canFadeControls || !showUI)
            return;

        isVisible = finalAlpha != 0f;

        // if the controls are shown and the player is playing they should automatically hide after a while.
        // if the controls are hidden remove fade out runnable
        if (finalAlpha == 1f && isPlaying)
            startFadeOutViewTimer();
        else
            handler.removeCallbacks(fadeOutRunnable);

        controlsRoot.animate()
                .alpha(finalAlpha)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        if (finalAlpha == 1f)
                            controlsRoot.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        if (finalAlpha == 0f)
                            controlsRoot.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                }).start();
    }

    private void startFadeOutViewTimer() {
        handler.postDelayed(fadeOutRunnable, 300000);
    }

    @Override
    public void onYouTubePlayerEnterFullScreen() {
        fullScreenButton.setImageResource(R.drawable.ic_fullscreen_exit_24dp);
    }

    @Override
    public void onYouTubePlayerExitFullScreen() {
        fullScreenButton.setImageResource(R.drawable.ic_fullscreen_24dp);
    }

    // YouTubePlayer callbacks

    // TODO refactor this method
    @Override
    public void onStateChange(@PlayerConstants.PlayerState.State int state) {
        newSeekBarProgress = -1;

        updateControlsState(state);

        if (state == PlayerConstants.PlayerState.PLAYING || state == PlayerConstants.PlayerState.PAUSED || state == PlayerConstants.PlayerState.VIDEO_CUED) {
            panel.setBackgroundColor(ContextCompat.getColor(youTubePlayerView.getContext(), android.R.color.transparent));
            progressBar.setVisibility(View.GONE);

            if (showPlayPauseButton) playPauseButton.setVisibility(View.VISIBLE);


            canFadeControls = true;
            boolean playing = state == PlayerConstants.PlayerState.PLAYING;
            updatePlayPauseButtonIcon(playing);

            if (playing)
                startFadeOutViewTimer();
            else
                handler.removeCallbacks(fadeOutRunnable);

        } else {
            updatePlayPauseButtonIcon(false);
            fadeControls(1f);

            if (state == PlayerConstants.PlayerState.BUFFERING) {
                panel.setBackgroundColor(ContextCompat.getColor(youTubePlayerView.getContext(), android.R.color.transparent));
                if (showPlayPauseButton) playPauseButton.setVisibility(View.INVISIBLE);

                customActionLeft.setVisibility(View.GONE);
                customActionRight.setVisibility(View.GONE);

                canFadeControls = false;
            }

            if (state == PlayerConstants.PlayerState.UNSTARTED) {
                canFadeControls = false;

                progressBar.setVisibility(View.GONE);
                if (showPlayPauseButton) playPauseButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void updateControlsState(int state) {
        switch (state) {
            case PlayerConstants.PlayerState.ENDED:
                isPlaying = false;
                break;
            case PlayerConstants.PlayerState.PAUSED:
                isPlaying = false;
                break;
            case PlayerConstants.PlayerState.PLAYING:
                isPlaying = true;
                break;
            case PlayerConstants.PlayerState.UNSTARTED:
                resetUI();
                break;
            default:
                break;
        }


        updatePlayPauseButtonIcon(!isPlaying);
    }

    @Override
    public void onCurrentSecond(float second) {
        // ignore if the user is currently moving the SeekBar
        if (seekBarTouchStarted)
            return;
        // ignore if the current time is older than what the user selected with the SeekBar
        if (newSeekBarProgress > 0 && !Utils.formatTime(second).equals(Utils.formatTime(newSeekBarProgress)))
            return;

        newSeekBarProgress = -1;

        seekBar.setProgress((int) second);
    }

    @Override
    public void onVideoDuration(float duration) {
        videoDuration.setText(Utils.formatTime(duration));
        seekBar.setMax((int) duration);
    }

    @Override
    public void onVideoLoadedFraction(float loadedFraction) {
        if (showBufferingProgress)
            seekBar.setSecondaryProgress((int) (loadedFraction * seekBar.getMax()));
        else
            seekBar.setSecondaryProgress(0);
    }


    @Override
    public void onVideoId(@NonNull final String videoId) {

        youTubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!needToHideToggle) {
                        if (PaintActivity.obj_interface != null)
                            PaintActivity.obj_interface.hidePlayer();
                    } else {
                        if (Play_YotubeVideo.objRedirect != null)
                            Play_YotubeVideo.objRedirect.selectItem(0, false);
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onReady() {
    }

    @Override
    public void onPlaybackQualityChange(@NonNull @PlayerConstants.PlaybackQuality.Quality String playbackQuality) {
    }

    @Override
    public void onPlaybackRateChange(@NonNull @PlayerConstants.PlaybackRate.Rate String rate) {
    }

    @Override
    public void onError(@PlayerConstants.PlayerError.Error int error) {
    }

    @Override
    public void onApiChange() {
    }


    // SeekBar callbacks

    private boolean seekBarTouchStarted = false;
    // I need this variable because onCurrentSecond gets called every 100 mils, so without the proper checks on this variable in onCurrentSeconds the seek bar glitches when touched.
    private int newSeekBarProgress = -1;

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        videoCurrentTime.setText(Utils.formatTime(i));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        seekBarTouchStarted = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (isPlaying)
            newSeekBarProgress = seekBar.getProgress();

        youTubePlayer.seekTo(seekBar.getProgress());
        seekBarTouchStarted = false;
    }

    private void resetUI() {
        seekBar.setProgress(0);
        seekBar.setMax(0);
        videoDuration.post(new Runnable() {
            @Override
            public void run() {
                videoDuration.setText("");
            }
        });
//        youTubeButton.setOnClickListener(null);
    }
}
