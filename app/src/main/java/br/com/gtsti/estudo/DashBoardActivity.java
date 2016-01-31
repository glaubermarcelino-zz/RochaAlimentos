package br.com.gtsti.estudo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Glauber on 18/01/2016.
 */
public class DashBoardActivity  extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setando o layout que ira executar
        setContentView(R.layout.dashboard);
    }

    public void SelecionarOpcao(View v)
    {
        /*
        * Com base do botao acionado iremnos tomar a View correta
        * */

        switch(v.getId())
        {
            case R.id.novo_cliente:
                startActivity(new Intent(this,CadClientes.class));
                break;
            case R.id.novo_pedido:
                startActivity(new Intent(this,CadPedidos.class));
                break;
            case R.id.mov_financeiro:
                startActivity(new Intent(this,CadFinanceiro.class));
                break;
            case R.id.configuracoes:
                startActivity(new Intent(this,Configuracao.class));
                break;
            case R.id.meus_produtos:
                startActivity(new Intent(this,CadProdutos.class));
                break;
            case R.id.nova_rota:
                startActivity(new Intent(this,CadCobrancas.class));
                break;

        }
    }
    //Criação de Menu para activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
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

        if (id == R.id.action_usuario) {
           startActivity(new Intent(this,CadUsuarios.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
