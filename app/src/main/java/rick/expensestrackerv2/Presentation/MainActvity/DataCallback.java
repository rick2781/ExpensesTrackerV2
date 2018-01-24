package rick.expensestrackerv2.Presentation.MainActvity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import rick.expensestrackerv2.Domain.Model.BillModel;

/**
 * Created by Rick on 1/23/2018.
 */

public interface DataCallback {

    ArrayList<BillModel> getData (ArrayList<BillModel> firebaseBillsl);
}