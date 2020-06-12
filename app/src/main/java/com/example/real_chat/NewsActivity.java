package com.example.real_chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

        private RecyclerView recyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager layoutManager;
        private String[] mDataset = {"6", "2", "3", "5"};

        RequestQueue queue;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_news);
            recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);



            queue = Volley.newRequestQueue(this);
            getNews();
        }

        public void getNews(){

            // Instantiate the RequestQueue.
            String url ="http://newsapi.org/v2/top-headlines?country=kr&apiKey=055cfc336b4a48878772aa298837edd9";

            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            Log.d("news: ", response);

                            try {
                                JSONObject jsonObj = new JSONObject(response);

                                JSONArray arrayArticles = jsonObj.getJSONArray("articles");

                                List<NewsData> news = new ArrayList<>();

                                for(int i=0, j=arrayArticles.length(); i<j; i++) {
                                    JSONObject obj = arrayArticles.getJSONObject(i);


                                    Log.d("news: ", obj.toString());

                                    NewsData newsData = new NewsData();
                                    newsData.setTitle(obj.getString("title"));
                                    newsData.setUrlToImage(obj.getString("urlToImage"));
                                    newsData.setContent(obj.getString("content"));

                                    news.add(newsData);

                                }


                                // specify an adapter (see also next example)
                                mAdapter = new MyAdapter(news, NewsActivity.this, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Object obj = v.getTag();
                                        if(obj != null){
                                            int position = (int)obj;
                                            ((MyAdapter)mAdapter).getNews(position);

                                        }
                                    }
                                });
                                recyclerView.setAdapter(mAdapter);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            //response를 NewsData에 분류


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        }

}