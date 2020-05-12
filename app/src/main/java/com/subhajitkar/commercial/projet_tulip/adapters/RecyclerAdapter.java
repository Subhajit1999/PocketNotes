package com.subhajitkar.commercial.projet_tulip.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.objects.ObjectNote;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;
import com.subhajitkar.commercial.projet_tulip.utils.StringBuilder;

import java.util.ArrayList;
import java.util.Random;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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

    @Override
    public int getItemViewType(int position) {
        if (!mNotesList.get(position).getmEditorType().equals("drawing")){
            return 0;
        }else{
            return 1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: gets called");
        View v;
        switch(viewType) {
            case 0:
                v = LayoutInflater.from(mContext).inflate(R.layout.element_notes_recycler, parent, false);
                return new RecyclerViewHolderNormal(v);
            case 1:
                v = LayoutInflater.from(mContext).inflate(R.layout.element_canvas_recycler, parent, false);
                return new RecyclerViewHolderDrawing(v);
        }
        return onCreateViewHolder(parent,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: binding data with the element layout views");
        //getting random index for view color
        do {
            randInt = new Random().nextInt(7);
        } while (randInt == prevcolor);
        switch (holder.getItemViewType()) {
            case 0:   //normal note view
            RecyclerViewHolderNormal holderNormal = (RecyclerViewHolderNormal) holder;
            //putting the values in the respective views
            holderNormal.note_title.setText(mNotesList.get(position).getmNoteTitle());
            holderNormal.note_body.setText(mNotesList.get(position).getmNoteContent());
            holderNormal.note_date.setText(mNotesList.get(position).getmDateCreated());
            holderNormal.cardBorderView.setBackgroundColor(mContext.getResources().getColor(StaticFields.colorLists[randInt]));
            if (mNotesList.get(position).getIsStarred()) {
                holderNormal.starLayout.setVisibility(View.VISIBLE);
                holderNormal.iv_star.setVisibility(View.VISIBLE);
                if (StaticFields.darkThemeSet) {
                    holderNormal.iv_star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_filled_dark));
                } else {
                    holderNormal.iv_star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_filled));
                }
            } else {
                holderNormal.starLayout.setVisibility(View.INVISIBLE);
                holderNormal.iv_star.setVisibility(View.INVISIBLE);
            }
                break;
            case 1:  //canvas note view
                RecyclerViewHolderDrawing holderDrawing = (RecyclerViewHolderDrawing) holder;
                //putting data in the respective views
                holderDrawing.iv_drawing.setImageBitmap(Bitmap.createScaledBitmap(new StringBuilder().base64ToBitmap(mNotesList.get(position).getmNoteContent())
                ,400,400,false));
                holderDrawing.cardBorderView.setBackgroundColor(mContext.getResources().getColor(StaticFields.colorLists[randInt]));
                if (mNotesList.get(position).getIsStarred()) {
                    holderDrawing.starLayout.setVisibility(View.VISIBLE);
                    holderDrawing.iv_star.setVisibility(View.VISIBLE);
                    if (StaticFields.darkThemeSet) {
                        holderDrawing.iv_star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_filled_dark));
                    } else {
                        holderDrawing.iv_star.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_star_filled));
                    }
                } else {
                    holderDrawing.starLayout.setVisibility(View.INVISIBLE);
                    holderDrawing.iv_star.setVisibility(View.INVISIBLE);
                }
                break;
        }
        prevcolor = randInt;
    }

    @Override
    public int getItemCount() {
        //getting the table length
        return mNotesList.size();
    }

    public static class RecyclerViewHolderNormal extends RecyclerView.ViewHolder {
        TextView note_title, note_body, note_date;
        View cardBorderView;
        ImageView iv_star;
        LinearLayout starLayout;

        private RecyclerViewHolderNormal(View itemView) {
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

    public static class RecyclerViewHolderDrawing extends RecyclerView.ViewHolder {
        View cardBorderView;
        ImageView iv_star, iv_drawing;
        LinearLayout starLayout;

        private RecyclerViewHolderDrawing(View itemView) {
            super(itemView);

            iv_drawing = itemView.findViewById(R.id.iv_canvas_note);
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