package com.subhajitkar.commercial.projet_tulip;

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
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private static final String TAG = "RecyclerAdapter";
    private Context mContext;
    private static Cursor mCursor;
    private int mTitleIndex, mContentIndex, mCreatedDateIndex;
    private static OnItemClickListener mListener;
//    private static OnItemLongClickListener mLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
//    public void setOnItemLongClickListener(OnItemLongClickListener listener){
//        mLongClickListener = listener;
//    }

    public RecyclerAdapter(Context context, Cursor c, int titleIndex, int contentIndex, int createdDateIndex){
        mContext = context;
        mCursor = c;
        mTitleIndex = titleIndex;
        mContentIndex = contentIndex;
        mCreatedDateIndex = createdDateIndex;
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
        mCursor.moveToPosition(position);
        holder.note_title.setText(mCursor.getString(mTitleIndex));
        holder.note_body.setText(mCursor.getString(mContentIndex));
        holder.note_date.setText(mCursor.getString(mCreatedDateIndex));
    }

    @Override
    public int getItemCount() {
        //getting the table length
        return StaticFields.getProfilesCount("notes");
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView note_title, note_body, note_date;

        private RecyclerViewHolder(View itemView) {
            super(itemView);

            note_title = itemView.findViewById(R.id.tv_note_title);
            note_body = itemView.findViewById(R.id.tv_note_body);
            note_date = itemView.findViewById(R.id.tv_note_date);

            itemView.setOnCreateContextMenuListener(this);  //registering context menu to the element views
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
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (mLongClickListener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            mLongClickListener.onItemLongClick(position);
//                        }
//                    }
//                    return true;
//                }
//            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Choose option:");
        MenuItem archive = menu.add(Menu.NONE, 1,1, "Archive");
        MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
        MenuItem share = menu.add(Menu.NONE, 3, 3, "Share");
        MenuItem copy = menu.add(Menu.NONE, 4, 4, "Make a copy");
        MenuItem changeColour = menu.add(Menu.NONE, 5, 5, "Change colour");
        //attaching menuItems with the click action method
        archive.setOnMenuItemClickListener(onEditMenu);
        delete.setOnMenuItemClickListener(onEditMenu);
        share.setOnMenuItemClickListener(onEditMenu);
        copy.setOnMenuItemClickListener(onEditMenu);
        changeColour.setOnMenuItemClickListener(onEditMenu);
        }
        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case 1:
                        //menu action archive
                        break;
                    case 2:
                        //menu action delete
//                        int position = getAdapterPosition();
//                        if (position!=RecyclerView.NO_POSITION){
//                            int Uid = mCursor.getColumnIndex("id");
//                            mCursor.moveToPosition(position);
//                            StaticFields.database.delete("notes","id = ?",new String[]{mCursor.getString(Uid)});
//                        }
                        break;
                    case 3:
                        //menu action share
                        break;
                    case 4:
                        //menu action copy
                        break;
                    case 5:
                        //menu action change colour
                        break;
                }
                return true;
            }
        };
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
//    public interface OnItemLongClickListener{
//        void onItemLongClick(int position);
//    }
    public void NotifyListUpdate(int position){
        Log.d(TAG, "NotifyListUpdate: list dataset changed");
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,StaticFields.getProfilesCount("notes"));
    }
}
