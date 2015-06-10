package com.alieeen.smartchair.fragments.walktrough;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alieeen.smartchair.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by alinekborges on 20/05/15.
 */
@EFragment(R.layout.walktrought04)
public class FragmentWalkthrough04 extends Fragment implements RecognitionListener {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitiesFragment.
     */

    @ViewById
    ImageView img_ahead;
    @ViewById
    ImageView img_back;
    @ViewById
    ImageView img_right;
    @ViewById
    ImageView img_left;
    @ViewById
    ImageView img_break;

    //region speech recognition variables
    private static final String LOG_TAG = "VOZ";

    /* Named searches allow to quickly reconfigure the decoder */
    private static final String KWS_SEARCH = "wakeup";
    private static final String MENU_SEARCH = "digits";

    private SpeechRecognizer recognizer;

    public static FragmentWalkthrough04_ newInstance() {
        FragmentWalkthrough04_ fragment = new FragmentWalkthrough04_();
        return fragment;
    }

    public FragmentWalkthrough04() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @AfterViews
    public void initComponents() {
        setUpSpeechRegonition();
    }

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

    /*
    public void setTextResult(TextView tv) {
        txt_back.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        txt_front.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        txt_right.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        txt_left.setTextColor(getResources().getColor(android.R.color.primary_text_light));
        txt_stop.setTextColor(getResources().getColor(android.R.color.primary_text_light));

        tv.setTextColor(getResources().getColor(R.color.accent));
    }*/

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

        Log.i(LOG_TAG, "result = " + text);

        switch (text) {
            case "front ":
                img_ahead.setImageResource(R.drawable.check_on);
                //setTextResult(txt_front);
                break;
            case "ahead ":
                img_ahead.setImageResource(R.drawable.check_on);
                //setTextResult(txt_front);
                break;
            case "backward ":
                img_back.setImageResource(R.drawable.check_on);
                //setTextResult(txt_back);
                break;
            case "reverse ":
                img_back.setImageResource(R.drawable.check_on);
                //setTextResult(txt_back);
                break;
            case "right ":
                img_right.setImageResource(R.drawable.check_on);
                //setTextResult(txt_right);
                break;
            case "left ":
                img_left.setImageResource(R.drawable.check_on);
                //setTextResult(txt_left);
                break;
            case "break ":
                img_break.setImageResource(R.drawable.check_on);
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

        //String caption = getResources().getString(captions.get(searchName));
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
}