package com.example.ariel.healthcalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
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

    final String BUCKET_NAME = "cc-bucket1-2016";
    final String USER_SCORE_BUCKET_NAME = "cc-bucket2-2016";
    final String AVERAGE_BUCKET_NAME = "cc-bucket3-2016";
    final String IDENTITY_POOL_ID = "us-west-2:fc102c26-5209-4634-a446-ac873b0ef305";
    String dataToWriteToFile;
    int alcohol, smoker, overweight = 0;
    String yourScore;
    static String firstName;
    EditText firstNameEditText;
    private boolean hasDownloaded = false;
    private int count = 0;
    Context context;


    float sleepRatingScore,dietRatingScore, exerciseRatingScore, socialRatingScore, stressRatingScore,drinksRatingScore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc_page2);
        this.context = this;
        sleepRatingScore = getIntent().getFloatExtra("sleep",0);
        dietRatingScore = getIntent().getFloatExtra("diet",0);
        exerciseRatingScore = getIntent().getFloatExtra("exercise",0);
        socialRatingScore = getIntent().getFloatExtra("social",0);
        stressRatingScore = getIntent().getFloatExtra("stress",0);

        firstNameEditText = (EditText) findViewById(R.id.firstName);

        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),    /* get the context for the application */
                IDENTITY_POOL_ID,    /* Identity Pool ID */
                Regions.US_WEST_2           /* Region for your identity pool--US_EAST_1 or EU_WEST_1*/
        );

        AmazonS3 s3 = new AmazonS3Client(credentialsProvider);

        final TransferUtility transferUtility = new TransferUtility(s3, this);



        drinkRating = (RatingBar) findViewById(R.id.drinkRating);
        drinkSwitch = (Switch) findViewById(R.id.drinkSwitch);
        smokerSwitch = (Switch) findViewById(R.id.smokerSwitch);
        weightSwitch = (Switch) findViewById(R.id.weightSwitch);

        drinkSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    alcohol = 7;
                }else{
                    alcohol =0;
                }
            }
        });
        smokerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    smoker = 7;
                }else{
                    smoker =0;
                }
            }
        });
        weightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b==true){
                    overweight = 7;
                }else{
                    overweight =0;
                }
            }
        });


        calculateButton = (Button) findViewById(R.id.calculateButton);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drinksRatingScore = drinkRating.getRating();
                dataToWriteToFile = sleepRatingScore + ", " + dietRatingScore + ", " + exerciseRatingScore+ ", " +socialRatingScore+ ", " +stressRatingScore+ ", " +drinksRatingScore+ ", " +alcohol+ ", " +smoker+ ", " +overweight;
                firstName = firstNameEditText.getText().toString();
                Singleton.getInstance().setFirstName(firstName);
                file = new File(getFilesDir(),firstName+".txt");
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"utf-8"));
                    writer.write(dataToWriteToFile);
                    writer.flush();
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
        TransferObserver observer = transferUtility.upload(BUCKET_NAME,"cloudtest",file);

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                if (bytesCurrent==bytesTotal){
                    downloadFromBucket(transferUtility);
                }
            }

            @Override
            public void onError(int id, Exception ex) {

            }
        });
    }

    public void downloadFromBucket(final TransferUtility transferUtility){

        final File resultFile = new File(getFilesDir(),"result.txt");
        try {
            resultFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TransferObserver transferObserver = transferUtility.download(USER_SCORE_BUCKET_NAME, "calcedcloudtest",resultFile);

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                // do something
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                count++;

                if (bytesCurrent == bytesTotal) {
                    BufferedReader br = null;
                    try {
                        if (!resultFile.exists()){
                            resultFile.createNewFile();
                        }
                        br = new BufferedReader(new FileReader(resultFile));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        String line = br.readLine();
                        yourScore = line;

                        if (!hasDownloaded && count>1) {
                            Singleton.getInstance().setFirstName(firstName);
                            Singleton.getInstance().setYourScore(yourScore);
                            br.close();

                            if (yourScore!=null){
                                hasDownloaded=true;
                            }
                            ListView listView = Singleton.getInstance().getListView();
                            Singleton.getInstance().getScores().add(firstName + " : " + yourScore.substring(0,4));
                            ArrayAdapter adapter = new ArrayAdapter(listView.getContext(), R.layout.leaderboard_list_item, Singleton.getInstance().getScores());
                            listView.setAdapter(adapter);
                            Intent intent = new Intent(getBaseContext(),ScoreActivity.class);
                            intent.putExtra("score",yourScore.substring(0,4));
                            context.startActivity(intent);
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onError(int id, Exception ex) {
                // do something
            }

        });
    }

}
