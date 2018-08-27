import 'dart:async';

import 'package:flutter/services.dart';

class Barometer {
  static const MethodChannel _channel =
      const MethodChannel('barometer');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<double> get reading async {
    final double reading = await _channel.invokeMethod('getBarometer');
    return reading;
  }

  static Future<bool> initialize() async {
    bool ready = await _channel.invokeMethod('initializeBarometer');

  }
}
