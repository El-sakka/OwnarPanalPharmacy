package www.ownerpanal.com.ownarpanal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String CLASS_TYPE = "class";
    private static final String USER_TYPE = "user_type";
    private static final String EMAIL_OWNER = "email_owner";
    private static final String EMAIL_USER = "email_user";

    //widgets
    private TextView mRegister;
    private EditText mEmail, mPassword;
    private Button mLogin;
    private ProgressDialog mProgressDialog;
    String userType;


    //firebase
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // if user is already log in
        if(mFirebaseAuth.getCurrentUser() != null)
            directToOwnerPanal();





        Log.d(TAG, "onCreate: Userype: "+userType);

        mRegister = (TextView) findViewById(R.id.link_register);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mLogin = (Button) findViewById(R.id.btn_login);


        mProgressDialog = new ProgressDialog(this);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    LoginOwner();
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directRegister();
            }
        });



    }

    private void directRegister() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        intent.putExtra(CLASS_TYPE,userType);
        startActivity(intent);
    }

    private void LoginUser() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // start to pharmacy Activity
                            directToOwnerPanal();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"You email or password is invalid",Toast.LENGTH_SHORT).show();
                            mProgressDialog.cancel();
                        }
                    }
                });
        mProgressDialog.setMessage("loging in  Please Wait....");
        mProgressDialog.show();
    }

    private void LoginOwner() {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            directToOwnerPanal();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"You email or password is invalid",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        mProgressDialog.setMessage("loging in  Please Wait....");
        mProgressDialog.show();
    }


    private void directToOwnerPanal() {
        Intent intent = new Intent(MainActivity.this, OwnerPanalActivity.class);
        String ownerEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        intent.putExtra(USER_TYPE, "owner");
        intent.putExtra(EMAIL_OWNER,ownerEmail);
        startActivity(intent);
    }


}
