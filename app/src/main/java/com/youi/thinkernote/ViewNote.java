package com.youi.thinkernote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import java.net.URL;
import java.net.URLConnection;

public class ViewNote extends AppCompatActivity {

    MainActivity mact = new MainActivity();
    EditText titulo;
    EditText descripcion;
    String startUrl = "https://thinkernote.000webhostapp.com/";
    int mat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        Intent intent = getIntent();
        FirebaseAuth fbauth = FirebaseAuth.getInstance();

        String id = intent.getStringExtra("id");

        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = mDatabaseRef.orderByChild("correo").equalTo(fbauth.getCurrentUser().getEmail());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    mat =  Integer.parseInt(data.child("matricula").getValue().toString());
                }
                Toast.makeText(getApplicationContext(),"Hey mat: " + mat,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });



        Toast.makeText(getApplicationContext(),"ID vn: " + id,Toast.LENGTH_SHORT).show();
        getInfoFromNote(id);

        ImageButton ret = findViewById(R.id.retVNote);
        titulo = findViewById(R.id.titulo);
        descripcion = findViewById(R.id.desc);
        Button genact = findViewById(R.id.btn_load);

        genact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote(id);
            }
        });

        ret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

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
            titulo.setText("");
            descripcion.setText("");
            //Toast.makeText(getApplicationContext(),"Wat is this: "  + result,Toast.LENGTH_SHORT).show();
            try {
                JSONObject jsonObject = new JSONObject(result);
                String ptitulo = jsonObject.getString("titulo");
                String pdesc = jsonObject.getString("desc");
                titulo.setText(ptitulo);
                descripcion.setText(pdesc);
            } catch (JSONException e) {
                descripcion.setText("Error: " + e);
                e.printStackTrace();
            }
        }
    }

    private class MyUpdate extends AsyncTask<String, Void, String> {
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
            Toast.makeText(getApplicationContext(),result + " wasa: " + mat,Toast.LENGTH_SHORT).show();
            finish();
            mact.GetNotesByMatricula(mat);
        }
    }
    public void getInfoFromNote(String id){
        ViewNote.MyTask taskLoad = new ViewNote.MyTask();
        taskLoad.execute(startUrl+"api/notes.php?action=GETNoteInfoById&noteId="+ id);
        //Toast.makeText(getApplicationContext(),startUrl+"api/notes.php?action=GETNoteInfoById&noteId="+ id,Toast.LENGTH_SHORT).show();
    }

    public void updateNote(String id){
        ViewNote.MyUpdate taskUpdate = new ViewNote.MyUpdate();
        taskUpdate.execute(startUrl+"api/notes.php?action=Update&titulo="+titulo.getText().toString()+"&desc="+descripcion.getText().toString()+"&id="+id);
    }
}