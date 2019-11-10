package com.example.jasmineparekh.leftoverss;

import android.telecom.Call;
import android.util.Log;

import android.util.Log;

import com.example.jasmineparekh.leftoverss.Constants;
import com.example.jasmineparekh.leftoverss.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class FoodService {
    public static String ingredients = "";

    public static final String TAG = FoodService.class.getSimpleName();

    public static void findRecipes(ArrayList<String> parameters, okhttp3.Callback callback) {
        String APP_KEY = Constants.APP_KEY;
        String APP_ID = Constants.APP_ID;
        OkHttpClient client = new OkHttpClient.Builder().build();
        for(String str: parameters) {
            ingredients += str + ",";
        }
        System.out.println("LOOK HERE BOIS: " + parameters);
        //String ingredients = (ingredient1 + "," + ingredient2).replaceAll("\\s","");
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.BASE_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.QUERY_PARAMETER, ingredients);
        urlBuilder.addQueryParameter(Constants.APP_QUERY_PARAMETER, APP_ID);
        urlBuilder.addQueryParameter(Constants.KEY_QUERY_PARAMETER, APP_KEY);
        String url = urlBuilder.build().toString();
        Log.v(TAG, url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        okhttp3.Call call = client.newCall(request);
        call.enqueue(callback);
    }


    public ArrayList<Recipe> processResults(Response response) {
        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONObject returnJSON = new JSONObject(jsonData);
                JSONArray recipesJSON = returnJSON.getJSONArray("hits");
                for (int i = 0; i < 10; i++) {
                    JSONObject recipeArrayJSON = recipesJSON.getJSONObject(i);
                    JSONObject recipeJSON = recipeArrayJSON.getJSONObject("recipe");
                    String name = recipeJSON.getString("label");
                    String imageUrl = recipeJSON.getString("image");
                    String sourceUrl = recipeJSON.getString("url");
                    String ingredients = recipeJSON.getJSONArray("ingredientLines").toString();

                    Recipe recipe = new Recipe (name, imageUrl, sourceUrl, ingredients);
                    recipes.add(recipe);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }
}
