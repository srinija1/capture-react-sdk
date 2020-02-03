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
    boolean hasBeenCalled;
    @Override
    public String getName() {
        return "RNHVFaceCapture";
    }

    @ReactMethod
    public void setLivenessEndpoint(String livenessEndpoint) {
        getFaceConfig().setLivenessEndpoint(livenessEndpoint);
    }

    public HVFaceConfig getFaceConfig() {
        if(this.faceConfig == null) {
            this.faceConfig = new HVFaceConfig();
        }
        return this.faceConfig;

    }

    @ReactMethod
    public void setShouldReturnFullImageUrl(Boolean shouldReturnFullImageUrl) {
        getFaceConfig().setShouldReturnFullImageUrl(shouldReturnFullImageUrl.booleanValue());
    }

    @ReactMethod
    public void setClientID(String clientID) {
        getFaceConfig().setClientID(clientID);

    }

    @ReactMethod
    public void setCustomUIStrings(String customStrings){
        try {
            JSONObject stringObj = new JSONObject();
            if( customStrings == null && !customStrings.trim().isEmpty() )
                stringObj = new JSONObject(customStrings);
            getFaceConfig().setCustomUIStrings(stringObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void setShouldShowCameraSwitchButton(Boolean shouldShow) {
        getFaceConfig().setShouldShowCameraSwitchButton(shouldShow.booleanValue());
    }


    @ReactMethod
    public void setShouldShowInstructionPage(Boolean shouldShowInstructionPage) {
        getFaceConfig().setShouldShowInstructionPage(shouldShowInstructionPage.booleanValue());
    }


    @ReactMethod
    public void setLivenessMode(String livenessValue) {
        try {
            liveness = livenessValue.split("\\.")[1];
            HVFaceConfig.LivenessMode livenessMode = HVFaceConfig.LivenessMode.valueOf(liveness);
            getFaceConfig().setLivenessMode(livenessMode);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    @ReactMethod
    public void setFaceCaptureTitle(String faceCaptureTitle) {
        getFaceConfig().setFaceCaptureTitle(faceCaptureTitle);
    }

    @ReactMethod
    public void setShouldUseBackCamera(Boolean shouldUseBackCamera) {
        getFaceConfig().setShouldUseBackCamera(shouldUseBackCamera.booleanValue());
    }

    @ReactMethod
    public void setShouldEnableDataLogging(Boolean dataLogging) {
        getFaceConfig().setShouldEnableDataLogging(dataLogging.booleanValue());
    }

    @ReactMethod
    public void setShouldAddPadding(Boolean shouldSetPadding) {
        getFaceConfig().setShouldEnablePadding(shouldSetPadding.booleanValue());

    }

    @ReactMethod
    public void setPadding(Number leftPadding, Number rightPadding, Number topPadding, Number bottomPadding) {
        getFaceConfig().setPadding((float)  (leftPadding) , (float)rightPadding, (float)topPadding, (float)bottomPadding);
    }

    @ReactMethod
    public void setLivenessAPIParameters(String params) {
        try {
            getFaceConfig().setLivenessAPIParameters(new JSONObject(params));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void setLivenessAPIHeaders(String headers) {
        try {
            getFaceConfig().setLivenessAPIHeaders(new JSONObject(headers));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @ReactMethod
    public void start( final Callback resultCallback) {
        hasBeenCalled = false;

        HVFaceActivity.start(getCurrentActivity(), getFaceConfig(), new FaceCaptureCompletionHandler() {
            @Override
            public void onResult(HVError error, JSONObject result, JSONObject headers) {

                try {
                    WritableMap errorObj = Arguments.createMap();
                    WritableMap resultsObj = Arguments.createMap();
                    WritableMap headersObj = Arguments.createMap();
                    if (error != null) {
                        errorObj.putInt("errorCode", error.getErrorCode());
                        errorObj.putString("errorMessage", error.getErrorMessage());
                        if(!hasBeenCalled) {
                            hasBeenCalled = true;
                            resultCallback.invoke(errorObj, null, null);
                        }
                    } else {
                        if (result != null) {
                            resultsObj = null;
                            try {
                                resultsObj = RNHVNetworkHelper.convertJsonToMap(result);
                            } catch (Exception e) {

                            }
                            resultsObj.putString("response", result.toString());
                        }
                        if (headers != null) {
                            headersObj = null;
                            try {
                                headersObj = RNHVNetworkHelper.convertJsonToMap(headers);
                            } catch (Exception e) {

                            }
                        }
                        if(!hasBeenCalled) {
                            hasBeenCalled = true;
                            resultCallback.invoke(null, resultsObj, headersObj);
                        }

                    }

            }catch(Exception e){

            }
        }});

    }


}
