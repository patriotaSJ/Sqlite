package com.tatoado.sqlite2;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tatoado.sqlite2.AdminSQLiteOpenHelper;
import com.tatoado.sqlite2.R;

import java.sql.SQLException;


public class MyActivity extends Activity {

    private EditText et1, et2, et3, et4;
    private Cursor fila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        et1 = (EditText) findViewById(R.id.et_dni);
        et2 = (EditText) findViewById(R.id.et_nombreyapellido);
        et3 = (EditText) findViewById(R.id.et_colegio);
        et4 = (EditText) findViewById(R.id.et_mesa);
    }

    public void alta(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,"administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String dni = et1.getText().toString();
        String nombre = et2.getText().toString();
        String colegio = et3.getText().toString();
        String nromesa = et4.getText().toString();

        Cursor fila = bd.rawQuery("select * from votantes where dni=" + dni, null);

        if(!fila.moveToFirst()) {  //devuelve true o false
            ContentValues registro = new ContentValues();  //es una clase para guardar datos

            registro.put("dni", dni);
            registro.put("nombre", nombre);
            registro.put("colegio", colegio);
            registro.put("nromesa", nromesa);
            bd.insert("votantes", null, registro);
            bd.close();
            et1.setText("");
            et2.setText("");
            et3.setText("");
            et4.setText("");

            Toast.makeText(this, "Se cargaron los datos de la persona",
                    Toast.LENGTH_SHORT).show();
        }
        else{
            bd.close();
            Toast.makeText(this, "La persona ya existe", Toast.LENGTH_SHORT).show();
        }
    }


    public void consulta(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase(); //Create and/or open a database that will be used for reading and writing.

        String dni = et1.getText().toString();

        Cursor fila = bd.rawQuery(  //devuelve 0 o 1 fila //es una consulta
                "select nombre,colegio,nromesa  from votantes where dni=" + dni, null);
        if (fila.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
            et2.setText(fila.getString(0));
            et3.setText(fila.getString(1));
            et4.setText(fila.getString(2));


        } else
            Toast.makeText(this, "No existe una persona con dicho dni" ,
                    Toast.LENGTH_SHORT).show();
        bd.close();

    }


    public void baja(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String dni = et1.getText().toString();

        int cant = bd.delete("votantes", "dni=" + dni, null); // (votantes es la nombre de la tabla, condición)

        bd.close();
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        if (cant == 1)
            Toast.makeText(this, "Se borró la persona con dicho documento",
                    Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No existe una persona con dicho documento",
                    Toast.LENGTH_SHORT).show();
    }

    public void modificacion(View v) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        String dni = et1.getText().toString();
        String nombre = et2.getText().toString();
        String colegio = et3.getText().toString();
        String nromesa = et4.getText().toString();

        ContentValues registro = new ContentValues();

        registro.put("nombre", nombre);
        registro.put("colegio", colegio);
        registro.put("nromesa", nromesa);

        int cant = bd.update("votantes", registro, "dni=" + dni, null);

        bd.close();
        if (cant == 1)
            Toast.makeText(this, "se modificaron los datos", Toast.LENGTH_SHORT)
                    .show();
        else
            Toast.makeText(this, "no existe una persona con dicho documento",
                    Toast.LENGTH_SHORT).show();
    }

    public void inicio(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        fila = bd.rawQuery(
                "select * from votantes order by dni asc ", null);

        if (fila.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
            et1.setText(fila.getString(0));
            et2.setText(fila.getString(1));
            et3.setText(fila.getString(2));
            et4.setText(fila.getString(3));
        } else
            Toast.makeText(this, "No hay registrados" ,
                    Toast.LENGTH_SHORT).show();
        bd.close();
    }

    public void anterior(View view){
        try {
            if (!fila.isFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
                fila.moveToPrevious();
                et1.setText(fila.getString(0));
                et2.setText(fila.getString(1));
                et3.setText(fila.getString(2));
                et4.setText(fila.getString(3));
            } else
                Toast.makeText(this, "Llego al principio de la tabla",
                        Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void siguiente(View view){
        try {
            if (!fila.isLast()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
                fila.moveToNext();
                et1.setText(fila.getString(0));
                et2.setText(fila.getString(1));
                et3.setText(fila.getString(2));
                et4.setText(fila.getString(3));
            } else
                Toast.makeText(this, "Llego al final de la tabla",
                        Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void fin(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this,
                "administracion", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery(
                "select * from votantes order by dni asc ", null);

        if (fila.moveToLast()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
            et1.setText(fila.getString(0));
            et2.setText(fila.getString(1));
            et3.setText(fila.getString(2));
            et4.setText(fila.getString(3));
        } else
            Toast.makeText(this, "No hay registrados" ,
                    Toast.LENGTH_SHORT).show();
        bd.close();
    }

    public void onReset(View view){
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
    }



}

/*
ContentValues = es una clase para guardar datos
http://developer.android.com/intl/es/reference/android/content/ContentValues.html

SqLiteDatabase =
http://developer.android.com/intl/es/reference/android/database/sqlite/SQLiteDatabase.html

getWritableDatabase
http://developer.android.com/intl/es/reference/android/database/sqlite/SQLiteOpenHelper.html

 */