package com.alieeen.smartchair;

import android.os.Bundle;


import com.alieeen.smartchair.fragments.main.AboutUsFragment;
import com.alieeen.smartchair.fragments.main.HelpFragment;
import com.alieeen.smartchair.fragments.main.MainFragment;
import com.alieeen.smartchair.fragments.main.SettingsFragment;
import com.alieeen.smartchair.fragments.main.StatisticsFragment;
import com.alieeen.smartchair.fragments.main.TestOutFragment;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

//https://github.com/akexorcist/Android-BluetoothSPPLibrary
public class MainActivity extends MaterialNavigationDrawer {


    private static final String TAG = "MAIN";

    @Override
    public void init(Bundle savedInstanceState) {

        this.disableLearningPattern();
        this.setDrawerHeaderImage(R.drawable.drawer_header);

        this.addSection(newSection("Home", new MainFragment()));
        this.addSection(newSection("Statistics", new StatisticsFragment()));
        this.addSection(newSection("Test Out", new TestOutFragment()));

        this.addBottomSection(newSection("HELP", new HelpFragment()));
        this.addBottomSection(newSection("SETTINGS", new SettingsFragment()));
        this.addBottomSection(newSection("ABOUT", new AboutUsFragment()));

    }

}
