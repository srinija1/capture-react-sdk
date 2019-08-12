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

HVFaceConfig * hvFaceConfig;

HVFaceConfig* getFaceConfig(){
  if (hvFaceConfig == NULL){
    hvFaceConfig = [[HVFaceConfig alloc] init];
  }
  return hvFaceConfig;
}

RCT_EXPORT_METHOD(setLivenessMode:(LivenessMode)mode){
  [getFaceConfig() setLivenessMode:mode];
}

RCT_EXPORT_METHOD(setShouldShowInstructionPage:(BOOL)shouldShow){
  [getFaceConfig() setShouldShowInstructionsPage:shouldShow];
}

RCT_EXPORT_METHOD(setShouldReturnFullImageUri:(BOOL)shouldReturn){
  [getFaceConfig() setShouldReturnFullImageUri:shouldReturn];
}

RCT_EXPORT_METHOD(setLivenessAPIParameters:(NSDictionary<NSString *,id> * _Nullable)parameters){
  [getFaceConfig() setLivenessAPIParameters:parameters];
}

RCT_EXPORT_METHOD(setLivenessAPIHeaders:(NSDictionary<NSString *,NSString *> * _Nullable)headers){
  [getFaceConfig() setLivenessAPIHeaders:headers];
}

RCT_EXPORT_METHOD(setLivenessEndpoint:(NSString *)endpoint){
  [getFaceConfig() setLivenessEndpoint:endpoint];
}

RCT_EXPORT_METHOD(setShouldUseBackCamera:(BOOL)shouldUse){
  [getFaceConfig() setShouldUseBackCamera:shouldUse];
}

RCT_EXPORT_METHOD(setShouldShowCameraSwitchButton:(BOOL)shouldShow){
  [getFaceConfig() setShouldShowCameraSwitchButton:shouldShow];
}


RCT_EXPORT_METHOD(start: (RCTResponseSenderBlock)completionHandler) {

  HVFaceConfig * hvFaceConfig = getFaceConfig();

  AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];

  UIViewController * topVC = delegate.window.rootViewController;
  UIViewController * presented = topVC.presentedViewController;
  if (presented != nil) {
    topVC = presented;
  }

  [HVFaceViewController start:presented hvFaceConfig:hvFaceConfig completionHandler: ^( HVError* _Nullable error,NSDictionary<NSString *,id> * _Nullable result, NSDictionary<NSString *,id> * _Nullable headers, UIViewController* vcNew){
    
    if(error != nil){
      NSMutableDictionary *errorDict = [[NSMutableDictionary alloc] init];
      NSNumber *errorCode = [NSNumber numberWithInteger:error.getErrorCode];
      NSString *errorMessage = [NSString stringWithString:error.getErrorMessage];
      [errorDict setValue: errorCode forKey: @"errorCode"];
      [errorDict setValue: errorMessage forKey: @"errorMessage"];
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
