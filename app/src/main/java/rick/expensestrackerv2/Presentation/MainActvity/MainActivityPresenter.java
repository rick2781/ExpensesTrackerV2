package rick.expensestrackerv2.Presentation.MainActvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.orhanobut.hawk.Hawk;

import java.lang.reflect.Array;
import java.util.ArrayList;

import rick.expensestrackerv2.Domain.Model.BillModel;
import rick.expensestrackerv2.R;
import rick.expensestrackerv2.Utils.Injection;
import rick.expensestrackerv2.Utils.RecyclerAdapter;

/**
 * Created by Rick on 1/21/2018.
 */

public class MainActivityPresenter implements DataCallback {

    private static final String TAG = "MainActivityPresenter";

    static RecyclerAdapter recyclerAdapter;

    DataCallback callback;
//
    ArrayList<BillModel> bills = new ArrayList<>();

    ArrayList<BillModel> userExpenses = new ArrayList<>();

    public MainActivityPresenter(RecyclerView recyclerView, Context context) {

        Injection.getFirebaseDatabaseClassInstance().getDatabaseInstance();
        Injection.getFirebaseDatabaseClassInstance().getDatabaseReferenceInstance();

        initData(context, recyclerView);

        Injection.getFirebaseDatabaseClassInstance().initializeCallback(this);
    }

    public void initData(Context context, RecyclerView recyclerView) {

        Hawk.init(context).build();

        BillModel bill1 = new BillModel("Energy", false);
        BillModel bill2 = new BillModel("Car", false);

        userExpenses.add(bill1);
        userExpenses.add(bill2);

        //TODO FIRST CHANGE THIS WHOLE METHOD TO POPULATE THE LISTVIEW WITH DATA RETRIEVED FROM DATABASE THEN ADD DUMMY EVERYSINGLE NEW USER

        Injection.getFirebaseDatabaseClassInstance().checkIsBillPaid(context, Injection.getDateInstance().getCurrentMonth());

//        //TODO change to a not hard coded string here
//        ArrayList<BillModel> savedExpenses;
//
//        savedExpenses = Hawk.get("userExpenses");
//
//        if (savedExpenses != null) {
//            userExpenses.addAll(savedExpenses);
//        }
//        } else {
//
//            userExpenses.add(bill1);
//            userExpenses.add(bill2);
//        }

        recyclerAdapter = new RecyclerAdapter(bills, context);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    public ArrayList<BillModel> getData(ArrayList<BillModel> firebaseBills) {
        return firebaseBills;
    }

    public void addNewBill(final Context context) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        View customDialogView = inflater.inflate(R.layout.dialog_add_new_bill, null);

        dialogBuilder.setView(customDialogView);

        final EditText userInput = customDialogView.findViewById(R.id.editTextDialogUserInput);

        dialogBuilder
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        BillModel userBill = new BillModel(userInput.getText().toString(), false);

                        userExpenses.add(userBill);
                        Log.d(TAG, "onClick: " + userExpenses.toString());

                        Hawk.put("userExpenses", userExpenses);

                        recyclerAdapter.notifyDataSetChanged();

                        Injection.getFirebaseDatabaseClassInstance().addBill(
                                userInput.getText().toString(),
                                Injection.getDateInstance().getCurrentMonth(),
                                userBill,
                                context
                                );
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.cancel();
                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}
