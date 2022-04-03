package com.example.chatapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.utiltites.Constants;
import com.example.chatapp.utiltites.PreferanceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import android.util.Base64;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private PreferanceManager preferanceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferanceManager = new PreferanceManager(getApplicationContext());
        loadUserDetails();
        getToken();
        setListener();
    }

    private void setListener() {

        binding.ImageSignOut.setOnClickListener(v -> signOut());
        binding.fabNewChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), UsersActivity.class)));
    }

    private void loadUserDetails() {
        binding.textName.setText(preferanceManager.getString(Constants.KEY_NAME));
        byte[] bytes = Base64.decode(preferanceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.imageProfile.setImageBitmap(bitmap);
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(
                preferanceManager.getString(Constants.KEY_USER_ID)
        );
        documentReference.update(Constants.KEY_FOR_TOKEN, token).addOnSuccessListener(unused -> showToast("Log In Successful"))
                .addOnFailureListener(e -> showToast("Unable to update Toast"));
    }

    private void signOut() {
        showToast("Signing out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferanceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FOR_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferanceManager.clear();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable to sign out"));
    }

}