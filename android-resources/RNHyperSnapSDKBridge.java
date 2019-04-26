package co.hyperverge.hypersnapdemoapp_react;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RNHyperSnapSDKBridge implements ReactPackage {

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(
            ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        modules.add(new RNHyperSnapSDK(reactContext));
        modules.add(new RNHVDocsCapture(reactContext));
        modules.add(new RNHVQRScanCapture(reactContext));
        modules.add(new RNHVFaceCapture(reactContext));
        modules.add(new RNHVNetworkHelper(reactContext));
        modules.add(new RNHyperSnapParams(reactContext));
        return modules;
    }
}