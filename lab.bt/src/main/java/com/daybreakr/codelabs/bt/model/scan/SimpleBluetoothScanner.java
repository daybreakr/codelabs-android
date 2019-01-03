package com.daybreakr.codelabs.bt.model.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class SimpleBluetoothScanner extends BluetoothScanner {
    private final WeakReference<Context> mContextRef;
    private final BluetoothAdapter mBluetoothAdapter;

    private final AtomicBoolean mIsStarting = new AtomicBoolean(false);

    SimpleBluetoothScanner(Context context, BluetoothAdapter adapter) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
        mContextRef = new WeakReference<>(context);

        if (adapter == null) {
            throw new NullPointerException("Bluetooth adapter is null");
        }
        mBluetoothAdapter = adapter;
    }

    @Override
    public void startScanning() {
        if (mIsStarting.compareAndSet(false, true)) {
            try {
                // Check for bluetooth enabled.
                if (!mBluetoothAdapter.isEnabled()) {
                    notifyFailToStartScanning(ERROR_BLUETOOTH_DISABLED);
                    return;
                }

                // Waiting for callbacks if already processing discovering.
                if (mBluetoothAdapter.isDiscovering()) {
                    return;
                }

                Context context = mContextRef.get();
                if (context == null) {
                    // Context already been destroyed no need to start scanning.
                    stopScanning();
                    return;
                }
                startListening(context);

                if (mBluetoothAdapter.startDiscovery()) {
                    notifyStartScanning();
                } else {
                    notifyFailToStartScanning(ERROR_START_DISCOVERY);
                }
            } finally {
                mIsStarting.set(false);
            }
        }
    }

    @Override
    public void stopScanning() {
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }

        Context context = mContextRef.get();
        if (context != null) {
            stopListening(context);
        }
    }

    private void startListening(Context context) {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);
    }

    private void stopListening(Context context) {
        context.unregisterReceiver(mReceiver);
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
