package space.wecarry.wecarryandroidapp201608;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Locale;

import space.wecarry.wecarryandroidapp201608.fragment.CalendarFragment;
import space.wecarry.wecarryandroidapp201608.fragment.DelegateFragment;
import space.wecarry.wecarryandroidapp201608.fragment.GoalFragment;
import space.wecarry.wecarryandroidapp201608.fragment.PlanFragment;
import space.wecarry.wecarryandroidapp201608.fragment.RoleFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            case R.id.nav_role:
                Log.d("Clicked", "role");
                // update the main content by replacing fragments
                fragment = new RoleFragment();
                fragmentManager.beginTransaction().replace(R.id.mainContainer, fragment).commit();
                break;
            case R.id.nav_goal:
                Log.d("Clicked", "goal");
                // update the main content by replacing fragments
                fragment = new GoalFragment();
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
//            case R.id.nav_share:
//                Log.d("Clicked", "share");
//                break;
//            case R.id.nav_send:
//                Log.d("Clicked", "send");
//                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    private void selectItem(int position) {
//        // update the main content by replacing fragments
//        Fragment fragment = new PlanetFragment();
//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
//    }
//    public static class PlanetFragment extends Fragment {
//        public static final String ARG_PLANET_NUMBER = "planet_number";
//
//        public PlanetFragment() {
//            // Empty constructor required for fragment subclasses
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_planet, container, false);
//            int i = getArguments().getInt(ARG_PLANET_NUMBER);
//            String planet = getResources().getStringArray(R.array.planets_array)[i];
//
//            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
//                    "drawable", getActivity().getPackageName());
//            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
//            getActivity().setTitle(planet);
//            return rootView;
//        }
//    }
}
