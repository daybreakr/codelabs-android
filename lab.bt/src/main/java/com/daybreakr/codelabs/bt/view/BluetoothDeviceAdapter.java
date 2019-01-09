package com.daybreakr.codelabs.bt.view;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daybreakr.codelabs.bt.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.recyclerview.widget.RecyclerView;

public class BluetoothDeviceAdapter
        extends RecyclerView.Adapter<BluetoothDeviceAdapter.ViewHolder>
        implements View.OnClickListener {
    private List<BluetoothDevice> mDevices = new ArrayList<>();

    private View mEmptyView;

    private Consumer<BluetoothDevice> mOnItemClickListener;

    public void setDevices(Set<BluetoothDevice> devices) {
        mDevices.clear();
        if (devices != null && !devices.isEmpty()) {
            mDevices.addAll(devices);
        }
        notifyDataSetChanged();

        refreshEmptyView();
    }

    public void addDevice(BluetoothDevice device) {
        mDevices.add(device);

        notifyItemInserted(getItemCount());

        refreshEmptyView();
    }

    public void clearDevices() {
        setDevices(null);
    }

    public void setOnItemClickListener(Consumer<BluetoothDevice> listener) {
        mOnItemClickListener = listener;
    }

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    private void refreshEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(mDevices.isEmpty() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDevices.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.bluetooth_device_item, parent, false);
        itemView.setOnClickListener(this);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BluetoothDevice device = mDevices.get(position);
        holder.itemView.setTag(position);
        holder.deviceName.setText(getName(device));
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        BluetoothDevice device = mDevices.get(position);
        if (mOnItemClickListener != null) {
            mOnItemClickListener.accept(device);
        }
    }

    private String getName(BluetoothDevice device) {
        if (device == null) {
            return null;
        }
        String name = device.getName();
        if (TextUtils.isEmpty(name)) {
            name = device.getAddress();
        }
        return name;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName;
        TextView deviceState;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.device_name);
            deviceState = itemView.findViewById(R.id.device_state);
        }
    }
}
