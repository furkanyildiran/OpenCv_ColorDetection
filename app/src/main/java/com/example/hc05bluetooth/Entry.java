package com.example.hc05bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Set;

public class Entry extends AppCompatActivity {
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    Button pairedBtn, toggleBtn;
    ListView pairedDeviceListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        pairedBtn = findViewById(R.id.paired_btn);
        toggleBtn = findViewById(R.id.toggle_btn);
        pairedDeviceListView = findViewById(R.id.paired_device_listView);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        toggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myBluetooth == null){
                    Toast.makeText(getApplicationContext(),"Bluetooth Bulunamadı", Toast.LENGTH_LONG).show();
                    finish();
                }
                else if(!myBluetooth.isEnabled()){
                    Intent open_bt_request = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(open_bt_request);
                }
                else if(myBluetooth.isEnabled()){
                    myBluetooth.disable();
                }
            }
        });
        pairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pairedDevicesList();
            }
        });
    }
    private void pairedDevicesList(){
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice bt : pairedDevices){
                list.add(bt.getName().toString() + "\n" + bt.getAddress().toString());
            }
        }else {
            Toast.makeText(getApplicationContext(),"Eşlenmiş cihaz Bulunamdı",Toast.LENGTH_LONG).show();
        }
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
        pairedDeviceListView.setAdapter(adapter);
        pairedDeviceListView.setOnItemClickListener(pairedDevicesListViewClickListener);
    }

    private AdapterView.OnItemClickListener pairedDevicesListViewClickListener = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String info = ((TextView)view).getText().toString();
            String address = info.substring(info.length()-17);

            Intent mainActivityIntent = new Intent(Entry.this, MainActivity.class);
            mainActivityIntent.putExtra(EXTRA_ADDRESS, address);
            startActivity(mainActivityIntent);

        }
    };
}