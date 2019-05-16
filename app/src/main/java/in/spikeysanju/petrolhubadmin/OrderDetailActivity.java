package in.spikeysanju.petrolhubadmin;

import android.content.res.ColorStateList;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderDetailActivity extends AppCompatActivity {
    DatabaseReference orderDB;
    Order currentOrder;
    String orderDetail="";
    private TextView nameD,priceD,quantityD,addressD,statuss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        orderDB = FirebaseDatabase.getInstance().getReference().child("Orders");

        nameD = (TextView)findViewById(R.id.nameS);
        priceD = (TextView)findViewById(R.id.priceS);
        quantityD = (TextView)findViewById(R.id.quantityS);
        addressD = (TextView)findViewById(R.id.addressS);
        statuss = (TextView)findViewById(R.id.statusu);



        Bundle extras = getIntent().getExtras();
        if (extras!=null){

            orderDetail = extras.getString("orderDetail");
            if (!orderDetail.isEmpty() && orderDetail!=null){

                fetchOrderDetails(orderDetail);

            }
        }







    }

    private void fetchOrderDetails(String orderDetail) {


        orderDB.child(orderDetail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                currentOrder = dataSnapshot.getValue(Order.class);

                nameD.setText(currentOrder.getName());
                priceD.setText(currentOrder.getPrice());
                quantityD.setText(currentOrder.getQuantity());
                addressD.setText(currentOrder.getAddress());



                if (currentOrder.getStatus().equals("0")){

                    quantityD.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.red)));
                } else if (currentOrder.getStatus().equals("1")){
                    quantityD.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.orange)));

                }else if (currentOrder.getStatus().equals("2")){
                    quantityD.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color._light_green)));

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





}





