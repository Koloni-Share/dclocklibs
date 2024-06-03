package com.kolonidclocklibs.datafile;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.hzdongcheng.drivers.bean.BoxStatus;
import com.hzdongcheng.drivers.bean.ICallBack;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.bean.SlaveStatus;
import com.hzdongcheng.drivers.peripheral.IObserver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzq on 2018/9/13
 **/
public class HAL {
    private static final String TAG = "HAL";
    private static ICallBack scannerCallBack;
    private static ICallBack cardCallBack;

    public static boolean init(Context context) {
        return ServiceProviderInstance.getInstance().bind(context);
    }

    //set scanner callback
    public static void setScannerCallBack(ICallBack _callBack) {
        scannerCallBack = _callBack;
    }

    //set RFID reader callback
    public static void setCardCallBack(ICallBack _callBack) {
        cardCallBack = _callBack;
    }

    //#region scanner
    public static IObserver scannerObserver = new IObserver.Stub() {
        @Override
        public void onMessage(String msg) throws RemoteException {
            if (scannerCallBack != null)
                scannerCallBack.onMessage(msg, 0);
        }
    };

    //#region RFID card reader
    public static IObserver cardObserver = new IObserver.Stub() {
        @Override
        public void onMessage(String msg) throws RemoteException {
            if (cardCallBack != null)
                cardCallBack.onMessage(msg, 0);
        }
    };

    /**
     * open box
     *
     * @param boxName
     * @return result
     */
    public static boolean openBox(String boxName) {
        try {
            Log.d(TAG, "open box: ");

            if (ServiceProviderInstance.getInstance() != null) {
                if (ServiceProviderInstance.getInstance().getSlaveController() != null) {
                    if (ServiceProviderInstance.getInstance().getSlaveController().openBoxByName(boxName) != null) {
                        Result result = ServiceProviderInstance.getInstance().getSlaveController().openBoxByName(boxName);
                        if (result.getCode() == 0) {
                            Log.d(TAG, "[HAL] open box success：" + boxName);
                            return true;
                        }
                        Log.e(TAG, "[HAL] open box fail：" + boxName + ",code " + result.getCode());
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "[HAL] open box fail，box No. " + boxName);
            return false;
        }
        return false;
    }


    /**
     * get door status
     *
     * @param boxName
     * @return
     */
    public static BoxStatus getBoxStatus(String boxName) throws Exception {
        try {
            Result result = ServiceProviderInstance.getInstance().getSlaveController().queryBoxStatusByName(boxName);
            if (result.getCode() == 0) {
                Log.e(TAG, "[HAL] get door status success -->" + new Gson().toJson(result));
                return new Gson().fromJson(result.getData(), BoxStatus.class);
            }
            Log.e(TAG, "[HAL] get door status failed, boxName " + boxName);
//            throw new Exception("get door status failed");
        } catch (RemoteException e) {
            Log.e(TAG, "[HAL] get door status error, boxName " + boxName);
//            throw new Exception("get door status error");
        }
        return null;
    }

    /**
     * get all door status by group
     */
//    public static void getBoxStatue() {
//        Map<Integer, SlaveStatus> boxStatusMap = new HashMap<>();
//        //suppose has 4 group of doors
//        for (int i = 0; i < 4; i++) {
//            try {
//                boxStatusMap.put(i, getSlaveStatus(i));
//            } catch (Exception e) {
//                Log.e(TAG, "get door status error ");
//            }
//        }
//
//    }

    /**
     * get locker board status
     *
     * @param boardId
     * @return board status
     */
//    public static SlaveStatus getSlaveStatus(int boardId) throws Exception {
//        try {
//            Result result = ServiceProviderInstance.getInstance().getSlaveController().queryStatus((byte) boardId);
//            if (result.getCode() == 0) {
//                return new Gson().fromJson(result.getData(), SlaveStatus.class);
//            }
//            Log.d(TAG, "[HAL] get locker board status fail, board" + boardId);
//            throw new Exception("get locker board status fail");
//        } catch (RemoteException e) {
//            Log.d(TAG, "[HAL] get locker board status error, board " + boardId);
////            throw new Exception("get locker board status error");
//        }
//    }


    /**
     * add scanner listener
     */
    public static void addObserver() throws Exception {
        try {
            Log.d(TAG, "[HAL] add scanner listener ");
            ServiceProviderInstance.getInstance().getScannerController().addObserver(scannerObserver);
        } catch (RemoteException e) {
            Log.e(TAG, "[HAL] add scanner listener " + e.getMessage());
        }
    }

    /**
     * enable bar code
     *
     * @param enabled true enable barcode，false disable barcode
     */
    public static void toggleBarcode(boolean enabled) throws Exception {
        try {
            Log.d(TAG, "[HAL] set scanner support barcode " + enabled);
            ServiceProviderInstance.getInstance().getScannerController().toggleBarcode(enabled);
        } catch (RemoteException e) {
            Log.e(TAG, "[HAL] scanner set error " + e.getMessage());
        }
    }

    /**
     * enable qr code
     *
     * @param enabled true enable qrcode，false disable qrcode
     */
    public static void toggleQRCode(boolean enabled) throws Exception {
        try {
            Log.d(TAG, "[HAL] set scanner support qr code " + enabled);
            ServiceProviderInstance.getInstance().getScannerController().toggleQRCode(enabled);
        } catch (RemoteException e) {
            Log.e(TAG, "[HAL] scanner set error " + e.getMessage());
        }
    }

    /**
     * start scan（scanner should in command mode）
     */
    public static void startScanning() throws Exception {
        try {
            Log.d(TAG, "[HAL] scanner start scan ");
            ServiceProviderInstance.getInstance().getScannerController().start();
        } catch (RemoteException e) {
            Log.e(TAG, "[HAL] scanner scan error " + e.getMessage());
        }
    }

    /**
     * stop scan（scanner should in command mode）
     */
    public static void stopScanning() throws Exception {
        try {
            Log.d(TAG, "[HAL] scanner stop scan ");
            ServiceProviderInstance.getInstance().getScannerController().stop();
        } catch (RemoteException e) {
            Log.e(TAG, "[HAL] scanner start scan error " + e.getMessage());
        }
    }

}
