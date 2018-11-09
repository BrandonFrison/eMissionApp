package ca.cmpt276.greengoblins.fragments;

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

import ca.cmpt276.greengoblins.emission.R;



public class ProvidedAvatar extends Fragment {
    private ImageView avater1;
    private ImageView avater2;
    private ImageView avater3;
    private ImageView avater4;
    private ImageView avater5;
    private ImageView avater6;
    private ImageView avater7;
    private ImageView avater8;
    private ImageView avater9;
    View view = null;
    private int id_avatar = 0;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.provided_avater, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        avater1 = view.findViewById(R.id.imageView1);
        avater1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_avatar = 1;
                finish();

            }
        });
        avater2 = view.findViewById(R.id.imageView2);
        avater2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_avatar = 2;
                finish();

            }
        });
        avater3 = view.findViewById(R.id.imageView3);
        avater3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_avatar = 3;
                finish();

            }
        });
        avater4 = view.findViewById(R.id.imageView4);
        avater4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_avatar = 4;
                finish();

            }
        });
        avater5 = view.findViewById(R.id.imageView5);
        avater5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_avatar = 5;
                finish();

            }
        });
        avater6 = view.findViewById(R.id.imageView6);
        avater6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_avatar = 6;
                finish();

            }
        });
        avater7 = view.findViewById(R.id.imageView7);
        avater7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_avatar = 7;
                finish();

            }
        });
        avater8 = view.findViewById(R.id.imageView8);
        avater8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_avatar = 8;
                finish();

            }
        });
        avater9 = view.findViewById(R.id.imageView9);
        avater9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id_avatar = 9;
                finish();

            }
        });



    }

    private void finish() {
        Bundle bundle = new Bundle();
        bundle.putInt("id_avatar", id_avatar );

        Fragment fragment = new MakePledgeFragment();
        fragment.setArguments( bundle );

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.frame_activity_content, fragment );
        fragmentTransaction.commit();
    }
}
