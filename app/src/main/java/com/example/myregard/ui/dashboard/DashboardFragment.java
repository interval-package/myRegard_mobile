package com.example.myregard.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myregard.AudioRecordActivity;
import com.example.myregard.databinding.FragmentDashboardBinding;
import com.example.myregard.editBookActivity;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    TextView textDashboard;
    Button dashboard_button;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        textDashboard = binding.textDashboard;
        dashboard_button = binding.dashboardButton;

        dashboard_button.setOnClickListener(Button_clicked);

//        textDashboard.setText("textDashboard");
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textDashboard::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private View.OnClickListener Button_clicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getContext(), editBookActivity.class));
        }
    };
}