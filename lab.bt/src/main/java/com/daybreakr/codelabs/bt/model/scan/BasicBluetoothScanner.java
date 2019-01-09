package com.daybreakr.codelabs.bt.model.scan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.concurrent.atomic.AtomicBoolean;

class BasicBluetoothScanner extends BluetoothScanner {
    private BluetoothAdapter mAdapter;
    private Context mContext;

    private final AtomicBoolean mIsStarting = new AtomicBoolean(false);
    private final AtomicBoolean mIsListening = new AtomicBoolean(false);

    BasicBluetoothScanner(BluetoothAdapter adapter, Context context) {
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public void startScanning() {
        if (mAdapter == null) {
            return;
        }

        if (mIsStarting.compareAndSet(false, true)) {
            try {
                // Check for bluetooth enabled.
                if (!mAdapter.isEnabled()) {
                    notifyFailToStart(new IllegalStateException("Bluetooth is disabled"));
                    return;
                }

                startListening();

                // Waiting for callbacks if already processing discovering.
                if (mAdapter.isDiscovering()) {
                    return;
                }

                if (!mAdapter.startDiscovery()) {
                    notifyFailToStart(new RuntimeException("Fail to start discovery"));
                }
            } finally {
                mIsStarting.set(false);
            }
        }
    }

    @Override
    public void stopScanning() {
        if (mAdapter == null) {
            return;
        }

        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }
    }

    @Override
    public boolean isScanning() {
        return mAdapter != null && mAdapter.isDiscovering();
    }

    @Override
    public void destroy() {
        // Try stop scanning first.
        stopScanning();
        stopListening();

        mContext = null;
        mAdapter = null;
        super.destroy();
    }

    private void startListening() {
        if (mContext == null) {
            return;
        }

        if (mIsListening.compareAndSet(false, true)) {
            registerReceiver();
        }
    }

    private void stopListening() {
        if (mContext == null) {
            return;
        }

        if (mIsListening.compareAndSet(true, false)) {
            unregisterReceiver();
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        mContext.registerReceiver(mReceiver, filter);
    }

    private void unregisterReceiver() {
        mContext.unregisterReceiver(mReceiver);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent != null ? intent.getAction() : null;
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                notifyScanStarted();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                notifyScanFinished();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    notifyFoundDevice(device);
                }
            }
        }
    };
}
