package com.subhajitkar.commercial.projet_tulip.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

import java.util.ArrayList;
import java.util.Random;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private static Context mContext;
    private ArrayList<ObjectNote> mNotesList;
    private static OnItemClickListener mListener;
    private static OnItemLongClickListener mLongClickListener;
    private int randInt,prevcolor=-1;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener){
        mLongClickListener = listener;
    }

    public RecyclerAdapter(Context context, ArrayList<ObjectNote> notesList){
        mContext = context;
        mNotesList = notesList;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: gets called");
        View v = LayoutInflater.from(mContext).inflate(R.layout.element_notes_recycler, parent, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: binding data with the element layout views");
        //getting random index for view color
        do {
            randInt = new Random().nextInt(7);
        }while(randInt==prevcolor);
        //putting the values in the respective views
        holder.note_title.setText(mNotesList.get(position).getmNoteTitle());
        holder.note_body.setText(mNotesList.get(position).getmNoteContent());
        holder.note_date.setText(mNotesList.get(position).getmDateCreated());
        holder.cardBorderView.setBackgroundColor(mContext.getResources().getColor(StaticFields.colorLists[randInt]));
        if (mNotesList.get(position).getIsStarred()){
            holder.starLayout.setVisibility(View.VISIBLE);
            holder.iv_star.setVisibility(View.VISIBLE);
            if (StaticFields.darkThemeSet){
                holder.iv_star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_filled_dark));
            }else{
                holder.iv_star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_filled));
            }
        }else{
            holder.starLayout.setVisibility(View.INVISIBLE);
            holder.iv_star.setVisibility(View.INVISIBLE);
        }
        prevcolor=randInt;
    }

    @Override
    public int getItemCount() {
        //getting the table length
        return mNotesList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView note_title, note_body, note_date;
        View cardBorderView;
        ImageView iv_star;
        LinearLayout starLayout;

        private RecyclerViewHolder(View itemView) {
            super(itemView);

            note_title = itemView.findViewById(R.id.tv_note_title);
            note_body = itemView.findViewById(R.id.tv_note_body);
            note_date = itemView.findViewById(R.id.tv_note_date);
            cardBorderView = itemView.findViewById(R.id.view);
            iv_star = itemView.findViewById(R.id.iv_note_star);
            starLayout = itemView.findViewById(R.id.layout_note_star);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mLongClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mLongClickListener.onItemLongClick(position);
                        }
                    }
                    return true;
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }
}