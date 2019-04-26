package co.hyperverge.hypersnapdemoapp_react;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class RNHyperSnapParams extends ReactContextBaseJavaModule {

    public static final String RegionIndia = "Region.India";
    public static final String RegionAsiaPacific = "Region.AsiaPacific";
    public static final String RegionUnitedStates = "Region.UnitedStates";
    public static final String ProductFACEID = "Product.FACEID";
    public static final String ProductIAM = "Product.IAM";
    public static final String DocumentCARD = "Document.CARD";
    public static final String DocumentA4 = "Document.A4";
    public static final String DocumentPASSPORT = "Document.PASSPORT";
    public static final String DocumentOTHER = "Document.OTHER";
    public static final String LivenessModeNONE = "LivenessMode.NONE";
    public static final String LivenessModeTEXTURELIVENESS = "LivenessMode.TEXTURELIVENESS";
    public static final String LivenessModeTEXTUREANDGESTURELIVENESS = "LivenessMode.TEXTUREANDGESTURELIVENESS";




    public RNHyperSnapParams(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNHyperSnapParams";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String,Object> constants = new HashMap<>();
        constants.put("RegionIndia",RegionIndia);
        constants.put("RegionAsiaPacific",RegionAsiaPacific);
        constants.put("RegionUnitedStates",RegionUnitedStates);
        constants.put("ProductFaceID",ProductFACEID);
        constants.put("ProductIAM",ProductIAM);
        constants.put("DocumentTypeCard",DocumentCARD);
        constants.put("DocumentTypeA4",DocumentA4);
        constants.put("DocumentTypePassport",DocumentPASSPORT);
        constants.put("DocumentTypeOther",DocumentOTHER);
        constants.put("LivenessModeNone",LivenessModeNONE);
        constants.put("LivenessModeTextureLiveness",LivenessModeTEXTURELIVENESS);
        constants.put("LivenessModeTextureAndGestureLiveness",LivenessModeTEXTUREANDGESTURELIVENESS);
        return constants;
    }
}
