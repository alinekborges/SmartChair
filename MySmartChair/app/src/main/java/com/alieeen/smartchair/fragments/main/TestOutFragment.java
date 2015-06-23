package com.alieeen.smartchair.fragments.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alieeen.smartchair.App;
import com.alieeen.smartchair.MainActivity;
import com.alieeen.smartchair.R;
import com.alieeen.smartchair.adapter.MessagesAdapter;
import com.alieeen.smartchair.util.Directions;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by alinekborges on 20/05/15.
 */
@EFragment(R.layout.testout_fragment)
public class TestOutFragment extends Fragment {

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CitiesFragment.
     */

    @ViewById
    RecyclerView recyclerView;
    private MessagesAdapter adapter;

    public static TestOutFragment_ newInstance() {
        TestOutFragment_ fragment = new TestOutFragment_();
        return fragment;
    }

    public TestOutFragment() {
        // Required empty public constructor
    }

    @Click(R.id.buttonAhead)
    public void sendCommandAhead() {
        ((MainActivity)getActivity()).bluetoothTestSend(Directions.Front);
    }

    @Click(R.id.buttonBack)
    public void sendCommandBack() {
        ((MainActivity)getActivity()).bluetoothTestSend(Directions.Back);
    }

    @Click(R.id.buttonLeft)
    public void sendCommandLeft() {
        ((MainActivity)getActivity()).bluetoothTestSend(Directions.Left);
    }

    @Click(R.id.buttonRight)
    public void sendCommandRight() {
        ((MainActivity)getActivity()).bluetoothTestSend(Directions.Right);
    }

    @Click(R.id.buttonBreak)
    public void sendCommandBreak() {
        ((MainActivity)getActivity()).bluetoothTestSend(Directions.Stop);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void updateData() {

        if (adapter != null) {
            adapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }

    }

    @AfterViews
    public void initComponents() {

        App.getInstance().addReceivedMessage("Hello");

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new MessagesAdapter(getActivity(), App.getInstance().getMessages());
        recyclerView.setAdapter(adapter);


    }


}



