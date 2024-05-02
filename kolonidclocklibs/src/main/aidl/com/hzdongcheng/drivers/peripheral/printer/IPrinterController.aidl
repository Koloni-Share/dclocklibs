// IPrinterController.aidl
package com.hzdongcheng.drivers.peripheral.printer;
import com.hzdongcheng.drivers.bean.Result;
// Declare any non-default types here with import statements

interface IPrinterController {
    //获取打印机厂家
    Result getVendor();
    //获取打印机版本
    Result getVersion();
    //打印
    Result print(String content);
    //获取打印机状态
    Result getStatus();
    //其他命令处理
    Result otherPort(byte type,String data);
}
