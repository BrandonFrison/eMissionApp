package ca.cmpt276.greengoblins.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import ca.cmpt276.greengoblins.emission.R;


public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Settings");

    }
    public static boolean delete(File file) {
        boolean result = true;
        if (file.exists()) {
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    result &= delete(child);
                }
                result &= file.delete(); // Delete empty directory.
            } else if (file.isFile()) {
                result &= file.delete();
            }
            return result;
        } else {
            return false;
        }
    }
}
