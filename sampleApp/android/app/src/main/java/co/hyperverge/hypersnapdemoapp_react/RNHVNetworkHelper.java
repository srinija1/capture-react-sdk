package co.hyperverge.hypersnapdemoapp_react;

import android.content.Context;

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

import co.hyperverge.hypersnapsdk.listeners.APICompletionCallback;
import co.hyperverge.hypersnapsdk.network.HVNetworkHelper;
import co.hyperverge.hypersnapsdk.objects.HVError;
import co.hyperverge.hypersnapsdk.objects.HyperSnapParams;

public class RNHVNetworkHelper extends ReactContextBaseJavaModule {
    public RNHVNetworkHelper(@Nonnull ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Nonnull
    @Override
    public String getName() {
        return "RNHVNetworkHelper";
    }

    @ReactMethod
    public   void makeOCRCall( String endpoint, String documentUri, String parameters, String headers, final Callback resultCallback) {
         APICompletionCallback completionCallback = new APICompletionCallback() {
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

         };
        try {
            HVNetworkHelper.makeOCRCall(getCurrentActivity(), endpoint, documentUri, new JSONObject(parameters), new JSONObject(headers), completionCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @ReactMethod
    public   void makeFaceMatchCall( String endpoint, String faceUri, String documentUri, String parameters, String  headers,  final Callback resultCallback) {
        APICompletionCallback completionCallback = new APICompletionCallback() {
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

        };
        try {
            HVNetworkHelper.makeFaceMatchCall(getCurrentActivity(), endpoint, faceUri, documentUri, new JSONObject(parameters), new JSONObject(headers), completionCallback  );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
