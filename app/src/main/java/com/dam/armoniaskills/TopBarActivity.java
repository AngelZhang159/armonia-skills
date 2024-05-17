package com.dam.armoniaskills;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dam.armoniaskills.fragments.DepositarFragment;
import com.dam.armoniaskills.fragments.InicioFragment;
import com.dam.armoniaskills.fragments.RetirarFragment;
import com.dam.armoniaskills.fragments.SkillFragment;
import com.dam.armoniaskills.model.Skill;
import com.google.android.material.appbar.MaterialToolbar;

public class TopBarActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    Skill skill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_bar);

        toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String i = getIntent().getStringExtra("rellenar");

        if (i.equals("fragmentoHome")) {
            cargarSkill();
        } else if (i.equals("fragmentoBalanceDepositar")) {
            cargarDepositar();
        } else if (i.equals("fragmentoBalanceRetirar")) {
            cargarRetirar();
        }
    }

    private void cargarSkill() {
        skill = getIntent().getParcelableExtra(InicioFragment.SKILL);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        SkillFragment grupoFragment = SkillFragment.newInstance(skill);
        transaction.replace(R.id.flTopBar, grupoFragment);

        transaction.commit();

        toolbar.inflateMenu(R.menu.topbar_menu);
    }

    private void cargarRetirar() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        RetirarFragment retirarFragment = new RetirarFragment();
        fragmentTransaction.replace(R.id.flTopBar, retirarFragment);

        fragmentTransaction.commit();
    }

    private void cargarDepositar() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DepositarFragment depositoFragment = new DepositarFragment();
        fragmentTransaction.replace(R.id.flTopBar, depositoFragment);

        fragmentTransaction.commit();
    }
}