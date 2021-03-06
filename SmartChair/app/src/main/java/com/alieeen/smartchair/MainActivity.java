package com.alieeen.smartchair;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alieeen.smartchair.bluetooth.BluetoothState;
import com.alieeen.smartchair.bluetooth.DeviceList;
import com.alieeen.smartchair.fragments.menu.MainFragment;
import com.alieeen.smartchair.util.BluetoothActivity;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

//https://github.com/akexorcist/Android-BluetoothSPPLibrary
public class MainActivity extends MaterialNavigationDrawer {


    private static final String TAG = "MAIN";

    @Override
    public void init(Bundle savedInstanceState) {

        this.addSection(newSection("Home", new MainFragment()));
        //this.addSection(newSection("Câmeras", new CamerasFragment()));

    }
    /*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnConnect = (Button)findViewById(R.id.btn_devices_choose);

        if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
            btnConnect.setText("CONNECTED!");
        }

        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    Log.i(TAG,"disconecting...");
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }*/


}
