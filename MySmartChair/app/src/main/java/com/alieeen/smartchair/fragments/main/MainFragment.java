
package com.alieeen.smartchair.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alieeen.smartchair.R;
import com.alieeen.smartchair.bluetooth.BluetoothSPP;
import com.alieeen.smartchair.bluetooth.BluetoothState;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by alinekborges on 18/05/15.
 */
@EFragment
public class MainFragment extends Fragment implements
        RecognitionListener {

    /**
     * 1 - front
     * 2 - right
     * 3 - left
     * 4 - back
     * 5 - stop
     */

    private final String DEVICE_NAME = "HC-05";

                                                                                                                                                                                                                                                        //************************************************
    //Cool views to show what we need to show
    private View v;
    private TextView txt_front;
    private TextView txt_back;
    private TextView txt_right;
    private TextView txt_left;
    private TextView txt_stop;

    private TextView txt_bluetooth_devicename;
    private TextView txt_bluetooth_address;
    private TextView txt_bluetooth_status;

    private ImageView img_microfone;

    private AnimationDrawable microfone_animation;

    //************************************************

    //region speech recognition variables
    private static final String LOG_TAG = "VOZ";

    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "digits";

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;
    private String bluetoothAddress = "";
    //endregion

    public BluetoothSPP getBluetooth() {
        return bluetooth;
    }

    //region bluetooth variables
    private BluetoothSPP bluetooth;
    //endregion

    //region fragment lifecycle
    //****************************************
    public static MainFragment_ newInstance() {
        MainFragment_ fragment = new MainFragment_();
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Log.i(LOG_TAG, "Main Fragment")
        captions = new HashMap<String, Integer>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_main, container, false);
        //Initialize components
        setupBluetooth();
        initComponents();
        setUpSpeechRegonition();

        return v;
    }

    @Override




    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
        //bluetooth.stopService();


    }

    public void initComponents() {
        txt_front = (TextView) v.findViewById(R.id.txt_front);
        txt_back = (TextView) v.findViewById(R.id.txt_back);
        txt_right = (TextView) v.findViewById(R.id.txt_right);
        txt_left = (TextView) v.findViewById(R.id.txt_left);
        txt_stop = (TextView) v.findViewById(R.id.txt_break);

        txt_bluetooth_address = (TextView) v.findViewById(R.id.txt_bluetooth_address);
        txt_bluetooth_devicename = (TextView) v.findViewById(R.id.txt_bluetooth_devicename);
        txt_bluetooth_status = (TextView) v.findViewById(R.id.txt_bluetooth_status);

        img_microfone = (ImageView) v.findViewById(R.id.img_microfone);
        img_microfone.setBackgroundResource(R.drawable.microphone_animation);
        microfone_animation = (AnimationDrawable) img_microfone.getBackground();
        microfone_animation.start();

        /*
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    //Log.i("MAIN", "disconecting...");
                    btnConnect.setText("CONNECTED!");
                    //bt.disconnect();
                } else {
                    Intent intent = new Intent(getActivity(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });*/



    }

    //endregion

    //region Speech Recognition Listener

    @Background
    public void setUpSpeechRegonition() {
        //txtCaptionText.setText("Preparing the recognizer");
        try {
            Assets assets = new Assets(getActivity());
            File assetDir = assets.syncAssets();
            Log.i(LOG_TAG, assetDir.getAbsolutePath());
            setupRecognizer(assetDir);
            switchSearch(KWS_SEARCH);
            //String caption = getResources().getString(captions.get(MENU_SEARCH));
            //txtCaptionText.setText(caption);
        } catch (Exception e) {
            Log.i(LOG_TAG, "ERRO");
            //txtCaptionText.setText("Failed to init recognizer " + e.getMessage());
        }

    }

    public void setTextResult(TextView tv) {
        txt_back.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        txt_front.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        txt_right.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        txt_left.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        txt_stop.setTextColor(getResources().getColor(android.R.color.primary_text_light));

        tv.setTextColor(getResources().getColor(R.color.accent));
    }

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        bluetooth.send(text, true);
        Log.i(LOG_TAG, "result = " + text);

        switch (text) {
            case "front ":
                setTextResult(txt_front);
                break;
            case "ahead ":
                setTextResult(txt_front);
                break;
            case "backward ":
                setTextResult(txt_back);
                break;
            case "reverse ":
                setTextResult(txt_back);
                break;
            case "right ":
                setTextResult(txt_right);
                break;
            case "left ":
                setTextResult(txt_left);
                break;

        }

        switchSearch(MENU_SEARCH);

    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        //txtResultText.setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
    }

    /**
     * We stop recognizer here to get a final result
     */
    @Override
    public void onEndOfSpeech() {
        switchSearch(MENU_SEARCH);
    }


    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        //if (searchName.equals(KWS_SEARCH))
        //    recognizer.startListening(searchName);
        //else {
            recognizer.startListening(MENU_SEARCH, 10000);
           // Log.i(LOG_TAG,"search: " + searchName);
        //}

        String caption = getResources().getString(captions.get(searchName));
        //txtCaptionText.setText(caption);
    }

    private void setupRecognizer(File assetsDir) throws IOException {
        // The recognizer can be configured to perform multiple searches
        // of different kind and switch between them

        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                        // To disable logging of raw audio comment out this call (takes a lot of space on the device)
                .setRawLogDir(assetsDir)

                        // Threshold to tune for keyphrase to balance between false alarms and misses
                .setKeywordThreshold(1e-20f)

                        // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)

                .getRecognizer();
        recognizer.addListener(this);

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "digits.gram");
        recognizer.addKeywordSearch(MENU_SEARCH, menuGrammar);


    }

    @Override
    public void onError(Exception error) {
        //txtCaptionText.setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
        //switchSearch(KWS_SEARCH);
    }

    //endregion

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
                Toast.makeText(getActivity()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void setupBluetooth() {
        bluetooth = new BluetoothSPP(getActivity());

        if(!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(getActivity()
                    , "BluButtonetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            //finish();
        }

        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
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
                    printBluetoothInfo();
                }
                // Do something when successfully connected
                else if (state == BluetoothState.STATE_CONNECTING) {

                }
                // Do something while connecting
                else if (state == BluetoothState.STATE_LISTEN) {

                }
                // Do something when device is waiting for connection
                else if (state == BluetoothState.STATE_NONE) {
                    printBluetoothError("");
                }
                // Do something when device don't have any connection
            }
        });
    }

    public void printBluetoothInfo() {

        txt_bluetooth_address.setText(this.bluetoothAddress);
        txt_bluetooth_devicename.setText(bluetooth.getConnectedDeviceName());
        txt_bluetooth_status.setText("Bluetooth:");

        txt_bluetooth_status.setTextColor(getResources().getColor(R.color.text_light));
        txt_bluetooth_devicename.setTextColor(getResources().getColor(R.color.text_light));
        txt_bluetooth_address.setTextColor(getResources().getColor(R.color.text_light));


    }

    public void printBluetoothError(String error) {
        txt_bluetooth_address.setText("");
        txt_bluetooth_devicename.setText(error);
        txt_bluetooth_status.setText("Bluetooth connection error");
        txt_bluetooth_status.setTextColor(getResources().getColor(R.color.text_error));
    }


    public void onStart() {
        super.onStart();
        if(!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if(!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
                setup();

            }
        }
    }

    public void setup() {


        /*Button btnSend = (Button) v.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.i(LOG_TAG,"send test");
                bluetooth.send("Test", true);
            }*/
               bluetooth.autoConnect(DEVICE_NAME);

    }
    //endregion

}
