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
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.insper.al.matheusp1.projeto2.R;
import br.edu.insper.al.matheusp1.projeto2.data.LoginRepository;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        ExpandableListView elvCompra = (ExpandableListView) findViewById(R.id.elvCompra);

        // cria os grupos
        List<String> lstGrupos = new ArrayList<>();
        lstGrupos.add("Resumo");
        lstGrupos.add("Benefícios");
        lstGrupos.add("Férias");

        // cria os itens de cada grupo
        List<Dados> lstReusmo = new ArrayList<>();
        lstReusmo.add(new Dados("Nome", "vitor"));
        lstReusmo.add(new Dados("Rg", "21421424"));
        lstReusmo.add(new Dados("CPF", "23523532143"));

        List<Dados> lstBeneficios = new ArrayList<>();
        lstBeneficios.add(new Dados("Alface", "23523532143"));
        lstBeneficios.add(new Dados("Tomate", "23523532143"));

        List<Dados> lstFerias = new ArrayList<>();
        lstFerias.add(new Dados("Chave de Fenda", "23523532143"));

        // cria o "relacionamento" dos grupos com seus itens
        HashMap<String, List<Dados>> lstItensGrupo = new HashMap<>();
        lstItensGrupo.put(lstGrupos.get(0), lstReusmo);
        lstItensGrupo.put(lstGrupos.get(1), lstBeneficios);
        lstItensGrupo.put(lstGrupos.get(2), lstFerias);


        // cria um adaptador (BaseExpandableListAdapter) com os dados acima
        Adaptador adaptador = new Adaptador(this, lstGrupos, lstItensGrupo);
        // define o apadtador do ExpandableListView
        elvCompra.setAdapter(adaptador);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_pagto) {
            // Constrói uma Intent que corresponde ao pedido de "iniciar Activity".
            Intent intent = new Intent(MainActivity.this, PgtoActivity.class);
            // Inicia a Activity especificada na Intent.
            startActivity(intent);
        } else if (id == R.id.nav_ferias) {
            // Constrói uma Intent que corresponde ao pedido de "iniciar Activity".
            Intent intent = new Intent(MainActivity.this, FeriasActivity.class);
            // Inicia a Activity especificada na Intent.
            startActivity(intent);
        } else if (id == R.id.nav_IR) {
            // Constrói uma Intent que corresponde ao pedido de "iniciar Activity".
            Intent intent = new Intent(MainActivity.this, ImpostoActivity.class);
            // Inicia a Activity especificada na Intent.
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            // Constrói uma Intent que corresponde ao pedido de "iniciar Activity".
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            // Inicia a Activity especificada na Intent.
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
