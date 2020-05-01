package com.subhajitkar.commercial.projet_tulip.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.subhajitkar.commercial.projet_tulip.R;

import java.util.ArrayList;

public class BottomSheetRecyclerAdapter extends RecyclerView.Adapter<BottomSheetRecyclerAdapter.RecyclerViewHolder>{
    private static final String TAG = "BottomSheetRecyclerAdap";

    private ArrayList<DataModel> itemsList;
    private Context context;
    private BottomSheetRecyclerAdapter.OnBottomSheetItemClickListener mListener;
    private BottomSheetDialog dialog;

    public BottomSheetRecyclerAdapter(Context context, ArrayList<DataModel> itemsList, BottomSheetDialog dialog){
        this.context = context;
        this.itemsList = itemsList;
        this.dialog = dialog;
    }

    public void setOnItemClickListener(BottomSheetRecyclerAdapter.OnBottomSheetItemClickListener listener){
        mListener = listener;
    }
    @NonNull
    @Override
    public BottomSheetRecyclerAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: gets called");
        View v = LayoutInflater.from(context).inflate(R.layout.element_bottomsheet_recyler, parent, false);
        return new BottomSheetRecyclerAdapter.RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BottomSheetRecyclerAdapter.RecyclerViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: preparing bottomsheet recycler views");
        //icon
        holder.item_icon.setImageDrawable(context.getResources().getDrawable(itemsList.get(position).getIcon()));
        //text
        holder.item_text.setText(itemsList.get(position).getText());
        holder.item_text.setTextColor(context.getResources().getColor(itemsList.get(position).getTextColor()));
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView item_text;
        ImageView item_icon;
        LinearLayout item_layout;

        private RecyclerViewHolder(View itemView) {
            super(itemView);

            item_text = itemView.findViewById(R.id.tv_bottomsheet_item);
            item_icon = itemView.findViewById(R.id.iv_bottomsheet_item);
            item_layout = itemView.findViewById(R.id.bottomsheet_layout_item);

            item_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onBottomSheetItemClick(position);
                            dialog.dismiss();
                        }
                    }
                }
            });
        }
    }

    public interface OnBottomSheetItemClickListener{
        void onBottomSheetItemClick(int position);
    }
}
