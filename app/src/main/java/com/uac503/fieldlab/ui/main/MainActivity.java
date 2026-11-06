package com.uac503.fieldlab.ui.main;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.ui.home.HomeFragment;
import com.uac503.fieldlab.ui.profile.ProfileFragment;
import com.uac503.fieldlab.ui.scenarios.ScenariosFragment;
import com.uac503.fieldlab.ui.simulator.SimuladorFragment;
import com.uac503.fieldlab.ui.theory.TheoryFragment;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }


        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();


            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_simulator) {
                selectedFragment = new SimuladorFragment();
            } else if (itemId == R.id.nav_scenarios) {
                selectedFragment = new ScenariosFragment();
            } else if (itemId == R.id.nav_theory) {
                selectedFragment = new TheoryFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new ProfileFragment();
            }


            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });
    }
}