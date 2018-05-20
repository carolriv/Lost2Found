package es.lost2found.lost2foundUI.otherUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import es.lost2found.R;
import es.lost2found.lost2foundUI.announceUI.AnnounceActivity;
import es.lost2found.lost2foundUI.chatUI.ChatActivity;
import es.lost2found.lost2foundUI.loginUI.LoginActivity;
import es.lost2found.lost2foundUI.openDataUI.OpenDataActivity;
import es.lost2found.lost2foundUI.seekerUI.SeekerActivity;

public class HelpActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        }
        mDrawerLayout = findViewById(R.id.drawer_layout);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.color700));

        NavigationView navView = findViewById(R.id.nav_view);

        String s = "<h2> <font color=#1976D2>¿Cómo funciona Lost2Found? </font></h2> &bull; Cuando una persona pierde un objeto lo notifica creando un anuncio de pérdida y rellenando los máximos campos posibles.<br><br>" +
                "&bull; Otra persona encuentra ese mismo objeto y crea un anuncio de hallazgo, completando todos los campos que le sea posible. <br><br>" +
                "&bull; Lost2Found cuenta con un algoritmo basado en un sistema de puntos, es decir, ambos anuncios deben tener características iguales y," +
                " cuantas más se acierten, más puntos se obtiene y podrá ser un posible acierto. <br><br>" +
                "&bull; La aplicación notificará a ambas personas de una coincidencia de objetos y les otorgará la posibilidad de ponerse en contacto. <br><br>" +
                "<h2> <font color=#1976D2>¿Puede haber anuncios cuyos procesos coincidan simultáneamente? </font></h2> &bull; Perfectamente. Varios objetos pueden reunir las condiciones necesarias para que sus coincidencias sean similares " +
                "(objetos parecidos, mismo lugar, fecha y hora). Por eso es imprescindible que los usuarios hablen entre ellos y se aseguren de que el objeto es el correcto.<br>" +
                "<h2> <font color=#1976D2>¿Cuándo me llegará la notificación? </font></h2> &bull; Tan pronto como ambas partes registren los anuncios de pérdida y hallazgo y nuestro algoritmo haga su magia.<br>" +
                "<h2> <font color=#1976D2>¿En qué se basa nuestro algoritmo? </font></h2> &bull; Cuando estamos comparando dos objetos, uno de pérdida y otro de hallazgo, dentro de la aplicación, el algoritmo usa factores como el color y la " +
                "distancia de ambos objetos. Cuanto más parecidos y más cerca estén, el porcentaje de que sea el mismo objeto es bastante alto. <br><br>" +
                "&bull; En cambio, cuando el match es con Open Data se basa en la distancia únicamente cuando está disponible. No todos los objetos que están registrados en el portal abierto tienen una dirección. <br>"+
                "<h2> <font color=#1976D2>¿Tengo que entrar en la aplicación para saber si tengo algún mensaje?</font></h2> &bull; ¡Claro que no! Nuestro chat, fácil e intuitivo de usar, se actualiza cada 15 segundos para que nuestros usuarios " +
                "no estén pendientes de meterse cada poco tiempo, sino que sea la aplicación quien avise.<br>";
        TextView texto = findViewById(R.id.textComoFunciona);
        texto.setText(Html.fromHtml(s));

        texto.setMovementMethod(new ScrollingMovementMethod());

        View headerLayout = navView.getHeaderView(0);
        TextView emailUser = headerLayout.findViewById(R.id.user_mail);
        TextView nameUser = headerLayout.findViewById(R.id.user_name);
        SharedPreferences spref = getApplicationContext().getSharedPreferences("Login", 0);
        if(spref != null) {
            if (spref.contains("email")) {
                String userEmail = spref.getString("email", "");
                emailUser.setText(userEmail);
            }
            if (spref.contains("nombre")) {
                String userName = spref.getString("nombre", "");
                nameUser.setText(userName);
            }
        }

        final Intent home = new Intent(this, AnnounceActivity.class);
        final Intent buscar = new Intent(this, SeekerActivity.class);
        final Intent aboutus = new Intent(this, AboutUsActivity.class);
        final Intent chat = new Intent(this, ChatActivity.class);
        final Intent contact = new Intent(this, ContactActivity.class);
        final Intent rate = new Intent(this, RateActivity.class);
        final Intent config = new Intent(this, SettingsActivity.class);
        final Intent openData = new Intent(this, OpenDataActivity.class);

        navView.setNavigationItemSelectedListener(
                menuItem -> {
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();

                    if(menuItem.getItemId()== R.id.nav_home) {
                        startActivity(home);
                        finish();
                    }else if(menuItem.getItemId()== R.id.nav_search) {
                        startActivity(buscar);
                        finish();
                    }else if(menuItem.getItemId()== R.id.nav_info) {
                        startActivity(aboutus);
                        finish();
                    } else if(menuItem.getItemId()== R.id.nav_chat) {
                        startActivity(chat);
                        finish();
                    } else if(menuItem.getItemId() == R.id.nav_settings) {
                        startActivity(config);
                        finish();
                    }else if(menuItem.getItemId()== R.id.nav_contact) {
                        startActivity(contact);
                        finish();
                    } else if(menuItem.getItemId()== R.id.nav_open_data) {
                        startActivity(openData);
                        finish();
                    } else if(menuItem.getItemId()== R.id.nav_feedback) {
                        startActivity(rate);
                        finish();
                    } else if(menuItem.getItemId()== R.id.nav_logout) {
                        logoutUser();
                    }
                    return true;
                }
        );
        navView.setCheckedItem(R.id.nav_help);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        SharedPreferences sp = getSharedPreferences("Login", 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("email", null);
        ed.putString("nombre", null);
        ed.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
