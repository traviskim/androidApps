package com.traviswkim.simpletodo;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EditTaskDialogFragment.EditTaskDialogListener {
    private final int REQUEST_CODE = 20;
    private int selectedPos = -1;
    ArrayList<Todo> items;
    TodosAdapter itemsAdapter;
    ListView lvlItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<Todo>();
        lvlItems = (ListView) findViewById(R.id.lvlItems);
        readItem();
        itemsAdapter = new TodosAdapter(this, items);
        lvlItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupListViewListener(){
        lvlItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                        boolean result = removeItem(items.get(pos).getId());
                        if(result){
                            items.remove(pos);
                            itemsAdapter.notifyDataSetChanged();
                            return true;
                        }
                        return false;
                    }
                });
        lvlItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                        selectedPos = pos;
                        showEditDialog("Edit a Task", items.get(pos).getId(), items.get(pos).taskName.toString(), items.get(pos).dueDate.toString(), items.get(pos).priority.toString());
                    }
                });
    }

    private void readItem(){
        try{
            long numberOfTodos = Todo.count(Todo.class);
            List<Todo> todoList = (ArrayList<Todo>) Todo.listAll(Todo.class);
            if(numberOfTodos > 0) {
                items.addAll(todoList);
            }
        }catch(Exception e){
            items = new ArrayList<Todo>();
        }
    }

    private boolean writeItem(Todo aTask, Long id){
        try{
            if(aTask != null){
                if(id > 0){
                    Todo updTask = Todo.findById(Todo.class, id);
                    updTask.taskName = aTask.taskName;
                    updTask.dueDate = aTask.dueDate;
                    updTask.priority = aTask.priority;
                    updTask.save();
                }else {
                    aTask.save();
                }
                return true;
            }else {
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean removeItem(long id){
        try{
            Todo aTodo = Todo.findById(Todo.class, id);
            if(aTodo != null) {
                aTodo.delete();
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void showAddDialog(View view) {
        selectedPos = -1;
        showEditDialog("Add a Task");
    }

    private void showEditDialog(String title) {
        FragmentManager fm = getSupportFragmentManager();
        EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance(title);
        editTaskDialogFragment.show(fm, "fragment_edit_name");
    }

    private void showEditDialog(String title, long id, String taskName, String dueDate, String priority) {
        FragmentManager fm = getSupportFragmentManager();
        EditTaskDialogFragment editTaskDialogFragment = EditTaskDialogFragment.newInstance(title, id, taskName, dueDate, priority);
        editTaskDialogFragment.show(fm, "fragment_edit_name");
    }

    public void onFinishInputDialog(Todo newTask) {
        String msg;
        if(selectedPos < 0) {
            //add
            itemsAdapter.add(newTask);
            msg = "Add";
        }else {
            //update
            items.set(selectedPos, newTask);
            itemsAdapter.notifyDataSetChanged();
            msg = "Update";
        }
        writeItem(newTask, newTask.getId());
        Toast.makeText(this, msg + " Succeeded : " + newTask.taskName, Toast.LENGTH_SHORT).show();

    }

}
