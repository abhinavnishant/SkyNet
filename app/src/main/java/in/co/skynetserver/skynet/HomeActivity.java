package in.co.skynetserver.skynet;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static in.co.skynetserver.skynet.R.string.signed_in_name;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener
    {

    private TextView mStatusTextView;
    private DatabaseReference mDatabase;
    private TextView mSignedInName;
    private TextView mSetUpNewDevice;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = getIntent();
        mSignedInName = findViewById(R.id.signed_in_name);
        mSetUpNewDevice = findViewById(R.id.new_device_setup);
        mStatusTextView =findViewById(R.id.no_device_found);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser  currentUser = mAuth.getCurrentUser();
        mSignedInName.setText(getString(signed_in_name, currentUser.getDisplayName()));
        updateUI(currentUser);

       // mSignedInName.setText(getString(R.string.signed_in_name, currentUser.getDisplayName()));
        //ref.child(currentUser.getUid()).setValue(currentUser.getUid());
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();



}


    private void updateUI(final FirebaseUser user){
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference();
        Query query = ref.child("users").child(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot users : dataSnapshot.getChildren()){

                        mStatusTextView.setText(getString(R.string.signed_in_name,user.getDisplayName()));
                        mSetUpNewDevice.setVisibility(View.GONE);
                    }
                }
                else
                {
                    mStatusTextView.setText(R.string.no_device_found);
                    mSetUpNewDevice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


        @Override
        public void onClick(View view) {
            Intent intent = new Intent(this, DeviceSetup.class);
            startActivity(intent);
        /*FirebaseUser user = mAuth.getCurrentUser();
        DatabaseReference ref =FirebaseDatabase.getInstance().getReference();
        mStatusTextView.setText(getString(R.string.firebase_status_fmt,user.getUid()));
        ref.child("users").child(user.getUid()).setValue(user.getUid());
        ref.child("users").child(user.getUid()).child("name").setValue(user.getDisplayName());
        updateUI(user);*/
        }
    }
