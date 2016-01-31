package br.com.gtsti.estudo.Util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glauber on 27/01/2016.
 */
public class funcoes {
    //Limpa os campos do Formulario
    public void ClearCampos(LinearLayout cliLayout)
    {
        for( int i = 0; i < cliLayout.getChildCount(); i++ )
            if( cliLayout.getChildAt( i ) instanceof EditText)
                ((EditText) cliLayout.getChildAt(i)).setText("");
    }

    /**
     * Pega todos os clientes
     * retorna lista de clientes
     * */
    public List<String> getAllDataTable(String tabela,int indCampo,SQLiteOpenHelper dbFactory){
        List<String> clientes = new ArrayList<String>();

        // Select todos os dados
        String selectQuery = "SELECT  * FROM "+tabela;

        SQLiteDatabase db = dbFactory.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Pega todos os dados da tabela de uma determinada coluna "indCampo"
        if (cursor.moveToFirst()) {
            do {
                clientes.add(cursor.getString(indCampo));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return clientes;
    }
}
