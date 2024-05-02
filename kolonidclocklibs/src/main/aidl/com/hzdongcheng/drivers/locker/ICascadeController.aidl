package com.hzdongcheng.drivers.locker;
import com.hzdongcheng.drivers.bean.Result;

//级联板
interface ICascadeController {
      //开门
      Result openBox(byte boxId);
      //获取副柜箱门状态
      Result queryStatus(byte boardId);
      //获取箱门状态
      Result queryBoxStatus(byte boxId);
      //设置箱门地址
      Result setBoxAddress(byte boxId);
      //清除箱门地址
      Result cleanAddress(byte boxId);
      //充电,照明灯控制
      Result chargeControl(byte boxId, boolean isEnabled);
      //其他命令处理
      Result otherPort(byte type,String data);
}