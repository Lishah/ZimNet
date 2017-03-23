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

public class AgricultureProfile extends AppCompatActivity {

    private TextView mAgricName, mAgricEmail, mAgricCellphone, mAgricTelephone, mAgricLocation, mAgricProfile,mAgricCtategory;
    private ImageView mAgricImage;
    private String mUser_key;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agriculture_profile);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Agriculture_Users");

       mUser_key = getIntent().getExtras().getString("user_id");

       mAgricName = (TextView) findViewById(R.id.agric_profile_name_view);
       mAgricEmail = (TextView) findViewById(R.id.agric_profile_email_view);
       mAgricCellphone = (TextView) findViewById(R.id.agric_profile_mobilephone_view);
       mAgricTelephone = (TextView) findViewById(R.id.agric_profile_telephone_view);
       mAgricLocation = (TextView) findViewById(R.id.agric_profile_location_view);
       mAgricProfile = (TextView) findViewById(R.id.agric_profile_profile_view);
       mAgricCtategory = (TextView) findViewById(R.id.agric_profile_category_view);
        mAgricImage = (ImageView) findViewById(R.id.agric_profile_image_view);

        mDatabase.child(mUser_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String agric_name = (String) dataSnapshot.child( "user_name").getValue();
                String agric_email = (String) dataSnapshot.child( "user_email").getValue();
                String agric_cellphone = (String) dataSnapshot.child( "user_cellphone").getValue();
                String agric_telephone = (String) dataSnapshot.child( "user_telephone").getValue();
                String agric_location = (String) dataSnapshot.child( "user_location").getValue();
                String agric_profile = (String) dataSnapshot.child( "user_profile").getValue();
                String agric_category = (String) dataSnapshot.child("user_category").getValue();
                String agric_image = (String) dataSnapshot.child("agriculture_user_image").getValue();

                mAgricName.setText(agric_name);
                mAgricEmail.setText(agric_email);
                mAgricCellphone.setText(agric_cellphone);
                mAgricTelephone.setText(agric_telephone);
                mAgricLocation.setText(agric_location);
                mAgricProfile.setText(agric_profile);
                mAgricCtategory.setText(agric_category);

                Picasso.with(AgricultureProfile.this).load(agric_image).into(mAgricImage);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
