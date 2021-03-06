
package com.alieeen.smartchair.fragments.menu;

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

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
public class MainFragment extends BluetoothFragment implements
        RecognitionListener {

    private static final String KWS_FOWARD = "foward";

    private static final String LOG_TAG = "MainFragment";
    private static final String KEYPHRASE = "oh mighty computer";
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitiesFragment.
     */
    private View v;
    private Button btnConnect;
    private TextView txtListening;
    private Button btnSend;

    private SpeechRecognizer recognizer;
    private HashMap<String, Integer> captions;

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

    @Background
    public void setUpSpeechRegonition() {
        try {
            Assets assets = new Assets(getActivity());
            File assetDir = assets.syncAssets();
            Log.i(LOG_TAG, assetDir.getAbsolutePath());
            setupRecognizer(assetDir);
        } catch (Exception e) {
            Log.i(LOG_TAG, "ERRO");
        }
    }

    public void initComponents() {
        btnConnect = (Button) v.findViewById(R.id.btn_devices_choose);
        btnSend = (Button) v.findViewById(R.id.btnSend);
        txtListening = (TextView) v.findViewById(R.id.txt_listening);

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

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();
        if (text.equals(KWS_FOWARD)) {
            Log.i(LOG_TAG, "FOWARD");
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        txtListening.setText("");
        if (hypothesis != null) {
            String text = hypothesis.getHypstr();
            makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onTimeout() {

    }

    private void setupRecognizer(File assetsDir) throws Exception {
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
        recognizer.addKeyphraseSearch(KWS_FOWARD, KEYPHRASE);

        //startRecognition();

    }

    @UiThread
    public void startRecognition() {
        Log.i(LOG_TAG, "start listening");
        txtListening.setText("Listening.......");
        recognizer.startListening(KWS_FOWARD);
    }
}
