package com.hzdongcheng.drivers.locker;
import com.hzdongcheng.drivers.bean.Result;

//钥匙柜
interface IKeyController {
      //获取钥匙卡号
      Result getCardId(byte boxId);
      //打开钥匙插槽
      Result openBoxById(byte boxId,byte boxOpenTime);
      //打开大门
      Result openDoor(byte doorOpenTime);
     //获取大门状态
      Result getDoorState();
      //钥匙插槽灯控制
      Result openLedState(byte boxId,boolean isOpenLed);
       //获取锁数量
      Result getBoxIdNum();
      //设置钥匙插槽地址
      Result setAddress(byte boxId);
       //批量设置钥匙插槽地址
       Result setBatchAddress(byte boxId);
      //清除钥匙插槽地址
      Result cleanAddress(byte boxId);
      //获取硬件板版本
      Result getVersion();
      //钥匙插槽蜂鸣
      Result getSound(byte boxId);
       //其他命令处理
      Result otherPort(byte type,String data);
}