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

public class AgricultureSignUp extends AppCompatActivity {


    private ImageButton agric_userImage;
    private EditText agricUserName, agricUserEmail,agricUserCellphone, agricUserTelephone, agricUserAddress,
                     agricUserProfile,agricUserCategory;
    private Button agricSbmitBtn;

    private Uri mImageUri = null;
   private StorageReference mStorage;
    private DatabaseReference mDatabase;


    public static final int GALLERY_REQUEST =1;

    private ProgressDialog mProgressDialog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agriculture_sign_up);

        mProgressDialog = new ProgressDialog(this);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Agriculture_Users");

        agricUserName = (EditText) findViewById(R.id.agric_username);
        agricUserEmail = (EditText) findViewById(R.id.agric_userEmail);
        agricUserCellphone = (EditText) findViewById(R.id.agric_userCellphone);
        agricUserTelephone = (EditText) findViewById(R.id.agric_userTelephone);
        agricUserAddress = (EditText) findViewById(R.id.agric_userLocation);
        agricUserProfile = (EditText) findViewById(R.id.agric_userProfile);
        agricUserCategory = (EditText) findViewById(R.id.agric_userCategory);
        agric_userImage = (ImageButton) findViewById(R.id.agric_profile_image);
        agricSbmitBtn = (Button) findViewById(R.id.agric_submitBtn);


        agricSbmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRegistering();
            }
        });


        agric_userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_REQUEST);

            }
        });
    }

    private void startRegistering() {

        final String agric_name = agricUserName.getText().toString().trim();
        final String agric_email = agricUserEmail.getText().toString().trim();
        final String agric_cellphone = agricUserCellphone.getText().toString().trim();
        final String agric_telephone = agricUserTelephone.getText().toString().trim();
        final String agric_location= agricUserAddress.getText().toString().trim();
        final String agric_profile = agricUserProfile.getText().toString().trim();
        final String agric_category = agricUserCategory.getText().toString().trim();

        if (!TextUtils.isEmpty(agric_name)&& !TextUtils.isEmpty(agric_email) &&
             !TextUtils.isEmpty(agric_cellphone) && !TextUtils.isEmpty(agric_telephone )&&
                !TextUtils.isEmpty(agric_location) && !TextUtils.isEmpty(agric_profile)&&
        !TextUtils.isEmpty(agric_category) && mImageUri != null){

            mProgressDialog.setMessage("Registering please wait...");
            mProgressDialog.show();
            StorageReference filePath = mStorage.child("Agriculture_user_images").child(mImageUri.getLastPathSegment());
            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")  Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    DatabaseReference newAgricultureUser = mDatabase.push();

                    newAgricultureUser.child("user_name").setValue(agric_name);
                    newAgricultureUser.child(" user_email").setValue(agric_email);
                    newAgricultureUser.child("user_cellphone").setValue(agric_cellphone);
                    newAgricultureUser.child("user_telephone").setValue(agric_telephone);
                    newAgricultureUser.child("user_location").setValue(agric_location);
                    newAgricultureUser.child("user_profile").setValue(agric_profile);
                    newAgricultureUser.child("user_category").setValue(agric_category);
                    newAgricultureUser.child("agriculture_user_image").setValue(downloadUrl.toString());


                    mProgressDialog.dismiss();

                    Toast.makeText(AgricultureSignUp.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AgricultureSignUp.this, AgricultureListing.class));

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
                agric_userImage.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

            }


        }}

}