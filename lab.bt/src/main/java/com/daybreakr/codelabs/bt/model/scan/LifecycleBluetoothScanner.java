package com.daybreakr.codelabs.bt.model.scan;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

class LifecycleBluetoothScanner extends BluetoothScanner implements DefaultLifecycleObserver {
    private final BluetoothScanner mScanner;

    LifecycleBluetoothScanner(BluetoothScanner scanner, LifecycleOwner owner) {
        owner.getLifecycle().addObserver(this);

        mScanner = scanner;
    }

    @Override
    public void startScanning() {
        mScanner.startScanning();
    }

    @Override
    public void stopScanning() {
        mScanner.stopScanning();
    }

    @Override
    public boolean isScanning() {
        return mScanner.isScanning();
    }

    @Override
    public void destroy() {
        mScanner.destroy();
    }

    @Override
    public void setScanCallback(ScanCallback callback) {
        mScanner.setScanCallback(callback);
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        stopScanning();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        destroy();
    }
}
