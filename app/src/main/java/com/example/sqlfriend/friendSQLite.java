package com.example.sqlfriend;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class friendSQLite extends SQLiteOpenHelper implements friendDAO {
    public friendSQLite(Context context) {
        super(context, "friendsql.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table friend(" +
                "id integer not null primary key autoincrement," +
                "name text not null," +
                "class text)";
        db.execSQL(sql);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            String sql = "drop table if exists friend";
            db.execSQL(sql);
            onCreate(db);
        }
    }
    @Override
    public void insert(friend f) {
        String sql = "insert into friend values(?,?,?)";
        getWritableDatabase().execSQL(sql, new Object[]{
                null,
                f.getName(),
                f.getClassID()
        });
    }
    @Override
    public void update(friend f) {
        String sql = "update friend set name=?, class=? where id=?";
        getWritableDatabase().execSQL(sql, new Object[]{
                f.getName(),
                f.getClassID(),
                f.getId()
        });
    }
    @Override
    public void delete(int id) {
        String sql = "delete from friend where id=?";
        getWritableDatabase().execSQL(sql, new Object[]{id});
    }
    @Override
    public friend getFriendById(int id) {
        String sql = "select * from friend where id=?";
        Cursor cursor = getReadableDatabase().rawQuery(sql, new
                String[]{String.valueOf(id)});
        if (cursor.moveToFirst())
            return new friend(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
            );
        else
            return null;
    }
    @Override
    public List<friend> getAllFriends() {
        List<friend> fs = new ArrayList<>();
        String sql = "select * from friend";
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        while(cursor.moveToNext()){
            fs.add(new friend(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
            ));
        }
        cursor.close();
        return fs;
    }
}
