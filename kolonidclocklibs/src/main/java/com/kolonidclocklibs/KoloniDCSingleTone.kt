package com.kolonidclocklibs

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.kolonidclocklibs.datafile.HAL
import com.kolonidclocklibs.datafile.IAPIKoloniDCCallback
import com.kolonidclocklibs.datafile.ServiceProviderInstance

/**
 *  Koloni DC Locker customized library.
 *  Developed by Nirav Mehta : 02-05-2024.
 */
public class KoloniDCSingleTone {

    companion object {
        private var instance: KoloniDCSingleTone? = null
        private var callback: IAPIKoloniDCCallback? = null

        fun getDCInstance(mActivity: Activity, callback: IAPIKoloniDCCallback): KoloniDCSingleTone {
            if (instance == null) {
                instance = KoloniDCSingleTone()
                this.callback = callback

                if (HAL.init(mActivity)) {
                    callback.onDriverAppConnected()
                } else {
                    callback.onDriverAppConnectionFailed()
                }

                ServiceProviderInstance.getInstance().bind(mActivity)

            }
            return instance as KoloniDCSingleTone
        }

        fun removeDCInstance() {
            try {
                instance = null
                callback = null
                //unbind
                if (ServiceProviderInstance.getInstance() != null) {
                    ServiceProviderInstance.getInstance().unBind()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onOpenDcLock(boxName: String) {
        // Open DC Lock.
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    if (HAL.openBox((boxName))) {
                        if (callback != null) {
                            callback?.onDCLockOpenSuccessfully()
                        }
                    } else {
                        if (callback != null) {
                            callback?.onDCLockOpenFailed()
                        }
                    }
                } catch (e: Exception) {
                    callback?.onDCLockOpenFailed()
                }
            }, 1000)
        } catch (e: Exception) {
            if (callback != null) {
                callback?.onDCLockOpenFailed()
            }
        }
    }

    fun onGetDCLockStatus(boxName: String) {
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                if (HAL.getBoxStatus(boxName) != null) {
                    val status = HAL.getBoxStatus(boxName)
                    if (status != null) {
                        if (callback != null) {
                            callback?.onDCGetLockStatus(status)
                        }
                    } else {
                        if (callback != null) {
                            callback?.onDCGetLockStatusFailed("")
                        }
                    }
                } else {
                    callback?.onDCGetLockStatusFailed("Something went wrong.")
                }
            }, 1000)

        } catch (e: java.lang.Exception) {
            if (callback != null) {
                callback?.onDCGetLockStatusFailed("" + e.message)
            }
        }
    }

    fun onGetDCScannedData() {
        // Set DC Scanning.
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                if (ServiceProviderInstance.getInstance().scannerController != null) {
                    ServiceProviderInstance.getInstance().scannerController
                    //scanner callback
                    HAL.setScannerCallBack { data, type ->
                        if (callback != null) {
                            callback?.onDCGetScanningData(data)
                        }
                    }
                    HAL.toggleBarcode(true)
                    HAL.toggleQRCode(true)
                } else {
                    if (callback != null) {
                        callback?.onDCScanningDataFailed("Do not get the scanner service.")
                    }
                }
            }, 1000)
        } catch (e: Exception) {
            if (callback != null) {
                callback?.onDCScanningDataFailed("" + e.message)
            }
        }
    }

    fun onGetRFIDCardReadingData() {
        // Set RFID Card Reading.
        try {
            Handler(Looper.getMainLooper()).postDelayed({
                if (ServiceProviderInstance.getInstance().cardController != null) {
                    ServiceProviderInstance.getInstance().cardController
                    HAL.setCardCallBack { data, type ->
                        if (callback != null) {
                            callback?.onDCGetRFIDCardData(data)
                        }
                    }
                } else {
                    if (callback != null) {
                        callback?.onDCRFIDCardDataFailed("Do not get the card reader service.")
                    }
                }
            }, 1000)

        } catch (e: java.lang.Exception) {
            if (callback != null) {
                callback?.onDCRFIDCardDataFailed("" + e.message)
            }
        }
    }

}