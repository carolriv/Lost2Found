package es.lost2found.lost2found.announceUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import es.lost2found.database.DB_announce;
import es.lost2found.database.DB_typeObject;
import es.lost2found.entities.Announce;
import es.lost2found.entities.ConcretePlace;
import es.lost2found.entities.MapPlace;
import es.lost2found.entities.TransportPlace;
import es.lost2found.lost2found.pickerUI.ColorPickerUI;
import es.lost2found.lost2found.pickerUI.DatePickerUI;
import es.lost2found.R;
import es.lost2found.lost2found.pickerUI.TimePickerUI;
import es.lost2found.lost2found.typeObjectUI.BankCardActivity;
import es.lost2found.lost2found.typeObjectUI.OtherObjectActivity;
import es.lost2found.lost2found.typeObjectUI.PhoneActivity;
import es.lost2found.lost2found.typeObjectUI.TransportCardActivity;
import es.lost2found.lost2found.typeObjectUI.WalletActivity;

public class NewAnnounceActivity extends AppCompatActivity {

    private ArrayAdapter<String> arrayAdapter2;
    private MaterialBetterSpinner spinner2;
    private ArrayList<String> list = new ArrayList<>();
    private String categorie = "";
    private String typePlace;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_announce);

        String[] announceType = {"Perdida", "Hallazgo"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, announceType);
        MaterialBetterSpinner materialDesignSpinner = findViewById(R.id.announce_type);
        materialDesignSpinner.setAdapter(arrayAdapter);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color700));

        arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        spinner2 = findViewById(R.id.listCategories);
        spinner2.setAdapter(arrayAdapter);
        new categoriesDB().execute();

        spinner2.setOnItemClickListener((parent, view, position, id) -> {
            if(parent.getItemAtPosition(position).toString() != null) {
                categorie = parent.getItemAtPosition(position).toString();
            }
        });

        Toolbar tb = findViewById(R.id.toolbar_center);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
    }

    private class categoriesDB extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... strings) {
            return DB_typeObject.getCategories();
        }

        @Override
        protected void onPostExecute(String[] result) {
            updateAdapter(result);
        }
    }

    public void updateAdapter(String[] result) {
        arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, result);
        spinner2.setAdapter(arrayAdapter2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent announce = new Intent(this, AnnounceActivity.class);
        startActivity(announce);
        finish();
        return true;
    }

    public void showTimePickerDialog(View v) {
        DialogFragment hourFragment = new TimePickerUI();
        hourFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment dateFragment = new DatePickerUI();
        dateFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showColorPickerDialog(View v) {
        ColorPickerUI.build()
                .title(R.string.color_dialog_title)
                .colorPreset(Color.BLACK)
                .allowCustom()
                .show(this, "dialog");
    }

    private String YYYYMMDD(String diaMesAnio){
        String anio = "", mes = "", dia = "";

        int ini = 0;

        while(diaMesAnio.charAt(ini) != '/'){
            dia += diaMesAnio.charAt(ini);
            ++ini;
        }
        ++ini;
        while(diaMesAnio.charAt(ini) != '/'){
            mes += diaMesAnio.charAt(ini);
            ++ini;
        }
        ++ini;
        while(ini <= diaMesAnio.length()-1){
            anio += diaMesAnio.charAt(ini);
            ++ini;
        }

        if(Integer.valueOf(dia) < 10)
            dia = "0" + dia;

        if(Integer.valueOf(mes) < 10)
            mes = "0" + mes;

        return (anio + "/" + mes + "/" + dia);
    }

    public void saveData(View view) {
        // Id AUTO_INCREMENT

        // TipoAnuncio (Perdida o Hallazgo)
        MaterialBetterSpinner announceTypeSpinner = findViewById(R.id.announce_type);
        String announceType = announceTypeSpinner.getText().toString();

        // Dia anuncio
        EditText announceDay = findViewById(R.id.date_show);
        String announceDayText;// = announceDay.getText().toString();


        //Invertimos la fecha para que se guarde en la BD como YYYY/MM/DD

        String diaMesAnio = announceDay.getText().toString();
        announceDayText = YYYYMMDD(diaMesAnio);

        // Hora actual (de creacion del anuncio)
        String actualHour = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

        // Hora de la pérdida o hallazgo
        EditText announceLostFoundHourHour = findViewById(R.id.hour_show);
        String announceLostFoundHourHourText = announceLostFoundHourHour.getText().toString();

        // Categoria // categorie variable

        // Color
        SharedPreferences sp = getApplicationContext().getSharedPreferences("colorBtn", 0);
        Integer colorchoice = sp.getInt("colorChoice", 0);
        String colorchoiceText = String.valueOf(colorchoice);

        // PlaceId
        SharedPreferences sp2 = getApplicationContext().getSharedPreferences("placeId", 0);
        Integer placeId = sp2.getInt("idLugar", 0);
        String placeIdText = String.valueOf(placeId);

        // Place (TransportPlace or ConcretePlace
        Object tp = getIntent().getSerializableExtra("transportPlace");
        Object cp = getIntent().getSerializableExtra("concretePlace");
        Object mp = getIntent().getSerializableExtra("mapPlace");
        String place = "";
        if(tp != null) {
            TransportPlace transportPlace = (TransportPlace) tp;
            typePlace = "transport";
            if(((TransportPlace) tp).getTipoTte().equals("metro") || ((TransportPlace) tp).getTipoTte().equals("bus"))
                place = transportPlace.getTipoTte() + ": " + transportPlace.getLine() + " - " + transportPlace.getStation();
            else if(((TransportPlace) tp).getTipoTte().equals("tren")) {
                place = transportPlace.getTipoTte() + ": " + transportPlace.getStation();
            }
        } else if(cp != null){
            ConcretePlace concretePlace = (ConcretePlace) cp;
            typePlace = "concrete";
            place = concretePlace.getCalle() + ", " + concretePlace.getNumber() + ", " + concretePlace.getPostalCode();
        } else if(mp != null) {
            MapPlace mapPlace = (MapPlace) mp;
            typePlace = "map";
            String latitud = String.valueOf(mapPlace.getLatitud());
            String longitud = String.valueOf(mapPlace.getLongitud());
            place = latitud + longitud;
        }

        SharedPreferences sp4 = getSharedPreferences("announcePlace", 0);
        SharedPreferences.Editor ed = sp4.edit();            // Saved the user login credencials.
        ed.putString("place", place);
        ed.apply();

        // UserId
        SharedPreferences sp3 = getApplicationContext().getSharedPreferences("Login", 0);
        Integer userId = sp3.getInt("userId", 0);
        String userIdext = String.valueOf(userId);

        // UserName
        String userName = sp3.getString("nombre", "");

        if(announceType.equalsIgnoreCase("") || announceDayText.equalsIgnoreCase("") || announceLostFoundHourHourText.equalsIgnoreCase("")
                || categorie.equalsIgnoreCase("") || colorchoiceText.equalsIgnoreCase("") || announceDayText.equalsIgnoreCase("")) {
            TextView textView = findViewById(R.id.wrong_information); // || announceBrandText.equalsIgnoreCase("") || announceModelText.equalsIgnoreCase("")
            textView.setText(textView.getResources().getString(R.string.error_txt3));
        } else {
            // Id, TipoAnuncio, HoraActual, DiaAnuncio, HoraPerdidaoHallazgo, Modelo, Marca, Color, idUsuario e idLugar, Categoria (NombreTabla), Lugar, Username
            new announceDB().execute(announceType, actualHour, announceDayText, announceLostFoundHourHourText, colorchoiceText, userIdext, placeIdText, categorie, place, userName);
        }
    }

    private class announceDB extends AsyncTask<String, Void, Announce> {

        private ProgressDialog dialog = new ProgressDialog(NewAnnounceActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Cargando...");
            this.dialog.show();
        }

        @Override
        protected Announce doInBackground(String... strings) {
            return DB_announce.insertAnnounce(strings[0], strings[1], strings[2], strings[3], strings[4], strings[5], strings[6], strings[7], strings[8], strings[9]);
        }

        @Override
        protected void onPostExecute(Announce result) {
            this.dialog.dismiss();
            processNewAnnounce(result);
        }
    }

    private void processNewAnnounce(Announce announce) {
        switch (categorie) {
            case "Cartera": {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("announcePlace", 0);
                String place = sp.getString("place", "");
                Intent intent = new Intent(this, WalletActivity.class);
                intent.putExtra("announce", announce);
                intent.putExtra("categorie", categorie);
                intent.putExtra("typePlace", typePlace);
                intent.putExtra("place", place);
                startActivity(intent);
                break;
            }
            case "Telefono": {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("announcePlace", 0);
                String place = sp.getString("place", "");
                Intent intent = new Intent(this, PhoneActivity.class);
                intent.putExtra("announce", announce);
                intent.putExtra("categorie", categorie);
                intent.putExtra("typePlace", typePlace);
                intent.putExtra("place", place);
                startActivity(intent);
                break;
            }
            case "Tarjeta bancaria": {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("announcePlace", 0);
                String place = sp.getString("place", "");
                Intent intent = new Intent(this, BankCardActivity.class);
                intent.putExtra("announce", announce);
                intent.putExtra("categorie", categorie);
                intent.putExtra("typePlace", typePlace);
                intent.putExtra("place", place);
                startActivity(intent);
                break;
            }
            case "Tarjeta transporte": {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("announcePlace", 0);
                String place = sp.getString("place", "");
                Intent intent = new Intent(this, TransportCardActivity.class);
                intent.putExtra("announce", announce);
                intent.putExtra("categorie", categorie);
                intent.putExtra("typePlace", typePlace);
                intent.putExtra("place", place);
                startActivity(intent);
                break;
            }
            case "Otro": {
                SharedPreferences sp = getApplicationContext().getSharedPreferences("announcePlace", 0);
                String place = sp.getString("place", "");
                Intent intent = new Intent(this, OtherObjectActivity.class);
                intent.putExtra("announce", announce);
                intent.putExtra("categorie", categorie);
                intent.putExtra("typePlace", typePlace);
                intent.putExtra("place", place);
                startActivity(intent);
                break;
            }
        }
        finish();
    }
}
