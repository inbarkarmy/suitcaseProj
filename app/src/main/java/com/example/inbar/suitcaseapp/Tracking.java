package com.example.inbar.suitcaseapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Tracking extends AppCompatActivity {

    TableLayout trackingTable;
    String email;
    DynamoDBMapper dynamoDBMapper;
    SuitcaseDO suitcase;
    UsersDO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AWSMobileClient.getInstance().initialize(this, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d("YourMainActivity", "AWSMobileClient is instantiated and you are connected to AWS!");
            }
        }).execute();

        setContentView(R.layout.activity_tracking);

        // Instantiate a AmazonDynamoDBMapperClient
        AmazonDynamoDBClient dynamoDBClient = new AmazonDynamoDBClient(AWSMobileClient.getInstance().getCredentialsProvider());
        this.dynamoDBMapper = DynamoDBMapper.builder()
                .dynamoDBClient(dynamoDBClient)
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .build();

        this.email = getIntent().getExtras().getString("email");
        trackingTable = (TableLayout) findViewById(R.id.trackingTable);
        TableRow tr_head = new TableRow(this);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView label_date = new TextView(this);
        label_date.setText("DATE");
        label_date.setTextColor(Color.WHITE);
        label_date.setTypeface(Typeface.SANS_SERIF);
        label_date.setTextSize(20);
        label_date.setPadding(5, 5, 100, 5);
        tr_head.addView(label_date);

        TextView label_airport = new TextView(this);
        label_airport.setText("LOCATION"); // set the text for the header
        label_airport.setTextColor(Color.WHITE); // set the color
        label_airport.setTypeface(Typeface.SANS_SERIF);
        label_airport.setTextSize(20);
        label_airport.setPadding(5, 5, 65, 5); // set the padding (if required)
        tr_head.addView(label_airport); // add the column to the table row here

        trackingTable.addView(tr_head, new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        readUser(email);

        if (user.getSuitcaseID() != null) {
            Log.d("Tracking", "Found suitcase of current user");
            String suitcaseID = user.getSuitcaseID();
            readSuitcase(suitcaseID);
            Map<String, String> locationDateMap = suitcase.getLocationDateMap();

            Comparator<String> comparator = new Comparator<String>() {
                @Override
                public int compare(String d1, String d2) {
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date1 = null;
                    Date date2 = null;
                    try {
                        date1 = formatter.parse(d1);
                        date2 = formatter.parse(d2);
                    } catch (Exception e) {
                        Log.d("Tracking", "compare date failed: " + e);
                    }

                    return date1.compareTo(date2);

                }
            };

            SortedSet<String> sortDates = new TreeSet<>(comparator);
            sortDates.addAll(locationDateMap.keySet());

            int count = 0;
            for (String date: sortDates) {
                  String location = locationDateMap.get(date);
                  TableRow tr = new TableRow(this);
                  if(count%2!=0) tr.setBackgroundColor(Color.GRAY);
                  tr.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                //Create two columns to add as table data
                // Create a TextView to add date
                TextView labelDATE = new TextView(this);
                labelDATE.setText(date);
                labelDATE.setPadding(2, 0, 100, 0);
                labelDATE.setTextColor(Color.WHITE);
                tr.addView(labelDATE);
                TextView labelLocation = new TextView(this);
                labelLocation.setText(location);
                labelLocation.setTextColor(Color.WHITE);
                tr.addView(labelLocation);

                trackingTable.addView(tr, new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                count++;
            }
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
            Log.d("Tracking: ", "look for user: " + email + " on DB- fail: " + e);
        }
    }

    public void readSuitcase(final String suitcaseId) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                suitcase = dynamoDBMapper.load(
                        SuitcaseDO.class,
                        suitcaseId,
                        null);
            }
        });

        t.start();
        try {
            t.join();
        } catch (Exception e) {
            Log.d("Tracking: ", "look for suitcase: " + suitcaseId + " on DB- fail: " + e);
        }
    }
}
