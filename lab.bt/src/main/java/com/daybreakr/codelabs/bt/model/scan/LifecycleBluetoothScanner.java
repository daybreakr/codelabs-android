package com.daybreakr.codelabs.bt.model.scan;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public class LifecycleBluetoothScanner extends BluetoothScanner
        implements DefaultLifecycleObserver {
    private final BluetoothScanner mScanner;

    LifecycleBluetoothScanner(LifecycleOwner owner, BluetoothScanner scanner) {
        mScanner = scanner;
        owner.getLifecycle().addObserver(this);
    }

    @Override
    public void setCallback(ScanCallback callback) {
        mScanner.setCallback(callback);
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
    public void destroy() {
        mScanner.destroy();
    }

    @Override
    public void onStart(@NonNull LifecycleOwner owner) {
        startScanning();
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
