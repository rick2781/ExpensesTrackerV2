package rick.expensestrackerv2.Utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rick.expensestrackerv2.Domain.Model.BillModel;
import rick.expensestrackerv2.R;

/**
 * Created by Rick on 1/21/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    ArrayList<BillModel> billModel;
    Context mContext;

    public RecyclerAdapter(ArrayList<BillModel> billModel, Context mContext) {
        this.billModel = billModel;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final BillModel bills = billModel.get(position);

        if (bills.getBillName() != null) {

            holder.tvBillName.setText(bills.getBillName());
        }

        if (!bills.isPaid()) {

            holder.iCheckBill.setImageResource(R.drawable.unpaidbill);
        }

        holder.payBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bills.setPaid(true);

                holder.iCheckBill.setImageResource(R.drawable.paidbill);
            }
        });

        holder.deleteBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notifyItemRangeChanged(position, billModel.size());
                billModel.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return billModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.billName)
        TextView tvBillName;

        @BindView(R.id.checkBill)
        ImageView iCheckBill;

        @BindView(R.id.payBill)
        ImageButton payBill;

        @BindView(R.id.deleteBill)
        ImageButton deleteBill;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
