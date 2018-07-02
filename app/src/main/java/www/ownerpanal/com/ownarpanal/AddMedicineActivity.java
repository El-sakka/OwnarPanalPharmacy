package www.ownerpanal.com.ownarpanal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import www.ownerpanal.com.ownarpanal.Model.Medicine;
import www.ownerpanal.com.ownarpanal.Model.PharmacyAndMedicine;

public class AddMedicineActivity extends AppCompatActivity {



    // declare keys for map and for intent
    private static final String TAG = "MedicineFragment";
    private static final String PHARMACY_NAME = "pharmacy_name";
    private static final String PHARMACY_KEY = "pharmacy_key";
    private static final String MEDICINE_KEY = "medicine_key";
    private static final String MEDICINE_QUANTITY = "medicine_quantity";
    private static final String CHOOSE_KEY = "choose";
    private static final String PHRMACY_SHOW = "show";



    // hash map for medicine key , and medinine quantity
    HashMap<String, String> map = new HashMap<>();

    //declare views to get text
    private EditText mMedicineName;
    private EditText mMedicineCategory;
    private EditText mMedicineQuantity;
    private EditText mMedicinePrice;
    private EditText mMedicineDiscription;
    private EditText mPharmacyName;
    private EditText mMedicineImage;
    private FloatingActionButton mMedicineDone;


    // database references for firebase
    DatabaseReference mDatabaseReferencePharmacy;
    DatabaseReference mDatabaseReferenceMedicine;
    DatabaseReference mDatabaseReferenceMedicinePharmacy;
    DatabaseReference mDatabaseReferenceCategory;


    String pharmacySearch;
    String pharmacyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        mMedicineName = (EditText) findViewById(R.id.et_medicine_name);
        mMedicineCategory = (EditText) findViewById(R.id.et_category_name);
        mMedicineQuantity = (EditText) findViewById(R.id.et_medicine_quantity);
        mMedicinePrice = (EditText) findViewById(R.id.et_medicine_price);
        mMedicineDiscription = (EditText) findViewById(R.id.et_medicine_description);
        mPharmacyName = (EditText) findViewById(R.id.et_pharmacy_name);
        mMedicineImage = (EditText) findViewById(R.id.et_medicine_image_url);
        mMedicineDone = (FloatingActionButton) findViewById(R.id.btn_medicine_save);


        // identify intent to get data from it's prev intent
        Intent intent = getIntent();
        final String classType = intent.getStringExtra("Class");
        if (classType.equals("A")) {
            pharmacyName = intent.getStringExtra(PHARMACY_NAME);
        } else if (classType.equals("B")) {
            pharmacyName = intent.getStringExtra(PHRMACY_SHOW);
        }
        // final String pharmacyKey = intent.getStringExtra(PHARMACY_KEY);


        mPharmacyName.setText(pharmacyName);
        pharmacySearch = pharmacyName;

        // tag for debugging
        Log.d(TAG, "onActivityCreated: pharmacy Name: " + pharmacyName);
/*
        // define ref to pharmacy node in firebase
        mDatabaseReferencePharmacy = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Pharmacy").child(pharmacySearch);*/
        // define ref for medicine node in firebase
        mDatabaseReferenceMedicine = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Medicine");
        // deine ref for medicine pharamcy node in firebase
        mDatabaseReferenceMedicinePharmacy = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("PharamcyAndMedicine");
        // define ref for catefory node in firebase
        mDatabaseReferenceCategory = FirebaseDatabase.getInstance().getReference()
                .child("Database").child("Category");

        // when clicking on Done Button
        mMedicineDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: pressed");
                // check if medicine name is empty or not
                if (!TextUtils.isEmpty(mMedicineName.getText().toString())) {


                    if(vaildate()) {
                        // getting all text from Edit text (Views)
                        String medicineName = mMedicineName.getText().toString();
                        String medicinePrice = mMedicinePrice.getText().toString();
                        String medicineCategory = mMedicineCategory.getText().toString();
                        String medicineQuantity = mMedicineQuantity.getText().toString();
                        String medicineDescription = mMedicineDiscription.getText().toString();
                        String medicineImageUrl = mMedicineImage.getText().toString();

                        // get medicine key when adding new medicine
                        String medicineKey = mDatabaseReferenceMedicine.push().getKey();

                        // object from medicine model
                        Medicine medicineObj = new Medicine(medicineName, medicinePrice, medicineDescription,
                                medicineImageUrl, medicineKey, 0);
                        // object from medicince and pharmacy model
                        PharmacyAndMedicine pharmacyAndMedicine = new PharmacyAndMedicine(pharmacyName, medicineName, medicineQuantity);

                        //set value in medinince node
                        mDatabaseReferenceMedicine.child(medicineName).setValue(medicineObj);
                        // set value in medince and phramacy node
                        mDatabaseReferenceMedicinePharmacy.push().setValue(pharmacyAndMedicine);
                        // set value in pharmacy node
                        //mDatabaseReferencePharmacy.child("ListMedicine").push().setValue(map);
                        // set value in category node

                        //check(medicineCategory,medicineName);
                        mDatabaseReferenceCategory.child(medicineCategory).push().setValue(medicineName);


                        Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(getApplicationContext(), "Medicine Name is Empty", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void clearData() {
        mMedicineName.setText("");
        mMedicineDiscription.setText("");
        mMedicineQuantity.setText("");
        mMedicinePrice.setText("");
        mMedicineImage.setText("");
        // mMedicineCategory.setText("");
    }


    private boolean vaildate() {
        if (TextUtils.isEmpty(mMedicineCategory.getText())) {
            mMedicineCategory.setError("Category is required!");
            return false;
        }
        if (TextUtils.isEmpty(mMedicinePrice.getText())) {
            mMedicinePrice.setError("Price is required!");
            return false;
        }
        if (TextUtils.isEmpty(mMedicineQuantity.getText())) {
            mMedicineQuantity.setError("Price is required!");
            return false;
        }
        return true;

    }
}
