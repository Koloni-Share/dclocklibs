package com.hzdongcheng.drivers.locker;
import com.hzdongcheng.drivers.bean.Result;

//驱动板
interface ISlaveController {
    //版本查询
    Result searchVersion(byte boardId);
    //获取资产编码
    Result getDeviceCode(byte boardId);
    //设置资产编码
    Result setDeviceCode(byte boardId,String code);
    //设置继电器
    Result setRelay(byte boardID,in Map channelValues);
    //获取继电器状态
    Result getRelay(byte boardId);
    //打开箱门
    Result openBoxById(byte boardId, byte boxId);
    //打开箱门
    Result openBoxByName(String boxName);
    //获取副机状态
    Result queryStatus(byte boardId);
    //获取箱门状态
    Result queryBoxStatusById(byte boardId, byte boxId);
    //获取箱门状态
    Result queryBoxStatusByName(String boxName);
    //箱门加热控制
    Result setBoxHeatingByName(String boxName,boolean active);
    //箱门加热控制
    Result setBoxHeatingById(byte boardId, byte boxId,boolean active);
    //获取箱门加热状态
    Result getBoxHeating(byte boardID);
    //单个箱门紫外灯控制
    Result setBoxUVLanternByName(String boxName, boolean active);
    //单个箱门紫外灯控制
    Result setBoxUVLanternById(byte boardId, byte boxId, boolean active);
    //获取副柜紫外灯状态
    Result getBoxUVLanterns(byte boardID);
    //单个箱门照明灯控制
    Result setBoxLanternByName(String boxName, boolean active);
    //单个箱门照明灯控制
    Result setBoxLanternById(byte boardId, byte boxId, boolean active);
    //获取副柜照明灯状态
    Result getBoxLanterns(byte boardID);
    //其他命令处理
    Result otherPort(byte type,String data);
}
