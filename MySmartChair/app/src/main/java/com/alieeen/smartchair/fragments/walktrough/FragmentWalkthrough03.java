package com.alieeen.smartchair.fragments.walktrough;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alieeen.smartchair.R;

/**
 * Created by alinekborges on 20/05/15.
 */
public class FragmentWalkthrough03 extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitiesFragment.
     */

    public static FragmentWalkthrough03 newInstance() {
        FragmentWalkthrough03 fragment = new FragmentWalkthrough03();
        return fragment;
    }

    public FragmentWalkthrough03() {
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
        View v = inflater.inflate(R.layout.walktrought03, container, false);

        return v;
    }
}
