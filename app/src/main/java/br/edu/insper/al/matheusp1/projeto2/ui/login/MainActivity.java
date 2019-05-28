package br.edu.insper.al.matheusp1.projeto2.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.edu.insper.al.matheusp1.projeto2.R;

public class MainActivity extends SecondaryActivity implements ValueEventListener {

    private TextView nome;
    private TextView email;

    private static final String TAG = "MainActivity";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String cpf = intent.getStringExtra(LoginActivity.KEY);

        // Obtém uma referência para o banco de dados.
        // que foi especificado durante a configuração.
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        // Obtém uma referência para o caminho /a do banco de dados.
        DatabaseReference user = database.getReference(cpf);
        DatabaseReference NOME = user.child("Nome");
        DatabaseReference EMAIL = user.child("E-mail");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);

        ExpandableListView elvCompra = findViewById(R.id.elvCompra);

        // cria os grupos
        List<String> lstGrupos = new ArrayList<>();
        lstGrupos.add(getString(R.string.resumo));
        lstGrupos.add(getString(R.string.beneficios));
        lstGrupos.add(getString(R.string.financeiro));

        // cria os itens de cada grupo
        List<Dados> lstReusmo = new ArrayList<>();
        lstReusmo.add(new Dados("Nome", "Vítor Calcete Marques"));
        lstReusmo.add(new Dados("RG", "3996669-X"));
        lstReusmo.add(new Dados("CPF", "385.001.958-60"));

        List<Dados> lstBeneficios = new ArrayList<>();
        lstBeneficios.add(new Dados("Vale Transporte (R$)", "450,00"));
        lstBeneficios.add(new Dados("Vale Refeição (R$)", "1050,00"));
        lstBeneficios.add(new Dados("Vale Alimentação (R$)", "250,00"));
        lstBeneficios.add(new Dados("Seguro (R$)", "550,00"));

        List<Dados> lstFinanceiro = new ArrayList<>();
        lstFinanceiro.add(new Dados("Salário (R$)", "6500,00"));

        // cria o "relacionamento" dos grupos com seus itens
        HashMap<String, List<Dados>> lstItensGrupo = new HashMap<>();
        lstItensGrupo.put(lstGrupos.get(0), lstReusmo);
        lstItensGrupo.put(lstGrupos.get(1), lstBeneficios);
        lstItensGrupo.put(lstGrupos.get(2), lstFinanceiro);

        // cria um adaptador (BaseExpandableListAdapter) com os dados acima
        Adaptador adaptador = new Adaptador(this, lstGrupos, lstItensGrupo);
        // define o apadtador do ExpandableListView
        elvCompra.setAdapter(adaptador);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Adiciona esta Activity à lista de
        // observadores de mudanças em /b.
        NOME.addValueEventListener(this);
        EMAIL.addValueEventListener(this);
    }

    private void signOut() {
        mAuth.signOut();
        Toast.makeText(getApplicationContext(), "Até mais!", Toast.LENGTH_LONG).show();
        // Constrói uma Intent que corresponde ao pedido de "iniciar Activity".
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        // Inicia a Activity especificada na Intent.
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            signOut();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Este método é chamado uma vez durante a chamada
    // de addValueEventListener acima e depois sempre
    // que algum valor em /b sofrer alguma mudança.
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        String text;
        try {

            // O método getValue recebe como parâmetro uma
            // classe Java que representa o tipo de dado
            // que você acredita estar lá. Se você errar,
            // esse método vai lançar uma DatabaseException.
            text = dataSnapshot.getValue(String.class);
        } catch (DatabaseException exception) {
            text = "Failed to parse value";
        }
        nome.setText(text);
        email.setText(text);
    }

    // Este método é chamado caso ocorra algum problema
    // com a conexão ao banco de dados do Firebase.
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        nome.setText("Failed to read value");
    }
}
