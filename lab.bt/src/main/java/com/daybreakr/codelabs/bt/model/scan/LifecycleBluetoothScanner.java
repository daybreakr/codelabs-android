package com.daybreakr.codelabs.bt.model.scan;

import androidx.lifecycle.LifecycleOwner;

public class LifecycleBluetoothScanner {
    private final BluetoothScanner mScanner;

    LifecycleBluetoothScanner(LifecycleOwner owner, BluetoothScanner scanner) {
        mScanner = scanner;
    }
}
