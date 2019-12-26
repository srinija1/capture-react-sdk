package co.hyperverge.hypersnapdemoapp_react;

import android.content.Context;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;

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
    public   void makeOCRCall( String endpoint, String documentUri, ReadableMap parameters, ReadableMap headers, final Callback resultCallback) {

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
                         resultsObj = null;
                         try {
                             resultsObj = convertJsonToMap(result);
                         }catch(Exception e){
                            e.printStackTrace();
                         }
                     }
                     if (headers != null) {
                         headersObj = null;
                         try {
                             headersObj = convertJsonToMap(headers);
                         }catch(Exception e){
                            e.printStackTrace();
                         }
                     }
                     resultCallback.invoke(null, resultsObj, headersObj);

                 }
             }

         };
        try {
            JSONObject params = new JSONObject();
            if(parameters != null) {
                params = convertMapToJson(parameters);
            }

            JSONObject headers2 = new JSONObject();
            if(headers != null) {
                headers2 = convertMapToJson(headers);
            }
            HVNetworkHelper.makeOCRCall(getCurrentActivity(), endpoint, documentUri, params, headers2, completionCallback);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    @ReactMethod
    public void makeFaceMatchCall( String endpoint, String faceUri, String documentUri, ReadableMap parameters, ReadableMap  headers,  final Callback resultCallback) {
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
                        resultsObj = null;
                        try {
                            resultsObj = convertJsonToMap(result);
                        }catch(Exception e){

                        }
                    }
                    if (headers != null) {
                        headersObj = null;
                        try {
                            headersObj = convertJsonToMap(headers);
                        }catch(Exception e){

                        }
                    }
                    resultCallback.invoke(null, resultsObj, headersObj);

                }
            }

        };
        try {
            JSONObject params = new JSONObject();
            if(parameters != null) {
                params = convertMapToJson(parameters);
            }

            JSONObject headers2 = new JSONObject();
            if(headers != null) {
                headers2 = convertMapToJson(headers);
            }

            HVNetworkHelper.makeFaceMatchCall(getCurrentActivity(), endpoint, faceUri, documentUri, params, headers2, completionCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    protected static WritableMap convertJsonToMap(JSONObject jsonObject) throws JSONException {
        WritableMap map = new WritableNativeMap();

        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                map.putMap(key, convertJsonToMap((JSONObject) value));
            } else if (value instanceof  JSONArray) {
                map.putArray(key, convertJsonToArray((JSONArray) value));
            } else if (value instanceof  Boolean) {
                map.putBoolean(key, (Boolean) value);
            } else if (value instanceof  Integer) {
                map.putInt(key, (Integer) value);
            } else if (value instanceof  Double) {
                map.putDouble(key, (Double) value);
            } else if (value instanceof String)  {
                map.putString(key, (String) value);
            } else {
                map.putString(key, value.toString());
            }
        }
        return map;
    }

    private static WritableArray convertJsonToArray(JSONArray jsonArray) throws JSONException {
        WritableArray array = new WritableNativeArray();

        for (int i = 0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof JSONObject) {
                array.pushMap(convertJsonToMap((JSONObject) value));
            } else if (value instanceof  JSONArray) {
                array.pushArray(convertJsonToArray((JSONArray) value));
            } else if (value instanceof  Boolean) {
                array.pushBoolean((Boolean) value);
            } else if (value instanceof  Integer) {
                array.pushInt((Integer) value);
            } else if (value instanceof  Double) {
                array.pushDouble((Double) value);
            } else if (value instanceof String)  {
                array.pushString((String) value);
            } else {
                array.pushString(value.toString());
            }
        }
        return array;
    }

    private static JSONObject convertMapToJson(ReadableMap readableMap) throws JSONException {
        JSONObject object = new JSONObject();
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            switch (readableMap.getType(key)) {
                case Null:
                    object.put(key, JSONObject.NULL);
                    break;
                case Boolean:
                    object.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    object.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    object.put(key, readableMap.getString(key));
                    break;
                case Map:
                    object.put(key, convertMapToJson(readableMap.getMap(key)));
                    break;
                case Array:
                    object.put(key, convertArrayToJson(readableMap.getArray(key)));
                    break;
            }
        }
        return object;
    }

    private static JSONArray convertArrayToJson(ReadableArray readableArray) throws JSONException {
        JSONArray array = new JSONArray();
        for (int i = 0; i < readableArray.size(); i++) {
            switch (readableArray.getType(i)) {
                case Null:
                    break;
                case Boolean:
                    array.put(readableArray.getBoolean(i));
                    break;
                case Number:
                    array.put(readableArray.getDouble(i));
                    break;
                case String:
                    array.put(readableArray.getString(i));
                    break;
                case Map:
                    array.put(convertMapToJson(readableArray.getMap(i)));
                    break;
                case Array:
                    array.put(convertArrayToJson(readableArray.getArray(i)));
                    break;
            }
        }
        return array;
    }
}
