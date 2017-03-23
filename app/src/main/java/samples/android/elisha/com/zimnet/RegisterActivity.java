package samples.android.elisha.com.zimnet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText signUpUserNameEditText, signUpUserEmailEditText, signUpUserPasswordEditText;
    private Button   signUpUserButton;

    private  FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mProgress = new ProgressDialog(this);
        signUpUserNameEditText = (EditText) findViewById(R.id.nameField);
        signUpUserEmailEditText = (EditText) findViewById(R.id.emailField);
        signUpUserPasswordEditText= (EditText) findViewById(R.id.passwordField);
        signUpUserButton= (Button) findViewById(R.id.signUpBtn);

        signUpUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignUp();
            }
        });
    }

    private void startSignUp() {
       final String userName= signUpUserNameEditText.getText().toString().trim();
       String userEmail = signUpUserEmailEditText.getText().toString().trim();
       String userPassword = signUpUserPasswordEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword)){

            mProgress.setMessage("Signing in please wait....");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                        String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db =    mDatabase.child(user_id);

                        current_user_db.child("user_name").setValue(userName);
                        current_user_db.child("user_image").setValue("default");

                        mProgress.dismiss();

                        Intent mainIntent = new Intent(RegisterActivity.this, AccountSetUp.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);

                    }

                }
            });


        }

    }
}
