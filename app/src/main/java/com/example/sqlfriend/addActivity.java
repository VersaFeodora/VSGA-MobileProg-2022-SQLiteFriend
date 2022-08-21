package com.example.sqlfriend;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import static com.example.sqlfriend.MainActivity.EVENT_UPDATE;
import static com.example.sqlfriend.MainActivity.EVENT_INSERT;

import androidx.appcompat.app.AppCompatActivity;

public class addActivity  extends AppCompatActivity {
    private TextView idText;
    private friendSQLite ds;
    private EditText editID;
    private EditText editName;
    private EditText editClass;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ds = new friendSQLite(this);
        idText = findViewById(R.id.id);
        editID = findViewById(R.id.editID);
        editName = findViewById(R.id.editName);
        editClass = findViewById(R.id.editClass);
        save = findViewById(R.id.savebutton);
        save.setOnClickListener(v -> {
            if (!isValidate()) return;
            if (getIntent().getIntExtra("event", -1) == EVENT_INSERT) {
                friend friend = new friend(
                        editName.getText().toString(),
                        editClass.getText().toString());
                ds.insert(friend);
            } else if (getIntent().getIntExtra("event", -1) == EVENT_UPDATE) {
                friend friend = new friend(
                        Integer.parseInt(editID.getText().toString()),
                        editName.getText().toString(),
                        editClass.getText().toString());
                ds.update(friend); //update friend
            }
            startActivity(new Intent(new Intent(this, MainActivity.class)));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (getIntent().getExtras() != null && getSupportActionBar() != null)
            if (getIntent().getIntExtra("event", -1) == EVENT_INSERT) {
                setTitle("Add Data");
                setDisplay(EVENT_INSERT);
            } else if (getIntent().getIntExtra("event", -1) == EVENT_UPDATE) {
                setTitle("Update Data");
                setDisplay(EVENT_UPDATE);
                display(getIntent().getIntExtra("id", -1));
            }
    }

    private void setDisplay(int event) {
        switch (event) {
            case 0:
                idText.setVisibility(View.GONE);
                editID.setVisibility(View.GONE);
                editName.setEnabled(true);
                editClass.setEnabled(true);
                save.setVisibility(View.VISIBLE);
                break;
            case 1:
                idText.setVisibility(View.VISIBLE);
                editID.setVisibility(View.VISIBLE);
                editID.setEnabled(true);
                editName.setEnabled(true);
                editClass.setEnabled(true);
                save.setVisibility(View.VISIBLE);
                break;
            case 2:
                idText.setVisibility(View.VISIBLE);
                editID.setVisibility(View.VISIBLE);
                editID.setEnabled(false);
                editName.setEnabled(false);
                editClass.setEnabled(false);
                save.setVisibility(View.GONE);
                break;
        }
    }

    private void display(int id) {
        friend friend = ds.getFriendById(id);
        editID.setText(String.valueOf(friend.getId()));
        editName.setText(friend.getName());
        editClass.setText(friend.getClassID());
    }

    private boolean isValidate() {
        EditText[] ets = new EditText[]{
                editName, editClass
        };
        for (EditText et : ets) {
            if (et.getText().toString().isEmpty()) {
                et.setError("fields need to be filled!");
                return false;
            }
        }
        return true;
    }
}
