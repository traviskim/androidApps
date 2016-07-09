package com.traviswkim.simpletodo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by traviswkim on 7/8/16.
 */
public class EditTaskDialogFragment extends DialogFragment {
    private long mId;
    private TextView mTitle;
    private EditText mTaskName;
    private EditText mDueDate;
    private Spinner mPriority;
    private String[] priorityItems = new String[]{"P1", "P2", "P3"};

    public interface EditTaskDialogListener {
        void onFinishInputDialog(Todo newTask);
    }

    public EditTaskDialogFragment(){
    }

    public static EditTaskDialogFragment newInstance(String title){
        //This is used for adding a task
        EditTaskDialogFragment frag = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public static EditTaskDialogFragment newInstance(String title, long id, String taskName, String dueDate, String priority){
        //This is used for editing a task
        EditTaskDialogFragment frag = new EditTaskDialogFragment();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putString("title", title);
        args.putString("taskName", taskName);
        args.putString("dueDate", dueDate);
        args.putString("priority", priority);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_todo, container);
    }


    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle = (TextView) view.findViewById(R.id.tvTitleDialog);
        mTaskName = (EditText) view.findViewById(R.id.etTaskNameDialog);
        mDueDate = (EditText) view.findViewById(R.id.etDueDateDialog);
        mPriority = (Spinner) view.findViewById(R.id.spnPriorityDialog);
        Button mSubmit = (Button) view.findViewById(R.id.btnSave);
        Button mCancel = (Button) view.findViewById(R.id.btnCancel);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Todo updTodo = new Todo(mTaskName.getText().toString(), mDueDate.getText().toString(), mPriority.getSelectedItem().toString());
                updTodo.setId(mId);
                EditTaskDialogListener listener = (EditTaskDialogListener)getActivity();
                listener.onFinishInputDialog(updTodo);
                dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // Fetch arguments from bundle and set title
        mId = getArguments().getLong("id", -1);
        String title = getArguments().getString("title", "Task");
        String taskName = getArguments().getString("taskName", "");
        String dueDate = getArguments().getString("dueDate", "");
        String priority = getArguments().getString("priority", "");
        getDialog().setTitle(title);
        mTitle.setText(title);
        mTaskName.setText(taskName);
        mDueDate.setText(dueDate);
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.task_priority, android.R.layout.simple_spinner_item);
        mPriority.setAdapter(priorityAdapter);
        int selectedPos = priorityAdapter.getPosition(priority);
        mPriority.setSelection(selectedPos);

        // Show soft keyboard automatically and request focus to field
        mTaskName.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
