package dev.lab.fnffoodtruckmap;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if (item.getItemId() == R.id.navigation_near_me) {
                    selectedFragment = new NearMeFragment();
                } else if (item.getItemId() == R.id.navigation_about) {
                    selectedFragment = new AboutFragment();
                } else if (item.getItemId() == R.id.navigation_account) {
                    selectedFragment = new AccountFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container, selectedFragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_near_me);
    }
}
