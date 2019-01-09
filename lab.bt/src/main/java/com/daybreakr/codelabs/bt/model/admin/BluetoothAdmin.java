package com.daybreakr.codelabs.bt.model.admin;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;

public class BluetoothAdmin {

    public void enableBluetooth(Context context) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

    }
}
