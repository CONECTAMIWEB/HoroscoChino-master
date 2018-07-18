package com.example.usuario.horscopochino;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity
{

    /////DECLARO VARIABLES SOBRE LLAMAR LA IMAGEN ALEATORIAMENTE
    private static final Random rgenerador = new Random();

    private static final Integer[] imagenesID =
            {R.drawable.chino_01, R.drawable.chino_02, R.drawable.chino_03, R.drawable.chino_04, R.drawable.chino_05, R.drawable.chino_06, R.drawable.chino_07, R.drawable.chino_08, R.drawable.chino_09, R.drawable.chino_10, R.drawable.chino_11, R.drawable.chino_12,};



    //Variable de tipo button que va a contener el boton generar Meses de nuestra aplicación y el boton de compartir en redes sociales
    private Button btnGenerar, btnCompartir;

    //Variable de tipo Textview que sera donde se pinten las frases de los meses xD
    private TextView txtMes;

    //Lista que va a contener todas las frases de mi Json
    private ArrayList<String> meses = new ArrayList<>();


    //Creo una variable del tipo String para almacenar el resultado de la frase que estoy mostrando
    private String mesFinal;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Busco el botón de generar mediante el id.
         btnGenerar = findViewById(R.id.btnGenerar);

        //Busco el botón de compartir mediante el id.
        btnCompartir = findViewById(R.id.btnshare);

        //Lo mismo con la frase, busco su valor mediante su id.
        txtMes = findViewById(R.id.txtMes);


        /////CACHEO TODOS SOBRE IMAGENES
        Integer index = imagenesID[rgenerador.nextInt(imagenesID.length)];


        final ImageView iv = (ImageView) findViewById(R.id.imagMes);

        View btnGenerar = findViewById(R.id.btnGenerar);




        //Añado un  listener (escuchador) al boton, para poder capturar LAS IMAGENES y LOS TEXTOS
        btnGenerar.setOnClickListener(new View.OnClickListener()
        {


            public void onClick(View V)
            {

                int resource = imagenesID[rgenerador.nextInt(imagenesID.length)];

                iv.setImageResource(resource);

                //Lanzo el método Cogerfrases para que empiece a buscar en el archivo Json
                CogerMeses();

            }

        });



        //Añado un listener (escuchador) al boton, para poder capturar cada vez que se pulsa
        btnCompartir.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {




                //Añado esta comprobacion para evitar que la aplicacion se cierre por un crash, puesto que puede darse
                // el caso que el usuario pulse el botón de compartir sin antes a ver generado una frase.

                if(mesFinal != null)
                {

                    //invoco la funcion que hace que se comparta el texto generado en redes sociales
                    CompartirTexto(mesFinal,false);
                }else
                {

                    //Si no se ha generado un texto lanzo un dialogo avisando que antes de compartir debe de generar una frase
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                    alertDialogBuilder.setTitle("Error");


                    alertDialogBuilder
                            .setMessage("Antes de compartir deves generar primero un mes pul....")
                            .setCancelable(false)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int id)
                                {

                                }
                            }).show();

                }



            }
        });



    }


    private void CogerMeses()
    {

        try
        {
            //Leo el contenido del Json que esta en la carpeta assets
            String jsonLocation = loadJSONFromAsset();

            //Instancio un objeto Json y en su constructor le paso el texto del Json
            JSONObject jsonobject = new JSONObject(jsonLocation);

            //Cojo el Array del archivo Json para poder recorrerlo
            JSONArray jarray = jsonobject.getJSONArray("meses");

            //Empiezo a recorrer el array del Json
            for(int i=0;i<jarray.length();i++)
            {
                //En cada iteracion cojo el objeto correspondiente y su valor
                JSONObject jb =(JSONObject) jarray.get(i);
                String mes = jb.getString("texto");

                //En cada iteracion añado la frase correspondiente a la lista
                meses.add(mes);

            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }


        //Si llegamos aqui a salvos es que lo anterior ha ido correctamente xD, ahora vamos a sacar un frase aleatoria de la lista

        //Generamos una instancia de Random, random la usaremos siempre que queramos generar algo aleatorio
         Random read = new Random();

        //Generamos un numero aleatorio comprendido entre 0 y el tamaño de la lista
        int index = read.nextInt(meses.size());

        //Cogemos de la lista una frase aleatoria y la guardamos en el String que acabamos de crear
        mesFinal = meses.get(index);

        //Aplicamos el resultado a texto de la vista de nuestra interfaz para mostrarlo por pantalla
        txtMes.setText(mesFinal);



    }


    //Función que lee el contenido del Json y nos devuelve un string con su texto
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("dataMeses.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    //Función para lanzar el Intent Share
     private void CompartirTexto(String textoCompartir, boolean onlyOnwhatsApp)
    {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        //sendIntent.putExtra(Intent.EXTRA_TEXT, textoCompartir);
        sendIntent.setType("text/plain");

        //Compruebo si queremos que lance whatsApp directamente o que nos muestre todas las opciones
        if(onlyOnwhatsApp)
            sendIntent.setPackage("com.whatsapp");



        startActivity(Intent.createChooser(sendIntent,"Compartir mes Generado"));


    }
      //ok

}

