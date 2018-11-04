package ca.cmpt276.greengoblins.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.fragments.subfragments.PledgeStatistics;
import ca.cmpt276.greengoblins.fragments.subfragments.MakeYourOwnPledgeFragment;
import ca.cmpt276.greengoblins.fragments.subfragments.MyPledge;
import ca.cmpt276.greengoblins.fragments.subfragments.OtherPledges;

public class PledgeFragment extends Fragment implements View.OnClickListener {


    private PledgeStatistics allCo2ePledgeAmount_fragment;
    private OtherPledges PledgesList_fragment;
    private MyPledge myPledge_fragment;
    private MakeYourOwnPledgeFragment makeYourOwnPledge_Fragment_fragment;

    private View ALLCo2ePledgeAmount_Layout;
    private View PledgesList_Layout;
    private View MyPledge_Layout;

    private ImageView ALLCo2ePledgeAmount_Image;
    private ImageView PledgesList_Image;
    private ImageView MyPledge_Image;

    private TextView ALLCo2ePledgeAmount_Text;
    private TextView PledgesList_Text;
    private TextView MyPledge_Text;


    private FragmentManager fragmentManager;
    View view = null;
    boolean MyPledgeExist = false;//  user hasn't make his/her pledge yet


    TextView mUserNameField;
    TextView mPasswordField;

    MainActivity mat;


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
                    allCo2ePledgeAmount_fragment = new PledgeStatistics();
                    transaction.add(R.id.Pledge_content, allCo2ePledgeAmount_fragment);
                } else {
                    //If the MessageFragment is not empty, display it directly
                    transaction.show(allCo2ePledgeAmount_fragment);
                }
                break;
            case 1:
                //OtherPledges_Image.setImageResource(R.drawable.contacts_selected);
                PledgesList_Text.setTextColor(Color.WHITE);
                if (PledgesList_fragment == null) {
                    PledgesList_fragment = new OtherPledges();
                    transaction.add(R.id.Pledge_content, PledgesList_fragment);
                } else {
                    transaction.show(PledgesList_fragment);
                }
                break;
            case 2:
                //MyPledge_Image.setImageResource(R.drawable.news_selected);
                MyPledge_Text.setTextColor(Color.WHITE);
                if(MyPledgeExist==true) {
                    if (myPledge_fragment == null) {
                        myPledge_fragment = new MyPledge();
                        transaction.add(R.id.Pledge_content, myPledge_fragment);
                    } else {
                        transaction.show(myPledge_fragment);
                    }
                    break;
                }else{
                    if (makeYourOwnPledge_Fragment_fragment == null) {
                        makeYourOwnPledge_Fragment_fragment = new MakeYourOwnPledgeFragment();
                        transaction.add(R.id.Pledge_content, makeYourOwnPledge_Fragment_fragment);
                    } else {
                        transaction.show(makeYourOwnPledge_Fragment_fragment);
                    }
                    break;

                }
        }
        transaction.commit();

    }

    private void hideFragments(FragmentTransaction transaction) {
        if (allCo2ePledgeAmount_fragment != null) {
            transaction.hide(allCo2ePledgeAmount_fragment);
        }
        if (PledgesList_fragment != null) {
            transaction.hide(PledgesList_fragment);
        }
        if (myPledge_fragment != null) {
            transaction.hide(myPledge_fragment);
        }
        if (makeYourOwnPledge_Fragment_fragment != null) {
            transaction.hide(makeYourOwnPledge_Fragment_fragment);
        }
    }

    private void clearSelection() {
        //ALLCo2ePledgeAmount_Image.setImageResource(R.drawable.contacts_unselected);
        ALLCo2ePledgeAmount_Text.setTextColor(Color.parseColor("#82858b"));
        //PledgesList_Image.setImageResource(R.drawable.contacts_unselected);
        PledgesList_Text.setTextColor(Color.parseColor("#82858b"));
        //MyPledge_Layout.setImageResource(R.drawable.news_unselected);
        MyPledge_Text.setTextColor(Color.parseColor("#82858b"));
    }

    private void initViews() {
        ALLCo2ePledgeAmount_Layout = view.findViewById(R.id.ALLCo2ePledgeAmount_layout);
        PledgesList_Layout = view.findViewById(R.id.PledgesList_layout);
        MyPledge_Layout = view.findViewById(R.id.MyPledge_layout);


        ALLCo2ePledgeAmount_Image = (ImageView) view.findViewById(R.id.ALLCo2ePledgeAmount_image);
        PledgesList_Image = (ImageView) view.findViewById(R.id.PledgesList_image);
        MyPledge_Image = (ImageView) view.findViewById(R.id.MyPledge_image);

        ALLCo2ePledgeAmount_Text = (TextView) view.findViewById(R.id.ALLCo2ePledgeAmount_textbutton);
        PledgesList_Text = (TextView) view.findViewById(R.id.PledgesList_textbutton);
        MyPledge_Text = (TextView) view.findViewById(R.id.MyPledge_textbutton);

        ALLCo2ePledgeAmount_Layout.setOnClickListener(this);
        PledgesList_Layout.setOnClickListener(this);
        MyPledge_Layout.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ALLCo2ePledgeAmount_layout:
                // When the message tab is clicked, the first tab is selected
                setTabSelection(0);
                break;
            case R.id.PledgesList_layout:
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

        getActivity().setTitle("Sign in to eMission Pledge");

        mat = (MainActivity) getActivity();
        FloatingActionButton fab = mat.getActionButton();

       // mUserNameField = (TextView) view.findViewById(R.id.username_input_field);
       // mPasswordField = (TextView) view.findViewById(R.id.password_input_field);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mat.signIn(String.valueOf(mUserNameField.getText()), String.valueOf(mPasswordField.getText()));
                mat.checkUserLogin();
            }
        });

    }
}
