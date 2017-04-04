package com.shineuntaek.membermanagement;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView idText =(TextView) findViewById(R.id.idText);
        TextView passwordText =(TextView) findViewById(R.id.passwordTXT);
        TextView welcomeMsg = (TextView)findViewById(R.id.welcomeMsg);
        Button managementButton =(Button)findViewById(R.id.managementButton);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userPassword = intent.getStringExtra("userPassword");
        String message = "Welcome "+ userID +"!!";

        idText.setText(userID);
        passwordText.setText(userPassword);
        welcomeMsg.setText(message);

        if(!userID.equals("admin")){
            managementButton.setVisibility(View.GONE);

        }

        managementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new BackgroundTask().execute();
            }
        });
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;



        //초기화 하는 부분
        @Override
        protected void onPreExecute() {
            target= "http://set00561.cafe24.com/List.php";
        }
        //실제 작동하는 부분
        @Override
        protected String doInBackground(Void... voids) {
            try{
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                //매열마다 입력을 받을수 있도록 하는것
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {

                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(String s) {
            Intent intent = new Intent(getApplicationContext(), ManagementActivity.class);
            intent.putExtra("userList",s);
            MainActivity.this.startActivity(intent);
        }
    }
}
