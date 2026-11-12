package com.uac503.fieldlab.ui.simulator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.uac503.fieldlab.R;

public class SimuladorFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_simulador, container, false);

        TabLayout tabLayout = root.findViewById(R.id.tab_layout_simulator);
        ViewPager2 viewPager = root.findViewById(R.id.view_pager_simulator);

        viewPager.setUserInputEnabled(false);
        viewPager.setOffscreenPageLimit(1);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return position == 0 ? new Simulator2DFragment() : new SimulatorCartesianFragment();
            }

            @Override
            public int getItemCount() {
                return 2;
            }
        });

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "⚡ Simulador 2D Avanzado" : "📐 Plano Cartesiano");
        }).attach();

        return root;
    }
}
