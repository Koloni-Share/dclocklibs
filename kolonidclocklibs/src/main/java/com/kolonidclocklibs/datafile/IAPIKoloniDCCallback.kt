package com.kolonidclocklibs.datafile

import com.hzdongcheng.drivers.bean.BoxStatus

public interface IAPIKoloniDCCallback {

    fun onDCLockOpenSuccessfully()
    fun onDCLockOpenFailed()
    fun onDCGetScanningData(data: String)
    fun onDCGetRFIDCardData(data: String)
    fun onDCScanningDataFailed(error: String)
    fun onDCRFIDCardDataFailed(error: String)
    fun onDriverAppConnected()
    fun onDriverAppConnectionFailed()
    fun onDCGetLockStatus(boxStatus: BoxStatus)
    fun onDCGetLockStatusFailed(error: String)

}