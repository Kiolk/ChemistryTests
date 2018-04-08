package kiolk.com.github.pen.utils;

import android.util.Log;

import kiolk.com.github.pen.BuildConfig;

public final class LogUtil  {

    private LogUtil(){
    }

    public static void msg(final String message){

        if(BuildConfig.DEBUG){
            Log.d(PenConstantsUtil.Log.LOG_TAG, message);
        }
    }
}
