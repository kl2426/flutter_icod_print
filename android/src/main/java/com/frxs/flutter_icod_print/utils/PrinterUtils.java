package com.frxs.flutter_icod_print.utils;

/**
 * Created by crf on 2020/5/30.
 */
public class PrinterUtils {
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
     */
    public static String commodity80Print(String name, String price, String number, String total) {
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
            printText += nameStr.substring(0, 12);
            printText += takeUp;
            nameLen -= byteNum(nameStr.substring(0, 12));
            nameStr = nameStr.substring(12, nameStr.length());
        }

        if (priceLen < 9) {
            for (int i = 0; i < 9 - priceLen; i++)
                printText += takeUp;
            printText += price;
        } else {
            return "-1";
        }

        if (numberLen < 5) {
            for (int i = 0; i < 5 - numberLen; i++)
                printText += takeUp;
            printText += number;
        } else {
            return "-1";
        }

        if (totalLen < 9) {
            for (int i = 0; i < 9 - totalLen; i++)
                printText += takeUp;
            printText += total;
        } else {
            return "-1";
        }
        printText += "\n";
        while (nameLen > 0) {
            if (nameLen > 24) {
                printText += nameStr.substring(0, 12);
                nameLen -= byteNum(nameStr.substring(0, 12));
                nameStr = nameStr.substring(12, nameStr.length());
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
    public static int byteNum(String str) {
        int m = 0;
        char arr[] = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if ((c >= 0x0391 && c <= 0xFFE5))  //中文字符
                m = m + 2;
            else if ((c >= 0x0000 && c <= 0x00FF)) //英文字符
                m = m + 1;
        }
        return m;
    }
}
