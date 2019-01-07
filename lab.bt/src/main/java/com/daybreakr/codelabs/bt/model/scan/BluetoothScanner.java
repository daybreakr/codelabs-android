package com.daybreakr.codelabs.bt.model.scan;

import android.bluetooth.BluetoothDevice;

import java.lang.ref.WeakReference;

public abstract class BluetoothScanner {

    public interface ScanCallback {

        default void onScanStarted() {
        }

        default void onScanFailedToStart(Throwable error) {
        }

        default void onFoundDevice(BluetoothDevice device) {
        }

        default void onScanFinished() {
        }
    }

    private WeakReference<ScanCallback> mCallbackRef;

    public abstract void startScanning();

    public abstract void stopScanning();

    public abstract boolean isScanning();

    public void destroy() {
        mCallbackRef = null;
    }

    public void setScanCallback(ScanCallback callback) {
        mCallbackRef = new WeakReference<>(callback);
    }

    protected void notifyFailToStart(Throwable error) {
        ScanCallback callback = getCallback();
        if (callback != null) {
            callback.onScanFailedToStart(error);
        }
    }

    protected void notifyFoundDevice(BluetoothDevice device) {
        ScanCallback callback = getCallback();
        if (callback != null) {
            callback.onFoundDevice(device);
        }
    }

    protected void notifyScanStarted() {
        ScanCallback callback = getCallback();
        if (callback != null) {
            callback.onScanStarted();
        }
    }

    protected void notifyScanFinished() {
        ScanCallback callback = getCallback();
        if (callback != null) {
            callback.onScanFinished();
        }
    }

    private ScanCallback getCallback() {
        return mCallbackRef != null ? mCallbackRef.get() : null;
    }
}
