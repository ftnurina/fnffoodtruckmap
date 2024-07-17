package dev.lab.fnffoodtruckmap;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AccountFragment extends Fragment {

    private TextView displayNameTextView;
    private TextView emailTextView;
    private ImageView profileImageView;
    private GoogleSignInClient mGoogleSignInClient;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        displayNameTextView = view.findViewById(R.id.display_name_textview);
        emailTextView = view.findViewById(R.id.email_textview);
        profileImageView = view.findViewById(R.id.profile_image);
        Button signOutButton = view.findViewById(R.id.sign_out_button);

        // Set up sign out button click listener
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        // Initialize GoogleSignInClient
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Check if user is already signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        updateUI(account);
    }

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // Sign-out successful, navigate to SignInActivity
                        Intent intent = new Intent(getActivity(), SignInActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            displayNameTextView.setText(account.getDisplayName());
            emailTextView.setText(account.getEmail());

            // Load profile image using Glide
            if (account.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(account.getPhotoUrl())
                        .into(profileImageView);
            } else {
                profileImageView.setImageResource(R.drawable.ic_profile); // Fallback image
            }
        } else {
            displayNameTextView.setText("");
            emailTextView.setText("");
            profileImageView.setImageResource(R.drawable.ic_profile); // Fallback image
        }
    }
}
