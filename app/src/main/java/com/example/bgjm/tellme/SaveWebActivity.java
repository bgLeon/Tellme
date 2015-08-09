package com.example.bgjm.tellme;

/**
 * @author hefesto
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class SaveWebActivity extends Activity {
    private static final String TAG = SaveWebActivity.class.getSimpleName();
    private EditText editTitulo;
    private EditText editUrl;
    private Button buttonGuardar;
    private Button buttonCancelar;
    private WebsDbAdaptor webDbAdaptor;
    private long id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_save);
        webDbAdaptor = new WebsDbAdaptor(this);
        editTitulo = (EditText) findViewById(R.id.editTitulo);
        editUrl = (EditText) findViewById(R.id.editUrl);

        buttonGuardar = (Button) findViewById(R.id.saveButton);
        buttonGuardar.setOnClickListener(new MiButtonGuardarOnClickListener());
        buttonCancelar = (Button) findViewById(R.id.cancelButton);
        buttonCancelar
                .setOnClickListener(new MiButtonCancelarOnClickListener());
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Toast.makeText(this, getString(R.string.error_detalle),
                    Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error sin extras ");
            finish();
        }
        int requestCode = extras.getInt(ListaWebsActivity.REQUEST_CODE);
        if (requestCode == ListaWebsActivity.MODIFICA_WEB) {
            Web web = (Web) extras.getSerializable(Web.WEB);
            id = extras.getLong(webDbAdaptor.COL_ID);
            String titulo = web.getTitulo();
            String url = web.getUrl();
            if (titulo != null) {
                editTitulo.setText(titulo);
            }
            ;
            if (url != null) {
                editUrl.setText(url);
            }
            ;
            Log.d(TAG, "Edito " + web);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    private class MiButtonGuardarOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick SaveButton");
            Intent miIntent = new Intent();
            Bundle extras = new Bundle();
            extras.putLong(webDbAdaptor.COL_ID, id);
            String titulo = editTitulo.getText().toString();
            String url = editUrl.getText().toString();
            Web wb = new Web(titulo, url);
            extras.putSerializable(Web.WEB, wb);
            ;
            miIntent.putExtras(extras);
            setResult(RESULT_OK, miIntent);
            decirGuardado();
        }
    }

    private void decirGuardado() {
        Toast.makeText(this, R.string.Web_guardada, Toast.LENGTH_LONG).show();
    }

    private class MiButtonCancelarOnClickListener implements
            View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick CancelButton");
            finish();
        }
    }
}
