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
import com.alieeen.smartchair.util.UIUpdater;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

//https://github.com/akexorcist/Android-BluetoothSPPLibrary
public class MainActivity extends MaterialNavigationDrawer {


    private static final String B_TAG = "BLUETOOTH";
    public static final String DEVICE_NAME = "HC-06";

    //region bluetooth variables
    private BluetoothSPP bluetooth;
    private String bluetoothAddress = "";

    UIUpdater mUIUpdater;

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

        setBackPattern(MaterialNavigationDrawer.BACKPATTERN_BACK_TO_FIRST);

        setupBluetooth();

    }

    private void checkConnection() {
        if (!isConnected) {
            fragmentMain.printBluetoothError("Connection lost");
        }
        if (isConnected) {
            //coloca o conectado para falso. A principio, em 10segundos virá
            //outro sinal de handshake que levará ele para true. Se isso não acontecer,
            //cai no if ali de cima e mostra erro
            isConnected = false;
        }
    }

    public void onDestroy() {
        if (mUIUpdater != null) {
            mUIUpdater.stopUpdates();
        }

    }

    public void bluetoothSend(String message) {

        if (bluetooth != null) {
            bluetooth.send(message, true);
            Log.i("BLUETOOTH SEND", "sent: " + message);
            App.getInstance().addSentMessage(message);
            fragmentTestOut.updateData();
        } else {
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
            isConnected = false;
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
        if (isConnected == true) {
            return;
        }
        boolean connected = bluetooth.autoConnect(DEVICE_NAME);
        if (connected) {
            isConnected = true;
            fragmentMain.printBluetoothInfo();
            mUIUpdater = new UIUpdater(new Runnable() {
                @Override
                public void run() {
                    checkConnection();
                }
            });
            mUIUpdater.startUpdates();
        } else {
            fragmentMain.printBluetoothError("Pair " + DEVICE_NAME + " to your device");
        }
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
                    , "Buttonetooth is not available"
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

        Log.i("Bluetooth", "received: " + message);

        if (message.contains("Hello")) {
            Log.i(B_TAG, "handshake sucessfull");
            isConnected = true;
            fragmentMain.printBluetoothInfo();
        }
        else if (message.contains("Sonar")) {
            fragmentMain.showWarning();
            Log.i("BT", "Warning");

        }
        else if (message.contains("Move")) {
            fragmentMain.hideWarning();
        }
        else if (message.contains("V:")) {
            String[] separated = message.split(":");
            float velocity = Float.parseFloat(separated[1]);
            fragmentStatistics.addEntryVelocity(velocity);
        }
        else if (message.contains("A:")) {
            String[] separeted = message.split(":");
            float angle = Float.parseFloat(separeted[1]);
            fragmentStatistics.addEntryAngle(angle);
        }

    }


    //region bluetooth
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
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

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }
    //endregion
}
