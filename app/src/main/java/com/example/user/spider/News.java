package com.example.user.spider;

import android.app.ListActivity;

/**
 * Created by USER on 2016/03/23.
 */
public class News extends ListActivity{
    private String Title;
    private String Address;
    private String Time;
    private String Image;
    private String Source;

    public News(String image,String title, String address, String time, String source){
        Title = title;
        Address = address;
        Time = time;
        Image = image;
        Source = source;
    }

    public String getTitletext(){
        return Title;
    }

    public String getAddress(){
        return Address;
    }

    public String getTime(){
        return Time;
    }

    public String getImage(){
        return Image;
    }

    public String getSource(){
        return Source;
    }
}
