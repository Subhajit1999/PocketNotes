package com.subhajitkar.commercial.projet_tulip.fragments;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.chootdev.csnackbar.Duration;
import com.chootdev.csnackbar.Type;
import com.subhajitkar.commercial.projet_tulip.R;
import com.subhajitkar.commercial.projet_tulip.objects.ToDoObject;
import com.subhajitkar.commercial.projet_tulip.utils.PortableContent;
import com.subhajitkar.commercial.projet_tulip.utils.StaticFields;
import com.subhajitkar.commercial.projet_tulip.utils.StringBuilder;
import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.OnItemClickListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.callback.BatCallback;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToDoNoteFragment extends Fragment  implements BatListener, OnItemClickListener, OnOutsideClickedListener {
    private static final String TAG = "ToDoNoteFragment";

    private BatRecyclerView mRecyclerView;
    private BatAdapter mAdapter;
    private List<BatModel> mGoals;
    private BatItemAnimator mAnimator;
    private boolean exit = false;
    private EditText noteTitle;
    private String noteFlag, title, content, dateCreated;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_todo, container, false);
    }

        @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get bundle data
        if (getArguments()!=null){
            noteFlag = getArguments().getString(StaticFields.KEY_EDITOR_INTENTFLAG);
            if (noteFlag.equals("existing")) {
                //get the data
                title = getArguments().getString(StaticFields.KEY_EDITOR_TITLE);
                content = getArguments().getString(StaticFields.KEY_EDITOR_CONTENT);
                dateCreated = getArguments().getString(StaticFields.KEY_EDITOR_DATECREATED);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noteTitle = view.findViewById(R.id.et_note_title);
        mRecyclerView = (BatRecyclerView) view.findViewById(R.id.bat_recycler_view);
        mAnimator = new BatItemAnimator();

        mGoals = new ArrayList<>();
        if (noteFlag.equals("existing")) {
            //set the data
            noteTitle.setText(title);
            mGoals = new StringBuilder().getListFromChecklistString(content);
            new PortableContent(getContext()).showSnackBar(Type.UPDATE, "Created at: " + dateCreated, Duration.INFINITE);
        }else{
            mGoals.add(new ToDoObject("Example item"));
            mGoals.add(new ToDoObject("Swipe to delete"));
        }

        mRecyclerView.getView().setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.getView().setAdapter(mAdapter = new BatAdapter(mGoals,
                this, mAnimator).setOnItemClickListener(this).setOnOutsideClickListener(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BatCallback(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView.getView());
        mRecyclerView.getView().setItemAnimator(mAnimator);
        mRecyclerView.setAddItemListener(this);

        view.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.revertAnimation();
            }
        });
    }

    @Override
    public void add(String string) {
        mGoals.add(0, new ToDoObject(string));
        mAdapter.notify(AnimationType.ADD, 0);
    }

    @Override
    public void delete(int position) {
        mGoals.remove(position);
        mAdapter.notify(AnimationType.REMOVE, position);
    }

    @Override
    public void move(int from, int to) {
        if (from >= 0 && to >= 0) {
            mAnimator.setPosition(to);
            BatModel model = mGoals.get(from);
            mGoals.remove(model);
            mGoals.add(to, model);
            mAdapter.notify(AnimationType.MOVE, from, to);

            if (from == 0 || to == 0) {
                mRecyclerView.getView().scrollToPosition(Math.min(from, to));
            }
        }
    }

    @Override
    public void onClick(BatModel item, int position) {
        Toast.makeText(getContext(), mGoals.get(position).getText()+". Checked: "+mGoals.get(position).isChecked(), Toast.LENGTH_SHORT).show();
        mRecyclerView.revertAnimation();
    }

    @Override
    public void onOutsideClicked() {
        mRecyclerView.revertAnimation();
    }

    public boolean backPressed(){
        Log.d(TAG, "backPressed: back function override");
        mRecyclerView.revertAnimation();
        String title = "Save?";
        String msg = "Do you want to save the note before exit?";
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AppDialogTheme);
        builder.setTitle(title);
        if (StaticFields.darkThemeSet) {
            builder.setIcon(R.drawable.dialog_warning_dark);
        }else{
            builder.setIcon(R.drawable.dialog_warning);
        }
        builder.setMessage(msg);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (!noteTitle.getText().toString().isEmpty()) {
                    exit = true;
                    //save the data
                    StaticFields.noteTitle = noteTitle.getText().toString();
                    StaticFields.noteContent = new StringBuilder().buildChecklistString(mGoals);
                    getActivity().onBackPressed();
                }else{
                    new PortableContent(getContext()).showSnackBar(Type.WARNING,"Title can't be empty.",
                            Duration.SHORT);
                }
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                exit = true;
                StaticFields.noteTitle = StaticFields.noteContent = "";
                getActivity().onBackPressed();
            }
        });
        if (!exit) {
            builder.show();
        }
        return exit;
    }

    public boolean onSharing(){
        Log.d(TAG, "onSharing: sharing current note data");
        //sending data
        if (!noteTitle.getText().toString().isEmpty()){
            StaticFields.shareNoteTitle = noteTitle.getText().toString();
            StaticFields.shareNoteContent = new StringBuilder().buildChecklistString(mGoals);
            return true;
        }else{
            new PortableContent(getContext()).showSnackBar(Type.WARNING,"Title can't be empty.",
                    Duration.SHORT);
            return false;
        }
    }
}