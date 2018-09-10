package com.mean.meanminesweeper.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mean.meanminesweeper.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private EditText et_rowNum = null;
    private EditText et_colNum = null;
    private EditText et_mineNum = null;
    private LinearLayout layout_main = null;
    private LinearLayout layout_ctrl = null;
    private LinearLayout layout_status = null;
    private int rowNum = 9;
    private int colNum = 9;
    private int mineNum = 10;
    private TextView tv_time = null;
    private TextView tv_score = null;
    private TextView tv_mineNum = null;
    private Button bt_start = null;
    private boolean isRoundEnd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        layout_main = findViewById(R.id.activity_main);
        layout_ctrl = findViewById(R.id.layout_ctrl);
        layout_status = findViewById(R.id.layout_status);

        et_rowNum = findViewById(R.id.et_rowNum);
        et_rowNum.setText(String.format(getString(R.string.str_int),rowNum));
        et_rowNum.setInputType(InputType.TYPE_CLASS_NUMBER);

        et_colNum = findViewById(R.id.et_colNum);
        et_colNum.setText(String.format(getString(R.string.str_int),colNum));
        et_colNum.setInputType(InputType.TYPE_CLASS_NUMBER);

        et_mineNum = findViewById(R.id.et_mineNum);
        et_mineNum.setText(String.format(getString(R.string.str_int),mineNum));
        et_mineNum.setInputType(InputType.TYPE_CLASS_NUMBER);
        tv_time = findViewById(R.id.tv_time);
        tv_score = findViewById(R.id.tv_score);
        tv_mineNum = findViewById(R.id.tv_mineNum);
        bt_start = findViewById(R.id.bt_start);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_game) {
            // Handle the camera action
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_quit) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
