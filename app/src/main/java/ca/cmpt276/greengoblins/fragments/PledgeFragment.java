package ca.cmpt276.greengoblins.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.greengoblins.Subfragment.AllCo2ePledgeAmount;
import ca.cmpt276.greengoblins.Subfragment.MyPledge;
import ca.cmpt276.greengoblins.Subfragment.OtherPledges;
import ca.cmpt276.greengoblins.emission.R;

public class PledgeFragment extends Fragment implements View.OnClickListener {


    private AllCo2ePledgeAmount allCo2ePledgeAmount_fragment;
    private OtherPledges otherPledges_fragment;
    private MyPledge myPledge_fragment;

    private View ALLCo2ePledgeAmount_Layout;
    private View OtherPledges_Layout;
    private View MyPledge_Layout;

    private ImageView ALLCo2ePledgeAmount_Image;
    private ImageView OtherPledges_Image;
    private ImageView MyPledge_Image;

    private TextView ALLCo2ePledgeAmount_Text;
    private TextView OtherPledges_Text;
    private TextView MyPledge_Text;


    private FragmentManager fragmentManager;
    View view = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pledge,null);


        //Initialize layout elements
        initViews();
        fragmentManager = getFragmentManager();
        //The 0th tab is selected when starting for the first time.
        setTabSelection(0);
        return view;
    }

    private void setTabSelection(int index) {

        // Clear the last selected state before each selection
        clearSelection();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //Hide all the Fragments first to prevent multiple Fragments from appearing on the interface.
        hideFragments(transaction);
        switch (index) {
            case 0:
                //Change the control's image and text color when the message tab is clicked
                //ALLCo2ePledgeAmount_Image.setImageResource(R.drawable.message_selected);
                ALLCo2ePledgeAmount_Text.setTextColor(Color.WHITE);
                if (allCo2ePledgeAmount_fragment == null) {
                    //If the MessageFragment is empty, create one and add it to the interface.
                    allCo2ePledgeAmount_fragment = new AllCo2ePledgeAmount();
                    transaction.add(R.id.Pledge_content, allCo2ePledgeAmount_fragment);
                } else {
                    //If the MessageFragment is not empty, display it directly
                    transaction.show(allCo2ePledgeAmount_fragment);
                }
                break;
            case 1:
                //OtherPledges_Image.setImageResource(R.drawable.contacts_selected);
                OtherPledges_Text.setTextColor(Color.WHITE);
                if (otherPledges_fragment == null) {
                    otherPledges_fragment = new OtherPledges();
                    transaction.add(R.id.Pledge_content, otherPledges_fragment);
                } else {
                    transaction.show(otherPledges_fragment);
                }
                break;
            case 2:
                //MyPledge_Image.setImageResource(R.drawable.news_selected);
                MyPledge_Text.setTextColor(Color.WHITE);
                if (myPledge_fragment == null) {
                    myPledge_fragment = new MyPledge();
                    transaction.add(R.id.Pledge_content, myPledge_fragment);
                } else {
                    transaction.show(myPledge_fragment);
                }
                break;
        }
        transaction.commit();

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (allCo2ePledgeAmount_fragment != null) {
            transaction.hide(allCo2ePledgeAmount_fragment);
        }
        if (otherPledges_fragment != null) {
            transaction.hide(otherPledges_fragment);
        }
        if (myPledge_fragment != null) {
            transaction.hide(myPledge_fragment);
        }
    }

    private void clearSelection() {
        //ALLCo2ePledgeAmount_Image.setImageResource(R.drawable.contacts_unselected);
        ALLCo2ePledgeAmount_Text.setTextColor(Color.parseColor("#82858b"));
        //OtherPledges_Image.setImageResource(R.drawable.contacts_unselected);
        OtherPledges_Text.setTextColor(Color.parseColor("#82858b"));
        //MyPledge_Layout.setImageResource(R.drawable.news_unselected);
        MyPledge_Text.setTextColor(Color.parseColor("#82858b"));
    }

    private void initViews() {
        ALLCo2ePledgeAmount_Layout = view.findViewById(R.id.ALLCo2ePledgeAmount_layout);
        OtherPledges_Layout = view.findViewById(R.id.OtherPledges_layout);
        MyPledge_Layout = view.findViewById(R.id.MyPledge_layout);


        ALLCo2ePledgeAmount_Image = (ImageView) view.findViewById(R.id.ALLCo2ePledgeAmount_image);
        OtherPledges_Image = (ImageView) view.findViewById(R.id.OtherPledges_image);
        MyPledge_Image = (ImageView) view.findViewById(R.id.MyPledge_image);

        ALLCo2ePledgeAmount_Text = (TextView) view.findViewById(R.id.ALLCo2ePledgeAmount_text);
        OtherPledges_Text = (TextView) view.findViewById(R.id.OtherPledges_text);
        MyPledge_Text = (TextView) view.findViewById(R.id.MyPledge_text);

        ALLCo2ePledgeAmount_Layout.setOnClickListener(this);
        OtherPledges_Layout.setOnClickListener(this);
        MyPledge_Layout.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ALLCo2ePledgeAmount_layout:
                // When the message tab is clicked, the first tab is selected
                setTabSelection(0);
                break;
            case R.id.OtherPledges_layout:
                // When the message tab is clicked, the second tab is selected
                setTabSelection(1);
                break;
            case R.id.MyPledge_layout:
                // When the message tab is clicked, the third tab is selected
                setTabSelection(2);
                break;
            default:
                break;
        }

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }
}






