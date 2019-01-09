package com.daybreakr.codelabs.bt.model.admin;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class BluetoothAdminResultActivity extends Activity {

    private static final String EXTRA_REQ = "req";

    private static final int REQ_UNKNOWN = 0;

    private static final int REQ_ENABLE_BLUETOOTH = 0x1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final int req = intent != null ? intent.getIntExtra(EXTRA_REQ, REQ_UNKNOWN) : REQ_UNKNOWN;
        switch (req) {
            case REQ_ENABLE_BLUETOOTH:
                enableBluetooth();
                break;
            default:
                finish();
        }
    }

    private void enableBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(intent, REQ_ENABLE_BLUETOOTH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_ENABLE_BLUETOOTH:
                handleEnableBluetooth(resultCode, data);
                break;
            default:
                break;
        }

        finish();
    }

    private void handleEnableBluetooth(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

        } else {

        }
    }
}
