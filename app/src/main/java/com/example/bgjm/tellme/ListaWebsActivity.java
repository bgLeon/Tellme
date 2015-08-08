package com.example.bgjm.tellme;
/**
 * @author hefesto
 */
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class ListaWebsActivity extends ListActivity{

    private static final String TAG = ListaWebsActivity.class.getSimpleName();
    public static final String REQUEST_CODE = "requestCode";
    public static final int CREA_WEB = 0;
    public static final int MODIFICA_WEB = 1;

    private WebsDbAdaptor webDbAdaptor;
    private Cursor websCursor;
    private SimpleCursorAdapter cursorAdapter;

    private int indiceTitulo;
    private int indiceUrl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_list);
        websCursor = webDbAdaptor.recuperaTodasLasWebs();
        startManagingCursor(websCursor); // le dice a la actividad que gestione
        // el cursor
        indiceTitulo = websCursor.getColumnIndexOrThrow(webDbAdaptor.COL_TITULO);
        indiceUrl=websCursor.getColumnIndexOrThrow(webDbAdaptor.COL_URL);
        // Crea un array para indicar los campos que queremos mostrar en la
        // lista
        String[] from = new String[] { webDbAdaptor.COL_TITULO,
                webDbAdaptor.COL_URL, };
        // y un array con los campos de la plantilla que queremos asignarles

        int[] to = new int[] { android.R.id.text1, android.R.id.text2 };
        // Creamos un SimpleCursorAdaptor y escogemos una plantilla de android
        // para mostrar 2 campos
        cursorAdapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_2, // PodrÃ­a usarse
                // android.R.layout.simple_list_item_2
                websCursor, from, to);
        setListAdapter(cursorAdapter);
    }
    private void actualizaLista() {
        websCursor.requery();
        cursorAdapter.notifyDataSetChanged();
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

    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Cursor c = websCursor;
        c.moveToPosition(position);
        Web web = new Web(c.getString(indiceTitulo),
                c.getString(indiceUrl));

        Intent intent = new Intent(this, SaveWebActivity.class);
        Bundle extras = new Bundle();
        extras.putLong(WebsDbAdaptor.COL_ID, id);
        extras.putSerializable(Web.WEB, web);
        extras.putInt(REQUEST_CODE, MODIFICA_WEB);
        intent.putExtras(extras);
        startActivityForResult(intent, MODIFICA_WEB);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lista, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.info: {
                Toast.makeText(this, getString(R.string.msg_info),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            case R.id.Annadir: {
                Intent miIntent = new Intent(this, SaveWebActivity.class);
                miIntent.putExtra(REQUEST_CODE, CREA_WEB);
                startActivityForResult(miIntent, CREA_WEB);
                return true;
            }
            case R.id.BorrarLista: {
                borrarLista();
                actualizaLista();
                return true;

            }

            default: {
                Log.w(TAG, "Opción desconocida " + item.getItemId());
                return false;
            }
        }
    }
        private void borrarLista() {
            AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
            dialogo.setMessage(R.string.dialogo_borrar_lista);
            dialogo.setPositiveButton(android.R.string.ok,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            webDbAdaptor.borraTodasLasWebs();
                            actualizaLista();
                        }
                    });
            dialogo.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.w(TAG, "Cancelado borrar lista");

                        }
                    });
            AlertDialog confirma = dialogo.create();
            confirma.show();
        }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_CANCELED) {
            Log.i(TAG, "Cancelado");
            return;
        }
        Bundle extras = intent.getExtras();
        if (extras == null) {
            Toast.makeText(this, getString(R.string.error_detalle),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        for (String s : extras.keySet()) {
            Log.i(TAG, "extra " + s);
        }
        Web web = (Web) extras.getSerializable(Web.WEB);

        switch (requestCode) {
            case CREA_WEB: {
                Log.i(TAG, "Crea producto " + web);
                webDbAdaptor.creaWeb(web);

                cursorAdapter.notifyDataSetChanged();
                actualizaLista();
                break;
            }
            case MODIFICA_WEB: {
                Long id = extras.getLong(WebsDbAdaptor.COL_ID);
                Log.i(TAG, "Modifica producto " + id);
                if (id == null) {
                    Log.e(TAG, getString(R.string.error_detalle));
                    finish();
                }

                webDbAdaptor.actualizaWeb(id, web);
                actualizaLista();
                break;
            }
            default: {
                Log.e(TAG, "Opción no conocida " + requestCode);
            }
        }

    }
}
