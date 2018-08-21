package com.example.capture.bluetoothcapture;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.capture.common.logger.Log;
import com.example.android.bluetoothchat.R;
import java.io.FileDescriptor;

/**
 * Created by Ziqiao Charlie Li on 10/23/2015.
 */
public class MediaPlayerFragment extends Fragment implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback{

    private static final String TAG = "MediaPlayerFrament";
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    private String path;
    private Bundle extras;
    private static final String MEDIA = "media";
    private static final int LOCAL_AUDIO = 1;
    private static final int STREAM_AUDIO = 2;
    private static final int RESOURCES_AUDIO = 3;
    private static final int LOCAL_VIDEO = 4;
    private static final int STREAM_VIDEO = 5;
    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    private boolean isReady = false;
    private FileDescriptor mScreenStream = null;

    /**
     *
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        extras = getActivity().getIntent().getExtras();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_player, container, false);
        mPreview = (SurfaceView) view.findViewById(R.id.surface);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        if (savedInstanceState == null) {
//
//            //TODO: Remove this logic and replace with a generic welcome screen.
////            getChildFragmentManager()
////                    .beginTransaction()
////                    .add(R.id.screen_capture_fragment, mCaptureFragment, "ScreenCaptureFrament")
////                    .commit();
//        }
        return view;
    }

    public void setReady() {
        isReady = true;
    }

    public void setFileDescriptor(FileDescriptor fd) {
        mScreenStream = fd;
        playVideo(STREAM_VIDEO);
    }

    private void playVideo(Integer Media) {
        doCleanUp();
        try {

            switch (Media) {
                case LOCAL_VIDEO:
        /*
         * TODO: Set the path variable to a local media file path.
         */
                    path = "";
                    if (path == "") {
                        // Tell the user to provide a media file URL.
                        FragmentActivity activity = getActivity();

                        Toast.makeText(
                                activity,
                                "Please edit MediaPlayerActivity Activity, "
                                        + "and set the path variable to your media file path."
                                        + " Your media file must be stored on sdcard.",
                                Toast.LENGTH_LONG).show();

                    }
                    break;
                case STREAM_VIDEO:
        /*
         * TODO: Set path variable to progressive streamable mp4 or 3gpp
         * format URL. Http protocol should be used. Mediaplayer can
         * only play "progressive streamable contents" which basically
         * means: 1. the movie atom has to precede all the media data
         * atoms. 2. The clip has to be reasonably interleaved.
         */
                    path = "";
                    if (path == "") {
                        // Tell the user to provide a media file URL.
                        FragmentActivity activity = getActivity();
                        Toast.makeText(
                                activity,
                                "Please edit MediaPlayerActivity Activity,"
                                        + " and set the path variable to your media file URL.",
                                Toast.LENGTH_LONG).show();

                    }

                    break;

            }

            // Create a new media player and set the listeners
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(mScreenStream);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        } catch (Exception e) {
            Log.e(TAG, "error: " + e.getMessage(), e);
        }
    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        Log.d(TAG, "onBufferingUpdate percent:" + percent);

    }

    public void onCompletion(MediaPlayer arg0) {
        Log.d(TAG, "onCompletion called");
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height
                    + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d(TAG, "onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        Log.d(TAG, "surfaceChanged called");

    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d(TAG, "surfaceDestroyed called");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called");
        //playVideo(extras.getInt(MEDIA));
        playVideo(5);
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.v(TAG, "startVideoPlayback");
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }
}
