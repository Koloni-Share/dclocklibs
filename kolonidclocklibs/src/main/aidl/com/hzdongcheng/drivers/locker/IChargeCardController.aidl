package com.hzdongcheng.drivers.locker;
import com.hzdongcheng.drivers.bean.Result;

//充电卡 操作后需要1秒后才能对此终端进行第二次操作
interface IChargeCardController {
      //版本查询
      Result searchVersion(byte boardId);
      //打开充电槽，插入充电卡
      Result insertCard(byte boxId);
      //弹出充电卡
      Result takeCard(byte boxId);
     //充电控制
      Result chargeControl(byte boxId, boolean isEnabled);
      //获取副柜箱门状态
      Result queryStatus(byte boardId);
      //获取箱门状态
      Result queryBoxStatus(byte boxId);
      //清除警报
      Result clearWarning(byte boxId);
       //其他命令处理
      Result otherPort(byte type,String data);
}