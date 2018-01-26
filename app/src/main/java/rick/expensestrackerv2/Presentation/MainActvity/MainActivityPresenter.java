package rick.expensestrackerv2.Presentation.MainActvity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import rick.expensestrackerv2.Domain.Model.BillModel;
import rick.expensestrackerv2.R;
import rick.expensestrackerv2.Utils.Injection;
import rick.expensestrackerv2.Utils.RecyclerAdapter;

/**
 * Created by Rick on 1/21/2018.
 */

public class MainActivityPresenter implements NotificationCallback {

    private static final String TAG = "MainActivityPresenter";

    static RecyclerAdapter recyclerAdapter;

    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    LayoutInflater inflater;

    ArrayList<BillModel> bills = new ArrayList<>();

    public MainActivityPresenter(RecyclerView recyclerView, Context context, RecyclerView.ItemDecoration decoration) {

        Injection.getFirebaseDatabaseClassInstance().getDatabaseInstance();
        Injection.getFirebaseDatabaseClassInstance().getDatabaseReferenceInstance();

        Injection.getFirebaseDatabaseClassInstance().initializeCallback(this);

        checkFirstTime(context);

        initData(context, recyclerView, decoration);
    }

    public void initData(Context context, RecyclerView recyclerView, RecyclerView.ItemDecoration decoration) {

        sharedPreferences = context.getSharedPreferences("savedBills", Context.MODE_PRIVATE);

        Gson gson = new Gson();

        String json = sharedPreferences.getString("bills", "");

        Type type = new TypeToken<ArrayList<BillModel>>(){}.getType();
        ArrayList<BillModel> savedBills = gson.fromJson(json, type);

        if (savedBills != null) {
            bills.addAll(savedBills);
        }

        resetBillsNewMonth();

        recyclerAdapter = new RecyclerAdapter(bills);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void notifyWhendone() {

        recyclerAdapter.notifyDataSetChanged();
    }

    public void addNewBill(final Context context) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        inflater = LayoutInflater.from(context);
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

                        saveBills(context);
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

    public void newRemainingFundsLimit (final Context context, final TextView remainingFunds) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        inflater = LayoutInflater.from(context);

        View customDialogView = inflater.inflate(R.layout.dialog_add_new_grocery, null);

        dialogBuilder.setView(customDialogView);

        final EditText userInput = customDialogView.findViewById(R.id.editTextDialogUserInput);

        dialogBuilder
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (userInput.getText().toString().isEmpty()) {

                            Toast.makeText(context, "You need to fill out all empty spaces!", Toast.LENGTH_SHORT).show();
                        } else {

                            sharedPreferences = context.getSharedPreferences("FundsLimit", Context.MODE_PRIVATE);
                            editor = sharedPreferences.edit();
                            editor.putString("fundsLimit", userInput.getText().toString());
                            editor.apply();

                            remainingFunds.setText("Remaining Funds: $ " + userInput.getText().toString());
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void addNewGrocery(final Context context, final TextView remainingFunds) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        inflater = LayoutInflater.from(context);

        View customDialogView = inflater.inflate(R.layout.dialog_add_new_grocery, null);

        dialogBuilder.setView(customDialogView);

        final EditText userInput = customDialogView.findViewById(R.id.editTextDialogUserInput);

        dialogBuilder
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (userInput.getText().toString().isEmpty()) {

                            Toast.makeText(context, "You need to fill out all empty spaces!", Toast.LENGTH_SHORT).show();
                        } else {

                            int currentGroceryValue = Integer.parseInt(userInput.getText().toString());

                            sharedPreferences = context.getSharedPreferences("FundsLimit", Context.MODE_PRIVATE);

                            int fundsLimit = Integer.parseInt(sharedPreferences.getString("fundsLimit", ""));

                            remainingFunds.setText("Remaining Funds: $ " + String.valueOf(fundsLimit - groceryPrefs(context, currentGroceryValue)));
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    public void saveBills(Context context) {

        sharedPreferences = context.getSharedPreferences("savedBills", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(bills);
        editor.putString("bills", json);
        editor.apply();
    }

    public void checkFirstTime(Context context) {

        sharedPreferences = context.getSharedPreferences("FirstTime", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        boolean firstRun = sharedPreferences.getBoolean("firstRun", true);

        if (firstRun) {

            bills.add(new BillModel("Energy", false));
            bills.add(new BillModel("Internet", false));
            bills.add(new BillModel("Car", false));
            bills.add(new BillModel("Rent", false));

            editor.putBoolean("firstRun", false).apply();
        }
    }

    public void resetBillsNewMonth() {

        if (Injection.getDateInstance().checkMonth()) {

            for (BillModel bills : bills) {

                bills.setPaid(false);
            }
        }
    }

    public int groceryPrefs(Context context, int currentGroceryValue) {

        String grocerySumKey = "grocerySum";

        int grocerySum = 0;

        sharedPreferences = context.getSharedPreferences("Grocery", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getInt(grocerySumKey, 0) == 0) {

            grocerySum = currentGroceryValue + grocerySum;

            editor.putInt(grocerySumKey, grocerySum);

        } else {

            grocerySum = sharedPreferences.getInt(grocerySumKey, 0) + currentGroceryValue;

            editor.putInt(grocerySumKey, grocerySum);
        }

        return grocerySum;
    }
}
