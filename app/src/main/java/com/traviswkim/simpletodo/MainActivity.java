package com.traviswkim.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 20;
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvlItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvlItems = (ListView) findViewById(R.id.lvlItems);
        readItem();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvlItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItem();
    }

    private void setupListViewListener(){
        lvlItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id){
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItem();
                        return true;
                    }
                });
        lvlItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> adapter, View item, int pos, long id){
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("itemName", items.get(pos).toString());
                        i.putExtra("position", pos);
                        i.putExtra("id", id);
                        startActivityForResult(i, REQUEST_CODE);
                    }
                });
    }

    private void readItem(){
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
        }catch(IOException e){
            items = new ArrayList<String>();
        }
    }

    private void writeItem(){
        File fileDir = getFilesDir();
        File todoFile = new File(fileDir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            String newItemName = data.getExtras().getString("newItemName");
            int position = data.getExtras().getInt("position");
            items.set(position, newItemName);
            itemsAdapter.notifyDataSetChanged();
            writeItem();
        }
    }
}
