package www.ownerpanal.com.ownarpanal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import www.ownerpanal.com.ownarpanal.Model.PharmacyInfo;

public class CreatePharmacy extends AppCompatActivity {

    private static final String TAG = "CreatePhamacyFragment";
    private static final String PHARMACY_KEY = "pharmacy_key";
    private static final String PHARMACY_NAME = "pharmacy_name";
    private static final String LATITUDE_KEY = "latitude";
    private static final String LANGITUDE_KEY = "longitude";
    private static final String NAME_KEY = "name";

    private EditText mPharmacyNameEditText;
    private EditText mPharmacyPhoneEditText;
    private EditText mPharmacylatEditText;
    private EditText mPharmacylanEditText;
    private Button mSetLocationBtn;


    private FloatingActionButton fabNext;
    private FloatingActionButton fabDone;

    private FirebaseAuth mFirebaseAuth;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pharmacy);

        mFirebaseAuth = FirebaseAuth.getInstance();

        userEmail = mFirebaseAuth.getCurrentUser().getEmail();

        mPharmacyNameEditText = (EditText) findViewById(R.id.ed_pharmacy_name);
        mPharmacyPhoneEditText = (EditText) findViewById(R.id.ed_pharmacy_phone);
        mPharmacylatEditText = (EditText)findViewById(R.id.et_lat);
        mPharmacylanEditText = (EditText) findViewById(R.id.et_lan);
        mSetLocationBtn = (Button) findViewById(R.id.btn_setLocation);

        fabNext = (FloatingActionButton) findViewById(R.id.fab_next);
        fabDone = (FloatingActionButton)findViewById(R.id.fab_done_phrmacy);


        /*
         * check if intent comes from set location Activity
         * */
        // get lat,lan from location Activity
        Intent intent = getIntent();

        double lat = intent.getDoubleExtra(LATITUDE_KEY, 0.0);
        double lng = intent.getDoubleExtra(LANGITUDE_KEY, 0.0);
        String name = intent.getStringExtra(NAME_KEY);
        Log.d(TAG, "onActivityCreated: lat:" + lat + " lng: " + lng);
        // set values to editTexts
        mPharmacylatEditText.setText(String.valueOf(lat));
        mPharmacylanEditText.setText(String.valueOf(lng));
        mPharmacyNameEditText.setText(name);





        final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Database").child("Pharmacy");

        mSetLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directToLocation(mPharmacyNameEditText.getText().toString());
            }
        });

        fabNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mPharmacyNameEditText.getText().toString())) {
                    Log.d(TAG, "onClick: pushing pharmacy data into firebase");
                    String pharmacyName = mPharmacyNameEditText.getText().toString();
                    String pharmacyPhone = mPharmacyPhoneEditText.getText().toString();
                    String pharmacylat = mPharmacylatEditText.getText().toString();
                    String pharmacylan = mPharmacylanEditText.getText().toString();

                    String pharmacyKey = mDatabaseReference.push().getKey();

                    PharmacyInfo pharmacy = new PharmacyInfo(pharmacyName, pharmacyPhone, pharmacylat, pharmacylan, pharmacyKey,userEmail);
                    mDatabaseReference.child(pharmacyName).setValue(pharmacy);

                    Intent intent = new Intent(getApplicationContext(), AddMedicineActivity.class);
                    intent.putExtra("Class","A");
                    intent.putExtra(PHARMACY_NAME, pharmacyName);
                    startActivity(intent);

                } else {
                    Log.d(TAG, "onClick: edit text field is emtpy");
                    Toast.makeText(getApplicationContext(), "Pharamcy name is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mPharmacyNameEditText.getText().toString())) {
                    Log.d(TAG, "onClick: pushing pharmacy data into firebase");
                    String pharmacyName = mPharmacyNameEditText.getText().toString();
                    String pharmacyPhone = mPharmacyPhoneEditText.getText().toString();
                    String pharmacylat = mPharmacylatEditText.getText().toString();
                    String pharmacylan = mPharmacylanEditText.getText().toString();

                    String pharmacyKey = mDatabaseReference.push().getKey();

                    PharmacyInfo pharmacy = new PharmacyInfo(pharmacyName, pharmacyPhone, pharmacylat, pharmacylan, pharmacyKey,userEmail);
                    mDatabaseReference.child(pharmacyName).setValue(pharmacy);

                    Intent intent = new Intent(getApplicationContext(), OwnerPanalActivity.class);
                    startActivity(intent);

                } else {
                    Log.d(TAG, "onClick: edit text field is emtpy");
                    Toast.makeText(getApplicationContext(), "Pharamcy name is Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void directToLocation(String phrmacyName) {
        Intent intent = new Intent(getApplicationContext(), SetLocationActivity.class);
        intent.putExtra(NAME_KEY,phrmacyName);
        startActivity(intent);
    }
}
