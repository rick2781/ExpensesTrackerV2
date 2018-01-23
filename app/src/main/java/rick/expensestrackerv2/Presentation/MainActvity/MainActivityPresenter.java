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

public class MainActivityPresenter {

    private static final String TAG = "MainActivityPresenter";

    static RecyclerAdapter recyclerAdapter;

    ArrayList<BillModel> bills = new ArrayList<>();

    public MainActivityPresenter(RecyclerView recyclerView, Context context) {

        Injection.getFirebaseDatabaseClassInstance().getDatabaseInstance();
        Injection.getFirebaseDatabaseClassInstance().getDatabaseReferenceInstance();

        initData(context, recyclerView);
    }

    public void initData(Context context, RecyclerView recyclerView) {

        Hawk.init(context).build();

        BillModel bill1 = new BillModel("Energy", false);
        BillModel bill2 = new BillModel("Car", true);
        BillModel bill3 = new BillModel("Rent", false);
        BillModel bill4 = new BillModel("Internet", true);
        BillModel bill5 = new BillModel("Water", true);

        bills.add(bill1);
        bills.add(bill2);
        bills.add(bill3);
        bills.add(bill4);
        bills.add(bill5);

        //TODO FIRST CHANGE THIS WHOLE METHOD TO POPULATE THE LISTVIEW WITH DATA RETRIEVED FROM DATABASE THEN ADD DUMMY EVERYSINGLE NEW USER

        ArrayList<BillModel> savedBillList;

        if (Hawk.get("bills") != null) {

            savedBillList = Hawk.get("bills");

            for (BillModel savedBill : savedBillList) {
                if (!bills.contains(savedBill)) {
                    bills.add(savedBill);
                }
            }
        }

        Injection.getFirebaseDatabaseClassInstance().checkIsBillPaid(
                Injection.getDateInstance().getCurrentMonth(),
                bills,
                context
        );

        recyclerAdapter = new RecyclerAdapter(bills, context);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
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

                        BillModel bill = new BillModel(userInput.getText().toString(), false);

                        bills.add(bill);

                        Hawk.put("bills", bills);

                        recyclerAdapter.notifyDataSetChanged();

                        Injection.getFirebaseDatabaseClassInstance().addBill(
                                userInput.getText().toString(),
                                Injection.getDateInstance().getCurrentMonth(),
                                bill,
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
