# flutter_icod_print

## 研科条码打印机，标签打印机插件项目，自助收银机条码打印插件。

## 使用
- 引用
```
import 'package:flutter_icod_print/flutter_icod_print.dart';
```
- 代码
```
Center(
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
            bool bol = await FlutterIcodPrint.print('打印\n');
            await FlutterIcodPrint.print('String Print \n');

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
        ],
    ),
)
```

- 标签打印简单计算排版
```
///  打印文本
///
///
///
/**
* 商品打印样式
* 80：######################## ++++++++  ---- ********(#名称 +单价 -数量 *总计)
*
* @param name   商品名称
* @param price  商品单价 不能大于99999.99元
* @param number 商品数量 不能大于9999个
* @param total  商品总计 不能大于99999.99元
* @return 返回商品打印样式。
* -1 ：参数错误
* 打印区域宽度48个字符位置
*/
static String commodity80Print(String name, String price, String number, String total) {
    String printText = "";
    String takeUp = " ";                // 填充字符
    String nameStr = name;
    int nameLen = byteNum(nameStr);
    int priceLen = byteNum(price);
    int numberLen = byteNum(number);
    int totalLen = byteNum(total);
    if (nameLen < 25) {
        printText += nameStr;
        for (int i = 0; i < 25 - nameLen; i++) {
            printText += takeUp;
        }
        nameLen -= byteNum(nameStr);
    } else {
        int strLen = strGetLen(nameStr);
        printText += nameStr.substring(0, strLen);
        int _sps = 25 - byteNum(nameStr.substring(0, strLen));
        for (var i = 0; i < _sps; i++) {
            printText += takeUp;
        }
        nameLen -= byteNum(nameStr.substring(0, strLen));
        nameStr = nameStr.substring(strLen, nameStr.length);
    }

    if (priceLen < 9) {
        for (int i = 0; i < 9 - priceLen; i++)
        printText += takeUp;
        printText += price;
    } else {
        printText += price;
        // return "-1";
    }

    if (numberLen < 5) {
        for (int i = 0; i < 5 - numberLen; i++)
        printText += takeUp;
        printText += number;
    } else {
        printText += number;
        // return "-1";
    }

    if (totalLen < 9) {
        for (int i = 0; i < 9 - totalLen; i++)
        printText += takeUp;
        printText += total;
    } else {
        printText += total;
        // return "-1";
    }
    printText += "\n";
    while (nameLen > 0) {
        if (nameLen > 24) {
            int strLen = strGetLen(nameStr);
            printText += nameStr.substring(0, strLen);
            nameLen -= byteNum(nameStr.substring(0, strLen));
            nameStr = nameStr.substring(strLen, nameStr.length);
        } else {
            printText += nameStr;
            nameLen -= byteNum(nameStr);
        }
        printText += "\n";
    }
    return printText;
}

/**
* 获取占用打印位：汉字二个打印位、英文数字符号一个打印位
*
* @param str
* @return
*/
static int byteNum(String str) {
    int m = 0;
    if (str == null) {
        return m;
    }
    str.codeUnits.forEach((charCode) {
        String str16 = charCode.toRadixString(16);
        int c = int.parse(str16, radix: 16);
        if ((c >= 0x0391 && c <= 0xFFE5)) {//中文字符
            m = m + 2;
        } else if ((c >= 0x0000 && c <= 0x00FF)) {//英文字符
            m = m + 1;
        }
    });
    return m;
}


///  不超过25的文本长度
static int strGetLen (String str) {
    List<String> _arr = str.split("");
    int len = 0;
    for (var i = 0; i < _arr.length; i++) {
        len += byteNum(_arr[i]);
        if (len >= 25) {
            return i;
        }
    }
    return str.length;
}
```
