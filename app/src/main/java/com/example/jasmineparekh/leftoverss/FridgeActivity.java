package com.example.jasmineparekh.leftoverss;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static android.media.MediaCodec.MetricsConstants.MODE;

public class FridgeActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton add;
    String name;
    String date;
    //ArrayList<Food> fridgeList;
    ArrayList<Food> fridgeList = new ArrayList<>();
    FridgeActivity.CustomAdapter adapter;
    EditText dateEnt;
    EditText nameEnt;






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fridge_list);

        listView = findViewById(R.id.listView);
        add = findViewById(R.id.addToFridge);

//        loadData();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = (LayoutInflater.from(FridgeActivity.this)).inflate(R.layout.userinput, null);
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(FridgeActivity.this);
                alertBuilder.setView(view);
                dateEnt = (EditText) view.findViewById(R.id.dateEnter);
                nameEnt = (EditText) view.findViewById(R.id.nameEnter);


                alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            name = nameEnt.getText().toString();
                            date = dateEnt.getText().toString();
                        }catch(NullPointerException e){
                            System.out.println("HELPPPPP ME");
                        }
                        fridgeList.add(new Food(name, date));
                        FridgeActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new CustomAdapter(FridgeActivity.this, R.layout.fridge_list_item, fridgeList);
                                listView.setAdapter(adapter);
                            }
                        });
                    }
                });
                Dialog dialog = alertBuilder.create();
                dialog.show();


            }
        });


    }
//    @Override
//    protected void onPause() {
//
//        super.onPause();
//        saveData();
//        Toast.makeText(getApplicationContext(), "onPause called", Toast.LENGTH_LONG).show();
//
//    }
    private void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("Shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(fridgeList);
        editor.putString("fridge list",json);
        editor.apply();
    }
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("Shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("fridge list", null);
        Type type = new TypeToken<ArrayList<Food>>() {}.getType();
        fridgeList = gson.fromJson(json, type);

        if(fridgeList == null) {
            fridgeList = new ArrayList<>();
            System.out.println("NOTHING IN HERE");
        }
        else {
            FridgeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new CustomAdapter(FridgeActivity.this, R.layout.fridge_list_item, fridgeList);
                    listView.setAdapter(adapter);
                }
            });
        }
    }



    public class CustomAdapter extends ArrayAdapter<Food> {
        Context context;
        int resource;
        ArrayList<Food> list;

        public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Food> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View adapterLayout = layoutInflater.inflate(resource,null);

            TextView rName = adapterLayout.findViewById(R.id.rName);
            TextView rDate = adapterLayout.findViewById(R.id.rDate);
            ImageView colorTab = adapterLayout.findViewById(R.id.colorTab);
            rName.setText(fridgeList.get(position).getName());
            rDate.setText(fridgeList.get(position).getDate());
            colorTab.setColorFilter(fridgeList.get(position).getColorTab());


            return adapterLayout;
        }

    }//Custom Adapter

}
