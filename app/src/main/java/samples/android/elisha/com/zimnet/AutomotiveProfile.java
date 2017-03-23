package samples.android.elisha.com.zimnet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AutomotiveProfile extends AppCompatActivity {

    private TextView mAutomotiveName, mAutomotiveEmail, mAutomotiveCellphone, mAutomotiveTelephone, mAutomotiveLocation, mAutomotiveProfile,mAutomotiveCtategory;
    private ImageView mAutomotiveImage;
    private String mUser_key;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automotive_profile);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("Automotive_Users");

        mUser_key = getIntent().getExtras().getString("user_id");

        mAutomotiveName = (TextView) findViewById(R.id.automotive_profile_name_view);
        mAutomotiveEmail = (TextView) findViewById(R.id.automotive_profile_email_view);
        mAutomotiveCellphone = (TextView) findViewById(R.id.automotive_profile_mobilephone_view);
        mAutomotiveTelephone = (TextView) findViewById(R.id.automotive_profile_telephone_view);
        mAutomotiveLocation = (TextView) findViewById(R.id.automotive_profile_location_view);
        mAutomotiveProfile = (TextView) findViewById(R.id.automotive_profile_profile_view);
        mAutomotiveCtategory = (TextView) findViewById(R.id.automotive_profile_category_view);
        mAutomotiveImage = (ImageView) findViewById(R.id.automotive_profile_image_view);

        mDatabase.child(mUser_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String automotive_name = (String) dataSnapshot.child( "automotive_name").getValue();
                String automotive_email = (String) dataSnapshot.child( "automotive_email").getValue();
                String automotive_cellphone = (String) dataSnapshot.child( "automotive_cellphone").getValue();
                String automotive_telephone = (String) dataSnapshot.child( "automotive_telephone").getValue();
                String automotive_location = (String) dataSnapshot.child( "automotive_location").getValue();
                String automotive_profile = (String) dataSnapshot.child( "automotive_profile").getValue();
                String automotive_category = (String) dataSnapshot.child("automotive_category").getValue();
                String automotive_image = (String) dataSnapshot.child("automotive_image").getValue();

                mAutomotiveName.setText(automotive_name);
                mAutomotiveEmail.setText(automotive_email);
               mAutomotiveCellphone.setText(automotive_cellphone);
                mAutomotiveTelephone.setText(automotive_telephone);
                mAutomotiveLocation.setText(automotive_location);
                mAutomotiveProfile.setText(automotive_profile);
                mAutomotiveCtategory.setText(automotive_category);

                Picasso.with(AutomotiveProfile.this).load(automotive_image).into(mAutomotiveImage);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}