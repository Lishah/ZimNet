package samples.android.elisha.com.zimnet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class Messaging extends AppCompatActivity {

    private EditText mRoomText;
    private Button mRoomSendButton;
    private ListView mRoomListView;

    ArrayList<String> roomArrayList;
    ArrayAdapter<String> roomAdapter;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabaseUsers;
    private StorageReference mPostStorageref;



    DatabaseReference mDatabaseReference;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);


        mRoomText = (EditText) findViewById(R.id.room_edit_text);
        mRoomSendButton = (Button) findViewById(R.id.room_send_button);
        mRoomListView = (ListView) findViewById(R.id.roomListView);

        roomArrayList = new ArrayList<String>();
        roomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, roomArrayList);


        mRoomListView.setAdapter(roomAdapter);


        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mPostStorageref= FirebaseStorage.getInstance().getReference();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Chat");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


       mRoomSendButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final String text_msg = mRoomText.getText().toString();

               final DatabaseReference newPost =mDatabaseReference.push();

               mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(DataSnapshot dataSnapshot) {


                       Iterator iterator  = dataSnapshot.getChildren().iterator();
                       Set<String> set = new HashSet<String>();

                       while (iterator.hasNext()) {

                           set.add((String) ((DataSnapshot) iterator.next()).getKey());

                       }
                       roomArrayList.clear();
                       roomArrayList.addAll(set);

                       roomAdapter.notifyDataSetChanged();

                       newPost.child("message").setValue(text_msg);
                       newPost.child("uid").setValue(mCurrentUser.getUid());
                       newPost.child("username").setValue(dataSnapshot.child("name").getValue()).addOnCompleteListener(
                               new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {

                                   }
                               });
                   }

                   @Override
                   public void onCancelled(DatabaseError databaseError) {

                   }
               });


           }
       });

        mRoomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                     @Override
                     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                         Intent chatIntent = new Intent(Messaging.this, ChattingActivity.class);
                         chatIntent.putExtra("Chat_Name", ((TextView)view).getText().toString());
                         chatIntent.putExtra("UserName", userName);
                         startActivity(chatIntent);
                     }
                 });
    }


    }


























//
//        mRoomSendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Map<String, Object> map = new HashMap<String,Object>();
//                map.put(mRoomText.getText().toString()," ");
//                mDatabaseReference.updateChildren(map);
//            }
//        });
//               mDatabaseReference.addValueEventListener(new ValueEventListener() {
//                   @Override
//                   public void onDataChange(DataSnapshot dataSnapshot) {
//
//                       Iterator iterator  = dataSnapshot.getChildren().iterator();
//                       Set<String> set = new HashSet<String>();
//
//                       while (iterator.hasNext()){
//
//                          set.add((String) ((DataSnapshot)iterator.next()).getKey());
//                       }
//                       roomArrayList.clear();
//                       roomArrayList.addAll(set);
//
//                       roomAdapter.notifyDataSetChanged();
//
//                   }
//
//                   @Override
//                   public void onCancelled(DatabaseError databaseError) {
//
//                   }
//               });
//
//                 mRoomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                     @Override
//                     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                         Intent chatIntent = new Intent(Messaging.this, ChattingActivity.class);
//                         chatIntent.putExtra("Chat_Name", ((TextView)view).getText().toString());
//                         chatIntent.putExtra("UserName", userName);
//                         startActivity(chatIntent);
//                     }
//                 });
//    }

//    private void request_username() {
//
//        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
//        builder.setTitle("Enter your name");
//        final EditText editText = new EditText(this);
//        builder.setView(editText);
//
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                userName = editText.getText().toString();
//                if (!TextUtils.isEmpty(userName)){
//
//                }else {
//                    request_username();
//                }
//
//            }
//        }).setNegativeButton("Quit", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                 dialogInterface.cancel();
//                request_username();
//
//            }
//        });
//          builder.show();
//    }

