package com.thesis2.genise_villanueva.thesis;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import timber.log.Timber;

public class AssetsController {
    private Context mContext;
    AssetsController(Context context){
        this.mContext = context;
    }
    //Turn Coordinates JSON to String from Assets folder
    public String loadCoordinatesFromAsset() {
        String json;
        try {
            InputStream inputStream = mContext.getAssets().open("coordinates.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Timber.e("loadCoordinatesJSONFromAsset: %s", ex.getMessage());
            return null;
        }
        return json;
    }
    //Turn Data JSON to String from Assets folder
    public String loadDataFromAsset() {
        String json = null;
        try {
            InputStream inputStream = mContext.getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            Timber.e("loadDataJSONFromAsset: %s", ex.getMessage());
            return null;
        }
        return json;
    }
}
