package br.edu.insper.al.matheusp1.projeto2.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import br.edu.insper.al.matheusp1.projeto2.R;

public class PgtoActivity extends SecondaryActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pgto);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Constrói uma Intent que corresponde ao pedido de "iniciar Activity".
            Intent intent = new Intent(PgtoActivity.this, MainActivity.class);
            // Inicia a Activity especificada na Intent.
            startActivity(intent);
        } else if (id == R.id.nav_ferias) {
            // Constrói uma Intent que corresponde ao pedido de "iniciar Activity".
            Intent intent = new Intent(PgtoActivity.this, FeriasActivity.class);
            // Inicia a Activity especificada na Intent.
            startActivity(intent);
        } else if (id == R.id.nav_IR) {
            // Constrói uma Intent que corresponde ao pedido de "iniciar Activity".
            Intent intent = new Intent(PgtoActivity.this, ImpostoActivity.class);
            // Inicia a Activity especificada na Intent.
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            // Constrói uma Intent que corresponde ao pedido de "iniciar Activity".
            Intent intent = new Intent(PgtoActivity.this, LoginActivity.class);
            // Inicia a Activity especificada na Intent.
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
