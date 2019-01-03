package com.daybreakr.codelabs.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.daybreakr.codelabs.bt.model.scan.BluetoothScanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BluetoothMainActivity extends AppCompatActivity {
    private static final String TAG = "Codelabs-BT";

    private BluetoothScanner mScanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bluetooth_main);

        findViewById(R.id.btn_bt_scan).setOnClickListener(this::startScanning);
    }

    private void startScanning(View view) {
        if (mScanner == null) {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
            if (adapter == null) {
                // Device not support Bluetooth.
                return;
            }
            mScanner = BluetoothScanner.getScanner(this, adapter);
            mScanner.setCallback(mScanCallback);
        }

        mScanner.startScanning();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mScanner != null) {
            mScanner.stopScanning();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mScanner != null) {
            mScanner.setCallback(null);
            mScanner = null;
        }
    }

    private final BluetoothScanner.ScanCallback mScanCallback = new BluetoothScanner.ScanCallback() {

        @Override
        public void onStartScanning() {
            Log.i(TAG, "Start Bluetooth scanning...");
        }

        @Override
        public void onFailToStartScanning(int error) {
            Log.i(TAG, "Fail to start scanning, error=" + error);
        }

        @Override
        public void onFoundDevice(BluetoothDevice device) {
            Log.i(TAG, "Found Bluetooth device: " + printDevice(device));
        }
    };

    private static String printDevice(BluetoothDevice device) {
        return "{"
                + "\n name: " + device.getName()
                + "\n MAC: " + device.getAddress()
                + "\n state: " + device.getBondState()
                + "\n type: " + device.getType()
                + "}";
    }
}
