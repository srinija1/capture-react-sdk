//
//  RNHVDocsCapture.m
//  HyperSnapSDK
//
//  Created by Srinija on 27/06/18.
//  Copyright © 2018 HyperVerge. All rights reserved.
//

#import "RNHVDocsCapture.h"
#import "AppDelegate.h"

@implementation RNHVDocsCapture

RCT_EXPORT_MODULE()

BOOL shouldShowInstructionPage = false;
HVDocConfig * hvDocConfig;

HVDocConfig* getDocConfig(){
  if (hvDocConfig == NULL){
    hvDocConfig = [[HVDocConfig alloc] init];
  }
  return hvDocConfig;
}

RCT_EXPORT_METHOD(setDocCaptureTitle:(NSString *)titleText){
  [getDocConfig().textConfig setDocCaptureTitle:titleText];
}
RCT_EXPORT_METHOD(setDocCaptureDescription:(NSString *)description){
  [getDocConfig().textConfig setDocCaptureDescription:description];
}
RCT_EXPORT_METHOD(setDocCaptureSubText:(NSString *)subText){
  [getDocConfig().textConfig setDocCaptureSubText:subText];
}
RCT_EXPORT_METHOD(setDocReviewTitle:(NSString *)docReviewTitle){
  [getDocConfig().textConfig setDocReviewTitle:docReviewTitle];
}
RCT_EXPORT_METHOD(setDocReviewDescription:(NSString *)docReviewDescription){
  [getDocConfig().textConfig setDocReviewDescription:docReviewDescription];
}

RCT_EXPORT_METHOD(setAspectRatio:(double)aspectRatio){
  [getDocConfig() setAspectRatio:aspectRatio];
}

RCT_EXPORT_METHOD(setDocumentType:(DocumentType)documentType){
  [getDocConfig() setDocumentType:documentType];
}

RCT_EXPORT_METHOD(setShouldAddPadding:(DocumentType)shouldAdd){
  [getDocConfig() setShouldAddPadding:shouldAdd];
}

RCT_EXPORT_METHOD(setShouldShowInstructionsPage:(BOOL)shouldShow){
  [getDocConfig() setShouldShowInstructionsPage:shouldShow];
  shouldShowInstructionPage = shouldShow;
}
RCT_EXPORT_METHOD(setShouldShowReviewScreen:(BOOL)shouldShow){
  [getDocConfig() setShouldShowReviewPage:shouldShow];
}

RCT_EXPORT_METHOD(start: (RCTResponseSenderBlock)completionHandler) {
  
  HVDocConfig * hvDocConfig = getDocConfig();
  
  UIViewController *root = RCTPresentedViewController();

  
  [HVDocsViewController start:root hvDocConfig:hvDocConfig completionHandler:^(HVError* error,NSDictionary<NSString *,id> * _Nonnull result, UIViewController* vcNew){
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
    
    if ([vcNew isKindOfClass:[HVDocsViewController class]]) {
      if(shouldShowInstructionPage){
        vcToDismiss = vcNew.presentingViewController.presentingViewController;
      }
    }else{
      if(shouldShowInstructionPage){
        vcToDismiss = vcNew.presentingViewController.presentingViewController.presentingViewController;
      }else{
          vcToDismiss = vcNew.presentingViewController.presentingViewController;
      }
    }

    [vcToDismiss dismissViewControllerAnimated:false completion:nil];

  }];


  
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}


@end
