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
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private static Context mContext;
    private ArrayList<ObjectNote> mNotesList;
    private static OnItemClickListener mListener;
    private static OnItemLongClickListener mLongClickListener;

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

        //putting the values in the respective views
        holder.note_title.setText(mNotesList.get(position).getmNoteTitle());
        holder.note_body.setText(mNotesList.get(position).getmNoteContent());
        holder.note_date.setText(mNotesList.get(position).getmDateCreated());
    }

    @Override
    public int getItemCount() {
        //getting the table length
        return mNotesList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView note_title, note_body, note_date;
        CardView cardNotes;

        private RecyclerViewHolder(View itemView) {
            super(itemView);

            note_title = itemView.findViewById(R.id.tv_note_title);
            note_body = itemView.findViewById(R.id.tv_note_body);
            note_date = itemView.findViewById(R.id.tv_note_date);
            cardNotes = itemView.findViewById(R.id.card_notes);

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
