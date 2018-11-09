package ca.cmpt276.greengoblins.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ca.cmpt276.greengoblins.emission.MainActivity;
import ca.cmpt276.greengoblins.emission.R;
import ca.cmpt276.greengoblins.foodsurveydata.Convertor;
import ca.cmpt276.greengoblins.foodsurveydata.User;
import ca.cmpt276.greengoblins.foodsurveydata.UserAdapter;

import static java.lang.Integer.parseInt;

public class PledgeListFragment extends Fragment {

    private DatabaseReference mUsersDatabase;

    private MainActivity mMainActivity;
    private TextView mPledgeListView;
    private Button mMakePledgeButton;
    private Spinner mFilterDropdown;
    private EditText mSearchBox;
    private String mFilterOptions[];

    private RecyclerView mRecyclerView;
    private UserAdapter mAdapter;
    private List<User> mUserList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pledge_list, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(R.string.toolbar_pledge_list);
        mMainActivity = (MainActivity) getActivity();

        mPledgeListView = (TextView) view.findViewById(R.id.pledge_list_text);
        mPledgeListView.setText(R.string.pledge_list_text);
        mMakePledgeButton = (Button) view.findViewById(R.id.button_make_pledge);
        mFilterDropdown = (Spinner) view.findViewById(R.id.dropdown_filter);
        mSearchBox = (EditText) view.findViewById(R.id.textview_search_box);

        mUserList = new ArrayList<User>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.pledge_list_view) ;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mMainActivity.getBaseContext()));
        mAdapter = new UserAdapter(mMainActivity.getBaseContext(), mUserList);
        mRecyclerView.setAdapter(mAdapter);

        // Only using City option so no need for others
        mFilterOptions = getResources().getStringArray(R.array.pledge_list_filters);

        ArrayAdapter<String> dropdownFilterAdapter = new ArrayAdapter<String>(mMainActivity.getBaseContext(),
                R.layout.support_simple_spinner_dropdown_item, mFilterOptions);
        dropdownFilterAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        mFilterDropdown.setAdapter(dropdownFilterAdapter);
        mFilterDropdown.setPrompt(getResources().getString(R.string.filter_prompt));

        displayPledges();

        mMakePledgeButton.setOnClickListener(new View.OnClickListener() {
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
    }

    public void displayPledges() {
        mUsersDatabase = FirebaseDatabase.getInstance().getReference("Users");
        queryData(mUsersDatabase);

        mSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterList();
            }
        });
        mSearchBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                filterList();
                return false;
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
                mUserList.clear();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User userInfo = (User) snapshot.getValue(User.class);
                    if(userInfo.isShowNamePublic()) {
                        userInfo.setFirstName(userInfo.getFirstName().substring(0, 1).toUpperCase() + userInfo.getFirstName().substring(1));
                        userInfo.setLastName(userInfo.getFirstName().substring(0, 1).toUpperCase() + userInfo.getLastName().substring(1));
                        if(!userInfo.getCity().isEmpty()) {
                            userInfo.setCity(userInfo.getCity().substring(0, 1).toUpperCase() + userInfo.getCity().substring(1));
                        }
                        userInfo.setLastName(String.valueOf(userInfo.getLastName().charAt(0)));
                        mUserList.add(userInfo);
                    }
                    else {
                        userInfo.setFirstName( getString(R.string.anonymous_name) );
                        userInfo.setLastName("");
                        if(!userInfo.getCity().isEmpty()) {
                            userInfo.setCity(userInfo.getCity().substring(0, 1).toUpperCase() + userInfo.getCity().substring(1));
                        }
                        mUserList.add(userInfo);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void filterList(){
        String searchText = mSearchBox.getText().toString().trim().toLowerCase();
        String searchFilter = String.valueOf(mFilterDropdown.getSelectedItem());

        if( searchText.isEmpty() ) return;

        if( searchFilter.equals(mFilterOptions[0]) ){
            queryData(mUsersDatabase);
        } else if ( searchFilter.equals(mFilterOptions[1]) ){
            String filterField = getResources().getString(R.string.first_name_option);
            queryData(mUsersDatabase.orderByChild(filterField).equalTo(searchText));
        } else if ( searchFilter.equals(mFilterOptions[2]) ){
            String filterField = getResources().getString(R.string.city_option);
            queryData(mUsersDatabase.orderByChild(filterField).equalTo(searchText));
        } else if ( searchFilter.equals(mFilterOptions[3]) ){
            Double pledgeValue = Double.parseDouble(searchText);
            String filterField = getResources().getString(R.string.pledge_amount_option);
            queryData(mUsersDatabase.orderByChild(filterField).equalTo(pledgeValue));
        }
    }
}