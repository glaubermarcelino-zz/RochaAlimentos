package br.com.gtsti.estudo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.gtsti.estudo.Util.funcoes;

/**
 * Created by Glauber on 24/01/2016.
 */
public class CadUsuarios extends Activity {
    private DbFactory dbFactory;
    private EditText nomecompleto,usuario,senha;
    private funcoes funcao;
    private Spinner sp_perfil;
    private LinearLayout lUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_usuarios);
        dbFactory = new DbFactory(this);
        funcao = new funcoes();

        lUsuario = (LinearLayout)findViewById(R.id.lUsuarios);
        nomecompleto = (EditText)findViewById(R.id.usuario_nome_completo);
        usuario = (EditText)findViewById(R.id.usuario_nome);
        senha = (EditText)findViewById(R.id.usuario_senha);
        sp_perfil = (Spinner)findViewById(R.id.sp_perfil);

        //Popular o cadastro de perfis
        ArrayAdapter<String>adapterPerfil = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,funcao.getAllDataTable("PERFIL_USUARIO",2,dbFactory));
        adapterPerfil.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        sp_perfil.setAdapter(adapterPerfil);


    }
    //Criação de Menu para activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_usuarios, menu);
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

        if (id == R.id.action_permissoes) {
            startActivity(new Intent(this,PermissoesUsuario.class));
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onDestroy()
    {
        dbFactory.close();
        super.onDestroy();
    }
    public void AddUsuario(View view)
    {
        try{
        SQLiteDatabase db  = dbFactory.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOMECOMPLETO",nomecompleto.getText().toString());
        values.put("USERNAME",usuario.getText().toString());
        values.put("PASSWORD",senha.getText().toString());
        values.put("IDPERFIL",sp_perfil.getSelectedItemId());

        long retorno = db.insert("PERFIL_USUARIO",null,values);
        if (retorno != -1) {
            Toast.makeText(this, "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
            funcao.ClearCampos(lUsuario);
        } else {
            Toast.makeText(this, "Ocorreu um erro ao inserir as informações no banco de dados!", Toast.LENGTH_LONG).show();
        }
    } catch (Exception ex) {
        Toast.makeText(this, "Ocorreu um erro desconhecido " + ex.getMessage(), Toast.LENGTH_LONG).show();
    }
    }
}
