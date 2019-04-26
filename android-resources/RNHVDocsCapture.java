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

import co.hyperverge.hypersnapsdk.activities.HVDocsActivity;
import co.hyperverge.hypersnapsdk.objects.Error;
import co.hyperverge.hypersnapsdk.objects.HyperSnapParams;
import co.hyperverge.hypersnapsdk.listeners.CaptureCompletionHandler;

public class RNHVDocsCapture extends ReactContextBaseJavaModule {
    public String docType;
    public String topText;
    public String bottomText;
    public Float aspectRatio;

    public RNHVDocsCapture(ReactApplicationContext reactContext) {
        super(reactContext);

    }

    @Override
    public String getName() {
        return "RNHVDocsCapture";
    }

    @ReactMethod
    public void setDocumentType(String docTypeValue){
        try{
            docType = docTypeValue.split("\\.")[1];
        }
        catch (Exception exp){
            exp.printStackTrace();
        }
    }

    @ReactMethod
    public void setTopText(String topTextValue){
        topText = topTextValue;
    }

    @ReactMethod
    public void setBottomText(String bottomTextValue){
        bottomText = bottomTextValue;
    }

    @ReactMethod
    public void setAspectRatio(float aspectRatioValue){
        aspectRatio = aspectRatioValue;
    }

    @ReactMethod
    public void start(final Callback resultCallback){
        if(docType==null){
            docType="A4";
        }
        HyperSnapParams.Document doc = HyperSnapParams.Document.valueOf(docType);
        if(topText!=null){
            doc.setTopText(topText);
        }
        if(bottomText!=null){
            doc.setBottomText(bottomText);
        }
        if(aspectRatio!=null){
            doc.setAspectRatio(aspectRatio);
        }
        HVDocsActivity.start(getCurrentActivity().getApplicationContext(), doc, new CaptureCompletionHandler() {
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
        });

    }

}
