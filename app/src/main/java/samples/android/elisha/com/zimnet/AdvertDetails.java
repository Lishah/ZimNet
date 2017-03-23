package samples.android.elisha.com.zimnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class AdvertDetails extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private TextView mAdvert_title_show, mAdvert_Username_Show, mAdvert_Desc_Show;
    private ImageView  mAdvert_Image_Show;

    private String mUser_key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_details);


        mUser_key = getIntent().getExtras().getString("advert_key");



        mAdvert_title_show = (TextView) findViewById(R.id.advert_title_show);
        mAdvert_Username_Show = (TextView) findViewById(R.id.advert_username_show);
        mAdvert_Desc_Show = (TextView) findViewById(R.id.advert_desc_show);
        mAdvert_Image_Show = (ImageView) findViewById(R.id.advert_image_show);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Adverts");
        mDatabase.keepSynced(true);

        mUser_key = getIntent().getExtras().getString("advert_key");

        mDatabase.child(mUser_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String ad_title = (String) dataSnapshot.child( "advert_title").getValue();
                String ad_desc = (String) dataSnapshot.child( "advert_description").getValue();
                String ad_image = (String) dataSnapshot.child("advert_image").getValue();
                String name = (String) dataSnapshot.child("username").getValue();

                mAdvert_title_show.setText(ad_title);
                mAdvert_Username_Show.setText(name);
                mAdvert_Desc_Show.setText(ad_desc);

                Picasso.with(AdvertDetails.this).load(ad_image).into(mAdvert_Image_Show);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

}}
