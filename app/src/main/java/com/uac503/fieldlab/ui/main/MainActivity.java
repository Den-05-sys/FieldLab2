package com.uac503.fieldlab.ui.main;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uac503.fieldlab.R;
import com.uac503.fieldlab.ui.about.AboutFragment;
import com.uac503.fieldlab.ui.home.HomeFragment;
import com.uac503.fieldlab.ui.profile.ProfileFragment;
import com.uac503.fieldlab.ui.scenarios.ScenariosFragment;
import com.uac503.fieldlab.ui.simulator.SimuladorFragment;
import com.uac503.fieldlab.ui.theory.TheoryFragment;


public class MainActivity extends AppCompatActivity {


    private Fragment activeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        FragmentManager fm = getSupportFragmentManager();


        if (savedInstanceState == null) {
            activeFragment = new HomeFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, activeFragment, "HOME")
                    .commit();
        } else {
            activeFragment = fm.findFragmentById(R.id.fragment_container);
        }


        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            String tag = "";


            if (itemId == R.id.nav_home) tag = "HOME";
            else if (itemId == R.id.nav_simulator) tag = "SIM";
            else if (itemId == R.id.nav_scenarios) tag = "SCEN";
            else if (itemId == R.id.nav_challenges) tag = "CHALLENGES";
            else if (itemId == R.id.nav_theory) tag = "THEORY";
            else if (itemId == R.id.nav_about) tag = "ABOUT";


            if (tag.isEmpty()) return false;


            switchFragment(tag);
            return true;
        });
    }


    public void switchFragment(String tag) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment targetFragment = fm.findFragmentByTag(tag);
        FragmentTransaction transaction = fm.beginTransaction();


        if (activeFragment != null && activeFragment != targetFragment) {
            transaction.hide(activeFragment);
        }


        if (targetFragment == null) {
            targetFragment = createFragmentByTag(tag);
            transaction.add(R.id.fragment_container, targetFragment, tag);
        } else {
            transaction.show(targetFragment);
        }


        activeFragment = targetFragment;
        transaction.commitAllowingStateLoss(); // Evita fallos silenciosos de transacción
    }


    private Fragment createFragmentByTag(String tag) {
        switch (tag) {
            case "SIM": return new SimuladorFragment();
            case "SCEN": return new ScenariosFragment();
            case "THEORY": return new TheoryFragment();
            case "PROFILE": return new ProfileFragment();
            case "ABOUT": return new AboutFragment();
            case "CHALLENGES": return new com.uac503.fieldlab.ui.challenges.ChallengesFragment();
            default: return new HomeFragment();
        }
    }
}