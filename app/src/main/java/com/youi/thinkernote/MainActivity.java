package com.youi.thinkernote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    List<String> IDs = new ArrayList<String>();
    List<String> Titulos = new ArrayList<String>();
    List<String> Descs = new ArrayList<String>();

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 1000;
    int mat;
    String startUrl = "https://thinkernote.000webhostapp.com/";
    AdapterNotes adapterNotes;
    EditText titulo;
    EditText descripcion;



    private class MyTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String ... urls) {
            try {
                URL url = new URL(urls[0]);
                URLConnection uc = url.openConnection();
                //String j = (String) uc.getContent();
                uc.setDoInput(true);
                BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                String inputLine;
                StringBuilder a = new StringBuilder();
                while ((inputLine = in.readLine()) != null)
                    a.append(inputLine);
                in.close();
                return a.toString();
            }
            catch (Exception e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            IDs = new ArrayList<String>();
            Titulos = new ArrayList<String>();
            Descs = new ArrayList<String>();
            try {
                JSONArray mJsonArray = new JSONArray(result);
                for (int i = 0; i < mJsonArray.length(); i++) {
                    JSONObject jsonObject = mJsonArray.getJSONObject(i);
                    IDs.add(jsonObject.getString("ID"));
                    Titulos.add(jsonObject.getString("titulo"));
                    Descs.add(jsonObject.getString("desc"));
                }
                RecyclerView rvnotes = findViewById(R.id.recycler4notes);
                adapterNotes = new AdapterNotes(IDs,Titulos,Descs,IDs.size(),mat,getApplicationContext());
                rvnotes.setAdapter(adapterNotes);
                rvnotes.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    //Toast.makeText(getApplicationContext(),"Matricula en main: " + mat,Toast.LENGTH_SHORT).show();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth fbauth = FirebaseAuth.getInstance();
        
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = mDatabaseRef.orderByChild("correo").equalTo(fbauth.getCurrentUser().getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int matricula = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    matricula =  Integer.parseInt(data.child("matricula").getValue().toString());
                }
                Toast.makeText(getApplicationContext(),"Hey mat: " + matricula,Toast.LENGTH_SHORT).show();
                GetNotesByMatricula(matricula);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                GetNotesByMatricula(mat);
            }
        }, delay);

        descripcion = findViewById(R.id.desc);
        titulo = findViewById(R.id.titulo);

    }

    public void GetNotesByMatricula(int matricula){
        MyTask taskLoad = new MyTask();
        taskLoad.execute(startUrl+"api/notes.php?action=GETNotesByMatricula&matricula="+matricula);
    }

}