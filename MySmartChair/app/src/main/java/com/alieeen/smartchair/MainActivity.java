package com.alieeen.smartchair;


import android.os.Bundle;
import android.support.v4.app.Fragment;


import com.alieeen.smartchair.bluetooth.BluetoothSPP;
import com.alieeen.smartchair.fragments.main.AboutUsFragment;
import com.alieeen.smartchair.fragments.main.HelpFragment;
import com.alieeen.smartchair.fragments.main.MainFragment;
import com.alieeen.smartchair.fragments.main.MainFragment_;
import com.alieeen.smartchair.fragments.main.SettingsFragment;
import com.alieeen.smartchair.fragments.main.StatisticsFragment;
import com.alieeen.smartchair.fragments.main.TestOutFragment;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

//https://github.com/akexorcist/Android-BluetoothSPPLibrary
public class MainActivity extends MaterialNavigationDrawer {


    private static final String TAG = "MAIN";
    private final String DEVICE_NAME = "HC-05";

    //region bluetooth variables
    private BluetoothSPP bluetooth;

    private MainFragment_ fragmentMain;

    private void setupBluetooth() {
        bluetooth = new BluetoothSPP(this);
        //
    }
    //endregion

    @Override
    public void init(Bundle savedInstanceState) {

        setupBluetooth();

        fragmentMain = new MainFragment_();
        this.bluetooth = fragmentMain.getBluetooth();

        this.disableLearningPattern();
        this.setDrawerHeaderImage(R.drawable.drawer_header);

        this.addSection(newSection("Home", fragmentMain));
        this.addSection(newSection("Statistics", new StatisticsFragment()));
        this.addSection(newSection("Test Out", new TestOutFragment()));

        this.addBottomSection(newSection("HELP", new HelpFragment()));
        this.addBottomSection(newSection("SETTINGS", new SettingsFragment()));
        this.addBottomSection(newSection("ABOUT", new AboutUsFragment()));

    }

}
