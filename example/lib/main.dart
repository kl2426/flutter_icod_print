import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_icod_print/flutter_icod_print.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    // We also handle the message potentially returning null.
    try {
      platformVersion =
          await FlutterIcodPrint.platformVersion ?? 'Unknown platform version';
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(children: [
            Text('Running on: $_platformVersion\n'),
            MaterialButton(onPressed: () async {
              bool bol = await FlutterIcodPrint.init();
              setState(() {
                _platformVersion = '${bol}';
              });
            }, child: const Text("初始化"),),
            MaterialButton(onPressed: () async {
              int bol = await FlutterIcodPrint.openUsb();
              setState(() {
                _platformVersion = '${bol}';
              });

            }, child: const Text("打开USB连接"),),
            MaterialButton(onPressed: () async {
              bool bol = await FlutterIcodPrint.closeUsb();
              setState(() {
                _platformVersion = '${bol}';
              });

            }, child: const Text("关闭"),),
            MaterialButton(onPressed: () async {
              DeviceStatusModel bol = await FlutterIcodPrint.getStatus();
              setState(() {
                _platformVersion = '${bol}';
              });

            }, child: const Text("查询状态"),),
            MaterialButton(onPressed: () async {
              bool bol = await FlutterIcodPrint.print();
              setState(() {
                _platformVersion = '${bol}';
              });
            }, child: const Text("打印"),),
            MaterialButton(onPressed: () async {
              bool bol = await FlutterIcodPrint.cut(CutType.allCut);
              setState(() {
                _platformVersion = '${bol}';
              });
            }, child: const Text("切纸"),),
          ],),
        ),
      ),
    );
  }
}
