package com.example.inbar.suitcaseapp;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


public class MainActivity extends AppCompatActivity {

    DynamoDBMapper dynamoDBMapper;
    String email;
    UsersDO user = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        this.email = account.getEmail();
        Log.d("MainActivity", "email is: " + email);

        // Instantiate a AmazonDynamoDBMapperClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();

        // look for current user in DB
        readUser(this.email);

        // if it is the user first time- goto register activity
        if (user == null) {
            Intent intent = new Intent(MainActivity.this, Register.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else { // if this user is already register - go to tracking activity
            Intent intent = new Intent(MainActivity.this, Tracking.class);
            intent.putExtra("email", email);
            startActivity(intent);
        }

    }

    public void readUser(final String email) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                user = dynamoDBMapper.load(
                        UsersDO.class,
                        email,
                        null);
            }
        });

        t.start();
        try {
            t.join();
        } catch (Exception e) {
            Log.d("MainActivity: ", "look for user: " + email + " on DB- fail: " + e);
        }
    }
}
