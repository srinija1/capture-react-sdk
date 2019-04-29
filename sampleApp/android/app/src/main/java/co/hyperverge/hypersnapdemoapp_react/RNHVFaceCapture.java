package co.hyperverge.hypersnapdemoapp_react;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import co.hyperverge.hypersnapsdk.activities.HVFaceActivity;
import co.hyperverge.hypersnapsdk.listeners.FaceCaptureCompletionHandler;
import co.hyperverge.hypersnapsdk.objects.HVDocConfig;
import co.hyperverge.hypersnapsdk.objects.HVError;
import co.hyperverge.hypersnapsdk.objects.HVFaceConfig;


public class RNHVFaceCapture extends ReactContextBaseJavaModule {
    public String liveness;

    public RNHVFaceCapture(ReactApplicationContext reactContext) {
        super(reactContext);
    }
    HVFaceConfig faceConfig;
    @Override
    public String getName() {
        return "RNHVFaceCapture";
    }
    @ReactMethod
    public void setLivenessEndpoint(String livenessEndpoint) {
        this.faceConfig.setLivenessEndpoint(livenessEndpoint);
    }

    @ReactMethod
    public void createNewConfig() {
        this.faceConfig = new HVFaceConfig();

    }

    @ReactMethod
    public void setShouldReturnFullImageUrl(Boolean shouldReturnFullImageUrl) {
        this.faceConfig.setShouldReturnFullImageUrl(shouldReturnFullImageUrl.booleanValue());
    }

    @ReactMethod
    public void setClientID(String clientID) {
        this.faceConfig.setClientID(clientID);

    }

    @ReactMethod
    public void setCustomUIStrings(String customStrings){
        try {
            JSONObject stringObj = new JSONObject();
            if( customStrings == null && !customStrings.trim().isEmpty() )
                stringObj = new JSONObject(customStrings);
            this.faceConfig.setCustomUIStrings(stringObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void setShouldShowCameraSwitchButton(Boolean shouldShow) {
        this.faceConfig.setShouldShowCameraSwitchButton(shouldShow.booleanValue());
    }


    @ReactMethod
    public void setShouldShowInstructionPage(Boolean shouldShowInstructionPage) {
         this.faceConfig.setShouldShowInstructionPage(shouldShowInstructionPage.booleanValue());
    }


    @ReactMethod
    public void setLivenessMode(String livenessValue) {
        try {
            liveness = livenessValue.split("\\.")[1];
            HVFaceConfig.LivenessMode livenessMode = HVFaceConfig.LivenessMode.valueOf(liveness);
            faceConfig.setLivenessMode(livenessMode);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @ReactMethod
    public void setFaceCaptureTitle(String faceCaptureTitle) {
         this.faceConfig.setFaceCaptureTitle(faceCaptureTitle);
    }

    @ReactMethod
    public void setShouldUseBackCamera(Boolean shouldUseBackCamera) {
        this.faceConfig.setShouldUseBackCamera(shouldUseBackCamera.booleanValue());
    }

    @ReactMethod
    public void setShouldEnableDataLogging(Boolean dataLogging) {
        this.faceConfig.setShouldEnableDataLogging(dataLogging.booleanValue());
    }

    @ReactMethod
    public void setPadding(Number leftPadding, Number rightPadding, Number topPadding, Number bottomPadding) {
        this.faceConfig.setPadding((float)  (leftPadding) , (float)rightPadding, (float)topPadding, (float)bottomPadding);
    }

    @ReactMethod
    public void setLivenessAPIParameters(String params) {
        try {
            this.faceConfig.setLivenessAPIParameters(new JSONObject( params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void setLivenessAPIHeaders(String headers) {
        try {
            this.faceConfig.setLivenessAPIHeaders(new JSONObject( headers));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @ReactMethod
    public void start( final Callback resultCallback) {

        HVFaceActivity.start(getCurrentActivity(), this.faceConfig, new FaceCaptureCompletionHandler() {
            @Override
            public void onResult(HVError error, JSONObject result, JSONObject headers) {


                WritableMap errorObj = Arguments.createMap();
                WritableMap resultsObj = Arguments.createMap();
                WritableMap headersObj = Arguments.createMap();
                if (error != null) {
                    errorObj.putInt("errorCode", error.getErrorCode());
                    errorObj.putString("errorMessage", error.getErrorMessage());
                    resultCallback.invoke(errorObj, null);
                } else {
                    if (result != null) {
                        Iterator<?> keys = result.keys();
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            try {
                                resultsObj.putString(key, (String) result.get(key));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (headers != null) {
                        Iterator<?> keys = headers.keys();
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            try {
                                headersObj.putString(key, (String) headers.get(key));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    resultCallback.invoke(null, resultsObj, headersObj);

                }
            }
        });

    }


}
