package org.kvj.habtproxy

import android.bluetooth.le.ScanRecord
import android.util.Log
import java.util.Date

private val TAG = "DiscoveryResults"

private fun serviceRecordsEqual(a: ScanRecord, b: ScanRecord): Boolean {
    if (a.txPowerLevel != b.txPowerLevel) {
        Log.d(TAG, "TX Level differs")
        return false;
    }
    if (a.serviceUuids != b.serviceUuids) {
        Log.d(TAG, "Service UUIDs differs")
        return false;
    }
    if (a.deviceName != b.deviceName) {
        Log.d(TAG, "Device name differs")
        return false;
    }
    if (!a.serviceData.contentMapEquals(b.serviceData)) {
        Log.d(TAG, "Service data differs ${a.serviceData} vs ${b.serviceData}")
        return false;
    }
    if (!a.manufacturerSpecificData.contentMapEquals(b.manufacturerSpecificData)) {
        Log.d(TAG, "Manuf data differs ${a.manufacturerSpecificData} vs ${b.manufacturerSpecificData}")
        return false;
    }
    return true;
}

data class DiscoveredDevice(var record: ScanRecord, var rssi: Int, var txPower: Int, var name: String?, var timestamp: Long = Date().time) {

    private val rssiChangeThreshold = 10 // TODO: Configure

    fun updateMaybe(record: ScanRecord, rssi: Int, txPower: Int, name: String?): Boolean {
        val rssiChanged = Math.abs(this.rssi - rssi) >= rssiChangeThreshold
        val txChanged = this.txPower != txPower
        if (!serviceRecordsEqual(this.record, record) || rssiChanged || this.name != name || txChanged) {
            Log.d(TAG, "updateMaybe: ${!serviceRecordsEqual(this.record, record)} / ${this.rssi} != ${rssi} / ${this.txPower} != ${txPower} / ${this.name != name}")
            this.record = record
            this.rssi = rssi
            this.txPower = txPower
            this.name = name
            this.timestamp = Date().time
            return true
        }
        return false
    }

}


class DiscoveryResults private constructor() {
    companion object {
        val instance by lazy {
            DiscoveryResults()
        }
    }

    val discoveredRecords = mutableMapOf<String, DiscoveredDevice>()
    var lastUploadTimestamp: Long = 0

}