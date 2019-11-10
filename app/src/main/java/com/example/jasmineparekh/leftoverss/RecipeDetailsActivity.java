package com.example.jasmineparekh.leftoverss;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailsActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView recipeImage;
    TextView recipeName;
    TextView directions;
    ListView ingredientLV;
    Recipe recipe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recipe_detail);

        recipeImage = findViewById(R.id.rrImage);
        recipeName = findViewById(R.id.rrName);
        ingredientLV = findViewById(R.id.ingredientLV);
        directions = findViewById(R.id.directions);

        Intent detailsIntent = getIntent();
        recipe = (Recipe) detailsIntent.getParcelableExtra("recipeCalled");

        ArrayAdapter ingredientAdapter = new ArrayAdapter(RecipeDetailsActivity.this, android.R.layout.simple_list_item_1, recipe.getIngredients());
        ingredientLV.setAdapter(ingredientAdapter);
        Picasso.get().load(recipe.getImageUrl()).into(recipeImage);
        recipeName.setText(recipe.getName());
        directions.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == directions) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(recipe.getSourceUrl()));
            startActivity(webIntent);
        }
    }

}
