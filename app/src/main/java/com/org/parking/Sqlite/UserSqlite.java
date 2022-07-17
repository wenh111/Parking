package com.org.parking.Sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserSqlite extends SQLiteOpenHelper {
    private static SQLiteOpenHelper mInstance;
    public static SQLiteOpenHelper getmInstance(Context context){
        if(mInstance == null){
            mInstance = new UserSqlite(context,"person.db",null,1);
        }
        return mInstance;
    }
    public UserSqlite(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createtable = "create table user " +
                "( _id integer  primary key autoincrement," +
                "name text not null," +
                "account text unique not null," +
                "password text not null," +
                "Telephone_number text unique not null,"+
                "identity text)";
        String creattable_Card="CREATE TABLE Card" +
                "( _id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "Car_number TEXT , " +
                "Car_in_time TIMESTAMP, " +
                "isused BOOLEAN)";

        String creattable_Book =  "CREATE TABLE Car_book " +
                "( book_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Card_id INTEGER NOT NULL UNIQUE, " +
                "Car_number TEXT NOT NULL UNIQUE, " +
                "Submission_time TIMESTAMP NOT NULL, " +
                "Out_time TIMESTAMP NOT NULL, " +
                "lot TEXT NOT NULL UNIQUE)";

        String createtable_history = "CREATE TABLE history " +
                "( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Card_id INTEGER NOT NULL, " +
                "Car_number TEXT NOT NULL, " +
                "in_time TIMESTAMP NOT NULL, " +
                "out_time TIMESTAMP NOT NULL, " +
                "cost DOUBLE NOT NULL)";

        db.execSQL(createtable_history);
        db.execSQL(creattable_Card);
        db.execSQL(createtable);
        db.execSQL(creattable_Book);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
