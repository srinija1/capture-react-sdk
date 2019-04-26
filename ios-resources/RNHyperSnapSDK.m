//
//  RNHyperSnapSDK.m
//  HyperSnapSDK
//
//  Created by Srinija on 25/06/18.
//  Copyright © 2018 HyperVerge. All rights reserved.
//

#import "RNHyperSnapSDK.h"
#import "AppDelegate.h"

@implementation RNHyperSnapSDK

RCT_EXPORT_MODULE()


RCT_EXPORT_METHOD(initialize:(NSString *)appId appKey:(NSString *)appKey region:(Region)region product:(Product)product){
  [HyperSnapSDK initializeWithAppId:appId appKey:appKey region: region product: product];
 }


- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

@end





