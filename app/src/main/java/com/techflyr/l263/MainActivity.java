package com.techflyr.l263;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText name,email,age;
    Button submit;
    ListView listView;

    HashMap <String,String> hashMap;
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        age = findViewById(R.id.age);
        submit = findViewById(R.id.submit);
        listView = findViewById(R.id.listView);

        loadData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String names = name.getText().toString();
                String emails = email.getText().toString();
                String ages = age.getText().toString();

                String url ="https://techflyr.com/apps/con.php?name="+names+"&email="+emails+"&age="+ages;

                progressBar.setVisibility(View.VISIBLE);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressBar.setVisibility(View.GONE);

                        submit.setText("Data Submited");

                        loadData();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                if (names.length()>0 & emails.length()>0 &ages.length()>0) {
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);
                }else name.setError("input your name");
            }
        });


    }
    //===============================

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {

            LayoutInflater layoutInflater = getLayoutInflater();
            View myView = layoutInflater.inflate(R.layout.item,null,false);

            Button update = myView.findViewById(R.id.update);
            Button delete = myView.findViewById(R.id.delete);
            TextView edid = myView.findViewById(R.id.edid);
            TextView edName = myView.findViewById(R.id.edName);
            TextView edEmail = myView.findViewById(R.id.edEmail);
            TextView edAge = myView.findViewById(R.id.edAge);


            hashMap = arrayList.get(position);
            String idno = hashMap.get("id");
            String nameno = hashMap.get("name");
            String emailno = hashMap.get("email");
            String ageno = hashMap.get("age");

            edid.setText(idno);
            edName.setText(nameno);
            edEmail.setText(emailno);
            edAge.setText(ageno);

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String names = edName.getText().toString();
                    String emails = edEmail.getText().toString();
                    String ages = edAge.getText().toString();
                    String url ="https://techflyr.com/apps/update.php?name=" + names + "&email=" + emails + "&age=" + ages + "&id="+idno;

                    progressBar.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Server Response")
                                    .setMessage(response)
                                    .show();

                            loadData();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);

                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    String url ="https://techflyr.com/apps/delete.php?id="+idno;

                    progressBar.setVisibility(View.VISIBLE);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressBar.setVisibility(View.GONE);

                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Server Response")
                                    .setMessage(response)
                                    .show();

                            loadData();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
                    requestQueue.add(stringRequest);

                }
            });


            return myView;
        }
    }

  private void loadData(){

        arrayList = new ArrayList<>();

      RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
      String url = "https://techflyr.com/apps/view.php";
      progressBar.setVisibility(View.VISIBLE);
      JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
          @Override
          public void onResponse(JSONArray response) {
              progressBar.setVisibility(View.GONE);

              for (int x=0; x<response.length(); x++){
                  try {
                      JSONObject jsonObject = response.getJSONObject(x);
                      String id = jsonObject.getString("id");
                      String name = jsonObject.getString("name");
                      String email = jsonObject.getString("email");
                      String age = jsonObject.getString("age");

                      hashMap = new HashMap<>();
                      hashMap.put("id",id);
                      hashMap.put("name",name);
                      hashMap.put("email",email);
                      hashMap.put("age",age);
                      arrayList.add(hashMap);

                  } catch (JSONException e) {
                      throw new RuntimeException(e);
                  }
              }

              if (arrayList.size()>0){
                  MyAdapter myAdapter = new MyAdapter();
                  listView.setAdapter(myAdapter);
              }

          }
      }, new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

          }
      });
      requestQueue.add(jsonArrayRequest);
  };

}