package samples.android.elisha.com.zimnet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
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
import android.widget.Toast;

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


public class AgricultureListing extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private RecyclerView agricRecylerView;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseLike;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private String mUser_key;
    private  TextView username , usercategory;
    private  CircleImageView userimage;

    private boolean mProcessLike = false;

    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agriculture_listing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        username = (TextView) findViewById(R.id.userName);
        usercategory = (TextView) findViewById(R.id.userCategory);
        userimage = (CircleImageView) findViewById(R.id.userImage);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Agriculture_Users");
        mDatabase.keepSynced(true);
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
//      mUser_key = getIntent().getExtras().getString("user_id");


        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseLike.keepSynced(true);
        mDatabase.keepSynced(true);
        agricRecylerView = (RecyclerView) findViewById(R.id.agricultue_listing_recyclerView);
        agricRecylerView.setHasFixedSize(true);
        agricRecylerView.setLayoutManager(new LinearLayoutManager(this));

//
//        FirebaseUser user = mAuth.getCurrentUser();
//        usercategory.setText("Welcome" + user.getEmail());
////        usercategory.setText(user.getEmail());



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
//        TextView txtProfileName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.userName);

    }



    @Override
    protected void onStart() {
        super.onStart();




        FirebaseRecyclerAdapter<Adverts, AgricultureUserListingViewHolder> firebaseRecyclerAdapter= new FirebaseRecyclerAdapter<Adverts, AgricultureUserListingViewHolder>(
                Adverts.class,
                R.layout.agricuser_list_item,
                AgricultureUserListingViewHolder.class,
                mDatabase



) {
    @Override
    protected void populateViewHolder(AgricultureUserListingViewHolder viewHolder, Adverts model, int position) {

        final String user_key = getRef(position).getKey();

                viewHolder.setAgricUserName(model.getUser_name());
                viewHolder.setAgricUserCategory(model.getUser_category());
                viewHolder.setAgricultureUserImage(getApplicationContext(),model.getAgriculture_user_image());
                viewHolder.setLikeButton(user_key);
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        Toast.makeText(AgricultureListing.this, user_key, Toast.LENGTH_SHORT).show();

                        Intent profileIntent = new Intent(AgricultureListing.this,AgricultureProfile.class);
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


                pd.dismiss();
            }


        });

    }
        };

        agricRecylerView.setAdapter(firebaseRecyclerAdapter);
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

        public void setAgricUserName(String agricUserName) {



            TextView agric_username = (TextView) mView.findViewById(R.id.agric_username_textView);
            agric_username.setText(agricUserName);


        }

        public void setAgricUserCategory(String agricUserCategory) {
              TextView agric_userCategory = (TextView) mView.findViewById(R.id.agric_usercategory_textview);
            agric_userCategory.setText(agricUserCategory);


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
        getMenuInflater().inflate(R.menu.agriculture_listing, menu);
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

        if (id ==R.id.action_add_user){
            startActivity(new Intent(AgricultureListing.this,AgricultureSignUp.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_adverts) {
            startActivity(new Intent(AgricultureListing.this,AdvertPosts.class));

            // Handle the camera action
        }
        else if (id == R.id.nav_gallery)
        {
            startActivity(new Intent(AgricultureListing.this,PostActivity.class));
        }
        else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(AgricultureListing.this, ChattingActivity.class));

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
}






























































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































































