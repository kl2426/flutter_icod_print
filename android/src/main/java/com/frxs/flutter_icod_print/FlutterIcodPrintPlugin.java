package com.frxs.flutter_icod_print;

import android.content.Context;

import androidx.annotation.NonNull;

import com.frxs.flutter_icod_print.utils.PrinterUtils;
import com.frxs.flutter_icod_print.utils.StatusDescribe;

import java.io.UnsupportedEncodingException;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


import com.icod.serial.SerialPortFinder;
import com.szsicod.print.escpos.PrinterAPI;
import com.szsicod.print.io.BluetoothAPI;
import com.szsicod.print.io.InterfaceAPI;
import com.szsicod.print.io.SerialAPI;
import com.szsicod.print.io.SocketAPI;
import com.szsicod.print.io.USBAPI;
import com.szsicod.print.io.UsbNativeAPI;

import java.io.UnsupportedEncodingException;


/** FlutterIcodPrintPlugin */
public class FlutterIcodPrintPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;

  private PrinterAPI mPrinter;
  //  连接设备
  InterfaceAPI io = null;

  private Context context;

  private static final int DISCONNECT = -5;           // 断开连接
  private static final int NOCONNECT = -6;            // 断开连接

  static {
    //如果使用UsbNativeAPI 必须加载 适用root板 这个不需要权限窗口和申请权限
    System.loadLibrary("usb1.0");
    //串口
    System.loadLibrary("serial_icod");
    //图片
    System.loadLibrary("image_icod");
  }

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "flutter_icod_print");
    context = flutterPluginBinding.getApplicationContext();
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("init")) {
      //  初始化
      mPrinter = PrinterAPI.getInstance();
      result.success(true);
    } else if (call.method.equals("openUsb")) {
      if (mPrinter.isConnect()) {
        mPrinter.disconnect();
      }
      io = new USBAPI(context);
      int status = -999;
      if (io != null) {
        status = mPrinter.connect(io);
      }
      result.success(status);
    } else if (call.method.equals("closeUsb")) {
      //  关闭设备连接
      if (io != null) {
        io.closeDevice();
        io = null;
      }
      if (mPrinter.isConnect()) {
        mPrinter.disconnect();
      }
      result.success(true);
    } else if (call.method.equals("getStatus")) {
      String stateStr = Integer.toString(NOCONNECT);
      int status = -999;
      //  查询状态
      if (mPrinter.isConnect()) {
        status = mPrinter.getStatus();
        stateStr = StatusDescribe.getStatusDescribe(status);
      }
      result.success(String.format("{\"status\": %s, \"msg\": %s}",status,stateStr));
    } else if (call.method.equals("print")) {
      if (mPrinter.isConnect()) {
        try {
          // 打印方法：printString
          // 打印例范文本
          String str = "Hello world!!\n";
          int ret = mPrinter.printString(str, "UTF-8", false);
//                        // 打印商品模板
          mPrinter.printString(PrinterUtils.commodity80Print("商品名称","单价","数量","小计"), "UTF-8", false);
          mPrinter.printString(PrinterUtils.commodity80Print("我是一个商品名称1","0.55","1","8.33"), "UTF-8", false);
          mPrinter.printString(PrinterUtils.commodity80Print("我是一个商品名称1我是一个商品名称1","59.55","1","10.33"), "UTF-8", false);
          mPrinter.printString(PrinterUtils.commodity80Print("我是一个商品名称1我是一个商品名称1我是一个商品名称1","888.55","1","888.33"), "UTF-8", false);
          mPrinter.printString(PrinterUtils.commodity80Print("我是一个商品名称1我是一个商品名称1我是一个商品名称1我是一个商品名称1","8888.55","1","8888.33"), "UTF-8", false);
          mPrinter.printString(PrinterUtils.commodity80Print("我是一个商品名称1我是一个商品名称1我是一个商品名称1我是一个商品名称1","88888.55","1","88888.33"), "UTF-8", false);
          result.success(true);
        } catch (UnsupportedEncodingException e) {
//          e.printStackTrace();
          result.success(false);
        }
      } else {
        result.success(false);
      }
    } else if (call.method.equals("cut")) {
      if (mPrinter.isConnect()) {
        String type = call.argument("type");
        if (type.equals("CutType.halfCut")) {
          //  半切
          mPrinter.cutPaper(66, 50);
        } else if (type.equals("CutType.allCut")) {
          //  全切
          mPrinter.cutPaper(65, 50);
        } else {
          result.success(false);
        }
        result.success(true);
      } else {
        result.success(false);
      }
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
