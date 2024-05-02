package com.hzdongcheng.drivers.locker;
import com.hzdongcheng.drivers.bean.Result;

//Pizza双面柜
interface IPizzaController {
       //根据箱门名称开门
       Result openBox(String boxName);
       //根据箱门名称关门
       Result closeBox(String boxName);
       //获取副机所有状态
       Result queryStatus(byte boardId);
        //打开后门灯
       Result openBoxLan(String boxName);
       //关闭后门灯
       Result closeBoxLan(String boxName);
       //其他命令处理
        Result otherPort(byte type,String data);
}