package com.example.sqlfriend;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static String DB_NAME = "friendsql.db";
    public static int DB_VERSION = 1;
    public static int EVENT_INSERT = 0;
    public static int EVENT_UPDATE = 1;
    private List<friend> data = new ArrayList<>();
    private friendSQLite ds;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ds = new friendSQLite(this);
        ListView listView = findViewById(R.id.list_view);
        adapter = new ArrayAdapter(this, R.layout.item_list, R.id.item, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showMenu(adapterView, view, i, l);
            }
        });

    }
    private void showMenu(AdapterView<?> parent, View view, int position, long id) {
        final String[] items = {"Update", "Delete"};
        new AlertDialog.Builder(this)
                .setTitle("Action..")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setItems(items, (dialog, which) -> {
                    if (items[which].equals("Update")) {
                        friend f = (friend) parent.getAdapter().getItem(position);
                        Intent intent = new Intent(this, addActivity.class);
                        intent.putExtra("event", EVENT_UPDATE);
                        intent.putExtra("id", f.getId());
                        startActivity(intent);
                    } else if (items[which].equals("Delete")) {
                        friend friend = (friend) parent.getAdapter().getItem(position);
                        ds.delete(friend.getId());
                        data.remove(friend);
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_menu) {
            Intent intent = new Intent(this, addActivity.class);
            intent.putExtra("event", EVENT_INSERT);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("close confirmation")
                .setMessage("close this app?")
                .setIcon(android.R.drawable.ic_lock_power_off)
                .setPositiveButton("YES", (dialog, which) -> finish())
                .setNegativeButton("NO", null).show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        data.clear();
        data.addAll(ds.getAllFriends());
        adapter.notifyDataSetChanged();
    }
}