package es.lost2found.lost2found.seekerUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import es.lost2found.R;
import es.lost2found.database.DB_typeObject;
import es.lost2found.entities.Announce;
import es.lost2found.lost2found.announceUI.AnnounceActivity;
import es.lost2found.lost2found.announceUI.matchAnnounceUI.MatchAnnounce;

public class SeekerAnnounceInfoActivity extends AppCompatActivity {
    private Announce a;
    private String atributoDeterminante;
    private boolean openDataMatching = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekerannounce_info);

        Toolbar tb = findViewById(R.id.toolbar_center);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color700));

        TextView cat = findViewById(R.id.categoria);
        TextView type = findViewById(R.id.tipo);
        TextView dia = findViewById(R.id.dia);
        TextView lugar = findViewById(R.id.lugar);
        TextView usuario = findViewById(R.id.usuario);
        TextView hora = findViewById(R.id.hora);
        TextView textoColor = findViewById(R.id.colorTexto);
        View color = findViewById(R.id.color_view);

        if(getIntent().getBooleanExtra("back", false)) {
            a = (Announce) getIntent().getSerializableExtra("oldAnnounce");
        } else {
            a = (Announce) getIntent().getSerializableExtra("myAnnounce");
        }

        String idText = String.valueOf(a.getAnnounceId());
        new getObjectDataFromDB().execute(idText, a.announceCategorie);

        String c = "<h4> <font color=#699CFC> Categoría: </font>"+ a.announceCategorie +" </h4><br>";
        cat.setText(Html.fromHtml(c));

        String t = "<h4> <font color=#699CFC> Tipo: </font>"+ a.announceType +" </h4><br>";
        type.setText(Html.fromHtml(t));

        String d = "<h4> <font color=#699CFC> Día: </font>"+ a.DDMMYYYY() +" </h4><br>";
        dia.setText(Html.fromHtml(d));

        String h = "<h4> <font color=#699CFC> Hora: </font>"+ a.announceHourText +" </h4><br>";
        hora.setText(Html.fromHtml(h));

        String l = "<h4> <font color=#699CFC> Lugar: </font>"+ a.place +" </h4><br>";
        lugar.setText(Html.fromHtml(l));

        String u = "<h4> <font color=#699CFC> Creador del anuncio: </font>"+ a.userOwner +" </h4><br>";
        usuario.setText(Html.fromHtml(u));

        String col = "<h4> <font color=#699CFC> Color: </font> </h4><br>";
        textoColor.setText(Html.fromHtml(col));

        color.setBackgroundColor(a.color);

        ImageView image = findViewById(R.id.imageinfoannounce);

        SharedPreferences spref = getApplicationContext().getSharedPreferences("Login", 0);
        if (spref.contains("nombre")) {
            String actualUser = spref.getString("nombre", "");
            if(!actualUser.equals(a.userOwner)) {
                Button deleteButton = findViewById(R.id.delete);
                deleteButton.setVisibility(View.GONE);
            }
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String parentAct = bundle.getString("parentAct");
            if(parentAct != null) {
                if (parentAct.equals("seeker")) {
                    Button matchButton = findViewById(R.id.match);
                    matchButton.setVisibility(View.GONE);
                    Button matchOpenDataButton = findViewById(R.id.open_data_match);
                    matchOpenDataButton.setVisibility(View.GONE);

                }
            }
        }

        switch (a.announceCategorie) {
            case "Telefono": image.setImageResource(R.drawable.ic_smartphone);
                  break;
            case "Cartera":  image.setImageResource(R.drawable.ic_wallet);
                break;
            case "Otro": image.setImageResource(R.drawable.ic_other);
                break;
            default: image.setImageResource(R.drawable.ic_card);
                break;
        }
    }

    private class getObjectDataFromDB extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(SeekerAnnounceInfoActivity.this);

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Cargando...");
            this.dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            return DB_typeObject.getDataObjectById(strings[0], strings[1]);
        }

        @Override
        protected void onPostExecute(String dataObject) {
            if(dataObject != null) {
                TextView param = (TextView) findViewById(R.id.param);
                switch (a.announceCategorie) {
                    case "Telefono": {
                        String params[] = dataObject.split(",");
                        if (params[2].equalsIgnoreCase(" ")) {
                            String o = "<h4> <font color=#699CFC> Datos: </font><br>" + "Marca: " + params[0] + ", Modelo: " + params[1] + " </h4><br>";
                            param.setText(Html.fromHtml(o));
                            atributoDeterminante = params[0];
                        } else {
                            String o = "<h4> <font color=#699CFC> Datos: </font><br>" + "Marca: " + params[0] + ", Modelo: " + params[1] + "<br>" + "tara: " + params[2] + " </h4><br>";
                            param.setText(Html.fromHtml(o));
                            atributoDeterminante = params[0];
                        }
                        break;
                    }
                    case "Cartera": {
                        String params[] = dataObject.split(",");
                        if (params[1].equalsIgnoreCase(" ")) {
                            String o = "<h4> <font color=#699CFC> Datos: </font><br>" + "Marca: " + params[0] + " </h4><br>";
                            param.setText(Html.fromHtml(o));
                            atributoDeterminante = params[0];
                        } else {
                            String o = "<h4> <font color=#699CFC> Datos: </font><br>" + "Marca: " + params[0] + ", Documentacion: " + params[1] + " </h4><br>";
                            param.setText(Html.fromHtml(o));
                            atributoDeterminante = params[0];
                        }
                        break;
                    }
                    case "Otro": {
                        String params[] = dataObject.split(",");
                        String o = "<h4> <font color=#699CFC> Datos: </font><br>" + params[0] + ", " + params[1] + " </h4><br>";
                        param.setText(Html.fromHtml(o));
                        atributoDeterminante = params[0];
                        break;
                    }
                    case "Tarjeta bancaria": {
                        String params[] = dataObject.split(", ");
                        String o = "<h4> <font color=#699CFC> Datos: </font><br>" + "Banco: " + params[0] + ", Propietario: " + params[1] + " </h4><br>";
                        param.setText(Html.fromHtml(o));
                        atributoDeterminante = params[1];
                        break;
                    }
                    case "Tarjeta transporte": {
                        String params[] = dataObject.split(",");
                        String o = "<h4> <font color=#699CFC> Datos: </font><br>" + "Propietario: " + params[0] + " </h4><br>";
                        param.setText(Html.fromHtml(o));
                        atributoDeterminante = params[0];
                        break;
                    }
                }
            }
            this.dialog.dismiss();
        }
    }

    public void matching(View v) {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            openDataMatching = false;
            final Intent match = new Intent(this, MatchAnnounce.class);
            String typePlace = getIntent().getExtras().getString("typePlace");
            match.putExtra("openDataMatching", openDataMatching);
            match.putExtra("typePlace", typePlace);
            match.putExtra("oldAnnounceSet", true);
            match.putExtra("match", a);
            match.putExtra("atributoDeterminante", atributoDeterminante);
            startActivity(match);
        }
        finish();
    }

    public void open_data_matching(View v) {
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            openDataMatching = true;
            String typePlace = getIntent().getExtras().getString("typePlace");
            final Intent match = new Intent(this, MatchAnnounce.class);
            match.putExtra("announceType", a.announceType);
            match.putExtra("openDataMatching", openDataMatching);
            match.putExtra("oldAnnounceSet", true);
            match.putExtra("match", a);
            match.putExtra("typePlace", typePlace);
            match.putExtra("place", a.place);
            startActivity(match);
        }
        finish();
    }

    public void delete(View v) {
        // Elimina el anuncio actual
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String parentAct = bundle.getString("parentAct");
            if(parentAct != null) {
                if(parentAct.equals("announce")) {
                    Intent announce = new Intent(this, AnnounceActivity.class);
                    Announce an = (Announce) getIntent().getSerializableExtra("myAnnounce");
                    announce.putExtra("announce", an);
                    announce.putExtra("delete", a);
                    startActivity(announce);
                } else if(parentAct.equals("seeker")) {
                    Intent seeker = new Intent(this, SeekerActivity.class);
                    String place = getIntent().getExtras().getString("place");
                    getIntent().putExtra("place", place);
                    seeker.putExtra("delete", a);
                    startActivity(seeker);
                }
            } else {
                Intent announce = new Intent(this, AnnounceActivity.class);
                announce.putExtra("delete", a);
                startActivity(announce);
            }
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            String parentAct = bundle.getString("parentAct");
            if(parentAct != null) {
                if(parentAct.equals("announce")) {
                    Intent announce = new Intent(this, AnnounceActivity.class);
                    Announce an = (Announce) getIntent().getSerializableExtra("myAnnounce");
                    announce.putExtra("announce", an);
                    startActivity(announce);
                } else if(parentAct.equals("seeker")) {
                    Intent seeker = new Intent(this, SeekerActivity.class);
                    String place = getIntent().getExtras().getString("place");
                    getIntent().putExtra("place", place);
                    startActivity(seeker);
                }
            } else {
                Intent announce = new Intent(this, AnnounceActivity.class);
                startActivity(announce);
            }
        }
        finish();
        return true;
    }
}
