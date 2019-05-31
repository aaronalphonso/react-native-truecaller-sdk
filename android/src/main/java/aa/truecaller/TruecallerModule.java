package aa.truecaller;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueSDK;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;
import com.truecaller.android.sdk.TrueSdkScope;

import java.util.Map;


public class TruecallerModule extends ReactContextBaseJavaModule implements ITrueCallback{

    private TrueSDK trueSDK;
    private String TAG="TruecallerModule";
    private String mTruecallerRequestNonce;

    private  ReactContext reactContext;
    private ActivityEventListener mEventListener = new BaseActivityEventListener(){
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (null != trueSDK && trueSDK.isUsable()) {
                trueSDK.onActivityResultObtained(activity, resultCode, data);
            }
        }
    };

    //Need to hookup this on react native side
    @ReactMethod
    public boolean isUsable() {
        return trueSDK.isUsable();
    }


    public TruecallerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext=reactContext;
        reactContext.addActivityEventListener(mEventListener);
    }

    @Override
    public String getName() {
        return "TruecallerModule";
    }


    private int consentModePopup(String mode) {
        switch (mode) {
            case "CONSENT_MODE_POPUP":
                return TrueSdkScope.CONSENT_MODE_POPUP;
            case "CONSENT_MODE_FULLSCREEN":
                return TrueSdkScope.CONSENT_MODE_FULLSCREEN;
            default:
                return TrueSdkScope.CONSENT_MODE_FULLSCREEN;
        }
    }


    @ReactMethod
    public void initializeClient() {
        TrueSdkScope trueScope = new TrueSdkScope.Builder(this.reactContext, this)
                .consentMode(TrueSdkScope.CONSENT_MODE_POPUP )
                .consentTitleOption( TrueSdkScope.SDK_CONSENT_TITLE_GET_STARTED )
                .footerType( TrueSdkScope.FOOTER_TYPE_CONTINUE )
                .build();

        TrueSDK.init(trueScope);
        trueSDK = TrueSDK.getInstance();
        Log.d("SDK instance is", trueSDK.toString());
    }

    @ReactMethod
    public void requestTrueProfile() {
        Activity activity = getCurrentActivity();
        if (trueSDK.isUsable()) {
            trueSDK.getUserProfile(activity);
            Log.d("Truecaller", "Truecaller installed.");
        } else {
            Log.d("Truecaller", "Truecaller not installed");
        }
    }
    @Override
    public void onSuccessProfileShared(@NonNull TrueProfile trueProfile)
    {
        WritableMap params = Arguments.createMap();
        params.putString("firstName",trueProfile.firstName);
        params.putString("lastName",trueProfile.lastName);
        params.putString("phoneNumber",trueProfile.phoneNumber);
        params.putString("gender",trueProfile.gender);
        params.putString("street",trueProfile.street);
        params.putString("city",trueProfile.city);
        params.putString("zipcode",trueProfile.zipcode);
        params.putString("countryCode",trueProfile.countryCode);
        params.putString("facebookId",trueProfile.facebookId);
        params.putString("twitterId",trueProfile.twitterId);
        params.putString("email",trueProfile.email);
        params.putString("url",trueProfile.url);
        params.putString("avatarUrl",trueProfile.avatarUrl);
        params.putBoolean("isTrueName",trueProfile.isTrueName);
        params.putBoolean("isAmbassador",trueProfile.isAmbassador);
        params.putString("companyName",trueProfile.companyName);
        params.putString("jobTitle",trueProfile.jobTitle);
        params.putString("payload",trueProfile.payload);
        params.putString("signature",trueProfile.signature);
        params.putString("signatureAlgorithm",trueProfile.signatureAlgorithm);
        params.putString("requestNonce",trueProfile.requestNonce);
        params.putBoolean("isSimChanged",trueProfile.isSimChanged);
        params.putString("verificationMode",trueProfile.verificationMode);
        sendEvent(reactContext, "profileSuccessReponse", params);
    }

    @Override
    public void onFailureProfileShared(@NonNull TrueError trueError) {
        WritableMap params = Arguments.createMap();
        params.putString("profile","error");

        sendEvent(reactContext, "profileErrorReponse", params);

    }

    @Override
    public void onVerificationRequired() {

    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
