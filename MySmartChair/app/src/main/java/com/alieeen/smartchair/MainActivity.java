package com.alieeen.smartchair;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.alieeen.smartchair.bluetooth.BluetoothSPP;
import com.alieeen.smartchair.bluetooth.BluetoothState;
import com.alieeen.smartchair.fragments.main.AboutUsFragment;
import com.alieeen.smartchair.fragments.main.HelpFragment;

import com.alieeen.smartchair.fragments.main.MainFragment_;
import com.alieeen.smartchair.fragments.main.SettingsFragment;
import com.alieeen.smartchair.fragments.main.StatisticsFragment;

import com.alieeen.smartchair.fragments.main.TestOutFragment_;

import com.alieeen.smartchair.util.Directions;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;

//https://github.com/akexorcist/Android-BluetoothSPPLibrary
public class MainActivity extends MaterialNavigationDrawer {

    private static final String TAG = "MAIN";
    private static final String B_TAG = "BLUETOOTH";
    private final String DEVICE_NAME = "HC-06";

    //region bluetooth variables
    private BluetoothSPP bluetooth;
    private String bluetoothAddress;


    //holding instance of our fragments
    private MainFragment_ fragmentMain;
    private StatisticsFragment fragmentStatistics;
    private TestOutFragment_ fragmentTestOut;
    private boolean isConnected;

    //endregion

    @Override
    public void init(Bundle savedInstanceState) {


        this.isConnected = false;
        fragmentMain = new MainFragment_();
        setupBluetooth();
        //this.bluetooth = fragmentMain.getBluetooth();

        fragmentStatistics = new StatisticsFragment();
        fragmentTestOut = new TestOutFragment_();



        this.disableLearningPattern();
        this.setDrawerHeaderImage(R.drawable.drawer_header);

        this.addSection(newSection("Home", fragmentMain));
        this.addSection(newSection("Statistics", fragmentStatistics));
        this.addSection(newSection("Test Out", fragmentTestOut));

        this.addBottomSection(newSection("HELP", new HelpFragment()));
        this.addBottomSection(newSection("SETTINGS", new SettingsFragment()));
        this.addBottomSection(newSection("ABOUT", new AboutUsFragment()));

        //this.setDefaultSectionLoaded(4);

    }


    public void bluetoothSend(String message) {


        if (bluetooth != null && isConnected) {
            bluetooth.send(message, true);
            Log.i("BLUETOOTH SEND", "sent: " + message);
            App.getInstance().addSentMessage(message);
            fragmentTestOut.updateData();
        } else {
            sendHandShake();
            fragmentMain.printBluetoothError("Not Connected to "+ DEVICE_NAME);
            Log.i("BLUETOOTH", "Bluetooth not really connected");
        }

    }

    public void bluetoothTestSend(String message) {
        bluetooth.send(message, true);
        Log.i("BLUETOOTH SEND", "sent: " + message);
        App.getInstance().addSentMessage(message);
        fragmentTestOut.updateData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (bluetooth == null) {
            return;
        }
        if(!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if(!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
                bluetoothAutoConnect();

            }
        }
    }

    private void bluetoothAutoConnect() {
        Log.i("Check", "start auto connection");
        bluetooth.autoConnect(DEVICE_NAME);
        if (!isConnected) {
            sendHandShake();
        }
    }

    private void sendHandShake() {
        bluetooth.send("SmartChairAndroid", true);
    }

    public void bluetoothSend(Directions direction) {
        bluetoothSend(direction.getString());
    }

    public void bluetoothTestSend(Directions direction) {
        bluetoothTestSend(direction.getString());
    }

    private void setupBluetooth() {
        bluetooth = new BluetoothSPP(this);
        App.getInstance().setBluetooth(bluetooth);

        if (bluetooth == null) {
            return;
        }

        if(!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(this
                    , "BluButtonetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            //finish();
        }

        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(MainActivity.this
                        , "Connected to " + name
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                Toast.makeText(MainActivity.this
                        , "Connection lost"
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Log.i("Check", "Unable to connect");

            }
        });
        bluetooth.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
                bluetoothAddress = address;
                Log.i("Check", "New Connection - " + name + " - " + address);

            }

            public void onAutoConnectionStarted() {
                Log.i("Check", "Auto menu_connection started");
            }
        });

        bluetooth.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            public void onServiceStateChanged(int state) {
                if (state == BluetoothState.STATE_CONNECTED) {
                    //printBluetoothInfo();
                }
                // Do something when successfully connected
                else if (state == BluetoothState.STATE_CONNECTING) {

                }
                // Do something while connecting
                else if (state == BluetoothState.STATE_LISTEN) {

                }
                // Do something when device is waiting for connection
                else if (state == BluetoothState.STATE_NONE) {
                    //printBluetoothError("");
                }
                // Do something when device don't have any connection
            }
        });
        //

        bluetooth.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] data, String message) {
                receiveData(message);
            }
        });


    }

    private void receiveData(String message) {
        App.getInstance().addReceivedMessage(message);
        fragmentTestOut.updateData();

        if (message.contains("OK")) {
            Log.i(B_TAG, "handshake sucessfull");
            isConnected = true;
            fragmentMain.printBluetoothInfo();
        }

    }


    //region bluetooth
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
            //btnConnect.setText("CONNECTED+01!");
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(this
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();

            }
        }
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
