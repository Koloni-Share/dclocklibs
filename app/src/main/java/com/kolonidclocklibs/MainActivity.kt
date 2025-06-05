package com.kolonidclocklibs

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hzdongcheng.drivers.bean.BoxStatus
import com.hzdongcheng.drivers.bean.SlaveStatus
import com.kolonidclocklibs.databinding.ActivityMainBinding
import com.kolonidclocklibs.datafile.HAL
import com.kolonidclocklibs.datafile.IAPIKoloniDCCallback


class MainActivity : AppCompatActivity(), IAPIKoloniDCCallback {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetScannedData.setOnClickListener {
            KoloniDCSingleTone.getDCInstance(this@MainActivity, this).onGetDCScannedData()

//            runOnUiThread {
//                Handler(Looper.getMainLooper()).postDelayed({
//                    KoloniDCSingleTone.getDCInstance(this@MainActivity, this).onGetDCScannedData()
//                }, 1000)
//            }
        }

        binding.btnGetRFIDData.setOnClickListener {
            KoloniDCSingleTone.getDCInstance(this@MainActivity, this).onGetRFIDCardReadingData()
        }

        binding.btnUnlockTheLock.setOnClickListener {
            KoloniDCSingleTone.getDCInstance(this@MainActivity, this).onOpenDcLock("1")
        }

        binding.btnGetLockStatus.setOnClickListener {
            HAL.init(this@MainActivity)

            Handler(Looper.getMainLooper()).postDelayed({

                val allLockerStatuses: Map<Int, SlaveStatus> = HAL.getAllDcLockerStatusForSingleBoard(0)

                for ((boardId, status) in allLockerStatuses) {
                    println("Board ID: $boardId")
                    println("Box Count: " + status.boxNums)

                    for (i in 0 until status.boxNums) {
                        val boxStatus = status.getBoxStatusArray(i).openStatus
                        println(" - Box $i status: $boxStatus")
                    }
                }
            },1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        KoloniDCSingleTone.removeDCInstance()
    }

    override fun onDCLockOpenSuccessfully() {
        runOnUiThread {
            binding.tvScannedData.text = getString(R.string.door_open_success)
            Toast.makeText(this, getString(R.string.door_open_success), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDCLockOpenFailed() {
        runOnUiThread {
            binding.tvScannedData.text = getString(R.string.door_open_failed)
            Toast.makeText(this, getString(R.string.door_open_failed), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDCGetScanningData(data: String) {
        runOnUiThread {
            binding.tvScannedData.text = "" + getString(R.string.door_scan_success) + " : " + data
            Toast.makeText(this, getString(R.string.door_scan_success) + data, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDCGetRFIDCardData(data: String) {
        runOnUiThread {
            binding.tvScannedData.text = getString(R.string.door_read_success) + " : " + data
            Toast.makeText(this, getString(R.string.door_read_success) + data, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDCScanningDataFailed(error: String) {
        runOnUiThread {
            binding.tvScannedData.text = getString(R.string.door_scan_failed) + " : " + error
            Toast.makeText(this, getString(R.string.door_scan_failed) + error, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDCRFIDCardDataFailed(error: String) {
        runOnUiThread {
            binding.tvScannedData.text = getString(R.string.door_read_failed) + " : " + error
            Toast.makeText(this, getString(R.string.door_read_failed) + error, Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDriverAppConnected() {
        runOnUiThread {
            binding.tvScannedData.text = getString(R.string.driver_connection_success)
            Toast.makeText(this, getString(R.string.driver_connection_success), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDriverAppConnectionFailed() {
        runOnUiThread {
            binding.tvScannedData.text = getString(R.string.driver_connection_failed)
            Toast.makeText(this, getString(R.string.driver_connection_failed), Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDCGetLockStatus(boxStatus: BoxStatus) {
        runOnUiThread {
            binding.tvScannedData.text =
                getString(R.string.lock_status_success) + boxStatus.openStatus
            Toast.makeText(
                this,
                getString(R.string.lock_status_success) + boxStatus.openStatus,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDCGetLockStatusFailed(error: String) {
        runOnUiThread {
            binding.tvScannedData.text = getString(R.string.lock_status_failed) + error
            Toast.makeText(this, getString(R.string.lock_status_failed) + error, Toast.LENGTH_SHORT)
                .show()
        }
    }

}