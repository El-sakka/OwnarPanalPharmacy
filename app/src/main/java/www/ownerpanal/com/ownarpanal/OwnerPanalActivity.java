package www.ownerpanal.com.ownarpanal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class OwnerPanalActivity extends AppCompatActivity {

    TextView tvCreatePharmcy;
    TextView tvShowPhamracy;
    TextView tvSignOut;

    FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_panal);
        setupFirebase();

        init();

    }

    private void init(){
        tvCreatePharmcy = (TextView)findViewById(R.id.tv_create_phrmacy);
        tvShowPhamracy = (TextView)findViewById(R.id.tv_show_Phrmacy);
        tvSignOut = (TextView)findViewById(R.id.tv_sign_out);
        tvCreatePharmcy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToCreatePhramcy();
            }
        });



        tvShowPhamracy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectShowPhrmacy();
            }
        });

        tvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }




    private void redirectToCreatePhramcy(){
        Intent intent = new Intent(getApplicationContext(),CreatePharmacy.class);
        startActivity(intent);
    }


    private void redirectShowPhrmacy(){
        Intent intent = new Intent(getApplicationContext(),ShowPharmacy.class);
        startActivity(intent);
    }


    private void setupFirebase(){
        mFirebaseAuth = FirebaseAuth.getInstance();
        if(mFirebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }
    }

}
