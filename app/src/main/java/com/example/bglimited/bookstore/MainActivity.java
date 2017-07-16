package com.example.bglimited.bookstore;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {
    private static String url = "http://bin2580.16mb.com/library_services/soa_getbooks.php";

    private final static String TAG = "SID";
    private static final String TAG_STATUS = "status";
    private static final String TAG_ERROR = "error_msg";
    private static final String TAG_SINGLE_BRANCH = "SINGLE_BRANCH";
    public static final String ARRAY_NAME = "DATA";
    private Context context;
    private ListView lv;
    String[] al;
    String q;
    String userid1;
    private String errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        SessionManager sm= new SessionManager(getApplicationContext());
        userid1 = sm.getUserID();
        if(userid1=="")
        {
            Intent i= new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
        }
        lv = (ListView) findViewById(R.id.listView1);
        context = this;
        Button bin = (Button)findViewById(R.id.button1);
        bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,AddBook.class);
                startActivity(i);
            }
        });

        new LoadData().execute();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                int itemPosition = position;
                String itemValue = (String) lv.getItemAtPosition(position);
                Intent dept = new Intent(context, LastActivity.class);
                dept.putExtra("dept", itemValue);
                startActivity(dept);


            }
        });
    }

    private class LoadData extends AsyncTask<Void, Void, Void> {

        ProgressDialog pDialog;
        JSONArray branches = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
//            Toast.makeText(SecondaryActivity.this,url, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            WebRequest webreq = new WebRequest();
            String jsonStr = webreq.makeWebServiceCall(url, WebRequest.GET);
            ParseJSON(jsonStr);
            Log.d(TAG,"json string"+jsonStr);

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
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_list_item_1,al);

                    lv.setAdapter(adapter);
                }
            });

            //     Toast.makeText(DepartmentActivity.this,url, Toast.LENGTH_SHORT).show();

        }


        private void ParseJSON(String json) {


            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    branches = jsonObj.getJSONArray(ARRAY_NAME);

                    al=new String[branches.length()];
                    for (int i = 0; i < branches.length(); i++) {
                        JSONObject c = branches.getJSONObject(i);
                        String b = c.getString("name");
                        al[i]=b;

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }
        }



    }
}
