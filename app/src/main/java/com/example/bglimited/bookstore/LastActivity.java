package com.example.bglimited.bookstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class LastActivity extends AppCompatActivity {

    TextView  tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13;
    private Context context;
    String url = "http://bin2580.16mb.com/library_services/soa_last.php";

    private static final String ARRAY_NAME="DATA";
    String userid1;
    String BOOK_NAME;
    String AUTHOR;
    String PRICE;
    String DESC;
    String OWNER;
    String PHONE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        SessionManager sm= new SessionManager(getApplicationContext());
        userid1 = sm.getUserID();
        if(userid1=="")
        {
            Intent i= new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
        }
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);
        tv5 = (TextView) findViewById(R.id.textView5);
        tv6 = (TextView) findViewById(R.id.textView6);
        tv7 = (TextView) findViewById(R.id.textView7);
        tv8 = (TextView) findViewById(R.id.textView8);
        tv9 = (TextView) findViewById(R.id.textView9);
        tv10 = (TextView) findViewById(R.id.textView10);
        tv11 = (TextView) findViewById(R.id.textView11);
        tv12 = (TextView) findViewById(R.id.textView12);
        tv13 = (TextView) findViewById(R.id.textView13);


        context = this;

        Intent i = getIntent();
        BOOK_NAME = i.getStringExtra("dept");


        new LoadDetails().execute(BOOK_NAME);
    }

    private class LoadDetails extends AsyncTask<String, Void, Void> {

        ProgressDialog pDialog;
        JSONArray branches;
        LinkedHashMap<String, String> p;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(LastActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(String... params) {

            p = new LinkedHashMap<>();
            p.put("NAME",params[0]);
            WebRequest webreq = new WebRequest();
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.POST,p);
            ParseJSON(jsonStr);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();


            runOnUiThread(new Runnable() {
                public void run() {

                    tv3.setText(BOOK_NAME);
                    tv5.setText(AUTHOR);
                    tv7.setText(PRICE);
                    tv9.setText(DESC);
                    tv11.setText(OWNER);
                    tv13.setText(PHONE);
                }
            });
        }


        private void ParseJSON(String json) {

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    branches = jsonObj.getJSONArray(ARRAY_NAME);
                    JSONObject o = branches.getJSONObject(0);

                    BOOK_NAME =o.getString("BOOK_NAME");
                    AUTHOR = o.getString("AUTHOR");
                    PRICE = o.getString("PRICE");
                    DESC =o.getString("DESC");
                    OWNER=o.getString("OWNER");
                    PHONE=o.getString("PHONE");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }



    }

}
