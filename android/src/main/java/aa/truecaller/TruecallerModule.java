package aa.truecaller;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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



public class TruecallerModule extends ReactContextBaseJavaModule implements ITrueCallback{

    private TrueSDK trueSDK;
    private String TAG="TruecallerModule";

    private  ReactContext reactContext;
    private ActivityEventListener mEventListener = new BaseActivityEventListener(){
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
            if (null != trueSDK && trueSDK.isUsable()) {
                trueSDK.onActivityResultObtained(activity, resultCode, data);
            }
        }
    };

    private boolean isUsable() {
        return trueSDK.isUsable();
    }

    @ReactMethod
    public void isUsable(Callback boolCallBack) {
        boolCallBack.invoke(trueSDK.isUsable());
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

    private int getConsentMode(String mode) {
        switch (mode) {
            case "CONSENT_MODE_POPUP":
                return TrueSdkScope.CONSENT_MODE_POPUP;
            case "CONSENT_MODE_FULLSCREEN":
                return TrueSdkScope.CONSENT_MODE_FULLSCREEN;
            default:
                return TrueSdkScope.CONSENT_MODE_POPUP;
        }
    }

    private int getConsentTitle(String title) {
        switch (title) {
            case "SDK_CONSENT_TITLE_LOG_IN":
                return TrueSdkScope.SDK_CONSENT_TITLE_LOG_IN;
            case "SDK_CONSENT_TITLE_SIGN_UP":
                return TrueSdkScope.SDK_CONSENT_TITLE_SIGN_UP;
            case "SDK_CONSENT_TITLE_SIGN_IN":
                return TrueSdkScope.SDK_CONSENT_TITLE_SIGN_IN;
            case "SDK_CONSENT_TITLE_VERIFY":
                return TrueSdkScope.SDK_CONSENT_TITLE_VERIFY;
            case "SDK_CONSENT_TITLE_REGISTER":
                return TrueSdkScope.SDK_CONSENT_TITLE_REGISTER;
            case "SDK_CONSENT_TITLE_GET_STARTED":
                return TrueSdkScope.SDK_CONSENT_TITLE_GET_STARTED;
            default:
                return TrueSdkScope.SDK_CONSENT_TITLE_GET_STARTED;
        }
    }

    private int getFooterType(String footerType) {
        switch (footerType) {
            case "FOOTER_TYPE_SKIP":
                return TrueSdkScope.FOOTER_TYPE_SKIP;
            case "FOOTER_TYPE_CONTINUE":
                return TrueSdkScope.FOOTER_TYPE_CONTINUE;
            default:
                return TrueSdkScope.FOOTER_TYPE_CONTINUE;
        }
    }

    @ReactMethod
    public void initializeClient(String consentMode, String consentTitle, String footerType) {
        Log.d("SDK options: ", "Truecaller Mode: "+consentMode+". Title: "+consentTitle+". Footer: "+footerType);
        TrueSdkScope trueScope = new TrueSdkScope.Builder(this.reactContext, this)
                .consentMode( this.getConsentMode(consentMode) )
                .consentTitleOption( this.getConsentTitle(consentTitle) )
                .footerType( this.getFooterType(footerType) )
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
        } 
        else {
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
        params.putInt("errorCode",trueError.getErrorType());

        sendEvent(reactContext, "profileErrorReponse", params);

    }

    @Override
    public void onVerificationRequired() {

    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }
}
