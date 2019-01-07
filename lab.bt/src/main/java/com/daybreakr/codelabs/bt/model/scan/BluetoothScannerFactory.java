package com.daybreakr.codelabs.bt.model.scan;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

public class BluetoothScannerFactory {

    public static BluetoothScanner newScanner(BluetoothAdapter adapter, Context context) {
        return new BasicBluetoothScanner(adapter, context);
    }

    public static BluetoothScanner newScanner(BluetoothAdapter adapter, Context context,
                                              LifecycleOwner owner) {
        return new LifecycleBluetoothScanner(newScanner(adapter, context), owner);
    }
}
