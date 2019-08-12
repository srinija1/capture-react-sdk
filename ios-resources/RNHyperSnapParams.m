//
//  HyperSnapParams.m
//  HyperSnapSDK
//
//  Created by Srinija on 27/06/18.
//  Copyright © 2018 HyperVerge. All rights reserved.
//

#import "RNHyperSnapParams.h"

@implementation RNHyperSnapParams

RCT_EXPORT_MODULE()

- (NSDictionary *)constantsToExport
{
  return @{ @"RegionIndia" : @(RegionIndia),
            @"RegionAsiaPacific" : @(RegionAsiaPacific),
            @"RegionUnitedStates" : @(RegionUnitedStates),
            @"ProductFaceID" : @(ProductFaceID),
            @"ProductIAM" : @(ProductIAM),
            @"LivenessModeNone" : @(LivenessModeNone),
            @"LivenessModeTextureLiveness" : @(LivenessModeTextureLiveness),
            @"DocumentTypeCard" : @(DocumentTypeCard),
            @"DocumentTypeA4" : @(DocumentTypeA4),
            @"DocumentTypePassport" : @(DocumentTypePassport),
            @"DocumentTypeOther" : @(DocumentTypeOther)
            };
};


- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

  
+ (BOOL)requiresMainQueueSetup
  {
    return YES;
  }

@end



@implementation RCTConvert(Region)
RCT_ENUM_CONVERTER(Region, (@{ @"RegionIndia" : @(RegionIndia),
                               @"RegionAsiaPacific" : @(RegionAsiaPacific),
                               @"RegionUnitedStates" : @(RegionUnitedStates)}),RegionIndia, integerValue)
@end

@implementation RCTConvert(Product)
RCT_ENUM_CONVERTER(Product, (@{ @"ProductFaceID" : @(ProductFaceID),
                                @"ProductIAM" : @(ProductIAM)}),
                   ProductFaceID, integerValue)
@end

@implementation RCTConvert(LivenessMode)
RCT_ENUM_CONVERTER(LivenessMode, (@{ @"LivenessModeNone" : @(LivenessModeNone),
                               @"LivenessModeTextureLiveness" : @(LivenessModeTextureLiveness),
                               }),
                   LivenessModeTextureLiveness, integerValue)
@end

@implementation RCTConvert(DocumentType)
RCT_ENUM_CONVERTER(DocumentType, (@{ @"DocumentTypeCard" : @(DocumentTypeCard),
                               @"DocumentTypeA4" : @(DocumentTypeA4),
                               @"DocumentTypePassport" : @(DocumentTypePassport),
                               @"DocumentTypeOther" : @(DocumentTypeOther),
                               }),
                   DocumentTypeCard, integerValue)
@end
