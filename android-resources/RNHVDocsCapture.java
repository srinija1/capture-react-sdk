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
import co.hyperverge.hypersnapsdk.objects.HVFaceConfig;

public class RNHVDocsCapture extends ReactContextBaseJavaModule {


    public RNHVDocsCapture(ReactApplicationContext reactContext) {
        super(reactContext);

    }

    public String docType;


    public Float aspectRatio;

    HVDocConfig docConfig = null;

    HVDocConfig.Document doc;

    boolean hasBeenCalled;


    @Override
    public String getName() {
        return "RNHVDocsCapture";
    }


    public HVDocConfig getDocConfig() {
        if (this.docConfig == null) {
            this.docConfig = new HVDocConfig();
        }
        return this.docConfig;

    }

    @ReactMethod
    public void setCustomUIStrings(String customStrings) {
        try {
            JSONObject stringObj = new JSONObject();
            if (customStrings == null && !customStrings.trim().isEmpty())
                stringObj = new JSONObject(customStrings);
            getDocConfig().setCustomUIStrings(stringObj);
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
        getDocConfig().setShouldAddPadding(shouldSetPadding.booleanValue());

    }

    @ReactMethod
    public void setPadding(Number padding) {
        getDocConfig().setPadding((float) padding);

    }


    @ReactMethod
    public void setShouldShowFlashIcon(Boolean shouldShowFlashIcon) {
        getDocConfig().setShouldShowFlashIcon(shouldShowFlashIcon.booleanValue());
    }

    @ReactMethod
    public void setShouldShowReviewScreen(Boolean shouldShowReviewScreen) {
        getDocConfig().setShouldShowReviewScreen(shouldShowReviewScreen.booleanValue());
    }

    @ReactMethod
    public void setDocCaptureSubText(String subText) {
        getDocConfig().setDocCaptureSubText(subText);
    }

    @ReactMethod
    public void setDocCaptureDescription(String description) {
        getDocConfig().setDocCaptureDescription(description);
    }


    @ReactMethod
    public void setShouldShowInstructionPage(Boolean shouldShowInstructionPage) {
        getDocConfig().setShouldShowInstructionPage(shouldShowInstructionPage.booleanValue());
    }


    @ReactMethod
    public HVDocConfig getConfig() {
        return getDocConfig();
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

            getDocConfig().setDocumentType(this.doc);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }


    @ReactMethod
    public void setDocCaptureTitle(String titleText) {
        getDocConfig().setDocCaptureTitle(titleText);
    }


    @ReactMethod
    public void setDocReviewTitle(String docReviewTitle) {

        getDocConfig().setDocReviewTitle(docReviewTitle);
    }

    @ReactMethod
    public void setDocReviewDescription(String docReviewDescription) {
        getDocConfig().setDocReviewDescription(docReviewDescription);

    }


    @ReactMethod
    public void start(final Callback resultCallback) {
        hasBeenCalled = false;

        HVDocsActivity.start(getCurrentActivity(), getDocConfig(), new DocCaptureCompletionHandler() {
            @Override
            public void onResult(HVError error, JSONObject result) {
                try {

                    WritableMap errorObj = Arguments.createMap();
                    WritableMap resultsObj = Arguments.createMap();
                    if (error != null) {
                        errorObj.putInt("errorCode", error.getErrorCode());
                        errorObj.putString("errorMessage", error.getErrorMessage());
                        if (!hasBeenCalled) {
                            hasBeenCalled = true;
                            resultCallback.invoke(errorObj, null);

                        }

                    } else {
                        Iterator<?> keys = result.keys();
                        while (keys.hasNext()) {
                            String key = (String) keys.next();
                            try {
                                resultsObj.putString(key, (String) result.get(key));
                            } catch (JSONException e) {

                            }
                        }
                        if (!hasBeenCalled) {
                            hasBeenCalled = true;
                            resultCallback.invoke(null, resultsObj);
                        }
                    }
                } catch (
                        Exception e)

                {

                }
            } });

        }

    }
