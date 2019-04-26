//
//  RNHVFaceCapture.m
//  HyperSnapSDK
//
//  Created by Srinija on 27/06/18.
//  Copyright © 2018 HyperVerge. All rights reserved.
//

#import "RNHVFaceCapture.h"
#import "AppDelegate.h"

@implementation RNHVFaceCapture

RCT_EXPORT_MODULE()

LivenessMode livenessMode = LivenessModeTextureLiveness;
BOOL shouldUseBackCamera = false;
BOOL shouldShowCameraSwitchButton = false;
  BOOL setShouldReturnFullImageUri = false;

RCT_EXPORT_METHOD(setLivenessMode:(LivenessMode)mode){
  livenessMode = mode;
}

RCT_EXPORT_METHOD(setShouldUseBackCamera:(BOOL)shouldUse){
  shouldUseBackCamera = shouldUse;
}

RCT_EXPORT_METHOD(setShouldShowCameraSwitchButton:(BOOL)shouldShow){
  shouldShowCameraSwitchButton = shouldShow;
}

RCT_EXPORT_METHOD(setShouldReturnFullImageUri:(BOOL)shouldReturn){
   setShouldReturnFullImageUri = shouldReturn;
}

RCT_EXPORT_METHOD(start: (RCTResponseSenderBlock)completionHandler) {

  HVFaceConfig * hvFaceConfig = [[HVFaceConfig alloc] init];
  [hvFaceConfig setLivenessMode:livenessMode];
  [hvFaceConfig setShouldUseBackCamera:shouldUseBackCamera];
  [hvFaceConfig setShouldShowCameraSwitchButton:shouldShowCameraSwitchButton];
  [hvFaceConfig setShouldReturnFullImageUri:setShouldReturnFullImageUri];

  AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];

  UIViewController * topVC = delegate.window.rootViewController;
  UIViewController * presented = topVC.presentedViewController;
  if (presented != nil) {
    topVC = presented;
  }

  [HVFaceViewController start:presented hvFaceConfig:hvFaceConfig completionHandler:^(NSError* error,NSDictionary<NSString *,id> * _Nonnull result, UIViewController* vcNew){
    if(error != nil){
      NSMutableDictionary *errorDict = [[NSMutableDictionary alloc] init];
      NSNumber *errorCode = [NSNumber numberWithInteger:error.code];
      [errorDict setValue: errorCode forKey: @"errorCode"];
      [errorDict setValue: error.debugDescription forKey: @"errorMessage"];
      if (result == nil) {
        completionHandler(@[errorDict, [NSNull null]]);
      }else{
        completionHandler(@[errorDict, result]);
      }
    }else{
      completionHandler(@[[NSNull null], result]);
    }

    UIViewController* vcToDismiss = vcNew;

    [vcToDismiss dismissViewControllerAnimated:false completion:nil];

  }];

}

  - (dispatch_queue_t)methodQueue
  {
    return dispatch_get_main_queue();
  }




@end
