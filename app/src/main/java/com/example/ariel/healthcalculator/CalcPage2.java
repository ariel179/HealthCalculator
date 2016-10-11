package com.example.ariel.healthcalculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Switch;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by Ariel on 9/7/2016.
 */
public class CalcPage2 extends AppCompatActivity {

    RatingBar drinkRating;
    Switch drinkSwitch, smokerSwitch, weightSwitch;
    Button calculateButton;
    File file;
    final String BUCKET_NAME = "";
    final String BUCKET_KEY = "AKIAJEITGXHL54KCKSBQ";
    final String IDENTITY_POOL_ID = "us-west-2:4750d69f-b522-406d-84b2-c9aba53d91ef";
    String dataToWriteToFile;

    float sleepRatingScore,dietRatingScore, exerciseRatingScore, socialRatingScore, stressRatingScore,drinksRatingScore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_page2);

        sleepRatingScore = getIntent().getFloatExtra("sleep",0);
        dietRatingScore = getIntent().getFloatExtra("diet",0);
        exerciseRatingScore = getIntent().getFloatExtra("exercise",0);
        socialRatingScore = getIntent().getFloatExtra("social",0);
        stressRatingScore = getIntent().getFloatExtra("stress",0);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),    /* get the context for the application */
                IDENTITY_POOL_ID,    /* Identity Pool ID */
                Regions.US_WEST_2           /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );

        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        final TransferUtility transferUtility = new TransferUtility(s3, this);
        file = new File("text.txt");


        drinkRating = (RatingBar) findViewById(R.id.drinkRating);
        drinkSwitch = (Switch) findViewById(R.id.drinkSwitch);
        smokerSwitch = (Switch) findViewById(R.id.smokerSwitch);
        weightSwitch = (Switch) findViewById(R.id.weightSwitch);
        calculateButton = (Button) findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drinksRatingScore = drinkRating.getRating();
                dataToWriteToFile = sleepRatingScore + ", " + dietRatingScore + ", " + exerciseRatingScore+ ", " +socialRatingScore+ ", " +stressRatingScore+ ", " +drinksRatingScore;
                try {
                    BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"utf-8"));
                    writer.write(dataToWriteToFile);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                uploadToBucket(transferUtility,file);


                onBackPressed();
            }
        });


    }

    public void uploadToBucket(final TransferUtility transferUtility, final File file){
        TransferObserver observer = transferUtility.upload(
                BUCKET_NAME,     /* The bucket to upload to */
                BUCKET_KEY,    /* The key for the uploaded object */
                file        /* The file where the data to upload exists */
        );

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                if (bytesCurrent==bytesTotal){
                    downloadFromBucket(transferUtility,file);
                }
            }

            @Override
            public void onError(int id, Exception ex) {

            }
        });
    }

    public void downloadFromBucket(TransferUtility transferUtility, File file){
        TransferObserver transferObserver = transferUtility.download(BUCKET_NAME, BUCKET_KEY,file);
        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                // do something
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                //Display percentage transfered to user
            }

            @Override
            public void onError(int id, Exception ex) {
                // do something
            }

        });
    }

}
