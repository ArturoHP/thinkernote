package com.youi.thinkernote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login: ";
    FirebaseAuth fbauth = FirebaseAuth.getInstance();
    int mat;


    @Override
    public void onStart() {
        super.onStart();
        fbauth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = fbauth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("matricula",mat);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);




        final EditText user,contra;
        final TextView forgotc;

        user = findViewById(R.id.correo);
        contra = findViewById(R.id.pass);

        forgotc = findViewById(R.id.contraseñaforgot);
        forgotc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Forgot?",Toast.LENGTH_SHORT).show();
            }
        });

        CardView btnregist = findViewById(R.id.cvregister);
        btnregist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registrar = new Intent(getApplicationContext(),Register.class);
                startActivity(registrar);
            }
        });

        CardView btniniciarsec = findViewById(R.id.cventer);
        btniniciarsec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!user.getText().toString().isEmpty() && !contra.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Usuario: " + user.getText().toString() + "\n" + "Contraseña: " + contra.getText().toString(),Toast.LENGTH_SHORT).show();

                    String usa = user.getText().toString();
                    String cont = contra.getText().toString();

                    fbauth.signInWithEmailAndPassword(usa,cont).addOnSuccessListener(Login.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Intent go = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(go);
                        }
                    }).addOnFailureListener(Login.this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            String fail = "Failure " + e;
                            Log.e(TAG,fail);
                            Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Usuario Y/O Contraseña nulos",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}