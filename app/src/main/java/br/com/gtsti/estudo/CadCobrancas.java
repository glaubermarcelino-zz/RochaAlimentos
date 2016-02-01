package br.com.gtsti.estudo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

import br.com.gtsti.estudo.Util.funcoes;

/**
 * Created by Glauber on 24/01/2016.
 */
public class CadCobrancas extends Activity {
    private int dia,mes,ano;
    private Button dataCobranca;
    private Spinner sp_usuario_cobranca,sp_pedidos_cobranca;
    private EditText obs;
    private DbFactory dbFactory;
    private funcoes funcao;
    private LinearLayout lCobrancas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_rotas);
        dbFactory = new DbFactory(this);
        funcao = new funcoes();

        //Recupera objetos
        lCobrancas = (LinearLayout)findViewById(R.id.lCobranca);
        dataCobranca = (Button)findViewById(R.id.cobranca_datacobranca);
        sp_pedidos_cobranca = (Spinner)findViewById(R.id.sp_pedidos_cobranca);
        sp_usuario_cobranca = (Spinner)findViewById(R.id.sp_usuario_cobranca);
        obs = (EditText)findViewById(R.id.cobranca_obs);


        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        dataCobranca = (Button)findViewById(R.id.cobranca_datacobranca);
        dataCobranca.setText(dia +"/"+(mes+1)+"/"+ano);

        //Populando Spinners
        //PEDIDOS
        ArrayAdapter<String>adapterPedidos = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,funcao.getPedidos(dbFactory));
        adapterPedidos.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_pedidos_cobranca.setAdapter(adapterPedidos);

        //USUARIOS
        ArrayAdapter<String>adapterUsuarios = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,funcao.getAllDataTable("USUARIOS", 2, dbFactory));
        adapterUsuarios.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_usuario_cobranca.setAdapter(adapterUsuarios);

    }
    //Criação de Menu para activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cobrancas, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void selecionarData(View v)
    {
        showDialog(v.getId());
    }
    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(R.id.cobranca_datacobranca ==id)
        {
            return new DatePickerDialog(this,listener,ano,mes,dia);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ano =year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dataCobranca.setText(dia+ "/"+(mes+1)+"/"+ano);
        }
    };
    public void AddCobranca(View view)
    {
        try{
            SQLiteDatabase db = dbFactory.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("obs",obs.getText().toString());
            values.put("idpedido_cliente",sp_pedidos_cobranca.getSelectedItemId());
            values.put("idusuario",sp_usuario_cobranca.getSelectedItemId());
            values.put("obs",obs.getText().toString());
            values.put("datacobranca",dataCobranca.getText().toString().replace("//",""));


            long retorno = db.insert("COBRANCAS", null, values);

            if (retorno != -1) {
                Toast.makeText(this, "Cobrança lançada com sucesso!", Toast.LENGTH_SHORT).show();
                funcao.ClearCampos(lCobrancas);
            } else {
                Toast.makeText(this, "Ocorreu um erro ao inserir as informações no banco de dados!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Erro desconhecido " + ex.getStackTrace(), Toast.LENGTH_LONG).show();
        }
    }
}
