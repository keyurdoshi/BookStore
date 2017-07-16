package com.example.bglimited.bookstore;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class AddBook extends AppCompatActivity{
    public EditText name;
    private EditText category;
    private EditText price;
    private EditText description;
    private Button btnAdd;
    String userid1;
    private static final String REGISTER_URL="http://bin2580.16mb.com/library_services/addProduct.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        SessionManager sm= new SessionManager(getApplicationContext());
        userid1 = sm.getUserID();
        if(userid1=="")
        {
            Intent i= new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(i);
        }
        Button bin = (Button)findViewById(R.id.btnAdd);
        bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });
    }
    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, MainActivity.class));
    }
        private void addProduct() {
            name = (EditText) findViewById(R.id.name);
            category = (EditText) findViewById(R.id.category);
            price = (EditText) findViewById(R.id.price);
            description = (EditText) findViewById(R.id.description);
            String name1 = name.getText().toString();
            String category1 = category.getText().toString();
            String price1 = price.getText().toString();
            String description1 = description.getText().toString();

            add(name1,category1,price1,description1,userid1);
        }

        private void add(String name1, String category1, String price1, String description1, String userid1) {
            class AddProduct extends AsyncTask<String, Void, String>{
                ProgressDialog loading;
                AddProductClass ruc = new AddProductClass();


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(AddBook.this, "Please Wait",null, true, true);
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    loading.dismiss();
                    String s1="br";
                    if(s.toLowerCase().contains(s1.toLowerCase()))
                        s="Added successfully";
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }

                @Override
                protected String doInBackground(String... params) {

                    HashMap<String, String> data = new HashMap<String,String>();
                    data.put("name",params[0]);
                    data.put("category",params[1]);
                    data.put("price",params[2]);
                    data.put("description",params[3]);
                    data.put("bookOwner",params[4]);

                    String result = ruc.sendPostRequest(REGISTER_URL,data);

                    return  result;
                }
            }
            AddProduct ru = new AddProduct();
            ru.execute(name1, category1,price1,description1,userid1);
        }
}