package com.example.inbar.suitcaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {

    DynamoDBMapper dynamoDBMapper;
    String email;
    UsersDO userItem;
    Button registerBtn;
    EditText name;
    EditText address;
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        setContentView(R.layout.activity_register);

        // Instantiate a AmazonDynamoDBMapperClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();

        this.email = getIntent().getExtras().getString("email");

        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = true;

                name = (EditText) findViewById(R.id.username);
                address = (EditText) findViewById(R.id.address);
                phone = (EditText) findViewById(R.id.phone);

                String nameStr = name.getText().toString();
                String addressStr = address.getText().toString();
                String phoneStr = phone.getText().toString();

                check = checkFields(nameStr, addressStr, phoneStr, email);
                if (check) {
                    Double phoneDbl = Double.parseDouble(phoneStr);
                    createUser(nameStr, addressStr, phoneDbl, email);
                }
            }
        });

    }

    public boolean checkFields (final String userName, String address, String phone, final String email) {

        if (userName.matches("") || address.matches("") || phone.matches("")) {
            Log.d("Register", "missing information- at least one filed is blank!");
            Toast.makeText(this,
                    "missing information- please fill all fields above!",
                    Toast.LENGTH_LONG).show();
            return false;
        }

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                userItem = dynamoDBMapper.load(UsersDO.class, email, null);
                // Item read
            }
        });

        t.start();
        try {
            t.join();
        } catch (Exception e){
            Log.d("join exception: ", ""+ e);
        }

        if(userItem != null) {
            Log.d("Register", "Username already exist");
            Toast.makeText(this, "Username already exist!", Toast.LENGTH_LONG);
            return false;
        }

        return true;
    }

    public void createUser(String userName, String address, double phone, final String email) {

        userItem = new UsersDO();

        userItem.setName(userName);
        userItem.setAddress(address);
        userItem.setPhone(phone);
        userItem.setEmail(email);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    dynamoDBMapper.save(userItem);
                    Toast.makeText(Register.this, "REGISTERED", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Register.this, Tracking.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                } catch (Exception e){
                    Toast.makeText(Register.this, "ERROR-TRY AGAIN", Toast.LENGTH_LONG).show();
                    Log.d("Register", "failed to register: "+ e);
                }
                // Item saved
            }
        }).start();
    }

}
