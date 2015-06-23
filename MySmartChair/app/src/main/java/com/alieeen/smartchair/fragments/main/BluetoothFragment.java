package com.alieeen.smartchair.fragments.main;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alieeen.smartchair.bluetooth.BluetoothSPP;
import com.alieeen.smartchair.bluetooth.BluetoothState;

/**
 * Created by alinekborges on 18/05/15.
 */
public class BluetoothFragment extends Fragment {

    protected BluetoothSPP bt;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bt = new BluetoothSPP(getActivity());

        if(!bt.isBluetoothAvailable()) {
            Toast.makeText(getActivity()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            //finish();
        }

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getActivity()
                        , "Connected to " + name
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                Toast.makeText(getActivity()
                        , "Connection lost"
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Log.i("Check", "Unable to connect");
            }
        });

        bt.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
                Log.i("Check", "New Connection - " + name + " - " + address);
            }

            public void onAutoConnectionStarted() {
                Log.i("Check", "Auto menu_connection started");
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    public void onStart() {
        super.onStart();
        if(!bt.isBluetoothEnabled()) {
            bt.enable();
        } else {
            if(!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            }
        }
    }

    public void setup() {
        /*
        Button btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                bt.send("Text", true);
            }
        });*/

        bt.autoConnect("HC-06");
    }

    //app.akexorcist.bluetotohspp.library
}

