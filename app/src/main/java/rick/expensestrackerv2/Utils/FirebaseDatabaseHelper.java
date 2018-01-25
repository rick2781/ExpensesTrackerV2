package rick.expensestrackerv2.Utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import rick.expensestrackerv2.Domain.Model.BillModel;
import rick.expensestrackerv2.Presentation.MainActvity.NotificationCallback;
import rick.expensestrackerv2.R;

/**
 * Created by Rick on 1/22/2018.
 */

public class FirebaseDatabaseHelper {

    private static final String TAG = "FirebaseDatabaseHelper";

    static FirebaseDatabaseHelper INSTANCE;

    NotificationCallback callback;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;

    public static FirebaseDatabaseHelper getDatabaseClassInstance() {

        if (INSTANCE == null) {
            INSTANCE = new FirebaseDatabaseHelper();
        }

        return INSTANCE;
    }

    public FirebaseDatabase getDatabaseInstance() {

        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
        }

        return firebaseDatabase;
    }

    public DatabaseReference getDatabaseReferenceInstance() {

        if (myRef == null) {
            myRef = firebaseDatabase.getReference();
        }

        return myRef;
    }

    public void addBill(String billName, String month, Context context) {

        myRef.child(context.getString(R.string.bills))
                .child(billName)
                .child(context.getString(R.string.month) + month)
                .child("isPaid")
                .setValue(false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                callback.notifyWhendone();
            }
        });
    }

    public void payBill(BillModel bill, String month, Context context) {

        myRef.child(context.getString(R.string.bills))
                .child(bill.getBillName())
                .child(context.getString(R.string.month) + month)
                .child("isPaid")
                .setValue(true);

    }

//    public ArrayList<BillModel> checkIsBillPaid(final Context context, final String month) {
//
//        myRef.child(context.getString(R.string.bills))
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//
//                        DataSnapshot dataSnapshot1 = dataSnapshot.child(context.getString(R.string.month) + month).child("isPaid");
//
//                        for (DataSnapshot ds : dataSnapshot1.getChildren()) {
//
//                            boolean isPaid = (boolean) ds.getValue();
//
//                            BillModel firebaseBill = new BillModel(dataSnapshot.getKey(), isPaid);
//
//                            firebaseBills.add(firebaseBill);
//                            Log.d(TAG, "onDataChange: " + firebaseBills.toString());
//                        }
//
//                        callback.notifyWhendone();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//
//        return firebaseBills;
//    }

//    public void addGrocery(GroceryModel groceryModel, String month, String fullDate, Context context) {
//
//        Log.d(TAG, "addGrocery: executed");
//
//        myRef.child(context.getString(R.string.grocery))
//                .child(context.getString(R.string.month))
//                .child(month)
//                .child(fullDate)
//                .setValue("testing this shit");
//    }

    public void checkRemainingFunds(String month, Context context) {

        myRef.child(context.getString(R.string.grocery))
                .child(context.getString(R.string.month))
                .child(month)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot groceryModel : dataSnapshot.getChildren() ) {

                            int currentGroceryValue = Integer.parseInt(groceryModel.child("groceryValue").getValue().toString());

                            ArrayList<Integer> groceryValues = new ArrayList<>();
                            groceryValues.add(currentGroceryValue);

                            int totalValue = 0;

                            for (Integer groceryValue : groceryValues) {

                                totalValue = groceryValue + totalValue;
                            }

                            Log.d(TAG, "onDataChange: total value: " + totalValue);

                            Hawk.put("totalValue", totalValue);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public void initializeCallback(NotificationCallback callback) {
        this.callback = callback;
    }
}
