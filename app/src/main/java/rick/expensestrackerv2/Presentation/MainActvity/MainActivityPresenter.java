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

public class MainActivityPresenter implements NotificationCallback {

    private static final String TAG = "MainActivityPresenter";

    static RecyclerAdapter recyclerAdapter;

    ArrayList<BillModel> bills = new ArrayList<>();

    public MainActivityPresenter(RecyclerView recyclerView, Context context) {

        Injection.getFirebaseDatabaseClassInstance().getDatabaseInstance();
        Injection.getFirebaseDatabaseClassInstance().getDatabaseReferenceInstance();

        Injection.getFirebaseDatabaseClassInstance().initializeCallback(this);

        initData(context, recyclerView);
    }

    public void initData(Context context, RecyclerView recyclerView) {

        savedBills.removeAll(savedBills);

        recyclerAdapter = new RecyclerAdapter(savedBills, context);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    public void notifyWhendone() {

        recyclerAdapter.notifyDataSetChanged();
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
