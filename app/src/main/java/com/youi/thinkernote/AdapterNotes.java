package com.youi.thinkernote;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class AdapterNotes extends RecyclerView.Adapter<AdapterNotes.NotesViewHolder> {

    List<String> ids = new ArrayList<String>();
    List<String> titulos = new ArrayList<String>();
    List<String> descs = new ArrayList<String>();
    Context ctx;
    int Len;
    int mat;

    public AdapterNotes(List<String> idsN, List<String> titulosN, List<String> descsN, int length,int matricula, Context Ctx){
        ids = idsN;
        titulos = titulosN;
        descs = descsN;
        ctx = Ctx;
        Len = length;
        mat = matricula;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.recycler_note,parent, false);
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.setTitulo(ctx,titulos.get(position));
        holder.setDesc(ctx,descs.get(position));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ctx,ViewNote.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("id",String.valueOf(ids.get(position)));
                i.putExtra("matricula",mat);
                ctx.startActivity(i);
            }
        });

    }



    @Override
    public int getItemCount() {
        return Len;
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitulo(Context ctx, String nombre) {
            TextView titulo = mView.findViewById(R.id.titulonote);
            titulo.setText(nombre);
        }

        public void setDesc(Context ctx, String sdesc) {
            TextView desc = mView.findViewById(R.id.descnote);
            desc.setText(sdesc);
        }
    }


}
