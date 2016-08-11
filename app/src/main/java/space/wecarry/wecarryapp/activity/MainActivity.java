package space.wecarry.wecarryapp.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import space.wecarry.wecarryapp.R;
import space.wecarry.wecarryapp.fragment.CalendarFragment;
import space.wecarry.wecarryapp.fragment.DelegateFragment;
import space.wecarry.wecarryapp.fragment.PlanFragment;
import space.wecarry.wecarryapp.fragment.RoleGoalFragment;
import space.wecarry.wecarryapp.fragment.StatsFragment;
import space.wecarry.wecarryapp.sqlite.DBHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // clear db test tables
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.clearTestTables();
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
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
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
        Fragment fragment;
        FragmentManager fragmentManager = getFragmentManager();

        switch (id) {
            case R.id.nav_role_goal:
                Log.d("Clicked", "role");
                // update the main content by replacing fragments
                fragment = new RoleGoalFragment();
                fragmentManager.beginTransaction().replace(R.id.mainContainer, fragment).commit();
                break;
            case R.id.nav_plan:
                Log.d("Clicked", "plan");
                // update the main content by replacing fragments
                fragment = new PlanFragment();
                fragmentManager.beginTransaction().replace(R.id.mainContainer, fragment).commit();
                break;
            case R.id.nav_calendar:
                Log.d("Clicked", "calendar");
                // update the main content by replacing fragments
                fragment = new CalendarFragment();
                fragmentManager.beginTransaction().replace(R.id.mainContainer, fragment).commit();
                break;
            case R.id.nav_delegate:
                Log.d("Clicked", "delegate");
                // update the main content by replacing fragments
                fragment = new DelegateFragment();
                fragmentManager.beginTransaction().replace(R.id.mainContainer, fragment).commit();
                break;
            case R.id.nav_stats:
                Log.d("Clicked", "stats");
                // update the main content by replacing fragments
                fragment = new StatsFragment();
                fragmentManager.beginTransaction().replace(R.id.mainContainer, fragment).commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
