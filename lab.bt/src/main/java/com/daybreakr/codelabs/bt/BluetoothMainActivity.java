package com.daybreakr.codelabs.bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.daybreakr.codelabs.bt.model.pair.BluetoothPairingManager;
import com.daybreakr.codelabs.bt.model.scan.BluetoothScanner;
import com.daybreakr.codelabs.bt.model.scan.BluetoothScannerFactory;
import com.daybreakr.codelabs.bt.view.BluetoothDeviceAdapter;

import java.util.Collections;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class BluetoothMainActivity extends AppCompatActivity {
    private static final String TAG = "Codelabs-BT";

    private SwipeRefreshLayout mRefreshLayout;

    private BluetoothDeviceAdapter mPairedDevicesAdapter;
    private BluetoothDeviceAdapter mAvailableDevicesAdapter;

    private BluetoothScanner mBluetoothScanner;
    private BluetoothPairingManager mBluePairingManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            toast("Device not support Bluetooth.");
            return;
        }

        setContentView(R.layout.activity_bluetooth_main);
        setupView();

        mBluetoothScanner = BluetoothScannerFactory.newScanner(adapter, this, this);
        mBluetoothScanner.setScanCallback(mScanCallback);

        mBluePairingManager = new BluetoothPairingManager(adapter);

        refreshDevices();
    }

    private void setupView() {
        mRefreshLayout = findViewById(R.id.refresh_layout);
        mRefreshLayout.setOnRefreshListener(this::startScanning);

        mPairedDevicesAdapter = new BluetoothDeviceAdapter();
        mPairedDevicesAdapter.setEmptyView(findViewById(R.id.paired_devices_empty_view));
        RecyclerView pairedDevices = findViewById(R.id.paired_devices_list);
        pairedDevices.setAdapter(mPairedDevicesAdapter);

        mAvailableDevicesAdapter = new BluetoothDeviceAdapter();
        mAvailableDevicesAdapter.setEmptyView(findViewById(R.id.available_devices_empty_view));
        mAvailableDevicesAdapter.setOnItemClickListener(this::startPairing);
        RecyclerView availableDevices = findViewById(R.id.available_devices_list);
        availableDevices.setAdapter(mAvailableDevicesAdapter);
    }

    private void refreshDevices() {
        mPairedDevicesAdapter.setDevices(mBluePairingManager.getPairedDevices());
        mAvailableDevicesAdapter.setDevices(Collections.emptySet());
    }

    private void startScanning() {
        refreshDevices();

        if (mBluetoothScanner != null) {
            mBluetoothScanner.startScanning();
        }
    }

    private BluetoothScanner.ScanCallback mScanCallback = new BluetoothScanner.ScanCallback() {
        @Override
        public void onScanStarted() {
            toast("Start scanning...");
        }

        @Override
        public void onScanFailedToStart(Throwable error) {
            toast("Fail to start scanning, error: " + error);
        }

        @Override
        public void onFoundDevice(BluetoothDevice device) {
            Log.i(TAG, "Found Bluetooth device: " + printDevice(device));

            mAvailableDevicesAdapter.addDevice(device);
        }

        @Override
        public void onScanFinished() {
            toast("Scan finished.");

            mRefreshLayout.setRefreshing(false);
        }
    };


    private void startPairing(BluetoothDevice device) {
        toast("Start pairing " + device.getName());

        mBluetoothScanner.stopScanning();
    }

    private void toast(String message) {
        Log.i(message, message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private static String printDevice(BluetoothDevice device) {
        return "{"
                + "\n name: " + device.getName()
                + "\n MAC: " + device.getAddress()
                + "\n state: " + device.getBondState()
                + "\n type: " + device.getType()
                + "}";
    }
}
