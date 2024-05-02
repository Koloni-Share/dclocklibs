package com.hzdongcheng.drivers;

//东城硬件aidl
interface IDriverManager {
    //获取主柜控制服务
    IBinder getMasterService();
    //获取副柜控制服务(0:驱动板 1：级联板 2：充电卡  3：pizza双面柜 4：钥匙柜)
    IBinder getSlaveService(byte type);
    //获取读卡器控制服务
    IBinder getCardReaderService();
    //获取系统控制服务
    IBinder getSystemService();
    //获取扫描枪控制服务
    IBinder getScannerService();
    //获取指纹控制服务
    IBinder getFingerService();
    //获取打印机控制服务
    IBinder getPrinterService();
    //获取其他控制服务
    IBinder getService(String serviceName);

}
