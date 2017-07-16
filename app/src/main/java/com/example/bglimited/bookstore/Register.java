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
public class Register extends AppCompatActivity{
    private EditText name;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText phone;
    private Button btnAdd;
    private static final String REGISTER_URL = "http://bin2580.16mb.com/library_services/soa_register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Button bin = (Button)findViewById(R.id.btnAdd);
        bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
              //  startActivity(new Intent(Register.this, LoginActivity.class));
            }
        });
    }

        private void registerUser() {
            name = (EditText) findViewById(R.id.name);
            username = (EditText) findViewById(R.id.username);
            password = (EditText) findViewById(R.id.password);
            email = (EditText) findViewById(R.id.email);
            phone = (EditText) findViewById(R.id.phone);
            String name1 = name.getText().toString();
            String username1 = username.getText().toString();
            String password1 = password.getText().toString();
            String email1 = email.getText().toString();
            String phone1 = phone.getText().toString();
            register(name1,username1,password1,email1,phone1);
        }

        private void register(String name, String username, String password,String email, String phone) {
            class RegisterUser extends AsyncTask<String, Void, String>{
                ProgressDialog loading;
                RegisterUserClass ruc = new RegisterUserClass();


                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    loading = ProgressDialog.show(Register.this, "Please Wait",null, true, true);
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
                    data.put("username",params[1]);
                    data.put("password",params[2]);
                    data.put("email",params[3]);
                    data.put("phone",params[4]);
                    String result = ruc.sendPostRequest(REGISTER_URL,data);

                    return  result;
                }
            }

            RegisterUser ru = new RegisterUser();
            ru.execute(name,username,password,email,phone);
        }

}
