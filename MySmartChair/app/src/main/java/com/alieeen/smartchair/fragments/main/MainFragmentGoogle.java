package com.alieeen.smartchair.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alieeen.smartchair.R;
import com.alieeen.smartchair.bluetooth.BluetoothState;
import com.alieeen.smartchair.bluetooth.DeviceList;

import java.util.HashMap;

/**
 * Created by alinekborges on 18/05/15.
 */
public class MainFragmentGoogle extends BluetoothFragment {

    private static final String LOG_TAG = "MainFragment";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitiesFragment.
     */
    private View v;
    private Button btnConnect;
    //private TextView txtListening;
    private Button btnSend;

    private HashMap<String, Integer> captions;

    public static MainFragmentGoogle newInstance() {
        MainFragmentGoogle fragment = new MainFragmentGoogle();
        return fragment;
    }

    public MainFragmentGoogle() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_main, container, false);

        initComponents();
        setUpSpeechRegonition();


        return v;
    }

    public void setUpSpeechRegonition() {

    }

    public void initComponents() {
        btnConnect = (Button) v.findViewById(R.id.btn_devices_choose);
        btnSend = (Button) v.findViewById(R.id.btnSend);
        //txtListening = (TextView) v.findViewById(R.id.txt_listening);

        /*
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    Log.i("MAIN", "disconecting...");
                    btnConnect.setText("CONNECTED!");
                    //bt.disconnect();
                } else {
                    Intent intent = new Intent(getActivity(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });*/

        /*
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("Text", true);
            }
        });*/
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bt.connect(data);
            btnConnect.setText("CONNECTED+01!");
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
            } else {
                Toast.makeText(getActivity()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }

}


