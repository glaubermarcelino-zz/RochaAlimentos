package br.com.gtsti.estudo;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Glauber on 31/01/2016.
 */
public class PermissoesUsuario extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.permissoes);
    }
}
