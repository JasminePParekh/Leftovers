package com.example.jasmineparekh.leftoverss;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    ImageView actualImage;
    ImageView photo;
    Spinner spinner;
    TextView tv;
    Button cont;
    Button capture;
    Button addMore;
    Button confirm;
    ArrayList<String> list;
    ArrayAdapter spinnerAdapter;
    FirebaseVisionImage image;
    Bitmap bitmap;
    ArrayList<String> parameters;
    String text;
    Button reset;
    View divider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FirebaseApp.initializeApp(MainActivity.this);
        //converting jpeg image into BitMap to be converted again into the firebase specialized image
        actualImage = findViewById(R.id.actualImage);
        photo = findViewById(R.id.photo);
        spinner = findViewById(R.id.spinner);
        list = new ArrayList<>();
        cont = findViewById(R.id.ccontinue);
        capture = findViewById(R.id.capture);
        addMore = findViewById(R.id.addMore);
        confirm = findViewById(R.id.confirm);
        reset = findViewById(R.id.restart);
        divider = findViewById(R.id.divider);

        parameters = new ArrayList<>();


        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                divider.setVisibility(View.INVISIBLE);
                confirm.setVisibility(View.INVISIBLE);
                addMore.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                cont.setVisibility(View.INVISIBLE);
                capture.setVisibility(View.VISIBLE);
                photo.setVisibility(View.VISIBLE);
                photo.setImageResource(R.drawable.camera);
                actualImage.setVisibility(View.INVISIBLE);
                parameters = new ArrayList<>();
                list = new ArrayList<>();
                //spinnerAdapter.notifyDataSetChanged();

            }
        });


//        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.leftover);
//        imagee.setImageBitmap(bitmap);
//        image = FirebaseVisionImage.fromBitmap(bitmap);
//        runFirebase(image);
//        Toast.makeText(MainActivity.this,"Firebase running...",Toast.LENGTH_LONG).show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data.getExtras() != null) {
            bitmap = (Bitmap) data.getExtras().get("data");
            //bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.bellpepper);
            capture.setVisibility(View.INVISIBLE);
            photo.setVisibility(View.INVISIBLE);
            actualImage.setVisibility(View.VISIBLE);
            actualImage.setImageBitmap(bitmap);
            image = FirebaseVisionImage.fromBitmap(bitmap);
            runFirebase(image);
            Toast.makeText(MainActivity.this, "Firebase running...", Toast.LENGTH_LONG).show();
        }
    }
    public void runFirebase(FirebaseVisionImage image){
        list = new ArrayList<>();
        FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getCloudImageLabeler();

        //receives possible labels included with id and confidence intervals --> then print them to the screen
        labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
            @Override
            public void onSuccess(final List<FirebaseVisionImageLabel> labels) {
                for (FirebaseVisionImageLabel label: labels) {
                    System.out.println("GETTING RESULTS:");
                    String text = label.getText();
                    float confidence = label.getConfidence();
                    //if(confidence > .95 && confidence <.99 ){
                        list.add(text);
                        System.out.println(text);
                    //}
                    System.out.println(confidence);
                }
                spinnerPick(list);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("Failure");
                    }
                });

    }
    public void spinnerPick(final ArrayList<String> list){
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(spinnerAdapter);
        spinner.setVisibility(View.VISIBLE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text = list.get(position);
                confirm.setVisibility(View.VISIBLE);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parameters.add(text);
                        System.out.println("ADDED: " + parameters);
                        divider.setVisibility(View.VISIBLE);
                        addMore.setVisibility(View.VISIBLE);
                        cont.setVisibility(View.VISIBLE);
                    }
                });
                //spinnerAdapter.notifyDataSetChanged();
                addMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinner.setVisibility(View.INVISIBLE);
                        divider.setVisibility(View.INVISIBLE);
                        confirm.setVisibility(View.INVISIBLE);
                        addMore.setVisibility(View.INVISIBLE);
                        cont.setVisibility(View.INVISIBLE);
                        capture.setVisibility(View.VISIBLE);
                        photo.setVisibility(View.VISIBLE);
                        actualImage.setVisibility(View.INVISIBLE);

                    }
                });


                cont.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Intent recipesIntent = new Intent(MainActivity.this, RecipeListActivity.class);
                        recipesIntent.putStringArrayListExtra("ingredients",parameters);
                        continueOn(recipesIntent);
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void continueOn(Intent intent){

        startActivity(intent);
    }


}

