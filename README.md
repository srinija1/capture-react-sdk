# HyperSnapSDK for React Native

This is the official documentation for integrating the HyperSnapSDK in a React Native Project. To integrate into native apps, please refer to the [iOS](https://github.com/hyperverge/capture-ios-sdk) and the [Android](https://github.com/hyperverge/capture-android-sdk) documentation.

## Overview
HyperSnapSDK is HyperVerge's documents + face capture SDK that captures images at a resolution appropriate for our proprietary Deep Learning OCR and Face Recognition Engines.

The framework provides a liveness feature that uses our advanced AI Engines to tell if a captured image is that of a real person or a photograph.

 ## Table Of Contents

<!-- TOC depthFrom:1 depthTo:6 withLinks:1 updateOnSave:1 orderedList:0 -->

- [HyperSnapSDK for React Native](#hypersnapsdk-for-react-native)
	- [Overview](#overview)
		- [Requirements](#requirements)
	- [Setup the SampleApp](#setup-the-sampleapp)
	- [Integration](#integration)
		- [iOS](#ios)
		- [Android](#android)
	- [Usage](#usage)
		- [1. Define the NativeModules](#1-define-the-nativemodules)
		- [2. Initialize the SDK](#2-initialize-the-sdk)
		- [3. Present the Capture Screens](#3-present-the-capture-screens)
			- [Document Capture](#document-capture)
			- [Face Capture](#face-capture)
			- [Liveness in Face Capture](#liveness-in-face-capture)
	- [QR Scanner](#qr-scanner)
	- [API Calls](#api-calls)
		- [OCR API Call](#ocr-api-call)
		- [Face Match Call](#face-match-call)
			- [APICompletionCallback](#apicompletioncallback)
	- [Error Codes](#error-codes)



### Requirements
- Android: minimum SDK version 19
- iOS: Xcode 10
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
	      implementation('co.hyperverge:hypersnapsdk:2.4.8@aar', {
	          transitive=true
	          exclude group: 'com.android.support'
	          exclude group: 'com.google.android.gms'
	      })
	  }
	  ```
  >Note: If there is a build error around `implementation`, replace it with `compile`.


  Please note:
  - If you would like to use the instructions module, please remove the exclude statement for `hypersnapsdk-instructions`
  - If you would like to use the QR Scannner module, please remove the exclude statement for `hypersnapsdk-qrscanner`

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

3. (Optional) We recommend enabling minification and proguard to lower the app size. Add the following lines to the `android` section of the `app/build.gradle` file. FInd more about proguard [here](https://developer.android.com/studio/build/shrink-code).
	  ```groovy
	buildTypes {  
	    release {  
	        minifyEnabled true  
	        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
	  }    
    }
	  ```
4. Update Proguard File  
If you have a proguard file, add the following to it:
	```
	  -keepclassmembers class * implements javax.net.ssl.SSLSocketFactory {
         private javax.net.ssl.SSLSocketFactory delegate;
    }
	```

5. Open the `AndroidManifest.xml` and change allowBackup to true.
	```
	<application
	...
	android:allowBackup="true"
	...>
	</application>
	```

7. Add all the files present in the `android-resources` directory to your app under the same folder where the MainApplication is present.
8. Change the package name in all the added files to the package name of your application.
	```
	package <package name>
	...
	```
9. In your `MainApplication.java` import the `RNHyperSnapBridge` and add it's instance to the `getPackages` method.
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
- **closure** (required): This is the only argument passed to the start method. This is a closure with two dictionaries: `error` and `result`. The closure is called when a capture is successful or when an error has occured. The values of `error` and  `result` received by the closure determine whether the call was a success or failure. </br>
    - **error**: If the capture is successful, the error is set to `null`. Otherwise, it is a dictionary with the following information
        - **errorCode**: Integer - Error code stating type of error. (discussed later)
        - **errorMessage**: String - Explanation for the error.
    - **result**: If the capture failed, this is set to `null`. Otherwise, it has a single key-value pair. The key is `imageUri` and the value is the local path of the captured image.

- The following are the setter methods provided for various optional properties. If they are not called, the default values would be used.
    - **setDocumentType**: (Document) Document is an enum of type   `RNHyperSnapParams.Document`. It specifies the aspect ratio of the document that needs to be captured. Its default value is `DocumentTypeCard`. The parameter can be initialized as follows:
      - Following are the document types supported by the Document enum:
           - **CARD**: Aspect ratio: 0.625.
              Following documents require `DocumentTypeCard` aspect ratio:
				- Driving License
		      - Aadhaar Card (India)
		      - PAN Card (India)
		      - National ID (India)
		      - MRC (Vietnam)
          - **PASSPORT**: Aspect ratio: 0.67.  This is for Passports.
          - **A4**: Aspect ratio: 1.4.
            Following documents require `DocumentTypeA4` aspect ratio:
	          - Bank statement
	          - Insurance receipt
	          - EVN (Vietnam)
          - **OTHER**: This is for aspect ratios that don't fall in the above categories. In this case, the aspect ratio should be set in the next line by calling `document.setAspectRatio(aspectRatio);`
    	    Following documents require `DocumentTypeOther` aspect ratio.
		    - Voter ID (India) (here, set aspect ratio to 1.4)

    - **setShouldShowReviewScreen**: (Boolean) To determine if the document review page should be shown after capture page. It defaults to `false`.
    - **setShouldShowInstructionPage**: (Boolean) To determine if the instructions page should be shown before capture page. It defaults to `false`.
    - **setShouldShowFlashIcon**: (Boolean) Setting this to true will add a flash toggle button at the top right corner of the screen. It defaults to `false`.
    - **setShouldAddPadding**: (Boolean) Setting this to true will enable extra padding that will be added to all images captured using the Document Capture activity. It defaults to `true`.
    -  **setDocCaptureTitle**: (String) To set the title text that is shown in the document capture page.
    - **setDocReviewTitle**: (String) To set the title text that is shown in the Review screen after a document has been captured.
    - **setDocReviewDescription**: (String) To set the instruction text that is shown in the Review screen after a document has been captured.   
     - **setDocCaptureDescription**: (String) The text displayed at the top section of the Camera Preview in HVDocsActivity. This is to communicate the positioning of the document to the user.
    - **setDocCaptureSubText**: (String) The text displayed at the bottom end of the Camera Preview in HVDocsActivity. It is meant to tell the user about the document type.

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
- **closure** (required): This is the only argument passed to the start method. This is a closure with three dictionaries: `error`, `headers` and `result`. The closure is called when a capture is successful or when an error has occured. The values of `error`, `headers` and  `result` received by the closure determine whether the call was a success or failure. </br>
    - **error**: If the capture is successful, the error is set to `null`. Otherwise, it is a dictionary with following information
        - **errorCode**: Integer - Error code stating type of error. (discussed later)
        - **errorMessage**: String - Explanation for the error.
    - **result**:  JSONObject/Dictionary. It has the `imageUri` of the captured image. If the liveness call was successfully performed, it will have the entire results of the liveness call.
   -   **headers**  : JSONObject/Dictionary. If the liveness call has reached the HyperVerge servers, this object would have the headers returned by the server. Otherwise, it will be null.
- The following are the setter methods provided for various optional properties. If they are not called, the default values would be used.
    - **setLivenessMode**: `
RNHyperSnapParams.LivenessModeTextureLiveness
`. Explained [later](#Liveness-in-Face-Capture).
    - **setShouldShowInstructionPage**: (Boolean) To determine if the instructions page should be shown before capture page. It defaults to `false`.
    - **setFaceCaptureTitle**: (String) It allows to modify the title text that is shown in HVFaceActivity.
    -  **setLivenessAPIHeaders(_ headers : [String:String])**  : Any additional headers you want to send with the Liveness API Call. Following are headers that could be sent.
        -   _referenceId_: (String) This is a unique identifier that is assigned to the end customer by the API user. It would be the same for the different API calls made for the same customer. This would facilitate better analysis of user flow etc.
    -   **setLivenessAPIParameters(_ parameters : [String:AnyObject])**  : Any additional parameters you want to send with the Liveness API Call. Following are parameters that could be sent.
        -   _enableDashboard_: (String - "yes"/"no") If you want to access HyperVerge's QC dashboard, set this to 'yes' so that the information from the API calls are made available to the dashboard.
	    -   _allowEyesClosed_:(String - "yes"/"no") If this is set to 'no', a error would be thrown when eyes are closed in the user photo. By default, closed eyes are allowed.



#### Liveness in Face Capture
The `
RNHyperSnapParams.LivenessModeTextureLiveness
` enum takes two values.

**.none**: No liveness test is performed. The selfie that is captured is simply returned. If successful, the result JSON in the CaptureCompletionHandler has one key-value pair.
- `imageUri`: local path of the image captured.

**.textureLiveness**: Texture liveness test is performed on the selfie captured.  If successful, a result JSON with the following key-value pairs is returned in the CaptureCompletionHandler.

- `imageUri`: String. Local path of the image captured.
- `result`: JSONObject. This is the result obtained from the liveness API call. It has 3 key-value pairs.
    - `live`: String with values 'yes'/'no'. Tells whether the selfie is live or not.
    - `liveness-score`: Float with values between 0 and 1. The  confidence score for the liveness prediction. This score  would only be required for debugging purposes.
    - `to-be-reviewed`: String with values 'yes'/'no'. Yes indicates that it is flagged for manual review.

#### Properties
These are the properties to be set while initializing Face Capture.
- `closure` (required): This is the only argument passed to the start method. This is a closure with two dictionaries: `error` and `result`. The closure is called when a capture is successful or when an error has occured. The values of `error` and  `result` received by the closure determine whether the call was a success or failure. </br>
    - `error`: If the capture is successful, the error is set to `null`. Otherwise, it is an dictionary with following information
        - `errorCode`: Integer - Error code stating type of error. (discussed later)
        - `errorMessage`: String - Explanation for the error.
    - `result`:  If the capture failed, this is set to `null` and a corresponding error is set in `error`. If qr scan was successful, it will have the qr values extracted from the image scanned.  
 
## QR Scanner
The SDK provides a QR Scanner activity with a UI and implementation logic similar to the face and document capture activities. Please note that this is currently available only in Android.

To integrate it, please use the following code:

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
Please note: To use the QR Scanner, make sure that the following line is not present in your `app/build.gradle` file:
`exclude group: 'co.hyperverge', module: 'hypersnapsdk-qrscanner'`


## API Calls

### OCR API Call
 To make OCR API calls directly from the App, use the following method:
 ```java
    RNHVNetworkHelper.makeOCRCall(endpoint, documentUri, parameters, headers, completionCallback)
 ```
 where:
   - **parameters**: (JSONObject) This is usually an empty JSONObject. It could have the following optional field.
	  - *enableDashboard*: (String - "yes"/"no") If you want to access HyperVerge's QC dashboard, set this to 'yes' so that the information from the API calls are made available to the dashboard.
   - **headers**: (JSONObject) This is usually an empty JSONObject. It could have the following optional field.
	 - *referenceId*: (String) This is a unique identifier that is assigned to the end customer by the API user. It would be the same for the different API calls made for the same customer. This would facilitate better analysis of user flow etc.
   - **documentUri**: (String) The `imageUri` received in the completionHandler after Document Capture.
   - **completionCallback**: (APICompletionCallback) This is an interface which is used to return the results back after making the network request. Explained [here](#apicompletioncallback).     
   - **endpoint**: (String)
        - For India KYC, these are the supported endpoints
	        - https://ind-docs.hyperverge.co/v2.0/readKYC
			- https://ind-docs.hyperverge.co/v2.0/readPAN
			- https://ind-docs.hyperverge.co/v2.0/readAadhaar
			- https://ind-docs.hyperverge.co/v2.0/readPassport
	        -  Please find the full documentation [here](https://github.com/hyperverge/kyc-india-rest-api)

	  - For Vietnam KYC, these are the supported endpoints
		- https://apac.docs.hyperverge.co/v1.1/readNID
		- https://apac.docs.hyperverge.co/v1.1/readMRC
		- https://apac.docs.hyperverge.co/v1.1/readDL
        - Please find the full documentation [here](https://github.com/hyperverge/kyc-vietnam-rest-api)

### Face Match Call
 To make Face - ID match call from the SDK, use the following method:
  ```java
     HVNetworkHelper.makeFaceMatchCall(endPoint, faceUri, documentUri, parameters, headers, completionCallback)
  ```

  where:
   - **endpoint**: (String)
	   - India: https://ind-faceid.hyperverge.co/v1/photo/verifyPair
      - Asia Pacific: https://apac.faceid.hyperverge.co/v1/photo/verifyPair

For more information, please check out the documentation [here](https://github.com/hyperverge/face-match-rest-api)
- **parameters**: (JSONObject)
	- *type*: (required)For document to selfie match, please set "type" to "id". For selfie to selfie match, set "type" to "selfie".
	- *enableDashboard*: (optional)If you want to access HyperVerge's QC dashboard, set this to 'yes' so that the information from the API calls are made available to the dashboard.
-  **headers**: (JSONObject) This is usually an empty JSONObject. I could have the following optional field.
	- *referenceId*: (String) This is a unique identifier that is assigned to the end customer by the API user. It would be the same for the different API calls made for the same customer. This would facilitate better analysis of user flow etc.
- **faceUri**: (String) The `imageUri` received in the CompletionHandler after Face Capture.
- **documentUri**: (String) The `imageUri` received in the completionHandler after Document Capture.
- **completionCallback**: (APICompletionCallback)This is an interface used to return results/error from the network request. Explained [here](#apicompletioncallback).     


#### APICompletionCallback
APICompletionCallback is an interface whose object needs to be passed with `makeOCRCall`, `makeFaceMatchCall` or `makeValidationAPICall` . It has one `onResult` method that contains the error or result obtained in the process.

Following is a sample implementation of APICompletionCallback:
  ```java
  APICompletionCallback completionCallback = new APICompletionCallback() {
    @Override
    public void onResult(JSONOject error, JSONObject result, JSONObject headers) {
        if(error != null) {
            //Handler Error
        }
        else{
           //Handle Result
        }
    }
  }
  ```  
  Where,

 - **result**: The result JSONObject is the entire result obtained by the API without any modification.
 - **error**: Explained in [this](#hverror) section.
 - **headers** : JSONObject. It is the headers returned by the HyperVerge servers.

## Error Codes

Descriptions of the error codes returned in the closure are given here.

Here are the various error codes returned by the SDK.

|Code|Description|Explanation|Action|
|-----|------|-----------|------|
|2|Internal SDK Error|Occurs when an unexpected error has happened with the HyperSnapSDK.|Notify HyperVerge|
|3|Operation Cancelled By User|When the user taps on cancel button before capture|Try again.|
|4|Camera Permission Denied|Occurs when the user has denied permission to access camera.|In the settings app, give permission to access camera and try again.|
|5|Hardware Error|Occurs when camera could not be initialized|Try again|
|6|Input Error|Occurs when the input needed by the start method/HVNetworkHelper method is not correct|Read error message for more details|
|11|Initialization Error|Occurs when SDK has not been initialized properly|Make sure HyperSnapSDK.initialise method is called before using the capture functionality|
|12|Network Error|Occurs when the internet is either non-existant or very patchy|Check internet and try again. If Internet is proper, contact HyperVerge|
|14|Internal Server Error|Occurs when there is an internal error at the server|Notify HyperVerge|
|22|Face Detection Error|Occurs when a face couldn't be detected in an image by the server|Try capture again|
|23|Face not clear Error|Occurs when face captured is blurry|Hold the phone stead while capture|
|31|Instructions Module Not Included|Occurs when `setShouldShowInstructionPage` is set to true but the instructions module is excluded in gradle|Remove the exclude statement from gradle|
|32|QR Scanner Module Not Included|Occurs when there is an attempt to use the QR Scanner but the module is excluded in gradle|Remove the exclude statement from gradle|


Apart from the above errors, if the server returns a non 200 status code for any of the API calls (Liveness, Face Match, OCR or Validation API), the status code is as it is returned as the error code and the corresponding message is returned as the error message.

These are the possible error codes that could be received from the server:

|Code|Description|Explanation|Action|
|-----|------|-----------|------|
|400|Invalid Request|Something is wrong with the input|Check the error message/API documentation|
|401|Authentication Error|Credentials provided in the initialization step are wrong|Check credentails. If they seem correct, contact HyperVerge|
|404|Endpoint not found|Endpoint sent to makeFaceMatchCall or makeOCRCall is not correct|Check API documentation|
|423|No supported KYC document found|Happens only in makeOCRCall|Capture again with the correct document and ensure good image quality|
|429|Rate limit exceeded|Happens when the number of API calls per minute has exceeded the rate limit|Contact HyperVerge to revise the rate limit|
|501, 502|Server Error|Internal server error has occured|Notify HyperVerge|
|503|Server Busy|Server Busy|Notify HyperVerge|
