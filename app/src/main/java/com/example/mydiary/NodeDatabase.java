package com.example.mydiary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static java.sql.DriverManager.println;

//메모 데이터베이스
public class NodeDatabase {
    //DB 상태 출력을 위한 TAG
    private static final String TAG = "NodeDatabase";

    //싱글톤 패턴 인스턴스 사용
    private static NodeDatabase database;
    //DB NAME
    public static String DATABASE_NAME = "Node.db";
    //DB TABLE NAME
    public static String TABLE_NOTE = "NODE";
    //DB version
    public static int DATABASE_VERSION = 1;


    /**
     * Helper class defined
     */
    private DatabaseHelper dbHelper;

    /**
     * SQLiteDatabase 인스턴스
     */
    private SQLiteDatabase db;

    /**
     * 컨텍스트 객체
     */
    private Context context;

    //생성자
    private NodeDatabase(Context context) {
        this.context = context;
    }

    /**
     * 인스턴스 가져오기
     */
    public static NodeDatabase getInstance(Context context) {
        if (database == null) {
            database = new NodeDatabase(context);
        }
        return database;
    }

    //DB open
    public boolean open() {
        println("opening database [" + DATABASE_NAME + "].");
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return true;
    }

    //DB close
    public void close() {
        println("closing database [" + DATABASE_NAME + "].");
        db.close();
        database = null;
    }

    /**
     * execute raw query using the input SQL
     * close the cursor after fetching any result
     *
     * @param SQL
     * @return
     */
    public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
        }

        return c1;
    }

    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");

        try {
            Log.d(TAG, "SQL : " + SQL);
            db.execSQL(SQL);
        } catch(Exception ex) {
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }

        return true;
    }

    /**
     * Database Helper inner class
     */
    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            println("creating database [" + DATABASE_NAME + "].");

            // TABLE_NOTE
            println("creating table [" + TABLE_NOTE + "].");

            // drop existing table
            String DROP_SQL = "drop table if exists " + TABLE_NOTE;
            try {
                db.execSQL(DROP_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            // create table
            String CREATE_SQL = "create table " + TABLE_NOTE + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  TITLE TEXT DEFAULT '', "
                    + "  CONTENTS TEXT DEFAULT '', "
                    + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "  MODIFY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";
            try {
                db.execSQL(CREATE_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }

//            // create index
//            String CREATE_INDEX_SQL = "create index " + TABLE_NOTE + "_IDX ON " + TABLE_NOTE + "(" + "CREATE_DATE" + ")";
//            try {
//                db.execSQL(CREATE_INDEX_SQL);
//            } catch(Exception ex) {
//                Log.e(TAG, "Exception in CREATE_INDEX_SQL", ex);
//            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }
}