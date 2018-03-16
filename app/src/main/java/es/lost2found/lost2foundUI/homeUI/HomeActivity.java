package es.lost2found.lost2foundUI.homeUI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import es.lost2found.R;
import es.lost2found.entities.Announce;
import es.lost2found.lost2foundUI.chatUI.ChatActivity;
import es.lost2found.lost2foundUI.seekerUI.SeekerActivity;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navView = findViewById(R.id.nav_view);

        final Intent buscar = new Intent(this, SeekerActivity.class);
        final Intent chat = new Intent(this, ChatActivity.class);

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected

                        if(menuItem.getItemId()== R.id.nav_search) {
                            startActivity(buscar);
                        }else if(menuItem.getItemId()== R.id.nav_chat) {
                            startActivity(chat);
                        }

                        return true;
                    }
                }
        );
        navView.setCheckedItem(R.id.nav_home);
        /*
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {

                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                    }
                }
        );
        */

        // In this example we fill announceList with a function fill_with_data(), in the future we'll do it with the database info
       List<Announce> announceList = new ArrayList<>();
       Announce announce = new Announce();
       announce.fill_with_data(announceList);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.announce_recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(announceList, getApplication());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adding a ItemAnimator to the RecyclerView (Optional)
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        /*recyclerView.setItemAnimator(itemAnimator);*/
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

    /**
     *
     * @param view
     */
    public void home(View view) {
        /*EditText editText = (EditText) findViewById(R.id.email);
        String name = editText.getText().toString();

        editText = (EditText) findViewById(R.id.name);
        String email= editText.getText().toString();

        editText = (EditText) findViewById(R.id.password);
        String pass = editText.getText().toString();

        editText = (EditText) findViewById(R.id.repassword);
        String confirmPass = editText.getText().toString();

        String role = "Student";

        if(name.equalsIgnoreCase("") || email.equalsIgnoreCase("") || pass.equalsIgnoreCase("") || confirmPass.equalsIgnoreCase("")) {
            this.msgerror = "Please, complete all the fields.";
            TextView textView = (TextView) findViewById(R.id.user_already_exists);
            textView.setText(this.msgerror);
        }
        else if(!pass.equals(confirmPass)) {
            this.msgerror = "Passwords doesn't match.";
            TextView textView = (TextView) findViewById(R.id.user_already_exists);
            textView.setText(this.msgerror);
        }
        //else
        //new RegisterDB().execute(email, pass, name, role);*/
    }
}