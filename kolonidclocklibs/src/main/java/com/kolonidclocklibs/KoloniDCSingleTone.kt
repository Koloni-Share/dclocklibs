package com.kolonidclocklibs

import android.app.Activity
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
            instance = null
            callback = null
            //unbind
            ServiceProviderInstance.getInstance().unBind()
        }
    }

    fun onOpenDcLock(boxName: String) {
        // Open DC Lock.
        if (HAL.openBox((boxName))) {
            if (callback != null) {
                callback?.onDCLockOpenSuccessfully()
            }
        } else {
            if (callback != null) {
                callback?.onDCLockOpenFailed()
            }
        }
    }

    fun onGetDCLockStatus(boxName: String) {
        try {
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
        } catch (e: java.lang.Exception) {
            if (callback != null) {
                callback?.onDCGetLockStatusFailed("" + e.message)
            }
        }
    }

    fun onGetDCScannedData() {
        // Set DC Scanning.
        try {
            ServiceProviderInstance.getInstance().scannerController
            //scanner callback
            HAL.setScannerCallBack { data, type ->
                if (callback != null) {
                    callback?.onDCGetScanningData(data)
                }
            }
            HAL.toggleBarcode(true)
            HAL.toggleQRCode(true)
        } catch (e: Exception) {
            if (callback != null) {
                callback?.onDCScanningDataFailed("" + e.message)
            }
        }
    }

    fun onGetRFIDCardReadingData() {
        // Set RFID Card Reading.
        try {
            ServiceProviderInstance.getInstance().cardController
            HAL.setCardCallBack { data, type ->
                if (callback != null) {
                    callback?.onDCGetRFIDCardData(data)
                }
            }
        } catch (e: java.lang.Exception) {
            if (callback != null) {
                callback?.onDCRFIDCardDataFailed("" + e.message)
            }
        }
    }

}