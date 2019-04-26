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

import co.hyperverge.hypersnapsdk.activities.HVDocsActivity;
import co.hyperverge.hypersnapsdk.listeners.DocCaptureCompletionHandler;
import co.hyperverge.hypersnapsdk.objects.HVDocConfig;
import co.hyperverge.hypersnapsdk.objects.HVError;

public class RNHVDocsCapture extends ReactContextBaseJavaModule {


    public RNHVDocsCapture(ReactApplicationContext reactContext) {
        super(reactContext);

    }

    public String docType;


    public Float aspectRatio;

    HVDocConfig docConfig = null;

    HVDocConfig.Document doc;


    @Override
    public String getName() {
        return "RNHVDocsCapture";
    }

    @ReactMethod
    public void createNewConfig() {
        this.docConfig = new HVDocConfig();

    }

    @ReactMethod
    public void setCustomUIStrings(String customStrings){
        try {
            JSONObject stringObj = new JSONObject();
            if( customStrings == null && !customStrings.trim().isEmpty() )
                stringObj = new JSONObject(customStrings);
            this.docConfig.setCustomUIStrings(stringObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void setAspectRatio(Float aspectRatioValue) {
        aspectRatio = aspectRatioValue;
    }

    @ReactMethod
    public void setShouldAddPadding(Boolean shouldSetPadding) {
        this.docConfig.setShouldAddPadding(shouldSetPadding.booleanValue());

    }

    @ReactMethod
    public void setPadding(Number padding) {
        this.docConfig.setPadding((float) padding);

    }


    @ReactMethod
    public void setShouldShowFlashIcon(Boolean shouldShowFlashIcon) {
        this.docConfig.setShouldShowFlashIcon(shouldShowFlashIcon.booleanValue());
    }

    @ReactMethod
    public void setShouldShowReviewScreen(Boolean shouldShowReviewScreen) {
        this.docConfig.setShouldShowReviewScreen(shouldShowReviewScreen.booleanValue());
    }

    @ReactMethod
    public void setDocCaptureSubText(String subText) {
        this.docConfig.setDocCaptureSubText(subText);
    }

    @ReactMethod
    public void setDocCaptureDescription(String description) {

        this.docConfig.setDocCaptureDescription(description);
    }


    @ReactMethod
    public void setShouldShowInstructionPage(Boolean shouldShowInstructionPage) {
        this.docConfig.setShouldShowInstructionPage(shouldShowInstructionPage.booleanValue());
    }


    @ReactMethod
    public HVDocConfig getConfig() {
        return this.docConfig;
    }


    @ReactMethod
    public void setDocumentType(String docTypeValue) {
        try {
            this.docType = docTypeValue.split("\\.")[1];
            if (this.docType == null) {
                this.docType = "Card";
            }


            this.doc = HVDocConfig.Document.valueOf(docType);
            if (aspectRatio != null)
                this.doc.setAspectRatio(this.aspectRatio);

            this.docConfig.setDocumentType(this.doc);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


    @ReactMethod
    public void setDocCaptureTitle(String titleText) {
        this.docConfig.setDocCaptureTitle(titleText);
    }


    @ReactMethod
    public void setDocReviewTitle(String docReviewTitle) {

        this.docConfig.setDocReviewTitle(docReviewTitle);
    }

    @ReactMethod
    public void setDocReviewDescription(String docReviewDescription) {
        this.docConfig.setDocReviewDescription(docReviewDescription);

    }


    @ReactMethod
    public void start(final Callback resultCallback) {
 
        HVDocsActivity.start(getCurrentActivity(), this.docConfig, new DocCaptureCompletionHandler() {
            @Override
            public void onResult(HVError error, JSONObject result) {
                WritableMap errorObj = Arguments.createMap();
                WritableMap resultsObj = Arguments.createMap();
                if (error != null) {
                    errorObj.putInt("errorCode", error.getErrorCode());
                    errorObj.putString("errorMessage", error.getErrorMessage());
                    resultCallback.invoke(errorObj, null);
                } else {
                    Iterator<?> keys = result.keys();
                    while (keys.hasNext()) {
                        String key = (String) keys.next();
                        try {
                            resultsObj.putString(key, (String) result.get(key));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    resultCallback.invoke(null, resultsObj);
                }
            }
        });

    }

}
