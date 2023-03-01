package com.example.myregard.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myregard.R;
import com.example.myregard.databinding.FragmentHomeBinding;
import com.example.myregard.editActivity;

import com.example.myregard.utils.net_utils.base_net_api;
import com.example.myregard.utils.net_utils.wifi_api;

public class HomeFragment extends Fragment {
    public final String TAG = "<Home page>";

    private FragmentHomeBinding binding;
    private TextView textHome;
    private Button homeButton, wifiButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textHome = binding.textHome;
        homeButton = root.findViewById(R.id.button_home);
        wifiButton = root.findViewById(R.id.button_wifi);

        homeButton.setOnClickListener(homeButton_click);

        wifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_wifi_info();
            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), textHome::setText);

        return root;
    }

    public void update_text_info(String tar){
        textHome.setText(tar);
    }

    private View.OnClickListener homeButton_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), editActivity.class));
        }
    };

    @Override
    public void onResume() {

        super.onResume();
    }

    public void th_run(){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                String info = null;
                try {
                    info = base_net_api.get_from_tar("http://192.168.43.39:8080");
                } catch (Exception e) {
                    info = "fail.";
                    e.printStackTrace();
                }
//                Toast.makeText(getContext(), "get from baidu: " + info, Toast.LENGTH_LONG).show();
//                textHome.setText(info);
                info = "get from local: " + info;
                Log.i(TAG, info);
            }
        });
        th.start();
    }

    public void get_wifi_info(){
        wifi_api api = new wifi_api(getContext());

        api.get_wifi_info();

//        Log.d(api.parse_wifi_info_str_mat(api.results));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}