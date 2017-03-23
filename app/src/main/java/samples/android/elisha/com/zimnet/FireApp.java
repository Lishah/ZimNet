package samples.android.elisha.com.zimnet;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by elisha on 3/22/17.
 */

public class FireApp  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

     if(!FirebaseApp.getApps(this).isEmpty()){

         FirebaseDatabase.getInstance().setPersistenceEnabled(true);

     }


    }
}
