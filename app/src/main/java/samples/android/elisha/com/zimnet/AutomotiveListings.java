package samples.android.elisha.com.zimnet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AutomotiveListings extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private RecyclerView automotiveRecylerView;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private String mUser_key;
    private TextView username , usercategory;
    private CircleImageView userimage;

    private boolean mProcessLike = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automotive_listings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        usercategory = (TextView) findViewById(R.id.userCategory);
        userimage = (CircleImageView) findViewById(R.id.userImage);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Automotive_Users");
        mDatabase.keepSynced(true);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);


        mDatabase.keepSynced(true);
        automotiveRecylerView = (RecyclerView) findViewById(R.id.automotive_listing_recyclerView);
        automotiveRecylerView.setHasFixedSize(true);
        automotiveRecylerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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


    @Override
    protected void onStart() {
        super.onStart();




        FirebaseRecyclerAdapter<Adverts, AutomotiveListings.AgricultureUserListingViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Adverts, AutomotiveListings.AgricultureUserListingViewHolder>(
                Adverts.class,
                R.layout.agricuser_list_item,
               AutomotiveListings.AgricultureUserListingViewHolder.class,
                mDatabase



        ) {
            @Override
            protected void populateViewHolder(AutomotiveListings.AgricultureUserListingViewHolder viewHolder, Adverts model, int position) {

                final String user_key = getRef(position).getKey();

                viewHolder.setAutomotiveName(model.getAutomotive_name());
                viewHolder.setAutomtiveCategory(model.getAutomotive_category());
                viewHolder.setAgricultureUserImage(getApplicationContext(),model.getAgriculture_user_image());
                viewHolder.setLikeButton(user_key);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(AgricultureListing.this, user_key, Toast.LENGTH_SHORT).show();

                        Intent profileIntent = new Intent(AutomotiveListings.this,AgricultureProfile.class);
                        profileIntent.putExtra("user_id", user_key);
                        startActivity(profileIntent);
                    }
                });

                viewHolder.mLikeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mProcessLike = true;


                        mDatabaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mProcessLike) {

                                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);

                                    if (dataSnapshot.child(user_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(user_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mProcessLike = false;

                                    } else {
                                        mDatabaseLike.child(user_key).child(mAuth.getCurrentUser().getUid()).setValue("RandomValue");
                                        mProcessLike = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }


                });

            }
        };

        automotiveRecylerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static  class AgricultureUserListingViewHolder extends  RecyclerView.ViewHolder {

        ImageButton mLikeBtn;

        View mView;

        DatabaseReference mDatabaseLike;
        FirebaseAuth mAuth;
        public AgricultureUserListingViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mLikeBtn = (ImageButton) mView.findViewById(R.id.likeBtn);
            mDatabaseLike =FirebaseDatabase.getInstance().getReference().child("Likes");
            mAuth = FirebaseAuth.getInstance();
            mDatabaseLike.keepSynced(true);
        }

        public  void setLikeButton(final String user_key){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(user_key).hasChild(mAuth.getCurrentUser().getUid())){

                        mLikeBtn.setImageResource(R.mipmap.likegreen);

                    }else {
                        mLikeBtn.setImageResource(R.mipmap.likenavy);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setAutomotiveName(String automotiveName) {



            TextView automotive_username = (TextView) mView.findViewById(R.id.agric_username_textView);
            automotive_username.setText(automotiveName);


        }

        public void setAutomtiveCategory(String automtiveCategoryy) {
            TextView automotive_category = (TextView) mView.findViewById(R.id.agric_usercategory_textview);
            automotive_category.setText(automtiveCategoryy);


        }


    private  void setAgricultureUserImage(final Context ctx, final String agriculture_user_image){

        final ImageView agric_UserImage = (ImageView) mView.findViewById(R.id.agric_userprofile_imageView);
        Picasso.with(ctx).load(agriculture_user_image).networkPolicy(NetworkPolicy.OFFLINE).into(agric_UserImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {


                Picasso.with(ctx).load(agriculture_user_image).into(agric_UserImage);

            }
        });


    }

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
        getMenuInflater().inflate(R.menu.automotive_listings, menu);
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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
