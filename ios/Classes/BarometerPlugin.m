#import "BarometerPlugin.h"
@import CoreMotion;

@implementation BarometerPlugin

NSNumber *_pressure;
CMAltimeter *_altimeter;

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    FlutterMethodChannel* channel = [FlutterMethodChannel
                                     methodChannelWithName:@"barometer"
                                     binaryMessenger:[registrar messenger]];
    BarometerPlugin* instance = [[BarometerPlugin alloc] init];
    [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    if ([@"getPlatformVersion" isEqualToString:call.method]) {
        result([@"iOS " stringByAppendingString:[[UIDevice currentDevice] systemVersion]]);
    } else if ([@"initializeBarometer" isEqualToString: call.method]) {
        _altimeter = [[CMAltimeter alloc] init];
        [_altimeter startRelativeAltitudeUpdatesToQueue:NSOperationQueue.mainQueue withHandler:^(CMAltitudeData * _Nullable altitudeData, NSError * _Nullable error) {
            _pressure = [NSNumber numberWithDouble:altitudeData.pressure.doubleValue * 10];
        }];
        result([NSNumber numberWithBool:YES]);
    } else if ([@"getBarometer" isEqualToString: call.method]) {
        result(_pressure);
    } else {
        result(FlutterMethodNotImplemented);
    }
}

@end
