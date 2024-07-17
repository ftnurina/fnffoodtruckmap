package dev.lab.fnffoodtruckmap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        // Set the application name click listener
        view.findViewById(R.id.app_url).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://yourappwebsite.com"));
                startActivity(browserIntent);
            }
        });

        // Load logo using Glide
        Glide.with(requireContext())
                .load(R.drawable.logofnftr) // Corrected drawable reference
                .apply(new RequestOptions().centerCrop())
                .into((ImageView) view.findViewById(R.id.app_logo));

        // Load member images using Glide (repeat for each member)
        Glide.with(requireContext())
                .load(R.drawable.fnffatin) // Corrected drawable reference for member 1
                .apply(new RequestOptions().circleCrop())
                .into((ImageView) view.findViewById(R.id.member1_image));

        Glide.with(requireContext())
                .load(R.drawable.fnffareena) // Corrected drawable reference for member 2
                .apply(new RequestOptions().circleCrop())
                .into((ImageView) view.findViewById(R.id.member2_image));

        Glide.with(requireContext())
                .load(R.drawable.fnfnatrah) // Corrected drawable reference for member 3
                .apply(new RequestOptions().circleCrop())
                .into((ImageView) view.findViewById(R.id.member3_image));

        return view;
    }
}
