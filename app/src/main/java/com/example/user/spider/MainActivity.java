package com.example.user.spider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final static String URL = "https://news.google.com.tw/";
    private ListView listView;
    List<News> news_list = new ArrayList<News>();
    private Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);

        thread.start();
    }

    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            Log.d("Test", "run");
            Connection conn = Jsoup.connect(URL);
            conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");

            Document doc = null;
            try {
                doc = conn.get();
            } catch (Exception e) {
                Log.e("Connection", e.toString());
            }
            Elements elements = doc.select("table.esc-layout-table");

            for (int i = 0; i < elements.size(); i++) {
                String url = "",Title = "", Time = "", Address = "", Source = "";
                Bitmap Image;
                //google圖片是用Base64編碼存放，所以先把此字串前面data:image/jpeg;base64,的宣告刪掉再去解碼
                url = elements.get(i).select("div.esc-thumbnail-image-wrapper img").attr("src");
                if(url == ""){
                    url = "http:" + elements.get(i).select("div.esc-thumbnail-image-wrapper img").attr("imgsrc");
                    Image = getBitmapfromURL(url);
                }else {  //google有的圖片是用Base64編碼存放，所以先把此字串前面data:image/jpeg;base64,的宣告刪掉再去解碼
                    Log.d("URL", "found");
                    url = url.substring(url.indexOf(",") +1);
                    byte[] byteUrl = Base64.decode(url, Base64.DEFAULT);
                    Image = BitmapFactory.decodeByteArray(byteUrl, 0, byteUrl.length);
                }

                Title = elements.get(i).select("h2.esc-lead-article-title span.titletext").text();
                Address = elements.get(i).select("h2.esc-lead-article-title a").attr("url");
                Time = elements.get(i).select("div.esc-lead-article-source-wrapper span.al-attribution-timestamp").text();
                Source = elements.get(i).select("div.esc-lead-article-source-wrapper span.al-attribution-source").text();
                news_list.add(new News(Image, Title, Address, Time, Source));
            }

            adapter = new Adapter(MainActivity.this, news_list);
            runOnUiThread(new Runnable() {
                public void run() {
                    listView.setAdapter(adapter);
                }
            });
        }
    });

    //傳入圖片網址回傳Bitmap圖
    public static Bitmap getBitmapfromURL(String src){
        try{
            java.net.URL url = new URL(src);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();//建立連線

            InputStream input = conn.getInputStream();//把網址內資料丟進Stream
            Bitmap mBitmap = BitmapFactory.decodeStream(input);//把資料轉為Bitmap格式
            return mBitmap;
        }catch (Exception e){
            Log.e("URL", src);
            Log.e("getBitmap", e.toString());
        }
        return null;
    }

}
