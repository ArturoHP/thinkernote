package com.youi.thinkernote;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    // Métodos de solicitud Http admitidos
    public static enum Method{
        POST,PUT,DELETE,GET;
    }
    private URL url;
    private HttpURLConnection con;
    private OutputStream os;

    // Después de la creación de instancias, al abrir la conexión: puede ocurrir una excepción
    public HttpRequest(URL url)throws IOException {
        this.url = url;
        con = (HttpURLConnection)this.url.openConnection();
    }

    // Se puede crear una instancia con la representación de String de la url
    // Obliga a verificar la IOException si es que es lanzada
    public HttpRequest(String url)throws IOException{
        this(new URL(url));
        Log.d("parametros", url);
    }

    // Método que prepara toda la solicitud al servidor
    private void prepararTodo(Method method)throws IOException{
        con.setDoInput(true);
        con.setRequestMethod(method.name());
        if(method== Method.POST||method== Method.PUT){
            con.setDoOutput(true);
            os = con.getOutputStream();
        }
    }

    // Preparo la solicitud con los diferentes métodos (GET, POST, PUT, DELETE)
    public HttpRequest preparar(Method method)throws IOException{
        prepararTodo(method);
        return this;
    }

    // Le doy soporte de caracteres UTF-8 al archivo JSON
    public HttpRequest reciboDatos(String query) throws IOException{
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(query);
        writer.close();
        return this;
    }

    // Organizo los datos, a cada uno le defino un key = value
    public HttpRequest reciboDatos(HashMap<String,String> params) throws IOException{
        StringBuilder resultado = new StringBuilder();
        for(Map.Entry<String,String>entry : params.entrySet()){
            resultado.append((resultado.length()>0?"&":"")+entry.getKey()+"="+entry.getValue());
            Log.d("parametros",entry.getKey()+"  ===>  "+ entry.getValue());
        }
        reciboDatos(resultado.toString());
        return this;
    }

    // Solicito y recibo los datos en String
    public String enviaryRecibirString() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        for(String line;(line = br.readLine())!=null;)response.append(line+"\n");
        Log.d("resultado",response.toString());
        return response.toString();
    }

}
