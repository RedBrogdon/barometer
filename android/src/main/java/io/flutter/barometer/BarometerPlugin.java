package io.flutter.barometer;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;

import static android.content.Context.SENSOR_SERVICE;

/** BarometerPlugin */
public class BarometerPlugin implements MethodCallHandler, SensorEventListener {

  private SensorManager mSensorManager;
  private Sensor mBarometer;
  private Registrar mRegistrar;
  private float mLatestReading = 0;

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "barometer");
    channel.setMethodCallHandler(new BarometerPlugin(registrar));
  }

  BarometerPlugin(Registrar registrar) {
    mRegistrar = registrar;
  }

  boolean initializeBarometer() {
    mSensorManager = (SensorManager)(mRegistrar.activeContext().getSystemService(SENSOR_SERVICE));
    mBarometer = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
    mSensorManager.registerListener(this, mBarometer, SensorManager.SENSOR_DELAY_NORMAL);
    return true;
  }

  float getBarometer() {
    return mLatestReading;
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
      return;
    }

    if (call.method.equals("getBarometer")) {
      double reading = getBarometer();
      result.success(reading);
      return;
    }

    if (call.method.equals("initializeBarometer")) {
      result.success(initializeBarometer());
      return;
    }

    result.notImplemented();
  }

  public void onAccuracyChanged(Sensor sensor, int accuracy) {
  }

  public void onSensorChanged(SensorEvent event) {
    mLatestReading = event.values[0];
  }
}
