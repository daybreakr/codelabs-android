package com.daybreakr.codelabs.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.daybreakr.codelabs.bt.model.scan.BluetoothScanner;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BluetoothMainActivity extends AppCompatActivity {
    private static final String TAG = "Codelabs-BT";

    private BluetoothScanner mBluetoothScanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bluetooth_main);

        FloatingActionButton btnScan = findViewById(R.id.btn_bt_scan);
        btnScan.setOnClickListener(this::startScanning);

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            btnScan.setEnabled(false);
            btnScan.hide();
        }

        mBluetoothScanner = BluetoothScanner.newScanner(this, adapter, this);
        mBluetoothScanner.setCallback(mScanCallback);
    }

    private void startScanning(View view) {
        mBluetoothScanner.startScanning();
    }

    private final BluetoothScanner.ScanCallback mScanCallback = new BluetoothScanner.ScanCallback() {

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
