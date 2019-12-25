//
//  RNHVFaceConfig.m
//  HyperSnapDemoApp_React
//
//  Created by Srinija on 02/11/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import "RNHVFaceConfig.h"



@implementation RNHVFaceConfig

RCT_EXPORT_MODULE()

HVFaceConfig * hvFaceConfig;

- (BOOL)requiresMainQueueSetup
{
  return NO;
}

- (instancetype)init {
  hvFaceConfig = [[HVFaceConfig alloc] init];
  return self;
}


RCT_EXPORT_METHOD(setLivenessMode:(LivenessMode)livenessMode){
  [hvFaceConfig setLivenessMode:livenessMode];
}


RCT_EXPORT_METHOD(setShouldShowInstructionsPage:(bool)shouldShow){
  [hvFaceConfig setShouldShowInstructionsPage:shouldShow];
}


RCT_EXPORT_METHOD(setShouldUseBackCamera:(bool)shouldUse){
  [hvFaceConfig setShouldUseBackCamera:shouldUse];
}

RCT_EXPORT_METHOD(setShouldShowCameraSwitchButton:(bool)shouldShow){
  [hvFaceConfig setShouldShowCameraSwitchButton:shouldShow];
}


RCT_EXPORT_METHOD(setClientID:(NSString*)clientId){
  [hvFaceConfig setClientIDWithClientId:clientId];
}

  RCT_EXPORT_METHOD(setShouldReturnFullImageUri: (bool)shouldReturn){
    [hvFaceConfig setShouldReturnFullImageUri:shouldReturn];
  }


@end
