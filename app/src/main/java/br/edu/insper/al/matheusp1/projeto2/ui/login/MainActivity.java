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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import br.edu.insper.al.matheusp1.projeto2.R;

@SuppressWarnings("unchecked")
public class MainActivity extends SecondaryActivity implements ValueEventListener {

    private TextView nome;
    private TextView email;
//  Criando HashMap para pegar dados da pessoa
private static HashMap<String, String> personInfos;
    private final List<Dados> lstInfosCadastrais = new ArrayList<>();
    private final List<Dados> lstDocumentos = new ArrayList<>();
    private final List<Dados> lstAvante = new ArrayList<>();
    private final List<Dados> lstBanco = new ArrayList<>();
    private final List<Dados> lstFerias = new ArrayList<>();

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
        user.addValueEventListener(this);

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

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
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
        try {

            // O método getValue recebe como parâmetro uma
            // classe Java que representa o tipo de dado
            // que você acredita estar lá. Se você errar,
            // esse método vai lançar uma DatabaseException.
            personInfos = (HashMap<String, String>) dataSnapshot.getValue();

        } catch (DatabaseException exception) {
            Log.d("MainActivy", exception.getMessage());
        }

        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);

        ExpandableListView elvCompra = findViewById(R.id.elvCompra);

        // cria os grupos
        List<String> lstGrupos = new ArrayList<>();
        lstGrupos.add(getString(R.string.infos_cadastrais));
        lstGrupos.add(getString(R.string.documentos));
        lstGrupos.add(getString(R.string.avante));
        lstGrupos.add(getString(R.string.banco));
        lstGrupos.add(getString(R.string.ferias));

        // cria o "relacionamento" dos grupos com seus itens
        HashMap<String, List<Dados>> lstItensGrupo = new HashMap<>();
        lstItensGrupo.put(lstGrupos.get(0), lstInfosCadastrais);
        lstItensGrupo.put(lstGrupos.get(1), lstDocumentos);
        lstItensGrupo.put(lstGrupos.get(2), lstAvante);
        lstItensGrupo.put(lstGrupos.get(3), lstBanco);
        lstItensGrupo.put(lstGrupos.get(4), lstFerias);

        // cria um adaptador (BaseExpandableListAdapter) com os dados acima
        Adaptador adaptador = new Adaptador(this, lstGrupos, lstItensGrupo);
        // define o apadtador do ExpandableListView
        elvCompra.setAdapter(adaptador);
        String nome_upper = Objects.requireNonNull(personInfos.get("Nome")).toUpperCase();
        String nome_lower = Objects.requireNonNull(personInfos.get("Nome")).toLowerCase();
        StringBuilder first_name = new StringBuilder(nome_upper.substring(0, 1));
        for (int i = 1; i < nome_upper.length(); i++) {
            if (nome_upper.toCharArray()[i] == ' ') {
                break;
            }
            else {
                first_name.append(nome_lower.toCharArray()[i]);
            }
        }
        if (Objects.requireNonNull(personInfos.get("Sexo")).toCharArray()[0] == 'M') {
            Toast.makeText(getApplicationContext(), getString(R.string.welcomeM)
                    + ", " + first_name + "!", Toast.LENGTH_LONG).show();
        }
        else if (Objects.requireNonNull(personInfos.get("Sexo")).toCharArray()[0] == 'F') {
            Toast.makeText(getApplicationContext(), getString(R.string.welcomeF) + ", "
                    + first_name + "!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.welcome) + ", "
                    + first_name + "!", Toast.LENGTH_LONG).show();
        }

        nome.setText(personInfos.get("Nome"));
        email.setText(Objects.requireNonNull(personInfos.get("E-mail")).toLowerCase());
        // cria os itens de cada grupo
        lstInfosCadastrais.add(new Dados("Sexo", personInfos.get("Sexo")));
        lstInfosCadastrais.add(new Dados("Idade", personInfos.get("Idade")));
        lstInfosCadastrais.add(new Dados("Data de Nascimento",
                personInfos.get("Dt Nascimento")));
        lstInfosCadastrais.add(new Dados("UF Nascimento", personInfos.get("UF Nascimento")));
        lstInfosCadastrais.add(new Dados("Cidade de Nascimento",
                personInfos.get("Cidade Nascimento")));
        String estadoCivil_upper = Objects.requireNonNull(personInfos.get("Estado civil")).toUpperCase();
        String estadoCivil_lower = Objects.requireNonNull(personInfos.get("Estado civil")).toLowerCase();
        StringBuilder estadoCivil_final = new StringBuilder(estadoCivil_upper.substring(0, 1));
        for (int i = 1; i < estadoCivil_upper.length(); i++) {
            if (estadoCivil_upper.toCharArray()[i - 1] == ' ') {
                estadoCivil_final.append(estadoCivil_upper.toCharArray()[i]);
            }
            else {
                estadoCivil_final.append(estadoCivil_lower.toCharArray()[i]);
            }
        }
        lstInfosCadastrais.add(new Dados("Estado Civil", estadoCivil_final.toString()));
        lstInfosCadastrais.add(new Dados("UF Residência", personInfos.get("UF Residência")));
        lstInfosCadastrais.add(new Dados("Cidade de Residência",
                personInfos.get("Cidade Residência")));
        String bairro_upper = Objects.requireNonNull(personInfos.get("Bairro")).toUpperCase();
        String bairro_lower = Objects.requireNonNull(personInfos.get("Bairro")).toLowerCase();
        StringBuilder bairro_final = new StringBuilder(bairro_upper.substring(0, 1));
        for (int i = 1; i < bairro_upper.length(); i++) {
            if (bairro_upper.toCharArray()[i - 1] == ' ') {
                bairro_final.append(bairro_upper.toCharArray()[i]);
            }
            else {
                bairro_final.append(bairro_lower.toCharArray()[i]);
            }
        }
        lstInfosCadastrais.add(new Dados("Bairro", bairro_final.toString()));
        String endereco_upper = Objects.requireNonNull(personInfos.get("Endereço")).toUpperCase();
        String endereco_lower = Objects.requireNonNull(personInfos.get("Endereço")).toLowerCase();
        StringBuilder endereco_final = new StringBuilder(endereco_upper.substring(0, 1));
        for (int i = 1; i < endereco_upper.length(); i++) {
            if (endereco_upper.toCharArray()[i - 1] == ' ') {
                endereco_final.append(endereco_upper.toCharArray()[i]);
            }
            else {
                endereco_final.append(endereco_lower.toCharArray()[i]);
            }
        }
        lstInfosCadastrais.add(new Dados("Endereço", endereco_final
                + ", " + personInfos.get("Número")));
        if (personInfos.get("Complemento") != null) {
            String complemento_upper = Objects.requireNonNull(personInfos.get("Complemento")).toUpperCase();
            String complemento_lower = Objects.requireNonNull(personInfos.get("Complemento")).toLowerCase();
            StringBuilder complemento_final = new StringBuilder(complemento_upper.substring(0, 1));
            for (int i = 1; i < complemento_upper.length(); i++) {
                if (complemento_upper.toCharArray()[i - 1] == ' ') {
                    complemento_final.append(complemento_upper.toCharArray()[i]);
                }
                else {
                    complemento_final.append(complemento_lower.toCharArray()[i]);
                }
            }
            lstInfosCadastrais.add(new Dados("Complemento", complemento_final.toString()));
        }
        lstInfosCadastrais.add(new Dados("CEP", personInfos.get("CEP")));
        lstInfosCadastrais.add(new Dados("Grau de Instrução",
                personInfos.get("Grau de Instrução")));
        if (personInfos.get("Telefone Residência") != null) {
            lstInfosCadastrais.add(new Dados("Telefone Residencial", "("
                    + Objects.requireNonNull(personInfos.get("Telefone Residência")).substring(0,2) + ") "
                    + Objects.requireNonNull(personInfos.get("Telefone Residência")).substring(2)));
        }
        if (personInfos.get("Telefone Celular") != null) {
            lstInfosCadastrais.add(new Dados("Telefone Celular", "("
                    + Objects.requireNonNull(personInfos.get("Telefone Celular")).substring(0,2) + ") "
                    + Objects.requireNonNull(personInfos.get("Telefone Celular")).substring(2)));
        }

        if (personInfos.get("Dígito RG") != null) {
            lstDocumentos.add(new Dados("RG", personInfos.get("RG") + "-" +
                    personInfos.get("Dígito RG")));
        }
        else {
            lstDocumentos.add(new Dados("RG", personInfos.get("RG")));
        }
        lstDocumentos.add(new Dados("Data Expedição RG",
                personInfos.get("Data Expedição RG")));
        if (Objects.requireNonNull(personInfos.get("Órgão Expedidor RG")).equals("Secretaria de Segurança Publica")) {
            lstDocumentos.add(new Dados("Órgão Expedidor RG", "SSP"));
        }
        else {
            lstDocumentos.add(new Dados("Órgão Expedidor RG",
                    personInfos.get("Órgão Expedidor RG")));
        }

        lstDocumentos.add(new Dados("CPF", Objects.requireNonNull(personInfos.get("CPF")).substring(0,3)
                + "." + Objects.requireNonNull(personInfos.get("CPF")).substring(3,6) + "."
                + Objects.requireNonNull(personInfos.get("CPF")).substring(6,9) + "-"
                + Objects.requireNonNull(personInfos.get("CPF")).substring(9)));
        lstDocumentos.add(new Dados("PIS/PASEP", personInfos.get("PIS OU PASEP")));
        if (personInfos.get("Dt Cadastro PIS") != null) {
            lstDocumentos.add(new Dados("Data Cadastro PIS", personInfos.get("Dt Cadastro PIS")));
        }
        lstDocumentos.add(new Dados("CTPS", personInfos.get("CTPS")));
        lstDocumentos.add(new Dados("Série CTPS", personInfos.get("Serie CTPS")));
        lstDocumentos.add(new Dados("Data Expedição CTPS", personInfos.get("Dt Exp CTPS")));
        lstDocumentos.add(new Dados("UF CTPS", personInfos.get("UF CTPS")));
        if (personInfos.get("Certificado Militar") != null) {
            lstDocumentos.add(new Dados("Certificado Militar",
                    personInfos.get("Certificado Militar")));
        }
        if (personInfos.get("Categoria Militar") != null) {
            lstDocumentos.add(new Dados("Categoria Militar",
                    Objects.requireNonNull(personInfos.get("Categoria Militar")).toUpperCase()));
        }
        lstDocumentos.add(new Dados("Título de Eleitor", personInfos.get("Titulo Eleitor")));
        lstDocumentos.add(new Dados("UF Votação", personInfos.get("UF Votação")));
        lstDocumentos.add(new Dados("Zona Eleitoral", personInfos.get("Zona Eleitoral")));
        lstDocumentos.add(new Dados("Seção Eleitoral", personInfos.get("Seção Eleitoral")));

        lstAvante.add(new Dados("RE", personInfos.get("RE")));
        lstAvante.add(new Dados("Data de Admissão", personInfos.get("Dt Admissão")));
        String cargo_upper = Objects.requireNonNull(personInfos.get("Cargo")).toUpperCase();
        String cargo_lower = Objects.requireNonNull(personInfos.get("Cargo")).toLowerCase();
        StringBuilder cargo_final = new StringBuilder(cargo_upper.substring(0, 1));
        for (int i = 1; i < cargo_upper.length(); i++) {
            if (cargo_upper.toCharArray()[i - 1] == ' ') {
                cargo_final.append(cargo_upper.toCharArray()[i]);
            } else {
                cargo_final.append(cargo_lower.toCharArray()[i]);
            }
        }
        lstAvante.add(new Dados("Cargo", cargo_final.toString()));
        String dept_upper = Objects.requireNonNull(personInfos.get("DEPARTAMENTO OU FILIAL")).toUpperCase();
        String dept_lower = Objects.requireNonNull(personInfos.get("DEPARTAMENTO OU FILIAL")).toLowerCase();
        StringBuilder dept_final = new StringBuilder(dept_upper.substring(0, 1));
        for (int i = 1; i < dept_upper.length(); i++) {
            if (dept_upper.toCharArray()[i - 1] == ' ') {
                dept_final.append(dept_upper.toCharArray()[i]);
            } else {
                dept_final.append(dept_lower.toCharArray()[i]);
            }
        }
        lstAvante.add(new Dados("Departamento/Filial", dept_final.toString()));
        String lider_upper = Objects.requireNonNull(personInfos.get("Líder Imediato")).toUpperCase();
        String lider_lower = Objects.requireNonNull(personInfos.get("Líder Imediato")).toLowerCase();
        StringBuilder lider_final = new StringBuilder();
        boolean primeira_letra = false;
        for (int i = 2; i < lider_upper.length(); i++) {
            if (!primeira_letra) {
                if (lider_upper.toCharArray()[i - 2] == '-') {
                    lider_final.append(lider_upper.substring(i, i + 1));
                    primeira_letra = true;
                }
            } else {
                if (lider_upper.toCharArray()[i - 1] == ' ') {
                    lider_final.append(lider_upper.toCharArray()[i]);
                } else {
                    lider_final.append(lider_lower.toCharArray()[i]);
                }
            }
        }
        lstAvante.add(new Dados("Líder Imediato", lider_final.toString()));

        lstBanco.add(new Dados("Código do Banco", personInfos.get("Código Banco")));
        lstBanco.add(new Dados("Agência", personInfos.get("Código Agência")));
        lstBanco.add(new Dados("Conta Bancária", personInfos.get("Conta Bancária")));
    }

    // Este método é chamado caso ocorra algum problema
    // com a conexão ao banco de dados do Firebase.
    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        nome.setText(R.string.erro);
    }
}
