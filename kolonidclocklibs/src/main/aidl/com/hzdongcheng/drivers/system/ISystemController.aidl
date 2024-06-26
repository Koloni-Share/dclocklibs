// ISystemController.aidl
package com.hzdongcheng.drivers.system;
import com.hzdongcheng.drivers.bean.Result;

interface ISystemController {
    //获取当前APP版本信息
    Result getVersion();
    //获取厂家信息
    Result getVendor();
    //获取资产编码
    Result getAssetsCode();
    //获取外设配置信息
    Result getPeripheralsInfo();
    //设置外设配置信息
    Result setPeripheralsInfo(String configjson);
    //重启操作系统
    Result reboot(int delayMillis);
    //获取网络信息
    Result getNetworkInfo();
    //外设固件升级
    Result firmWareUpgrade(String pathname,int productId);
    //设置柜体配置信息
    Result setBoxGroupInfo(String boxGroupInfos);
    //获取柜体配置信息
     Result getBoxGroupInfo();
     //其他命令处理
     Result otherPort(byte type,String data);
}
