
package com.alieeen.smartchair.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alieeen.smartchair.R;
import com.alieeen.smartchair.bluetooth.BluetoothSPP;
import com.alieeen.smartchair.bluetooth.BluetoothState;
import com.alieeen.smartchair.bluetooth.DeviceList;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static android.widget.Toast.makeText;
import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by alinekborges on 18/05/15.
 */
@EFragment
public class MainFragment extends Fragment implements
        RecognitionListener {

    //************************************************
    //Cool views to show what we need to show
    private View v;
    private Button btnConnect;
    private TextView txtListening;
    private Button btnSend;
    private TextView txtCaptionText;
    private TextView txtResultText;

    //************************************************

    //region speech recognition variables
    private static final String LOG_TAG = "MainFragment";

    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "menu";

    private static final String COMMAND_FRONT = "front";
    private static final String COMMAND_BACK = "back";
    private static final String COMMAND_LEFT = "left";
    private static final String COMMAND_RIGHT = "right";
    private static final String COMMAND_STOP = "stop";

    /* Keyword we are looking for to activate menu */
    private static final String KEYPHRASE = "command";

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;
    //endregion

    //region bluetooth variables
    private BluetoothSPP bluetooth;
    //endregion

    //region fragment lifecycle
    //****************************************
    public static MainFragment newInstance() {
        MainFragment_ fragment = new MainFragment_();
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        initComponents();
        setUpSpeechRegonition();

        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        recognizer.cancel();
        recognizer.shutdown();
        bluetooth.stopService();


    }

    public void initComponents() {
        btnConnect = (Button) v.findViewById(R.id.btn_devices_choose);
        btnSend = (Button) v.findViewById(R.id.btnSend);
        txtListening = (TextView) v.findViewById(R.id.txt_listening);
        txtCaptionText = (TextView) v.findViewById(R.id.caption_text);
        txtResultText = (TextView) v.findViewById(R.id.result_text);

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
        });


        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("Text", true);
            }
        });
    }

    //endregion

    //region Speech Recognition Listener

    @Background
    public void setUpSpeechRegonition() {
        txtCaptionText.setText("Preparing the recognizer");
        try {
            Assets assets = new Assets(getActivity());
            File assetDir = assets.syncAssets();
            Log.i(LOG_TAG, assetDir.getAbsolutePath());
            setupRecognizer(assetDir);
            switchSearch(KWS_SEARCH);
        } catch (Exception e) {
            Log.i(LOG_TAG, "ERRO");
            txtCaptionText.setText("Failed to init recognizer " + e.getMessage());
        }

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


        if (text.equals(KEYPHRASE))
            switchSearch(MENU_SEARCH);
        else if (text.equals(COMMAND_FRONT)) {
            txtResultText.setText(COMMAND_FRONT);
            Log.i(LOG_TAG, COMMAND_FRONT);
            switchSearch(MENU_SEARCH);
        }
        else if (text.equals(COMMAND_BACK)) {
            Log.i(LOG_TAG, COMMAND_BACK);
            switchSearch(MENU_SEARCH);
            //txtResultText.setText(COMMAND_BACK);
        }
        else if (text.equals(COMMAND_LEFT)) {
            Log.i(LOG_TAG, COMMAND_LEFT);
            switchSearch(MENU_SEARCH);
            txtResultText.setText(COMMAND_LEFT);
        }
        else if (text.equals(COMMAND_RIGHT)) {
            Log.i(LOG_TAG, COMMAND_RIGHT);
            switchSearch(MENU_SEARCH);
            txtResultText.setText(COMMAND_RIGHT);
        }
        else if (text.equals(COMMAND_STOP)) {
            Log.i(LOG_TAG, COMMAND_STOP);
            switchSearch(MENU_SEARCH);
            txtResultText.setText(COMMAND_STOP);
        }

        else {
            txtResultText.setText("text hear: " + text);
            switchSearch(MENU_SEARCH);
        }


    }

    /**
     * This callback is called when we stop the recognizer.
     */
    @Override
    public void onResult(Hypothesis hypothesis) {
        txtResultText.setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
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
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);

        String caption = getResources().getString(captions.get(searchName));
        txtCaptionText.setText(caption);
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
                .setKeywordThreshold(1e-45f)

                        // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)

                .getRecognizer();
        recognizer.addListener(this);

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

        // Create grammar-based search for digit recognition
        /*File digitsGrammar = new File(assetsDir, "digits.gram");
        recognizer.addGrammarSearch(DIGITS_SEARCH, digitsGrammar);*/

        // Create language model search
        /*
        File languageModel = new File(assetsDir, "weather.dmp");
        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);

        // Phonetic search
        File phoneticModel = new File(assetsDir, "en-phone.dmp");
        recognizer.addAllphoneSearch(PHONE_SEARCH, phoneticModel);*/
    }

    @Override
    public void onError(Exception error) {
        txtCaptionText.setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }

    //endregion


    //region bluetooth
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
            btnConnect.setText("CONNECTED+01!");
        } else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getActivity()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                //finish();
            }
        }
    }

    private void setupBluetooth() {
        bluetooth = new BluetoothSPP(getActivity());

        if(!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(getActivity()
                    , "Bluetooth is not available"
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
                Log.i("Check", "New Connection - " + name + " - " + address);
            }

            public void onAutoConnectionStarted() {
                Log.i("Check", "Auto menu_connection started");
            }
        });
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
        /*
        Button btnSend = (Button)findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                bt.send("Text", true);
            }
        });*/

        bluetooth.autoConnect("HC-05");
    }
    //endregion

}
