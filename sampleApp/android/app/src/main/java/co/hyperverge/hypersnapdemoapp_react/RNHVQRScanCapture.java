package co.hyperverge.hypersnapdemoapp_react;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import javax.annotation.Nonnull;

import co.hyperverge.hypersnapsdk.activities.HVQrScannerActivity;
import co.hyperverge.hypersnapsdk.listeners.QRScannerCompletionHandler;
import co.hyperverge.hypersnapsdk.objects.HVError;

public class RNHVQRScanCapture extends ReactContextBaseJavaModule {

    public RNHVQRScanCapture(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
    }

    boolean hasBeenCalled;

    @Nonnull
    @Override
    public String getName() {
        return "RNHVQRScanCapture";
    }

    @ReactMethod
    public void start(final Callback resultCallback) {
        hasBeenCalled = false;
        HVQrScannerActivity.start(getCurrentActivity(), new QRScannerCompletionHandler() {
            @Override
            public void onResult(HVError error, JSONObject result) {
                try {
                    WritableMap errorObj = Arguments.createMap();
                    WritableMap resultsObj = Arguments.createMap();
                    if (error != null) {
                        errorObj.putInt("errorCode", error.getErrorCode());
                        errorObj.putString("errorMessage", error.getErrorMessage());
                        if(!hasBeenCalled) {
                            hasBeenCalled = true;
                            resultCallback.invoke(errorObj, null);
                        }
                        return;
                    } else if (result != null) {
                        Iterator<?> keys = result.keys();
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            try {
                                if (result.get(key) instanceof String) {
                                    resultsObj.putString(key, (String) result.get(key));
                                } else if (result.get(key) instanceof JSONObject) {
                                    resultsObj.putString(key, result.get(key).toString());
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(!hasBeenCalled) {
                        hasBeenCalled = true;
                        resultCallback.invoke(null, resultsObj);
                    }
                    return;


            }catch(Exception e){}


        }});
    }
}
