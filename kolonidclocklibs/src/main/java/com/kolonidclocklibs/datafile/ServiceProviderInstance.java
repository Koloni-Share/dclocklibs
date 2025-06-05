package com.kolonidclocklibs.datafile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.hzdongcheng.drivers.IDriverManager;
import com.hzdongcheng.drivers.bean.Result;
import com.hzdongcheng.drivers.bean.SlaveStatus;
import com.hzdongcheng.drivers.locker.ISlaveController;
import com.hzdongcheng.drivers.peripheral.cardreader.ICardReaderController;
import com.hzdongcheng.drivers.peripheral.scanner.IScannerController;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by zzq on 2018/9/13
 **/
public class ServiceProviderInstance {
    private static final String TAG = "ServiceProviderInstance";
    private static ServiceProviderInstance instance = new ServiceProviderInstance();
    private static IDriverManager driverManager;
    private static ISlaveController slaveController;
    private static IScannerController scannerController;
    private static ICardReaderController cardReaderController;
    private WeakReference<Context> contextWeakReference;
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> schedule;
    private Intent driverIntent;

    /**
     * 监听Aidl客户端和服务端的状态
     * listen AIDL's server and client status
     */
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (driverManager != null && contextWeakReference != null) {
                driverManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
                contextWeakReference.get().unbindService(serviceConnection);
            }
        }
    };
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "->driver connect success");
            driverManager = IDriverManager.Stub.asInterface(iBinder);
            try {
                iBinder.linkToDeath(mDeathRecipient, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "->driver disconnect");
            driverManager = null;
            scannerController = null;
            slaveController = null;
            reconnectService();

        }
    };


    private ServiceProviderInstance() {

    }

    public static ServiceProviderInstance getInstance() {
        return instance;
    }

    /**
     * service reconnect(delay 3 seconds)
     */
    private synchronized void reconnectService() {

        if (schedule != null && !schedule.isDone()) {
            Log.d(TAG, "--> driver is reconnecting");
            return;
        }

        schedule = executorService.schedule(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "--> start reconnect");
                if (contextWeakReference.get() != null) {
                    boolean isBind;
                    int tryCount = 0;
                    do {
                        try {
                            Thread.sleep(tryCount * 30 * 1000);
                        } catch (InterruptedException ignored) {
                        }
                        isBind = contextWeakReference.get().bindService(driverIntent, serviceConnection, Context.BIND_AUTO_CREATE);
                    } while (!isBind || tryCount++ < 10);
                }
            }
        }, 3, TimeUnit.SECONDS);
    }

    /**
     * bind driver service
     *
     * @param context
     */
    public boolean bind(Context context) {
        driverIntent = new Intent("hzdongcheng.intent.action.DRIVER");
        driverIntent.setPackage("com.hzdongcheng.drivers");
        contextWeakReference = new WeakReference<>(context);
        return context.bindService(driverIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * get locker board
     *
     * @return
     */
    public static ISlaveController getSlaveController() {
        if (slaveController != null && slaveController.asBinder().isBinderAlive())
            return slaveController;
        if (driverManager != null) {
            try {
                slaveController = ISlaveController.Stub.asInterface(driverManager.getSlaveService((byte) 0));
            } catch (RemoteException e) {
                Log.e(TAG, ">>locker board get fail>>" + e.getMessage());
            }
        }
        if (slaveController == null) {
            Log.e(TAG, "do not get the locker board model");
        }
        return slaveController;
    }

    /**
     * get scanner service
     *
     * @return
     * @throws
     */
    public IScannerController getScannerController() throws Exception {
        if (scannerController != null && scannerController.asBinder().isBinderAlive())
            return scannerController;
        if (driverManager != null) {
            try {
                scannerController = IScannerController.Stub.asInterface(driverManager.getScannerService());
                if (scannerController == null) {
//                    throw new Exception("do not get the scanner service");
                }
                scannerController.start();
                scannerController.addObserver(HAL.scannerObserver);
            } catch (RemoteException e) {
                Log.e(TAG, ">>get the scanner service fail>>" + e.getMessage());
            }
        }
        if (scannerController == null) {
//            throw new Exception("do not get the scanner service");
        }
        return scannerController;
    }


    /**
     * get card reader service
     *
     * @return
     * @throws
     */
    public ICardReaderController getCardController() throws Exception {
        if (cardReaderController != null && cardReaderController.asBinder().isBinderAlive())
            return cardReaderController;
        if (driverManager != null) {
            try {
                cardReaderController = ICardReaderController.Stub.asInterface(driverManager.getCardReaderService());
                if (cardReaderController == null) {
//                    throw new Exception("do not get the card reader service");
                }
                cardReaderController.start();
                cardReaderController.addObserver(HAL.cardObserver);
            } catch (RemoteException e) {
                Log.e(TAG, ">>get the card reader service fail>>" + e.getMessage());
            }
        }
        if (cardReaderController == null) {
//            throw new Exception("do not get the card reader service");
        }
        return cardReaderController;
    }

    //unbind
    public void unBind() {
        if (contextWeakReference.get() != null) {
            contextWeakReference.get().unbindService(serviceConnection);
        }

    }
}
