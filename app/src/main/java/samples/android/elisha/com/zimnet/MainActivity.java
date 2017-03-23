package samples.android.elisha.com.zimnet;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mAdvertsRecyclerView;
    private DatabaseReference mAdvertsDatabseReference;
    private DatabaseReference mDatabaseUsers;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      mAuth= FirebaseAuth.getInstance();
      mAdvertsDatabseReference = FirebaseDatabase.getInstance().getReference().child("Adverts");
      mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
      mDatabaseUsers.keepSynced(true);
      mAdvertsDatabseReference.keepSynced(true);

      mAuthStateListener =new FirebaseAuth.AuthStateListener() {
          @Override
          public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

              if (firebaseAuth.getCurrentUser() == null){
                  Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                  loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(loginIntent);
              }

          }
      };

      mAdvertsRecyclerView = (RecyclerView) findViewById(R.id.adverts_recyclerView);
      mAdvertsRecyclerView.setHasFixedSize(true);
      mAdvertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        checkUserExists();
    }

    @Override
    protected void onStart() {
        super.onStart();


        mAuth.addAuthStateListener(mAuthStateListener);




        FirebaseRecyclerAdapter<Adverts,AdvertViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Adverts, AdvertViewHolder>(

                Adverts.class,
                R.layout.adverts_row,
                AdvertViewHolder.class,
                mAdvertsDatabseReference

        ) {
            @Override
            protected void populateViewHolder(AdvertViewHolder viewHolder, Adverts model, int position) {

             final String advert_key = getRef(position).getKey();

                viewHolder.setTitle(model.getAdvert_title());
//               viewHolder.setDescription(model.getAdvert_description());
                viewHolder.setUsername(model.getUsername());
               viewHolder.setImage(getApplicationContext(),model.getAdvert_image());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                       Intent advertIntent = new Intent(MainActivity.this, AdvertDetails.class);
                        advertIntent.putExtra("advert_key", advert_key);
                        startActivity(advertIntent);
                    }
                });
            }
        };

        mAdvertsRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }



    private void checkUserExists() {

        if (mAuth.getCurrentUser() != null) {

            final String user_uid = mAuth.getCurrentUser().getUid();

            mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (!dataSnapshot.hasChild(user_uid)) {

                        Intent setupIntent = new Intent(MainActivity.this, AccountSetUp.class);
                        setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(setupIntent);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {


                }
            });

        }
    }



    public static class AdvertViewHolder extends RecyclerView.ViewHolder{

       View mView;
        public AdvertViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String title){
            TextView advert_title = (TextView) mView.findViewById(R.id.advert_title);
            advert_title.setText(title);
        }

//        public void setDescription(String description){
//            TextView advert_description = (TextView) mView.findViewById(R.id.advert_description);
//            advert_description.setText(description);
//        }


        public void setUsername(String username){

            TextView advert_username= (TextView) mView.findViewById(R.id.advert_user_name);
           advert_username.setText(username);



        }


        public void setImage(final Context ctx, final String image){
            final ImageView adverts_Image = (ImageView) mView.findViewById(R.id.advert_image);
//            Picasso.with(ctx).load(image).into(adverts_Image);

            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(adverts_Image, new
                    Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(ctx).load(image).into(adverts_Image);

                        }
                    });

        }
    }



    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()== R.id.action_add){

          startActivity(new Intent(MainActivity.this, PostActivity.class));
        }

        if(item.getItemId()== R.id.action_logout){

            logout();

        }

        if (item.getItemId()==R.id.action_editProfile){

//            startActivity(new Intent(MainActivity.this, SetupActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        mAuth.signOut();
    }


}
