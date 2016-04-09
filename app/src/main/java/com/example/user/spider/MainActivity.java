package com.example.user.spider;

import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private final static String URL = "https://news.google.com.tw/";
//    private DrawerLayout drawerLayout;
    private ListView listView;
    List<News> news_list = new ArrayList<News>();
    private Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listView);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Log.d("Test", "run");
                String Title = "", Time = "", Address = "", Image = "", Source = "";
//                Message msg = new Message();
                Connection conn = Jsoup.connect(URL);
                conn.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36");

                Document doc = null;
                try {
                    doc = conn.get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements elements = doc.select("table.esc-layout-table");

                for (int i = 0; i < elements.size(); i++) {
                    Image = elements.get(i).select("div.esc-thumbnail-image-wrapper img").attr("src");
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
        }).start();
    }


//    public class LoadImg extends AsyncTask<String, Integer, byte[]> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressDialog.show();
//        }
//
//        @Override
//        protected byte[] doInBackground(String... params) {
//
//        }
//    }
}
