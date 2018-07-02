package www.ownerpanal.com.ownarpanal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import www.ownerpanal.com.ownarpanal.Model.PharmacyInfo;

public class ShowPharmacy extends AppCompatActivity {

    private static final String PHRMACY_NAME = "phrmacy";
    private static final String PHRMACY_SHOW = "show";
    RecyclerView mRecyclerView;
    private static final String TAG = "ShowPharmacy";


    DatabaseReference mRef;

    String userEmail;
    FirebaseAuth  mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pharmacy);
        mFirebaseAuth = FirebaseAuth.getInstance();
        userEmail = mFirebaseAuth.getCurrentUser().getEmail();

        init();

    }

    private void init(){
        mRecyclerView = (RecyclerView)findViewById(R.id.rv_show_phrmacy);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



        mRef = FirebaseDatabase.getInstance().getReference().child("Database").child("Pharmacy");
        mRef.keepSynced(true);

        Query query = mRef.orderByChild("ownerEmail").equalTo(userEmail);

        final FirebaseRecyclerAdapter<PharmacyInfo,myViewHolder> adapter = new FirebaseRecyclerAdapter<PharmacyInfo, myViewHolder>(
                PharmacyInfo.class,
                R.layout.custom_show_pharmacy,
                myViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(final myViewHolder viewHolder, final PharmacyInfo model, final int position) {
                viewHolder.phrmacyNameTextView.setText(model.getPharmacyName());

                viewHolder.AddMedicineTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        redirectToAddMedicine(viewHolder.phrmacyNameTextView.getText().toString());
                    }
                });

                viewHolder.ShowReservationTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "you Clicked on: "+position);
                        Toast.makeText(getApplicationContext(),"Item #"+model.getPharmacyName(),Toast.LENGTH_SHORT).show();

                        // directToCreatePharmacy(model);
                        redirectToReservation(viewHolder.phrmacyNameTextView.getText().toString());
                    }
                });

            }
        };

        mRecyclerView.setAdapter(adapter);

    }

    public static class myViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView phrmacyNameTextView;
        public TextView ShowReservationTextView;
        public TextView AddMedicineTextView;


        public myViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            phrmacyNameTextView = (TextView)itemView.findViewById(R.id.tv_phramcy_name);
            AddMedicineTextView = (TextView)itemView.findViewById(R.id.tv_add_medicine);
            ShowReservationTextView = (TextView)itemView.findViewById(R.id.tv_show_reservation);
        }

    }
    private void redirectToReservation(String name){
        Intent intent = new Intent(getApplicationContext(),ShowReservationActivity.class);
        intent.putExtra(PHRMACY_NAME,name);
        startActivity(intent);
    }

    private void redirectToAddMedicine(String phrmacyName){
        Intent intent = new Intent(getApplicationContext(),AddMedicineActivity.class);
        intent.putExtra("Class","B");
        intent.putExtra(PHRMACY_SHOW,phrmacyName);
        startActivity(intent);
    }


}
