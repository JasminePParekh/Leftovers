package com.example.jasmineparekh.leftoverss;

import android.graphics.Color;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Food {
    private String name;
    private Date expiryDate;
    private String date;

    public Food(String name, String date){
        this.name = name;
        this.date = date;
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            expiryDate = sdf.parse(date);
        }catch(ParseException ex){
            System.out.println("JASMINE: Invalid Date");
        }
    }
    public String getName(){
        return name;
    }
    public Date getExpiryDate(){
        return expiryDate;
    }
    public String getDate(){
        return date;
    }
    public int getColorTab(){
        Date now = new Date();
        long diff = expiryDate.getTime() - now.getTime();
        String days = Long.toString(diff / 1000 / 60 / 60 / 24);
        System.out.println(days);
        int daysLeft = Integer.parseInt(days);
        //int daysLeft = Integer.parseInt(days.substring(0,days.indexOf(",")));
        if(daysLeft < 0){
            return Color.BLACK;
        }
        else if(daysLeft < 4){
            return Color.RED;
        }
        else if(daysLeft< 7){
            return Color.YELLOW;
        }
        else{
            return Color.rgb(0,255,51);
        }

    }


}
