package ca.cmpt276.greengoblins.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.User;
import ca.cmpt276.greengoblins.foodsurveydata.UserAdapter;

import static java.lang.Integer.parseInt;

public class PledgeListFragment extends Fragment {

    private DatabaseReference mUsersDatabase;

    private MainActivity mMainActivity;
    private FloatingActionButton mActionButton;
    private Spinner mFilterDropdown;
    private EditText mSearchBox;
    private String mFilterOptions[];

    private RecyclerView mRecyclerView;
    private UserAdapter mUserAdapter;
    private ArrayList<User> mFilteredUserList;
    private ArrayList<User> mDatabaseUserList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pledge_list, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();
        mMainActivity.setTitle(R.string.toolbar_pledge_list);

        mActionButton = mMainActivity.getActionButton();
        mActionButton.setImageResource(R.drawable.baseline_add_24);
        mActionButton.show();
        mMainActivity.showActionButtonLabel(R.string.fablabel_add_pledge);

        mFilterDropdown = (Spinner) view.findViewById(R.id.dropdown_filter);
        mSearchBox = (EditText) view.findViewById(R.id.textview_search_box);

        mDatabaseUserList = new ArrayList<User>();
        mFilteredUserList = new ArrayList<User>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.pledge_list_view) ;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity.getBaseContext()));
        mUserAdapter = new UserAdapter(mMainActivity.getBaseContext(), mFilteredUserList);
        mRecyclerView.setAdapter(mUserAdapter);

        mFilterOptions = getResources().getStringArray(R.array.pledge_list_filters);

        ArrayAdapter<String> dropdownFilterAdapter = new ArrayAdapter<String>(mMainActivity.getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, mFilterOptions);
        dropdownFilterAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mFilterDropdown.setAdapter(dropdownFilterAdapter);
        mFilterDropdown.setPrompt(getResources().getString(R.string.filter_prompt));

        mUsersDatabase = FirebaseDatabase.getInstance().getReference("Users");
        queryData(mUsersDatabase);

        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !mMainActivity.checkUserLogin() ) {
                    mMainActivity.popupLogin();
                } else {
                    Fragment newFragment = new MakePledgeFragment();
                    mMainActivity.startFragment(newFragment, true, false);
                }
            }
        });

        mSearchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if( keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP ){ //enter button handling (physical and soft keyboard)
                    filterList();
                }
                return false;
            }
        });
        mSearchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterList();
            }
        });

        mFilterDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void queryData(Query query) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mDatabaseUserList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userInfo = (User) snapshot.getValue(User.class);
                    if(userInfo.isShowNamePublic()) {
                        /*userInfo.setFirstName(userInfo.getFirstName().substring(0, 1).toUpperCase() + userInfo.getFirstName().substring(1));
                        userInfo.setLastName(userInfo.getFirstName().substring(0, 1).toUpperCase() + userInfo.getLastName().substring(1));
                        if(!userInfo.getCity().isEmpty()) {
                            userInfo.setCity(userInfo.getCity().substring(0, 1).toUpperCase() + userInfo.getCity().substring(1));
                        }
                        userInfo.setLastName(String.valueOf(userInfo.getLastName().charAt(0)));*/
                        mDatabaseUserList.add(userInfo);
                    }
                    else {
                        // userInfo.setFirstName( getString(R.string.anonymous_name) ); RESULTS IN BUG. NEEDS TO BE FIGURED OUT
                        userInfo.setFirstName( "Anonymous");
                        userInfo.setLastName("");
                        /*if(!userInfo.getCity().isEmpty()) {
                            userInfo.setCity(userInfo.getCity().substring(0, 1).toUpperCase() + userInfo.getCity().substring(1));
                        }*/
                        mDatabaseUserList.add(userInfo);
                    }
                }
                clearFilters();
                //mUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void clearFilters(){
        mFilteredUserList.clear();
        for ( User listedDatabaseMeal : mDatabaseUserList) {
            mFilteredUserList.add( listedDatabaseMeal );
        }
        mUserAdapter.notifyDataSetChanged();
    }

    //category int relates to order of spinner, aka user_list_filters in values.xml
    private void searchCategory( int category, String searchTerm ) {
        mFilteredUserList.clear();
        double pledgeAmount = 0.0;
        for ( User listedDatabaseUser : mDatabaseUserList) {
            switch(category){
                case 2: // City
                    Log.d("test", "city: "+listedDatabaseUser.getCity()+" searchterm: "+searchTerm);
                    if ( listedDatabaseUser.getCity().toLowerCase().contains( searchTerm ) ){
                        mFilteredUserList.add( listedDatabaseUser );
                    }
                    break;
                case 3: // Pledge Amount
                    final double PLEDGE_VALUE_EPSILON = 0.1;
                    try{
                        pledgeAmount = Double.parseDouble(searchTerm);
                    } catch (Exception exception){
                        exception.printStackTrace();
                    }
                    if ( Math.abs(listedDatabaseUser.getPledgeAmount() - pledgeAmount) < PLEDGE_VALUE_EPSILON ){
                        mFilteredUserList.add( listedDatabaseUser );
                    }
                    break;
                default: // No Filter or First Name
                    if ( listedDatabaseUser.getFirstName().toLowerCase().contains( searchTerm ) ){
                        mFilteredUserList.add( listedDatabaseUser );
                    }
            }
        }
        mUserAdapter.notifyDataSetChanged();
    }

    private void filterList() {
        String searchText = mSearchBox.getText().toString().trim().toLowerCase();
        String searchFilter = String.valueOf(mFilterDropdown.getSelectedItem());

        //if searchtext is empty, sort elements alphabetically by filter field
        if (searchText.isEmpty()) {
            clearFilters();
            if (searchFilter.equals(mFilterOptions[0])) { //No Filter
                Collections.sort(mFilteredUserList, User.COMPARE_BY_FIRST_NAME);
                mUserAdapter.notifyDataSetChanged();
            } else if (searchFilter.equals(mFilterOptions[1])) { //Name
                Collections.sort(mFilteredUserList, User.COMPARE_BY_FIRST_NAME);
                mUserAdapter.notifyDataSetChanged();
            } else if (searchFilter.equals(mFilterOptions[2])) { //City
                Collections.sort(mFilteredUserList, User.COMPARE_BY_LOCATION);
                mUserAdapter.notifyDataSetChanged();
            } else if (searchFilter.equals(mFilterOptions[3])) { //Pledge
                Collections.sort(mFilteredUserList, User.COMPARE_BY_PLEDGE_VALUE);
                mUserAdapter.notifyDataSetChanged();
            }
            //if search text not empty, only show elements matching input text
        } else {
            if (searchFilter.equals(mFilterOptions[0])) { //No Filter
                searchCategory(0, searchText);
            } else if (searchFilter.equals(mFilterOptions[1])) { //Name
                searchCategory(1, searchText);
            } else if (searchFilter.equals(mFilterOptions[2])) { //City
                searchCategory(2, searchText);
            } else if (searchFilter.equals(mFilterOptions[3])) { //Pledge
                searchCategory(3, searchText);
            }
        }
    }
}