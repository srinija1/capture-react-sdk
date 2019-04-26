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

NSString* toptext;
NSString* bottomtext;
DocumentType documenttype = DocumentTypeCard;
Boolean shouldShowInstructionPage;
Boolean shouldShowReviewPage;
double aspectratio;

RCT_EXPORT_METHOD(setTopText:(NSString *)topText){
  toptext = topText;
}

RCT_EXPORT_METHOD(setBottomText:(NSString *)bottomText){
  bottomtext = bottomText;
}

RCT_EXPORT_METHOD(setAspectRatio:(double)aspectRatio){
  aspectratio = aspectRatio;
}

RCT_EXPORT_METHOD(setDocumentType:(DocumentType)documentType){
  documenttype = documentType;
}

RCT_EXPORT_METHOD(setShouldShowInstructionsPage:(Boolean)shouldShow){
  shouldShowInstructionPage = shouldShow;
}
RCT_EXPORT_METHOD(setShouldShowReviewPage:(Boolean)shouldShow){
  shouldShowReviewPage = shouldShow;
}

RCT_EXPORT_METHOD(start: (RCTResponseSenderBlock)completionHandler) {
  
  HVDocConfig * hvDocConfig = [[HVDocConfig alloc] init];
  [hvDocConfig setDocumentType:documenttype];
  if (aspectratio){
  [hvDocConfig setAspectRatio:aspectratio];
  }
  if (toptext){
  [hvDocConfig setDocCaptureTitle:toptext];
  }
  if (bottomtext){
  [hvDocConfig setDocCaptureDescription:bottomtext];
  }
  [hvDocConfig setShouldShowInstructionsPage:shouldShowInstructionPage];
  [hvDocConfig setShouldShowReviewPage:shouldShowReviewPage];
  
  AppDelegate *delegate = (AppDelegate *)[[UIApplication sharedApplication] delegate];
  
  NSBundle *bundle  = [NSBundle bundleForClass:[HyperSnapSDK self]];

  
  UIViewController * topVC = delegate.window.rootViewController;
  UIViewController * presented = topVC.presentedViewController;
  if (presented != nil) {
    topVC = presented;
  }
  
  [HVDocsViewController start:presented hvDocConfig:hvDocConfig completionHandler:^(NSError* error,NSDictionary<NSString *,id> * _Nonnull result, UIViewController* vcNew){
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
