package samples.android.elisha.com.zimnet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class PostActivity extends AppCompatActivity {

    private ImageButton addImageBtn;
    private EditText titleEditText, descEditText;
    private Button submitPostBtn;
    private Uri mImageUri = null;
    private ProgressDialog mAdvertsProgressDialog;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;

    public static final int  GALLERY_INTENT= 1;

    private StorageReference mPostStorageref;
    private DatabaseReference mPostDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mAuth = FirebaseAuth.getInstance();

        mCurrentUser= mAuth.getCurrentUser();
        mPostStorageref= FirebaseStorage.getInstance().getReference();
        mDatabaseUsers =FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        mPostDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Adverts");

        mAdvertsProgressDialog = new ProgressDialog(this);

        addImageBtn = (ImageButton) findViewById(R.id.imageSelect);
        titleEditText = (EditText) findViewById(R.id.titleField);
        descEditText = (EditText) findViewById(R.id.descField);
        submitPostBtn = (Button) findViewById(R.id.submitBtn);


        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_INTENT);
            }
        });
        
        submitPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }

    private void startPosting() {

        final String title_val = titleEditText.getText().toString().trim();
        final String desc_val = descEditText.getText().toString().toString();

        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri !=null){
            mAdvertsProgressDialog.setMessage("Posting please wait");
            mAdvertsProgressDialog.show();
            StorageReference filePath = mPostStorageref.child("Adverts_images").child(mImageUri.getLastPathSegment());

            filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") final Uri downloadUrl =taskSnapshot.getDownloadUrl();

                    final DatabaseReference newPost =mPostDatabaseRef.push();


                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            newPost.child("advert_title").setValue(title_val);
                            newPost.child("advert_description").setValue(desc_val);
                            newPost.child("advert_image").setValue(downloadUrl.toString());
                            newPost.child("uid").setValue(mCurrentUser.getUid());
                            newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                startActivity(new Intent(PostActivity.this, AdvertPosts.class));
                                            }

                                        }
                                    }
                            );


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    mAdvertsProgressDialog.dismiss();



                }
            });

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_INTENT && resultCode== RESULT_OK){

            mImageUri = data.getData();
            addImageBtn.setImageURI(mImageUri);
        }
    }
}
