package com.daybreakr.codelabs.bt.model.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;

public abstract class BluetoothScanner {

    public interface ScanCallback {

        default void onStartScanning() {
        }

        default void onFailToStartScanning(int error) {
        }

        default void onFoundDevice(BluetoothDevice device) {
        }
    }

    public static final int ERROR_BLUETOOTH_DISABLED = 1;

    public static final int ERROR_START_DISCOVERY = 2;

    private ScanCallback mCallback;

    public static BluetoothScanner getScanner(Context context, BluetoothAdapter adapter) {
        return new SimpleBluetoothScanner(context, adapter);
    }

    public void setCallback(ScanCallback callback) {
        mCallback = callback;
    }

    public abstract void startScanning();

    public abstract void stopScanning();

    protected void notifyStartScanning() {
        if (mCallback != null) {
            mCallback.onStartScanning();
        }
    }

    protected void notifyFailToStartScanning(int error) {
        if (mCallback != null) {
            mCallback.onFailToStartScanning(error);
        }
    }

    protected void notifyFoundDevice(BluetoothDevice device) {
        if (mCallback != null) {
            mCallback.onFoundDevice(device);
        }
    }
}