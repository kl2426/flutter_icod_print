
import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';

class FlutterIcodPrint {
  static const MethodChannel _channel = MethodChannel('flutter_icod_print');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  ///  初始化
  static Future<bool> init() async {
    var bol = await _channel.invokeMethod('init');
    return true;
  }
  ///  连接设备
  ///  [0] 设备已连接
  ///  [-2] 设备未连接
  ///  [-1] usb未连接
  static Future<int> openUsb() async {
    final int status = await _channel.invokeMethod('openUsb');
    return status;
  }
  ///  关闭设备
  static Future<bool> closeUsb() async {
    final bool bol = await _channel.invokeMethod('closeUsb');
    return bol;
  }
  /// 查询连接状态
  static Future<DeviceStatusModel> getStatus() async {
    final String stateStr = await _channel.invokeMethod('getStatus');
    return DeviceStatusModel.fromJson(jsonDecode(stateStr));
  }
  /// 打印文本
  static Future<bool> print() async {
    final bool bol = await _channel.invokeMethod('print');
    return bol;
  }

  /// 切纸
  static Future<bool> cut(CutType type) async {
    final bool bol = await _channel.invokeMethod('cut', {"type": type.toString()});
    return bol;
  }
}




///  设备状态

class DeviceStatusModel {
  int? status;
  String? msg;

  DeviceStatusModel({this.status, this.msg});

  DeviceStatusModel.fromJson(Map<String, dynamic> json) {
    this.status = json["status"];
    this.msg = json["msg"];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data["status"] = this.status;
    data["msg"] = this.msg;
    return data;
  }
}


///  标签打印机切纸类型
enum CutType{
  ///  半切
  halfCut,
  ///  全切
  allCut,
}