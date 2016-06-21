package com.traviswkim.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    EditText mltItemName;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        mltItemName = (EditText)findViewById(R.id.mltItemName);

        //Access extras from previous page
        String itemName = getIntent().getStringExtra("itemName");
        long id = getIntent().getLongExtra("id", 0);
        position = getIntent().getIntExtra("position", 0);

        //Set selected value into text field
        mltItemName.setText(itemName);
        //Set cursor position at the end of text
        mltItemName.setSelection(mltItemName.getText().length());
        //Toast.makeText(this, itemName + "-" + id, Toast.LENGTH_SHORT).show();
    }

    public void onItemSave(View view) {
        mltItemName = (EditText)findViewById(R.id.mltItemName);
        Intent data = new Intent();
        data.putExtra("newItemName", mltItemName.getText().toString());
        data.putExtra("position", position);
        setResult(RESULT_OK, data);
        this.finish();
    }
}
