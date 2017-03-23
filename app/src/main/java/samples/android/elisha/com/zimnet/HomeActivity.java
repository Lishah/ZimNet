package samples.android.elisha.com.zimnet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import samples.android.elisha.com.zimnet.mAdapter.CategoriesAdapter;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopupMenu.OnMenuItemClickListener {


    private TextView categoryText;
    private FirebaseAuth mAuth;


    private final String recyclerViewTitleText[] = {"Agriculture", "Automotive", "Chemicals", "Construction", "Energy", "Financial Services",
            "Food & Beverage", "Health Care", "Oil & Gas", "Oil & Gas"
//            "Pharmaceuticals & Biotechnology"
//            , "Printing & Publishing", "Telecommunications & Media", "Transport & Logistics","Schools",
//            "Hotels&Resorts"
    };


    private final int recyclerViewImages[] = {
            R.mipmap.agriculture, R.mipmap.automotive, R.mipmap.chemicals, R.mipmap.construction, R.mipmap.energy, R.mipmap.finance,
            R.mipmap.food_bev, R.mipmap.automotive, R.mipmap.gas_oil, R.mipmap.gas_oil

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initRecyclerViews();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        categoryText = (TextView) findViewById(R.id.categoryTextView);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        FirebaseUser user = mAuth.getCurrentUser();

        TextView txtProfileEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userCategory);
        txtProfileEmail.setText(user.getEmail());
    }


    private void initRecyclerViews() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.categories_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<UserCategories> av = prepareData();
        CategoriesAdapter mAdapter = new CategoriesAdapter(av, getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener
                (new CategoriesRecyclerClickListener(getApplicationContext(), new CategoriesRecyclerClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int i) {

                                switch (i) {
                                    case 0:

                                        PopupMenu popupMenu = new PopupMenu(HomeActivity.this, view);
                                        popupMenu.setOnMenuItemClickListener(HomeActivity.this);
                                        popupMenu.inflate(R.menu.agriculture);
                                        popupMenu.show();


                                        break;

                                    case 1:

//                                        startActivity(new Intent(HomeActivity.this, AutomotiveListings.class));
                                        break;
                                    case 2:
                                        Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                        break;
                                    case 3:
                                        Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                        break;
                                    case 4:
                                        Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                        break;
                                    case 5:
                                        Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                        break;
                                    case 6:
                                        Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                        break;
                                    case 7:
                                        Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
                                        break;
//                            case 8:
//                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
//                                break;
//                            case 9:
//                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
//                                break;
//                            case 10:
//                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
//                                break;
//                            case 11:
//                                Toast.makeText(view.getContext(), "position= " + i, Toast.LENGTH_LONG).show();
//                                break;
                                }
                            }
                        })
                );

    }

    private ArrayList<UserCategories> prepareData() {

        ArrayList<UserCategories> av = new ArrayList<>();
        for (int i = 0; i < recyclerViewTitleText.length; i++) {
            UserCategories mUserCategories = new UserCategories();
            mUserCategories.setRecyclerViewTitleText(recyclerViewTitleText[i]);
            mUserCategories.setRecyclerViewImage(recyclerViewImages[i]);
            av.add(mUserCategories);
        }
        return av;
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_signupToAgric:
                Intent intent = new Intent(HomeActivity.this, AgricultureSignUp.class);
                startActivity(intent);
                break;
            case R.id.item_viewAgricList:
                startActivity(new Intent(HomeActivity.this,AgricultureListing.class));
                break;

        }
        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mAuth.signOut();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_adverts) {
            // Handle the camera action
            startActivity(new Intent(HomeActivity.this, AdvertPosts.class));


        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(HomeActivity.this, PostActivity.class));

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(HomeActivity.this, Messaging.class));

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
