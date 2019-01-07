package com.daybreakr.codelabs.bt.model.pair;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;

public class BluetoothPairingManager {
    private BluetoothAdapter mAdapter;

    public BluetoothPairingManager(BluetoothAdapter adapter) {
        mAdapter = adapter;
    }

    public Set<BluetoothDevice> getPairedDevices() {
        return mAdapter.getBondedDevices();
    }
}
