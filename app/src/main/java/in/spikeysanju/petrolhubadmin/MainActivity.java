package in.spikeysanju.petrolhubadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mOrders;
    private RecyclerView mOrdersRV;
    FirebaseRecyclerAdapter<Order,OrderViewHolder> orderAdapter;
    private Query mQuery;
    private String mUid;
    private MaterialSpinner mspinner;

    private ImageButton updateBtn,deleteBtn,detailsBtn,directionBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        mOrders = FirebaseDatabase.getInstance().getReference().child("Orders");

        mOrdersRV = (RecyclerView)findViewById(R.id.ordersRV);


        LinearLayoutManager mLayout= new LinearLayoutManager(getApplicationContext());
        mOrdersRV.setLayoutManager(mLayout);// here u have to add
        mLayout.setOrientation(LinearLayoutManager.VERTICAL);
        mLayout.setReverseLayout(true);
        mLayout.setStackFromEnd(true);
        mOrdersRV.setNestedScrollingEnabled(false);
        mOrdersRV.setHasFixedSize(true);




        loadMyDonations();


    }

    private void loadMyDonations() {

        //Query to display data
        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>()
                .setQuery(mOrders,Order.class)
                .build();


        orderAdapter = new FirebaseRecyclerAdapter<Order, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final OrderViewHolder holder, final int position, @NonNull Order model) {




                holder.setAddress(model.getAddress());
                //  holder.setQuantity(model.getQuantity());
                holder.setPrice(model.getPrice());
                holder.setName(model.getName());

//                holder.nView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent detailsIntent = new Intent(getApplicationContext(),OrderDetailActivity.class);
//                        detailsIntent.putExtra("orderDetail",orderAdapter.getRef(position).getKey());
//                        startActivity(detailsIntent);
//
//                    }
//                });

                updateBtn = (ImageButton)holder.nView.findViewById(R.id.action_update);
                deleteBtn = (ImageButton)holder.nView.findViewById(R.id.action_delete);
                detailsBtn = (ImageButton)holder.nView.findViewById(R.id.action_details);



                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(orderAdapter.getRef(position).getKey(), orderAdapter.getItem(position));

                    }
                });

                deleteBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String postkey = getRef(holder.getAdapterPosition()).getKey();

                        deleteItem(postkey);

                    }
                });



                detailsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent detailsIntent = new Intent(getApplicationContext(),OrderDetailActivity.class);
                        detailsIntent.putExtra("orderDetail",orderAdapter.getRef(position).getKey());
                        startActivity(detailsIntent);

                    }
                });



            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myorder_rv_lay,parent,false);

                return new OrderViewHolder(view);            }
        };


        mOrdersRV.setAdapter(orderAdapter);

        orderAdapter.notifyDataSetChanged();

    }

    private void showUpdateDialog(String key, final Order item) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("Order Status");
        alert.setMessage("Please select Status");


        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_status_layout,null);

        mspinner = (MaterialSpinner)view.findViewById(R.id.spinner);

        mspinner.setItems("waiting","onway","delivered");

        alert.setView(view);

        final String localKey = key;
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                item.setStatus(String.valueOf(mspinner.getSelectedIndex()));
                mOrders.child(localKey).setValue(item);

                // sendOrderStatusToUser(localKey,item);
                orderAdapter.notifyDataSetChanged();


            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });

        alert.show();
    }

    private void deleteItem(String postkey) {
        mOrders.child(postkey).removeValue();

    }




    public static class OrderViewHolder extends RecyclerView.ViewHolder{

        View nView;

        public OrderViewHolder(View itemView) {
            super(itemView);

            nView = itemView;
        }

        public void setAddress(String address){


            TextView titleTxt = (TextView)nView.findViewById(R.id.addressO);
            titleTxt.setText(address);
        }
        public void setPrice(String price){


            TextView titledonation = (TextView)nView.findViewById(R.id.priceO);
            titledonation.setText(price);
        }



//        public void setQuantity(String quantity){
//
//
//            TextView locTxt = (TextView)nView.findViewById(R.id.petrolAmount);
//            locTxt.setText( quantity);
//        }


        public void setName(String name){


            TextView locName = (TextView)nView.findViewById(R.id.nameO);
            locName.setText( name);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        orderAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        orderAdapter.stopListening();
    }



    private String convertCodeToStatus(String status) {
        if (status.equals("0")){
            return "Waiting";

        } else if (status.equals("1")){
            return "On the Way";

        } else {
            return "Delivered";
        }
    }
}
