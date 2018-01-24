package rick.expensestrackerv2.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Map;

import rick.expensestrackerv2.Domain.Model.BillModel;
import rick.expensestrackerv2.Presentation.MainActvity.DataCallback;
import rick.expensestrackerv2.R;

/**
 * Created by Rick on 1/22/2018.
 */

public class FirebaseDatabaseHelper {

    private static final String TAG = "FirebaseDatabaseHelper";

    static FirebaseDatabaseHelper INSTANCE;

    DataCallback callback;

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

    public void addBill(String billName, String month, BillModel bill, Context context) {

        myRef.child(context.getString(R.string.bills))
                .child(billName)
                .child(context.getString(R.string.month) + month)
                .child("isPaid")
                .setValue(false);
    }

    public void payBill(BillModel bill, String month, Context context) {

//        BillModel paidBill = new BillModel(bill.getBillName(), true);

        myRef.child(context.getString(R.string.bills))
                .child(bill.getBillName())
                .child(context.getString(R.string.month) + month)
                .child("isPaid")
                .setValue(true);

    }

    public void checkIsBillPaid(final Context context, final String month) {

        myRef.child(context.getString(R.string.bills))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        ArrayList<BillModel> firebaseBills = new ArrayList<>();

                        for (DataSnapshot data : dataSnapshot.getChildren()) {

                            boolean isPaid = (boolean) data.child(context.getString(R.string.month) + month)
                                    .child("isPaid")
                                    .getValue();

                            BillModel firebaseBill = new BillModel(data.getKey().toString(), isPaid);

                            firebaseBills.add(firebaseBill);
                        }

                        callback.getData(firebaseBills);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

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

    public void initializeCallback(DataCallback callback) {
        this.callback = callback;
    }
}
