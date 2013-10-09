package com.mobileproto.lab5;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by evan on 9/25/13.
 */
public class FeedFragment extends Fragment {



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.feed_fragment, null);


        AsyncTask utahraptor = new AsyncTask<Void, Void, ArrayList<FeedItem>>(){
            protected ArrayList<FeedItem> doInBackground(Void... voids){

        ArrayList<FeedItem> twits = new ArrayList<FeedItem>();
        String gatsby = "";

        // defaultHttpClient
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpGet pull = new HttpGet("http://twitterproto.herokuapp.com/tweets");
        pull.setHeader("Content-type","application/json");

        try{
        HttpResponse httpResponse = httpClient.execute(pull);
        HttpEntity httpEntity = httpResponse.getEntity();
        InputStream is = httpEntity.getContent();

        BufferedReader read = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
        StringBuilder build = new StringBuilder();

        String line;
        String nl = System.getProperty("line.separator");
        while ((line = read.readLine()) != null) {
            build.append(line + nl);
        }

        gatsby = build.toString();

        }



        catch (Exception e) {
            e.printStackTrace();
        }

        try {
        JSONObject ntweet = new JSONObject(gatsby);
        JSONArray list = ntweet.getJSONArray("tweets");

        for (int i=0; i<list.length(); i++){
            FeedItem swag = new FeedItem(list.getJSONObject(i).getString("username"), list.getJSONObject(i).getString("tweet"));
            twits.add(swag);
        }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return twits;

            }
        protected void onPostExecute(ArrayList<FeedItem> twits) {
            FeedListAdapter feedListAdapter = new FeedListAdapter(getActivity(), twits);
            ListView feedList = (ListView) v.findViewById(R.id.feedList);
            feedList.setAdapter(feedListAdapter);
        }

    }.execute();

         /*
         * Creating some sample test data to see what the layout looks like.
         * You should eventually delete this.
         */
//        FeedItem item1 = new FeedItem("@TimRyan", "Dear reader, you are reading.");
//        FeedItem item2 = new FeedItem("@EvanSimpson", "Hey @TimRyan");
//        FeedItem item3 = new FeedItem("@JulianaNazare", "Everything happens so much.");
//        FeedItem item4 = new FeedItem("@reyner", "dGhlIGNvb2wgbmV3IHRoaW5nIHRvIGRvIGlzIGJhc2U2NCBlY29kZSB5b3VyIHR3ZWV0cw==");
//        List<FeedItem> sampleData = new ArrayList<FeedItem>();
//        sampleData.add(item1);
//        sampleData.add(item2);
//        sampleData.add(item3);
//        sampleData.add(item4);
//
//        // Set up the ArrayAdapter for the feedList
//        FeedListAdapter feedListAdapter = new FeedListAdapter(this.getActivity(), sampleData);
//        ListView feedList = (ListView) v.findViewById(R.id.feedList);
//        feedList.setAdapter(feedListAdapter);


        return v;

    }
}
