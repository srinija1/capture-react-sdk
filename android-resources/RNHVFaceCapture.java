package co.hyperverge.hypersnapdemoapp_react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import co.hyperverge.hypersnapsdk.activities.HVFaceActivity;
import co.hyperverge.hypersnapsdk.objects.Error;
import co.hyperverge.hypersnapsdk.objects.HyperSnapParams;
import co.hyperverge.hypersnapsdk.listeners.CaptureCompletionHandler;

public class RNHVFaceCapture extends ReactContextBaseJavaModule {
    public String liveness;

    public RNHVFaceCapture(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNHVFaceCapture";
    }

    @ReactMethod
    public void setLivenessMode(String livenessValue){
        try{
            liveness = livenessValue.split("\\.")[1];
        }
        catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @ReactMethod
    public void start(final Callback resultCallback){
        HyperSnapParams.LivenessMode livenessMode = HyperSnapParams.LivenessMode.valueOf(liveness);
        HVFaceActivity.start(getCurrentActivity().getApplicationContext(), livenessMode, new CaptureCompletionHandler() {
                    @Override
                    public void onResult(Error error, JSONObject result) {
                        WritableMap errorObj = Arguments.createMap();
                        WritableMap resultsObj = Arguments.createMap();
                        if(error!=null){
                            errorObj.putInt("errorCode",error.getError().getErrCode());
                            errorObj.putString("errorMessage",error.getErrMsg());
                            resultCallback.invoke(errorObj,null);
                        }
                        else{
                            Iterator<?> keys=result.keys();
                            while(keys.hasNext()){
                                String key = (String)keys.next();
                                try {
                                    resultsObj.putString(key,(String)result.get(key));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            resultCallback.invoke(null,resultsObj);
                        }
                    }
                }
        );
    }

}
