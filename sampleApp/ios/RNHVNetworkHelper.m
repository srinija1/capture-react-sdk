//
//  RNHVNetworkHelper.m
//  HyperSnapDemoApp_React
//
//  Created by Srinija on 25/12/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AppDelegate.h"
#import "RNHVNetworkHelper.h"

@implementation RNHVNetworkHelper

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(makeOCRCall:(NSString*)endpoint docUri:(NSString*)docUri params:(NSDictionary<NSString *,id> * _Nullable)params headers:(NSDictionary<NSString *,id> * _Nullable)headers completionHandler:(RCTResponseSenderBlock)completionHandler){
  

  [HVNetworkHelper makeOCRCallWithEndpoint:endpoint documentUri:docUri parameters:params headers:headers completionHandler: ^( HVError* _Nullable error,NSDictionary<NSString *,id> * _Nullable result, NSDictionary<NSString *,id> * _Nullable headers){

    if(error != nil){
      NSMutableDictionary *errorDict = [[NSMutableDictionary alloc] init];
      NSNumber *errorCode = [NSNumber numberWithInteger:error.getErrorCode];
      NSString *errorMessage = [NSString stringWithString:error.getErrorMessage];
      [errorDict setValue: errorCode forKey: @"errorCode"];
      [errorDict setValue: errorMessage forKey: @"errorMessage"];
      if (result == nil) {
        completionHandler(@[errorDict, [NSNull null], [NSNull null]]);
      }else{
        completionHandler(@[errorDict, result, [NSNull null]]);
      }
    }else{
      completionHandler(@[[NSNull null], result, [NSNull null]]);
    }

  }];
}

RCT_EXPORT_METHOD(makeFaceMatchCall:(NSString*)endpoint faceUri:(NSString*)faceUri docUri:(NSString*)docUri params:(NSDictionary<NSString *,id> * _Nullable)params headers:(NSDictionary<NSString *,id> * _Nullable)headers completionHandler:(RCTResponseSenderBlock)completionHandler){

  [HVNetworkHelper makeFaceMatchCallWithEndpoint:endpoint faceUri:faceUri documentUri:docUri parameters:params headers:headers completionHandler: ^( HVError* _Nullable error,NSDictionary<NSString *,id> * _Nullable result, NSDictionary<NSString *,id> * _Nullable headers){

    if(error != nil){
      NSMutableDictionary *errorDict = [[NSMutableDictionary alloc] init];
      NSNumber *errorCode = [NSNumber numberWithInteger:error.getErrorCode];
      NSString *errorMessage = [NSString stringWithString:error.getErrorMessage];
      [errorDict setValue: errorCode forKey: @"errorCode"];
      [errorDict setValue: errorMessage forKey: @"errorMessage"];
      if (result == nil) {
        completionHandler(@[errorDict, [NSNull null], [NSNull null]]);
      }else{
        completionHandler(@[errorDict, result, [NSNull null]]);
      }
    }else{
      completionHandler(@[[NSNull null], result, [NSNull null]]);
    }

  }];
}

//RCT_EXPORT_METHOD()


- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

@end
