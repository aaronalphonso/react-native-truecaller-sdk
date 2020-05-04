# react-native-truecaller-sdk

A react native module for Truecaller SDK Integration. Truecaller can be used as an alternate method of authenticating users for login/signup. This module exposes all the functionality available in the android-sdk for use in your react native app. (Note: Supports only android for now)

  

## Truecaller Authentication Flow

The typical authentication flow using truecaller would be as follows:

- The user needs to have the **truecaller app installed** on his device and he must be logged in to the truecaller app.

- When the user opens your app and chooses to **authenticate via truecaller** (based on your provided CTA[call to action] button), a standard truecaller consent box is shown to him. (The CTA is not required to trigger the consent request, you can do it directly too if needed!)

- If the user accepts the consent request, his **truecaller profile data is shared with your app** and you can continue with the login

  

## Module Installation

Run the following command to install the package:

> npm install react-native-truecaller-sdk --save

  

Then run the following command to link the package

> npm link react-native-truecaller-sdk

  
  

## Setup and Configuration

Before proceeding, head on over to the truecaller developer portal ( [https://developer.truecaller.com/login](https://developer.truecaller.com/login) ) and register there if you haven't done so already. You will need to login and create an app on truecaller. This app will be used by truecaller to identify your application.

  

To create this app you will need to provide the package name and the SHA1 fingerprint of your react native app. (Note that you will need to create a separate app for each of your builds. release build, debug build, etc. as they will have a different SHA1 fingerprint)

  
  

Once created, you will get a **truecaller appkey** which you need to add in your react native app. Open your AndroidManifest.xml file and add the following meta-data element to the application element.

```

<meta-data android:name="com.truecaller.android.sdk.PartnerKey" android:value="@string/truecaller_appkey"/>

```

  

Open your strings.xml file and add a new string with the name "truecaller_appkey" and value as your "appKey" as obtained from the truecaller dashboard after creating you app. For example:

```

<string name="truecaller_appkey">1941341109453811</string>

```

  

## Usage

  

Import the module and some of it's members for configuration of the consent request and handling of the accept/reject callback.

```

import TRUECALLER, {
TRUECALLER_EVENT,
TRUECALLER_CONSENT_MODE,
TRUECALLER_CONSENT_TITLE,
TRUECALLER_FOOTER_TYPE
} from 'react-native-truecaller-sdk'

```

Initialize the Truecaller instance before you can use it.

```

TRUECALLER.initializeClient()

```

You can also pass in some values while initializing if you need to customize the consent request.

```

TRUECALLER.initializeClient(
    TRUECALLER_CONSENT_MODE.Popup,
    TRUECALLER_CONSENT_TITLE.Login,
    TRUECALLER_FOOTER_TYPE.Continue
)

```

**Consent mode:** These will determine whether the consent request shows up in a pop-up or fullscreen. Possible values for Consent mode are:

```

TRUECALLER_CONSENT_MODE.Popup
TRUECALLER_CONSENT_MODE.FullScreen

```

**Consent Title:** These values determine the title of the consent request shown to the user. For example if your app name is XYZ and you select "TRUECALLER_CONSENT_TITLE.Login", then the title shown to the user will be "Login to XYZ". Possible values for Consent Title are:

```

TRUECALLER_CONSENT_TITLE.Login
TRUECALLER_CONSENT_TITLE.SignUp
TRUECALLER_CONSENT_TITLE.SignIn
TRUECALLER_CONSENT_TITLE.Verify
TRUECALLER_CONSENT_TITLE.Register
TRUECALLER_CONSENT_TITLE.GetStarted

```

**Footer-type:** These determine the title of the alternate button shown to the user if he wishes to reject the consent request.

```

// To use the "USE ANOTHER NUMBER" CTA at the bottom of the consent request
TRUECALLER_FOOTER_TYPE.Skip

// To use the "SKIP" CTA at the bottom of the consent request
TRUECALLER_FOOTER_TYPE.Continue

```

  

You can check if the truecaller app is installed and logged in on the user's device using the following snippet:

```

TRUECALLER.isUsable(result => {
    if (result === true)
        console.log('Authenticate via truecaller flow can be used')
    else
        console.log('Either truecaller app is not installed or user is not logged in')
}

```

  

Once you know the truecaller app is usable, you can proceed to request the user's consent to share their truecaller profile with your app with the following:

```

TRUECALLER.requestTrueProfile()

```

  

Before doing this however, you should set up some handlers for the success/failure events of the user accepting/rejecting the consent request. Register your callback functions like below.

  

For handling the success event:

```

TRUECALLER.on(TRUECALLER_EVENT.TrueProfileResponse, profile => {
    console.log('Truecaller profile data: ',profile)
    // add other logic here related to login/sign-up as per your use-case.
}

```

  

For handling the reject event:

```

TRUECALLER.on(TRUECALLER_EVENT.TrueProfileResponseError, error => {
    console.log('User rejected the truecaller consent request! ', error);

    if(error && error.errorCode){
        switch(error.errorCode){
            case 1:{
                //Network Failure
                break;
            }
            case 2:{
                //User pressed back
                break;
            }
            case 3:{
                //Incorrect Partner Key
                break;
            }
            case 4:{
                //User Not Verified on Truecaller
                break;
            }
            case 5:{
                //Truecaller App Internal Error
                break;
            }
            case 10:{
                //User Not Verified on Truecaller
                break;
            }
            case 13:{
                //User pressed back while verification in process
                break;
            }
            case 14:{
                //User pressed SKIP or USE ANOTHER NUMBER
                break;
            }
            default:{

            }
        }
    }
}

```
**Error Type 4** and **Error Type 10** could arise in different conditions depending on whether the user has not registered on Truecaller app on their smartphone or if the user has deactivated their Truecaller profile at any point of time from the app.
  


The profile object that is shared with your app in the success event contains the following attributes:

```

profile.firstName
profile.lastName
profile.phoneNumber
profile.gender
profile.street
profile.city
profile.zipcode
profile.countryCode
profile.facebookId
profile.twitterId
profile.email
profile.url
profile.avatarUrl
profile.isTrueName
profile.companyName
profile.jobTitle
profile.payload
profile.signature
profile.signatureAlgorithm
profile.requestNonce
profile.isSimChanged
profile.verificationMode

```

  

And that's it! You're all set to use truecaller authentication in your react native app!

  

----------

  
  
  

**Authors**

  
```
  
  Aaron Alphonso (https://github.com/aaronalphonso)
  Email: alphonsoaaron1993@gmail.com

  Anoop Singh (https://github.com/codesinghanoop)
  Email: anoop100singh@gmail.com
  Stack Overflow: codesingh(username)
  
```
