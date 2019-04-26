package co.hyperverge.hypersnapdemoapp_react;

import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import co.hyperverge.hypersnapsdk.HyperSnapSDK;
import co.hyperverge.hypersnapsdk.objects.HyperSnapParams;

import co.hyperverge.hypersnapsdk.objects.HVDocConfig;
import co.hyperverge.hypersnapsdk.objects.HVFaceConfig;


public class RNHyperSnapSDK extends ReactContextBaseJavaModule{


    public RNHyperSnapSDK(ReactApplicationContext reactContext){
        super(reactContext);
    }

    @Override
    public String getName(){
        return "RNHyperSnapSDK";
    }

    @ReactMethod
    public void initialize(String appId, String appName, String regionValue, String productValue){
        String[] regionParams = regionValue.split("\\.");
        String[] productParams = productValue.split("\\.");
        HyperSnapParams.Region region = getHVHyperSnapParam(regionParams[0],regionParams[1], HyperSnapParams.Region.class);
        HyperSnapParams.Product product = getHVHyperSnapParam(productParams[0],productParams[1],HyperSnapParams.Product.class);
        HyperSnapSDK.init(getCurrentActivity() , appId,appName,region,product);

    }

    public <T> T getHVHyperSnapParam(String enumClass, String param, Class<T> type){
        switch (enumClass){
            case "Product":{
                return type.cast(HyperSnapParams.Product.valueOf(param));
            }
            case "Region":{
                return  type.cast(HyperSnapParams.Region.valueOf(param));
            }
            case "Document":{
                return type.cast(HVDocConfig.Document.valueOf(param));
            }
            case "LivenessMode":{
                return type.cast(HVFaceConfig.LivenessMode.valueOf(param));
            }
            default:{
                return null;
            }
        }
    }

}
