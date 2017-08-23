package sigit.task_4_aplikasi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import sigit.task_4_aplikasi.R;
import sigit.task_4_aplikasi.model.Income;

/**
 * Created by sigit on 24/07/17.
 */

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Income> mIncome;



    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder  {
        public Income mIncome;
        public TextView mPrimary;
        public TextView mSecondary;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            this.mPrimary = (TextView) itemLayoutView.findViewById(R.id.primary);
            this.mSecondary = (TextView) itemLayoutView.findViewById(R.id.secondary);


        }

    }
    public IncomeAdapter(Context context, ArrayList<Income> incomes) {
        this.mContext = context;
        this.mIncome = incomes;
    }
    //inflate layout list item
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        //bind value from serializable to view
        Income income = (Income) this.mIncome.get(position);
        viewHolder.mIncome = income;
        viewHolder.mPrimary.setText(income.getDescription());
        viewHolder.mSecondary.setText("$"+String.valueOf(income.getAmount()));

    }

    public int getItemCount() {
        return this.mIncome.size();
    }
}

