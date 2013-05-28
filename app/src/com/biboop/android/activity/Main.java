package com.biboop.android.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.biboop.android.R;
import com.biboop.android.adapter.MenuAdapter;
import com.biboop.android.fragment.*;

public class Main extends SherlockFragmentActivity implements AdapterView.OnItemClickListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView menuListView;
    private int titleRes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getSupportActionBar().setTitle(titleRes);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);

        menuListView = (ListView) findViewById(R.id.left_drawer);
        menuListView.setAdapter(new MenuAdapter(this));
        menuListView.setOnItemClickListener(this);

        MenuAdapter.MenuItem defaultItem = MenuAdapter.MenuItem.DASHBOARD;
        showScreen(defaultItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void setTitle(int titleRes) {
        this.titleRes = titleRes;
        getSupportActionBar().setTitle(titleRes);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        MenuAdapter.MenuItem item = (MenuAdapter.MenuItem) menuListView.getItemAtPosition(i);
        showScreen(item);
        drawerLayout.closeDrawer(menuListView);
    }

    private void showScreen(MenuAdapter.MenuItem item) {
        Fragment fragment = null;
        switch (item) {
            case USER:
                fragment = new Account();
                break;
            case DASHBOARD:
                fragment = new Dashboard();
                break;
            case ALERTS:
                fragment = new Alerts();
                break;
            case SERVERS:
                fragment = new Servers();
                break;
            case COMMANDS:
                fragment = new Commands();
                break;
            case SIGN_OUT:
                // TODO Sign in
                return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content, fragment).commit();

        setTitle(item.title);
        menuListView.setItemChecked(item.ordinal(), true);
    }
}
