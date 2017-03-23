package samples.android.elisha.com.zimnet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class AutomotiveSignUp extends AppCompatActivity {


    private ImageButton automotive_userImage;
    private EditText automotiveUserName, atutomotivecUserEmail,automotiveUserCellphone, automotiveTelephone,automotiveUserAddress,
            automotiveProfile,automotiveUserCategory;
    private Button autmotiveSbmitBtn;

    private Uri mImageUri = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;


    public static final int GALLERY_REQUEST =1;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automotive_sign_up);


        mProgressDialog = new ProgressDialog(this);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Automotive_Users");

        automotiveUserName = (EditText) findViewById(R.id.automotive_username);
        atutomotivecUserEmail = (EditText) findViewById(R.id.automotive_userEmail);
       automotiveUserCellphone= (EditText) findViewById(R.id.automotive_userCellphone);
       automotiveTelephone = (EditText) findViewById(R.id.automotive_userTelephone);
        automotiveUserAddress= (EditText) findViewById(R.id.automotive_userLocation);
       automotiveProfile = (EditText) findViewById(R.id.automotive_userProfile);
        automotiveUserCategory = (EditText) findViewById(R.id.automotive_userCategory);
       automotive_userImage = (ImageButton) findViewById(R.id.automotive_profile_image);
        autmotiveSbmitBtn = (Button) findViewById(R.id.automotive_submitBtn);



        autmotiveSbmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegistering();
            }
        });


       automotive_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });
    }

    private void startRegistering() {

        final String auto_name = automotiveUserName.getText().toString().trim();
        final String agric_email = atutomotivecUserEmail.getText().toString().trim();
        final String auto_cellphone = automotiveUserCellphone.getText().toString().trim();
        final String auto_telephone = automotiveTelephone.getText().toString().trim();
        final String auto_location= automotiveUserAddress.getText().toString().trim();
        final String auto_profile = automotiveProfile.getText().toString().trim();
        final String auto_category = automotiveUserCategory.getText().toString().trim();

        if (!TextUtils.isEmpty(auto_name)&& !TextUtils.isEmpty(agric_email) &&
                !TextUtils.isEmpty(auto_cellphone) && !TextUtils.isEmpty(auto_telephone )&&
                !TextUtils.isEmpty(auto_location) && !TextUtils.isEmpty(auto_profile)&&
                !TextUtils.isEmpty(auto_category) && mImageUri != null){

            mProgressDialog.setMessage("Registering please wait...");
            mProgressDialog.show();
            StorageReference filePath = mStorage.child("Automotive_user_images").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")  Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newAgricultureUser = mDatabase.push();



                    newAgricultureUser.child("automotive_name").setValue(auto_name);
                    newAgricultureUser.child(" automotive_email").setValue(agric_email);
                    newAgricultureUser.child("automotive_cellphone").setValue(auto_cellphone);
                    newAgricultureUser.child("automotive_telephone").setValue(auto_telephone);
                    newAgricultureUser.child("automotive_location ").setValue(auto_location);
                    newAgricultureUser.child("automotive_profile").setValue(auto_profile);
                    newAgricultureUser.child("automotive_category").setValue(auto_category);
                    newAgricultureUser.child("automotive_image").setValue(downloadUrl.toString());


                    mProgressDialog.dismiss();

                    Toast.makeText(AutomotiveSignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AutomotiveSignUp.this, AutomotiveListings.class));

                }
            });

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==GALLERY_REQUEST && resultCode == RESULT_OK) {

            mImageUri = data.getData();

            CropImage.activity(mImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                automotive_userImage.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }


        }}

}


