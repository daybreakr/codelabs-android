package com.daybreakr.codelabs.bt.model.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleBluetoothScanner extends BluetoothScanner {
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;

    private final AtomicBoolean mIsStarting = new AtomicBoolean(false);
    private final AtomicBoolean mIsListening = new AtomicBoolean(false);

    SimpleBluetoothScanner(Context context, BluetoothAdapter adapter) {
        mContext = context;
        mBluetoothAdapter = adapter;
    }

    @Override
    public void startScanning() {
        if (mIsStarting.compareAndSet(false, true)) {
            try {
                if (mContext == null || mBluetoothAdapter == null) {
                    return;
                }

                // Check for bluetooth enabled.
                if (!mBluetoothAdapter.isEnabled()) {
                    notifyFailToStartScanning(ERROR_BLUETOOTH_DISABLED);
                    return;
                }

                startListening();

                // Waiting for callbacks if already processing discovering.
                if (mBluetoothAdapter.isDiscovering()) {
                    return;
                }

                if (!mBluetoothAdapter.startDiscovery()) {
                    stopListening();
                    notifyFailToStartScanning(ERROR_START_DISCOVERY);
                }
            } finally {
                mIsStarting.set(false);
            }
        }
    }

    @Override
    public void stopScanning() {
        if (mContext == null || mBluetoothAdapter == null) {
            return;
        }

        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        stopListening();
    }

    @Override
    public void destroy() {
        setCallback(null);
        mContext = null;
        mBluetoothAdapter = null;
    }

    private void startListening() {
        if (mIsListening.compareAndSet(false, true)) {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            mContext.registerReceiver(mReceiver, filter);
        }
    }

    private void stopListening() {
        if (mIsListening.compareAndSet(true, false)) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent != null ? intent.getAction() : null;
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    notifyFoundDevice(device);
                }
            }
        }
    };
}
