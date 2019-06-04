import {NativeModules,NativeEventEmitter} from 'react-native';
const EventEmitter = new NativeEventEmitter(NativeModules.TruecallerModule);
const RCTTruecaller = NativeModules.TruecallerModule;


const TRUECALLER = {};

// Call this method to initialize client before using it. 
// Pass in configuration params as required.
TRUECALLER.initializeClient = (
    consentMode = TRUECALLER_CONSENT_MODE.Popup,
    consentTitle = TRUECALLER_CONSENT_TITLE.Login,
    footerType = TRUECALLER_FOOTER_TYPE.Continue
) => {
    return RCTTruecaller.initializeClient(consentMode, consentTitle, footerType);
};

// TRUECALLER.initializeClientIOS = (appKey,appLink) => 
// {
//     return RCTTruecaller.initializeClientIOS(appKey,appLink);
// };

// Request consent from the user to fetch his profile data
TRUECALLER.requestTrueProfile = () => {
    return RCTTruecaller.requestTrueProfile();
};

// Whether the app is installed and logged in
TRUECALLER.isUsable = (resultCallBack) => {
  return RCTTruecaller.isUsable(resultCallBack);
}

// Use this method to add listeners for when the user accepts or rejects the consent request
// The event will be one of TrueProfileResponse or TrueProfileResponseError.
// The callback will be passed a single parameter which will be the user profile if the user accepts the request
// or an error object if the user rejects the request.
TRUECALLER.on = (event, callback) => {
    if (!Object.values(TRUECALLER_EVENT).includes(event)) {
      throw new Error(`Invalid TRUECALLER_EVENT event subscription, use import {TRUECALLER_EVENT} from 'react-native-truecaller-sdk' to avoid typo`);
    };
    return EventEmitter.addListener(event, callback);
};

// Callback event success and failure types
export const TRUECALLER_EVENT = {
    TrueProfileResponse: 'profileSuccessReponse',
    TrueProfileResponseError: 'profileErrorReponse',
};


// Constants for configuration
export const TRUECALLER_CONSENT_MODE = {
    Popup: 'CONSENT_MODE_POPUP',
    FullScreen: 'CONSENT_MODE_FULLSCREEN'
};
  
export const TRUECALLER_CONSENT_TITLE = {
    Login: 'SDK_CONSENT_TITLE_LOG_IN',
    SignUp: 'SDK_CONSENT_TITLE_SIGN_UP',
    SignIn: 'SDK_CONSENT_TITLE_SIGN_IN',
    Verify: 'SDK_CONSENT_TITLE_VERIFY',
    Register: 'SDK_CONSENT_TITLE_REGISTER',
    GetStarted: 'SDK_CONSENT_TITLE_GET_STARTED'
};

export const TRUECALLER_FOOTER_TYPE = {
    Skip: 'FOOTER_TYPE_SKIP',
    Continue: 'FOOTER_TYPE_CONTINUE'
};

export default TRUECALLER;