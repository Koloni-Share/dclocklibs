package com.kolonidclocklibs

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hzdongcheng.drivers.bean.BoxStatus
import com.kolonidclocklibs.databinding.ActivityMainBinding
import com.kolonidclocklibs.datafile.IAPIKoloniDCCallback


class MainActivity : AppCompatActivity(), IAPIKoloniDCCallback {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        KoloniDCSingleTone.getDCInstance(this@MainActivity, this).onDriverAppConnect()

        binding.btnGetScannedData.setOnClickListener {
            KoloniDCSingleTone.getDCInstance(this@MainActivity, this).onGetDCScannedData()
        }

        binding.btnGetRFIDData.setOnClickListener {
            KoloniDCSingleTone.getDCInstance(this@MainActivity, this).onGetRFIDCardReadingData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        KoloniDCSingleTone.removeDCInstance()
    }

    override fun onDCLockOpenSuccessfully() {
        runOnUiThread {
            Toast.makeText(this, getString(R.string.door_open_success), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDCLockOpenFailed() {
        runOnUiThread {
            Toast.makeText(this, getString(R.string.door_open_failed), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDCGetScanningData(data: String) {
        runOnUiThread {
            Toast.makeText(this, getString(R.string.door_scan_success) + data, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDCGetRFIDCardData(data: String) {
        runOnUiThread {
            Toast.makeText(this, getString(R.string.door_read_success) + data, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDCScanningDataFailed(error: String) {
        runOnUiThread {
            Toast.makeText(this, getString(R.string.door_scan_failed) + error, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDCRFIDCardDataFailed(error: String) {
        runOnUiThread {
            Toast.makeText(this, getString(R.string.door_read_failed) + error, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDriverAppConnected() {
        runOnUiThread {
            Toast.makeText(this, getString(R.string.driver_connection_success), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDriverAppConnectionFailed() {
        runOnUiThread {
            Toast.makeText(this, getString(R.string.driver_connection_failed), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDCGetLockStatus(boxStatus: BoxStatus) {
        runOnUiThread {
            Toast.makeText(
                this,
                getString(R.string.lock_status_success) + boxStatus.openStatus,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDCGetLockStatusFailed(error: String) {
        runOnUiThread {
            Toast.makeText(this, getString(R.string.lock_status_failed) + error, Toast.LENGTH_SHORT)
                .show()
        }
    }
}