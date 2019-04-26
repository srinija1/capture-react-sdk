//
//  RNHVDocConfig.m
//  HyperSnapDemoApp_React
//
//  Created by Srinija on 02/11/18.
//  Copyright © 2018 Facebook. All rights reserved.
//

#import "RNHVDocConfig.h"



@implementation RNHVDocConfig

RCT_EXPORT_MODULE()

HVDocConfig * hvDocConfig;

- (BOOL)requiresMainQueueSetup
{
  return NO;
}

- (instancetype)init {
  hvDocConfig = [[HVDocConfig alloc] init];
  return self;
}


RCT_EXPORT_METHOD(setDocumentType:(DocumentType)type){
  [hvDocConfig setDocumentType:type];
}


RCT_EXPORT_METHOD(setAspectRatio:(double)aspectRatio){
  [hvDocConfig setAspectRatio:aspectRatio];
}

RCT_EXPORT_METHOD(setDocCaptureTitle:(NSString*)text){
  [hvDocConfig setDocCaptureTitle:text];
}

RCT_EXPORT_METHOD(setDocCaptureDescription:(NSString*)text){
  [hvDocConfig setDocCaptureDescription:text];
}


RCT_EXPORT_METHOD(setDocReviewTitle:(NSString*)text){
  [hvDocConfig setDocReviewTitle:text];
}

RCT_EXPORT_METHOD(setDocReviewDescription:(NSString*)text){
  [hvDocConfig setDocReviewDescription:text];
}

RCT_EXPORT_METHOD(setDocCaptureSubText:(NSString*)text){
  [hvDocConfig setDocCaptureSubText:text];
}

RCT_EXPORT_METHOD(setShouldShowReviewPage:(bool)shouldShow){
  [hvDocConfig setShouldShowReviewPage:shouldShow];
}

RCT_EXPORT_METHOD(setShouldShowInstructionsPage:(bool)shouldShow){
  [hvDocConfig setShouldShowInstructionsPage:shouldShow];
}


RCT_EXPORT_METHOD(setShouldShowFlashButton:(bool)shouldShow){
  [hvDocConfig setShouldShowFlashButton:shouldShow];
}


RCT_EXPORT_METHOD(setShouldAddPadding:(bool)shouldShow){
  [hvDocConfig setShouldAddPadding:shouldShow];
}


@end

