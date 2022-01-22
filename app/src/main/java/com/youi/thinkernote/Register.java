package com.youi.thinkernote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private static final Object TAG = "Hello User";
    private FirebaseAuth fbauth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        ImageButton backbtn = findViewById(R.id.backregister);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Registrar usuario

        final EditText correo, contraseña,matricula,nombre,apellido;
        final LinearLayout llregister = findViewById(R.id.ll4register);
        CardView register = findViewById(R.id.cvregisterft);
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference().child("Users");
        ;
        correo = findViewById(R.id.createuser);
        contraseña = findViewById(R.id.createcontra);
        matricula = findViewById(R.id.creatematricula);
        nombre = findViewById(R.id.createnombre);
        apellido = findViewById(R.id.createapellido);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (correo.getText().toString().isEmpty()){
                    correo.setError("Ingrese el correo");
                }
                if (contraseña.getText().toString().isEmpty()){
                    contraseña.setError("Ingrese una contraseña");
                }
                if (matricula.getText().toString().isEmpty()){
                    matricula.setError("Ingrese una matricula");
                }
                if (nombre.getText().toString().isEmpty()){
                    nombre.setError("Ingrese un nombre");
                }

                if (apellido.getText().toString().isEmpty()){
                    apellido.setError("Ingrese un apellido");
                }


                if (!correo.getText().toString().isEmpty() && !contraseña.getText().toString().isEmpty() && !matricula.getText().toString().isEmpty() && !nombre.getText().toString().isEmpty() && !apellido.getText().toString().isEmpty()) {
                    String co,cont;
                    co = correo.getText().toString();
                    cont = contraseña.getText().toString();
                    try {
                        fbauth.createUserWithEmailAndPassword(co, cont);
                        WriterUsers writer = new WriterUsers(nombre.getText().toString(),apellido.getText().toString(),co,cont,Integer.parseInt(matricula.getText().toString()));
                        reference.child(matricula.getText().toString()).setValue(writer);
                    }catch (Exception e){
                        Log.e("Register", "onClick: ",e);
                        Snackbar.make(llregister,"Ocurrio un error inesperado, intente de nuevo mas tarde",Snackbar.LENGTH_SHORT).show();
                    }finally {
                        Snackbar.make(llregister,"Usuario registrado con exito!",Snackbar.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });
    }
}