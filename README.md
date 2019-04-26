# HyperSnapSDK for React Native

This is the official documentation for integrating the HyperSnapSDK in a React Native Project. To integrate into native apps, please refer to the [iOS](https://github.com/hyperverge/capture-ios-sdk) or the [android](https://github.com/hyperverge/capture-android-sdk) documentation.



## Overview

HyperSnapSDK is HyperVerge's documents + face capture SDK that captures images at a resolution appropriate for our proprietary Deep Learning OCR and Face Recognition Engines.

The framework provides a liveness feature that uses our advanced AI Engines to tell if a captured image is that of a real person or a photograph.

 ## Table Of Contents

 - [HyperSnapSDK for React Native](#hypersnapsdk-for-react-native)
	- [Overview](#overview)
		- [Requirements](#requirements)
	- [Table Of Contents](#table-of-contents)
	- [Setup the SampleApp](#setup-the-sampleapp)
	- [Integration](#integration)
		- [iOS](#ios)
		- [Android](#android)
	- [Usage](#usage)
		- [1. Define the NativeModules](#1-define-the-nativemodules)
		- [2. Initialize the SDK](#2-initialize-the-sdk)
		- [3. Present the Capture Screens](#3-present-the-capture-screens)
			- [Document Capture](#document-capture)
				- [Properties](#properties)
			- [Face Capture](#face-capture)
				- [Properties](#properties)
		        - [QR Scan Capture](#)		
		- [Integrating Liveness in Face Capture](#integrating-liveness-in-face-capture)
                - [QR Scan Capture](#qr-scan-capture)		
		
	- [Error Codes](#error-codes)


### Requirements

- Android: minimum SDK version 19

- iOS: Xcode 9.3/9.4

- iOS: Deployment Target 9.0

- React Native: minimum version 0.55.4



## Setup the SampleApp



Create React Native project and add the HyperSnapSDK's React Native module to it.

1. Clone/download the repo.

2. Open terminal and go to `sampleApp`

3. Run `npm install --save`

4. In `index.js`, set the values of `appId` and `appKey` to the credentials provided to you by HyperVerge.



## Integration

### iOS

1. Add the HyperSnapSDK to your React Native iOS project. This can be done manually or via CocoaPods. Please refer to the [SDK's documentation for native iOS](https://github.com/hyperverge/capture-ios-sdk#1-adding-the-sdk-to-your-project) for this.

2. Copy the contents of the `ios-resources` directory into your Xcode project/workspace at the **top level**.

3. Add `NSCameraUsageDescription` key in `info.plist` file to request the user for camera permissions.

4. Navigate to `Targets` -> `Your App name` and ensure if the following are set.

- `Build Setting` -> `Always Embed Swift Standard Libraries` is `Yes`.

- `Build Settings` -> `Header Search Paths` has a path pointing to the header files in the `React` project. Typically, it is `../node_modules/react-native/React`

- `Build Phases` -> `Target Dependencies` has the `React` library. This should be set to 'recursive'.



### Android


1. Add the HyperSnapSDK to your project by including the following set of lines to your `android/app/build.gradle`

  ```groovy
  android {
      defaultConfig {
          
	  }
  }
  dependencies {
      implementation('co.hyperverge:hypersnapsdk:2.4.6.2@aar', {
          transitive=true
          exclude group: 'com.android.support'
      })
  }
  ```
  >Note: If there is a build error around `implementation`, replace it with `compile`.

2. Add the following set of lines to the Project (top-level) `android/build.gradle`.

  ```groovy
  allprojects {
      repositories {
          maven {
              url "s3://hvsdk/android/releases"
              credentials(AwsCredentials) {
                  accessKey aws_access_key
                  secretKey aws_secret_pass
              }
          }
      }
  }
  ```
  Replace  **aws_access_key** and **aws_secret_pass** with the authorization credentials given by HyperVerge.


3. Set the `minSdkVersion` present under `defaultConfig`  in `android/app/build.gradle` to **19**.

4. Open the `AndroidManifest.xml` and change allowBackup to true.

	```

	<application

	...

	android:allowBackup="true"

	...>

	</application>

	```

5. Add all the files present in the `android-resources` directory to you app under the same folder where the MainApplication is present.
6. Change the package name in all the added files to the package name of your application.
	```
	package <package name>
	...
	```
7. In your `MainApplication.java` import the `RNHyperSnapBridge` and add it's instance to the `getPackages` method.
	```
	import com.myapplication.RNHyperSnapSDKBridge;

	public class MainApplication extends Application implements ReactApplication {

		...

	    @Override
	    protected List<ReactPackage> getPackages() {
	      return Arrays.<ReactPackage>asList(
	          new MainReactPackage(),
	          new RNHyperSnapSDKBridge()
	      );
	    }
	   ...
	  };
	```



## Usage

### 1. Define the NativeModules
- Include `NativeModules` under `import`.
```
import {
    ...
    NativeModules
} from 'react-native';
```

- Create constants of the classes from the SDK
```
const {RNHyperSnapSDK, RNHVDocsCapture, RNHVFaceCapture, RNHVQRScanCapture,  RNHyperSnapParams} = NativeModules;
```

### 2. Initialize the SDK
To initialize the SDK, call the `RNHyperSnapSDK.initialize` method. This must be done before launching the camera.
```
RNHyperSnapSDK.initialize("<appID>","<appKey>",RNHyperSnapParams.RegionAsiaPacific,RNHyperSnapParams.ProductFaceID)
```
Where,
- appId, appKey are given by HyperVerge
- region: This is an enum in `HypeSnapParams` with three values - `.RegionAsiaPacific`, `.RegionIndia` and `.RegionUnitedStates`.
- product: This is an enum in `HyperSnapParams` with two values - `.ProductFaceID` and `.ProductFaceIAM`. Right now, only `.ProductFaceID` is supported.

### 3. Present the Capture Screens
To present the capture screen for document capture and face capture, call the `start` method of `RNHVDocsCapture` and `RNHVFaceCapture` respectively. You should set the corresponding parameters(optional) before you call this method.

#### Document Capture
```
//Set optional Parameters
        RNHVDocsCapture.createNewConfig();
	RNHVDocsCapture.setDocumentType(RNHyperSnapParams.DocumentTypeCard);
	RNHVDocsCapture.setDocCaptureSubText("National ID");
	RNHVDocsCapture.setDocCaptureDescription("Place your National ID inside the box");

	var closure = (error,result) => {
		if(error != null){
			//Handle error
          }
          else{
             //Handle result
          }
      }
//Call the start method
	RNHVDocsCapture.start(closure)
```

##### Properties
These are the properties to be set while initializing Document Capture.
- `closure` (required): This is the only argument passed to the start method. This is a closure with two dictionaries: `error` and `result`. The closure is called when a capture is successful or when an error has occured. The values of `error` and  `result` received by the closure determine whether the call was a success or failure. </br>
    - `error`: If the capture is successful, the error is set to `null`. Otherwise, it is a dictionary with the following information
        - `errorCode`: Integer - Error code stating type of error. (discussed later)
        - `errorMessage`: String - Explanation for the error.
    - `result`: If the capture failed, this is set to `null`. Otherwise, it has a single key-value pair. The key is `imageUri` and the value is the local path of the captured image.
- `documentType` (optional): This is the type of the document to be captured. It determines the aspect ratio of the document. This is an enum in `HyperSnapParams`. It can take the following values.
    - `.DocumentTypeCard`- Aspect ratio : 0.625. Example: Vietnamese National ID, Driving License, Motor Registration Certificate
    - `.DocumentTypeA4`- Aspect ratio: 1.4. Example: Bank statement, insurance receipt
    - `.DocumentTypePassport`- Aspect ratio: 0.68. Example: Passport
    - `.DocumentTypeOther`- This is for custom aspect ratio. In this case, the aspect ratio should be set in the next line by calling `RNHVDocsCapture.setAspectRatio(<Aspect Ratio>)`
If the document is not set, it defaults to `.DocumentTypeCard`.
- `topText`, `bottomText` (optional): The text displayed above and below the capture area in the document capture screen.


#### Face Capture

```
//Set optional Parameters
        RNHVFaceCapture.createNewConfig();
        RNHVFaceCapture.setCustomUIStrings(" "); 
        RNHVFaceCapture.setShouldShowInstructionPage(true);
        RNHVFaceCapture.setFaceCaptureTitle("Face react native capture");
	RNHVFaceCapture.setLivenessMode(RNHyperSnapParams.LivenessModeTextureLiveness);
	var closure = (error,result,headers) => {
		if(error != null){
			//Handle error
          }
          else{
             //Handle result
          }
      }
//Call the start method
	RNHVFaceCapture.start(closure)
```

##### Properties
These are the properties to be set while initializing Face Capture.
- `closure` (required): This is the only argument passed to the start method. This is a closure with three dictionaries: `error`, `headers` and `result`. The closure is called when a capture is successful or when an error has occured. The values of `error`, `headers` and  `result` received by the closure determine whether the call was a success or failure. </br>
    - `error`: If the capture is successful, the error is set to `null`. Otherwise, it is an dictionary with following information
        - `errorCode`: Integer - Error code stating type of error. (discussed later)
        - `errorMessage`: String - Explanation for the error.
    - `result`:  If the capture failed, this is set to `null` and a corresponding error is set in `error`. If a capture was successful, it will have atleast one key-value pair. The key is `imageUri` and the value is the local path of the captured image. It may have more key-value pairs based on the `livenessMode` (next section).
 - livenessMode (optional) : Discussed in the next section.
    - `headers`: If the liveness call has reached the HyperVerge servers, this object would have the headers returned by the server. Otherwise, it will be null.

### Integrating Liveness in Face Capture

The framework has two liveness detection methods. Texture liveness and Gesture Liveness. This can be set before calling the  `start` method of HNHVFaceCapture by calling the 'setLivenessMode' method.

        RNHVFaceCapture.setLivenessMode(livenessMode);

Here, `livenessMode` is an enum in`HypeSnapParams`, it can take 3 values:

**.LivenessModeNone**: No liveness test is performed. The selfie that is captured is simply returned. If successful, the result dictionary in the closure has one key-value pair.
- `imageUri` : local path of the image captured

**.LivenessModeTextureLiveness** : Texture liveness test is performed on the selfie captured.  If successful, a result dictionary with the following key-value pairs is returned in the closure

- `imageUri`: String. Local path of the image captured.
- `result`: JSONObject. This is the result obtained from the liveness API call. It has 3 key-value pairs.
    - `live`: String with values 'yes'/'no'. Tells whether the selfie is live or not.
    - `liveness-score`: Float with values between 0 and 1. The  confidence score for the liveness prediction. This score  would only be required for debugging purposes.
    - `to-be-reviewed`: String with values 'yes'/'no'. Yes indicates that it is flagged for manual review.

**.LivenessModeTextureAndGestureLiveness**: In this mode, based on the results of the texture Liveness call, the user might be asked to do a series of gestures to confirm liveness. The user performing the gestures is arbitrarily matched with the selfie captured. If  one or more of these matches fail, a 'faceMatch' error is returned (refer to 'Error Codes' section).
If all the gestures are succefully performed and the face matches are sucessful, a result dictionary with the following key-value pairs is returned in the closure
- `imageUri` : String. Local path of the image captured <br/>
- `live`: String with values 'yes'/'no'. Tells whether the selfie is live or not.

#### QRScan Capture

```
//Set optional Parameters
       
	var closure = (error,result ) => {
		if(error != null){
			//Handle error
          }
          else{
             //Handle result
          }
      }
//Call the start method
	RNHVQRScanCapture.start(closure)
```

##### Properties
These are the properties to be set while initializing Face Capture.
- `closure` (required): This is the only argument passed to the start method. This is a closure with two dictionaries: `error` and `result`. The closure is called when a capture is successful or when an error has occured. The values of `error` and  `result` received by the closure determine whether the call was a success or failure. </br>
    - `error`: If the capture is successful, the error is set to `null`. Otherwise, it is an dictionary with following information
        - `errorCode`: Integer - Error code stating type of error. (discussed later)
        - `errorMessage`: String - Explanation for the error.
    - `result`:  If the capture failed, this is set to `null` and a corresponding error is set in `error`. If qr scan was successful, it will have the qr values extracted from the image scanned.  
   
## Error Codes

Descriptions of the error codes returned in the closure are given here.

|Error Code|Description|Explanation|Action|
|----------|-----------|-----------|------|
|2|Internal SDK Error|Occurs when an unexpected error has happened with the HyperSnapSDK.|Notify HyperVerge|
|3|Operation Cancelled By User|When the user taps on cancel button before capture|Try again.|
|4|Camera Permission Denied|Occurs when the user has denied permission to access camera.|In the settings app, give permission to access camera and try again.|
|5|Hardware Error|Occurs when there is an error setting up the camera.|Make sure the camera is accessible.|
|101|Initialization Error|Occurs when SDK has not been initialized properly.|Make sure HyperSnapSDK.initialise method is called before using the capture functionality|
|102|Network Error|Occurs when the internet is either non-existant or very patchy.|Check internet and try again. If Internet is proper, contact HyperVerge|
|103|Authentication Error|Occurs when the request to the server could not be Authenticated/Authorized.|Make sure appId and appKey set in the initialization method are correct|
|104|Internal Server Error|Occurs when there is an internal error at the server.|Notify HyperVerge|
|201|Face Match Error|Occurs when one or more faces captured in the gestures flow do not match the selfie|This is equivalent to liveness fail. Take corresponding action|
|202|Face Detection Error|Occurs when a face couldn't be detected in an image by the server|Try capture again|
