package com.example.capture.bluetoothcapture;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.capture.mjpeg.MjpegInputStream;
import com.example.capture.mjpeg.MjpegView;
import com.example.android.bluetoothchat.R;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.net.URI;

/**
 * Created by Ziqiao Charlie Li on 10/27/2015.
 */
public class MjpegFragment extends Fragment {
    private static final boolean DEBUG = true;
    private static final String TAG = "MJPEG";

    private MjpegView mMovieSurface = null;
    //String URL;

    // for settings (network and resolution)
    private static final int REQUEST_SETTINGS = 0;

    private int DISPLAY_WIDTH = 640;
    private int DISPLAY_HEIGHT = 480;

//    private int ip_ad1 = 192;
//    private int ip_ad2 = 168;
//    private int ip_ad3 = 2;
//    private int ip_ad4 = 1;
//    private int ip_port = 80;
//    private String ip_command = "?action=stream";

    private boolean suspending = false;

    //final Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getActivity().getSharedPreferences("SAVED_VALUES", 0);
        DISPLAY_WIDTH = preferences.getInt("width", DISPLAY_WIDTH);
        DISPLAY_HEIGHT = preferences.getInt("height", DISPLAY_HEIGHT);
//        setContentView(R.layout.main);
//        mMovieSurface = (MjpegView) findViewById(R.id.mMovieSurface);
//        if (mMovieSurface != null) {
//            mMovieSurface.setResolution(DISPLAY_WIDTH, DISPLAY_HEIGHT);
//        }
//

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mjpeg_surface, container, false);
        mMovieSurface = (MjpegView) view.findViewById(R.id.surface);
        if(mMovieSurface != null) {
            mMovieSurface.setResolution(DISPLAY_WIDTH, DISPLAY_HEIGHT);
        }
        // TODO: Bind Async task to read from BluetoothService
        //new DoRead().execute("");
        return view;
    }


//    public void onResume() {
//        if (DEBUG) Log.d(TAG, "onResume()");
//        super.onResume();
//        if (mMovieSurface != null) {
//            if (suspending) {
//                //new DoRead().execute(URL);
//                suspending = false;
//            }
//        }
//
//    }

    public void onStart() {
        if (DEBUG) Log.d(TAG, "onStart()");
        super.onStart();
    }

    public void onPause() {
        if (DEBUG) Log.d(TAG, "onPause()");
        super.onPause();
        if (mMovieSurface != null) {
            if (mMovieSurface.isStreaming()) {
                mMovieSurface.stopPlayback();
                suspending = true;
            }
        }
    }

    public void onStop() {
        if (DEBUG) Log.d(TAG, "onStop()");
        super.onStop();
    }

    public void onDestroy() {
        if (DEBUG) Log.d(TAG, "onDestroy()");

        if (mMovieSurface != null) {
            mMovieSurface.freeCameraMemory();
        }

        super.onDestroy();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.layout.option_menu, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.settings:
//                Intent settings_intent = new Intent(MjpegActivity.this, SettingsActivity.class);
//                settings_intent.putExtra("DISPLAY_WIDTH", DISPLAY_WIDTH);
//                settings_intent.putExtra("DISPLAY_HEIGHT", DISPLAY_HEIGHT);
//                settings_intent.putExtra("ip_ad1", ip_ad1);
//                settings_intent.putExtra("ip_ad2", ip_ad2);
//                settings_intent.putExtra("ip_ad3", ip_ad3);
//                settings_intent.putExtra("ip_ad4", ip_ad4);
//                settings_intent.putExtra("ip_port", ip_port);
//                settings_intent.putExtra("ip_command", ip_command);
//                startActivityForResult(settings_intent, REQUEST_SETTINGS);
//                return true;
//        }
//        return false;
//    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_SETTINGS:
                if (resultCode == Activity.RESULT_OK) {
                    DISPLAY_WIDTH = data.getIntExtra("DISPLAY_WIDTH", DISPLAY_WIDTH);
                    DISPLAY_HEIGHT = data.getIntExtra("DISPLAY_HEIGHT", DISPLAY_HEIGHT);


                    if (mMovieSurface != null) {
                        mMovieSurface.setResolution(DISPLAY_WIDTH, DISPLAY_HEIGHT);
                    }
                    SharedPreferences preferences = getActivity().getSharedPreferences("SAVED_VALUES", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("DISPLAY_WIDTH", DISPLAY_WIDTH);
                    editor.putInt("DISPLAY_HEIGHT", DISPLAY_HEIGHT);


                    editor.commit();

                    new RestartApp().execute();
                }
                break;
        }
    }

//    public void setImageError() {
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                setTitle(R.string.title_imageerror);
//                return;
//            }
//        });
//    }

    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
            //TODO: Read from BluetoothService input in AsyncTask
            int w = 0;
//            HttpResponse res = null;
//            DefaultHttpClient httpclient = new DefaultHttpClient();
//            HttpParams httpParams = httpclient.getParams();
//            HttpConnectionParams.setConnectionTimeout(httpParams, 5 * 1000);
//            HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);
//            if (DEBUG) Log.d(TAG, "1. Sending http request");
//            try {
//                res = httpclient.execute(new HttpGet(URI.create(url[0])));
//                if (DEBUG)
//                    Log.d(TAG, "2. Request finished, status = " + res.getStatusLine().getStatusCode());
//                if (res.getStatusLine().getStatusCode() == 401) {
//                    //You must turn off camera User Access Control before this will work
//                    return null;
//                }
//                return new MjpegInputStream(res.getEntity().getContent());
//            } catch (ClientProtocolException e) {
//                if (DEBUG) {
//                    e.printStackTrace();
//                    Log.d(TAG, "Request failed-ClientProtocolException", e);
//                }
//                //Error connecting to camera
//            } catch (IOException e) {
//                if (DEBUG) {
//                    e.printStackTrace();
//                    Log.d(TAG, "Request failed-IOException", e);
//                }
//                //Error connecting to camera
//            }
            return null;
        }

        protected void onPostExecute(MjpegInputStream result) {
            mMovieSurface.setSource(result);
            if (result != null) {
                result.setSkip(1);
                //setTitle(R.string.app_name);
            } else {
                //setTitle(R.string.title_disconnected);
            }
            mMovieSurface.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mMovieSurface.showFps(false);
        }
    }

    public class RestartApp extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... v) {
            getActivity().finish();
            return null;
        }

        protected void onPostExecute(Void v) {
            startActivity((new Intent(getActivity(), MjpegFragment.class)));
        }
    }
}
