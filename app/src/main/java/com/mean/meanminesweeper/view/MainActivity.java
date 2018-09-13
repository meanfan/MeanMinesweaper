package com.mean.meanminesweeper.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    private static final int DIFF_ESAY_ROW = 9;
    private static final int DIFF_ESAY_COL = 9;
    private static final int DIFF_ESAY_MINE = 10;
    private static final int DIFF_NORMAL_ROW = 16;
    private static final int DIFF_NORMAL_COL = 16;
    private static final int DIFF_NORMAL_MINE = 40;
    private static final int DIFF_HARD_ROW = 16;
    private static final int DIFF_HARD_COL = 16;
    private static final int DIFF_HARD_MINE = 40;

    private EditText et_rowNum = null;
    private EditText et_colNum = null;
    private EditText et_mineNum = null;
    private LinearLayout layout_main = null;
    private LinearLayout layout_ctrl = null;
    private LinearLayout layout_status = null;
    private int rowNum = 5;
    private int colNum = 5;
    private int mineNum = 3;
    private TextView tv_time = null;
    private TextView tv_score = null;
    private TextView tv_mineNum = null;
    private Button bt_start = null;
    private boolean isRoundEnd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_activity_title);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_game);
        FloatingActionButton fabtn_easy = findViewById(R.id.card_play_fab_easy);
        fabtn_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("row", DIFF_ESAY_ROW);
                bundle.putInt("col", DIFF_ESAY_COL);
                bundle.putInt("mine", DIFF_ESAY_MINE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        FloatingActionButton fabtn_normal = findViewById(R.id.card_play_fab_normal);
        fabtn_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("row", DIFF_NORMAL_ROW);
                bundle.putInt("col", DIFF_NORMAL_COL);
                bundle.putInt("mine", DIFF_NORMAL_MINE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        FloatingActionButton fabtn_hard = findViewById(R.id.card_play_fab_hard);
        fabtn_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("row", DIFF_HARD_ROW);
                bundle.putInt("col", DIFF_HARD_COL);
                bundle.putInt("mine", DIFF_HARD_MINE);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        FloatingActionButton fabtn_customize = findViewById(R.id.card_play_fab_customize);
        fabtn_customize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GameActivity.class);
                Bundle bundle = new Bundle();
                int row=9,col=9,mine=10;
                //TODO pop up dialog for user input
                Toast.makeText(MainActivity.this,R.string.str_function_closed,Toast.LENGTH_SHORT).show();
                return;

//                bundle.putInt("row", row);
//                bundle.putInt("col", col);
//                bundle.putInt("mine", mine);
//                intent.putExtras(bundle);
//                startActivity(intent);
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
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_game) {
            // Handle the camera action
        } else if (id == R.id.nav_option) {
            Toast.makeText(MainActivity.this,R.string.str_function_closed,Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_about) {
            //Toast.makeText(MainActivity.this,R.string.str_function_closed,Toast.LENGTH_SHORT).show();
            Intent share_intent = new Intent();
            share_intent.setAction(Intent.ACTION_SEND);//设置分享行为
            share_intent.setType("text/plain");//设置分享内容的类型
            String title = getResources().getString(R.string.share_title);
            String content = getResources().getString(R.string.share_content);

            share_intent.putExtra(Intent.EXTRA_SUBJECT, title);//添加分享内容标题
            share_intent.putExtra(Intent.EXTRA_TEXT, content);//添加分享内容
            //创建分享的Dialog
            share_intent = Intent.createChooser(share_intent, title);
            startActivity(share_intent);

        } else if (id == R.id.nav_quit) {
            MainActivity.this.finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
