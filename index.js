import {NativeModules,NativeEventEmitter} from 'react-native';
const EventEmitter = new NativeEventEmitter(NativeModules.TruecallerModule);
const RCTTruecaller = NativeModules.TruecallerModule;


const TRUECALLER = {};
export const TRUECALLEREvent = 
{
  TrueProfileResponse: 'profileSuccessReponse',
  TrueProfileResponseError: 'profileErrorReponse',
};

TRUECALLER.initializeClient = () => 
{
    return RCTTruecaller.initializeClient();
};
// TRUECALLER.initializeClientIOS = (appKey,appLink) => 
// {
//     return RCTTruecaller.initializeClientIOS(appKey,appLink);
// };
TRUECALLER.requestTrueProfile = () => 
{
    return RCTTruecaller.requestTrueProfile();
};

TRUECALLER.on = (event, callback) => 
{
    if (!Object.values(TRUECALLEREvent).includes(event)) {
      throw new Error(`Invalid TRUECALLEREvent event subscription, use import {TRUECALLEREvent} from 'react-native-truecaller' to avoid typo`);
    };
    return EventEmitter.addListener(event, callback);
};



export default TRUECALLER;