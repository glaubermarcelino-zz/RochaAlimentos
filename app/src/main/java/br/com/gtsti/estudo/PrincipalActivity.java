package br.com.gtsti.estudo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences.Editor;

public class PrincipalActivity extends Activity {
    private static final String MANTER_CONECTADO = "manter_conectado";
    private EditText usuario;
    private EditText senha;
    private CheckBox manterConectado;
    private Button btnEntrar;
    private DbFactory dbFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //Instancia o banco de dados SQLITE
        dbFactory = new DbFactory(this);

        usuario = (EditText)findViewById(R.id.usuario);
        senha = (EditText)findViewById(R.id.senha);
        btnEntrar = (Button)findViewById(R.id.btnEntrar);

        manterConectado  =(CheckBox)findViewById(R.id.manterConectado);

        SharedPreferences preferencias = getPreferences(MODE_PRIVATE);
        boolean conectado = preferencias.getBoolean(MANTER_CONECTADO,false);

        if(conectado)
        {
            startActivity(new Intent(this,DashBoardActivity.class));
        }


        btnEntrar.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {


           // Perform action on click
                 Context contexto = getApplicationContext();

                 String usuarioInformado = usuario.getText().toString();
                 String senhaInformada = senha.getText().toString();

                if ("admin".equals(usuarioInformado) && "123".equals(senhaInformada))
                {
                    SharedPreferences preferencias = getPreferences(MODE_PRIVATE);

                    SharedPreferences.Editor editor = preferencias.edit();
                    editor.putBoolean(MANTER_CONECTADO, manterConectado.isChecked());
                    editor.commit();

                    //Vai para a outra Activity apos a autenticação
                    startActivity(new Intent(getApplicationContext(),DashBoardActivity.class));

                    Toast toast = Toast.makeText(contexto, "Bem vindo ao Sistema Rocha Alimentos", Toast.LENGTH_SHORT);
                    toast.show();
                }else
                {
                    //mostra a mensagem de erro

                    String mensagemErro = getString(R.string.erro_autenticacao);
                    Toast toast = Toast.makeText(contexto,mensagemErro,Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //Cria uma barra
      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Colocar a ação aqui", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


}
