package com.example.jasmineparekh.leftoverss;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RecipeListActivity extends AppCompatActivity {


    public ArrayList<Recipe> mRecipes = new ArrayList<>();
    ArrayList<String> parameters = new ArrayList<>();
    ListView listView;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_list);

        listView = findViewById(R.id.listView);

        Intent recipesIntent = getIntent();
        parameters= recipesIntent.getStringArrayListExtra("ingredients");
        getRecipes(parameters);

    }

    private void getRecipes(ArrayList<String> parameters) {
        final FoodService foodService = new FoodService();

        foodService.findRecipes(parameters, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRecipes = foodService.processResults(response);
                for(Recipe r: mRecipes){
                    System.out.println(r.getName());
                }
                RecipeListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter = new CustomAdapter(RecipeListActivity.this, R.layout.recipe_list_item, mRecipes);
                        listView.setAdapter(adapter);

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final Intent detailsIntent = new Intent(RecipeListActivity.this, RecipeDetailsActivity.class);
                                detailsIntent.putExtra("recipeCalled", mRecipes.get(position));
                                startActivity(detailsIntent);

                                Toast.makeText(RecipeListActivity.this, position + " ", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });


            }
        });
    }

    public class CustomAdapter extends ArrayAdapter<Recipe> {
        Context context;
        int resource;
        ArrayList <Recipe> list;


        public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Recipe> objects) {
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
            ImageView rImage = adapterLayout.findViewById(R.id.rImage);
            rName.setText(mRecipes.get(position).getName());
            Picasso.get().load(mRecipes.get(position).getImageUrl()).into(rImage);

            return adapterLayout;
        }

    }//Custom Adapter



}
