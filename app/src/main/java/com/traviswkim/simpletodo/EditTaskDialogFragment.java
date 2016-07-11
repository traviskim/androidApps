package com.traviswkim.simpletodo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;

/**
 * Created by traviswkim on 7/8/16.
 */
public class EditTaskDialogFragment extends DialogFragment {
    private long mId;
    private TextView mTitle;
    private EditText mTaskName;
    private EditText mDueDate;
    private Spinner mPriority;
    private final String myFormat = "MM/dd/yyyy";
    SimpleDateFormat spf = new SimpleDateFormat(myFormat);
    Calendar myCalendar = Calendar.getInstance();
    final long today = System.currentTimeMillis() - 1000;
    Date selectedDate;

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

        // Fetch arguments from bundle and set title
        mId = getArguments().getLong("id", -1);
        String title = getArguments().getString("title", "Task");
        String taskName = getArguments().getString("taskName", "");
        String dueDate = getArguments().getString("dueDate", "");
        String priority = getArguments().getString("priority", "");
        getDialog().setTitle(title);
        mTitle.setText(title);
        mTaskName.setText(taskName);
        try{
            if(dueDate != ""){
                selectedDate = spf.parse(dueDate);
            }
        }catch(ParseException e){
            selectedDate = null;
        }
        mDueDate.setText(dueDate);
        ArrayAdapter<CharSequence> priorityAdapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.task_priority, android.R.layout.simple_spinner_item);
        mPriority.setAdapter(priorityAdapter);
        int selectedPos = priorityAdapter.getPosition(priority);
        mPriority.setSelection(selectedPos);

        // Show soft keyboard automatically and request focus to field
        mTaskName.requestFocus();

        mDueDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(selectedDate != null){
                    myCalendar.setTime(selectedDate);
                }
                int year = myCalendar.get(Calendar.YEAR);
                int month = myCalendar.get(Calendar.MONTH);
                int day = myCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(getActivity(), date, year, month, day);
                dpd.getDatePicker().setMinDate(today);
                dpd.show();
            }
        });

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

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDueDate();
        }

    };

    private void updateDueDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mDueDate.setText(sdf.format(myCalendar.getTime()));
    }
}
