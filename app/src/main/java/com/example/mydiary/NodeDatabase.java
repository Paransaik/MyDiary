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
    //DB name
    public static String DATABASE_NAME = "Node.db";
    //DB table name
    public static String TABLE_NODE = "NODE";
    //DB version
    public static int DATABASE_VERSION = 1;
    //DB 사용에 필요한 Helper
    private DatabaseHelper dbHelper;
    //DB instance
    private SQLiteDatabase db;
    //DB object
    private Context context;
    //DB constructor
    private NodeDatabase(Context context) {
        this.context = context;
    }

    //DB get Instance
    public static NodeDatabase getInstance(Context context) {
        //DB 가 null이면 새로운 DB를 만듦
        if (database == null) {
            database = new NodeDatabase(context);
        }
        return database;
    }

    //DB open
    public boolean open() {
        println("opening database [" + DATABASE_NAME + "].");
        //dnHelper object 생성
        dbHelper = new DatabaseHelper(context);
        //getWritableDatabase() 함수를 통해 SQLiteDatabase object을 가져와야 함
        db = dbHelper.getWritableDatabase();
        return true;
    }

    //DB close
    public void close() {
        println("closing database [" + DATABASE_NAME + "].");
        //현재 연결 중인 DB를 닫음
        db.close();
        database = null;
    }

     //DB select 쿼리문 실행
     public Cursor rawQuery(String SQL) {
        println("\nexecuteQuery called.\n");

        Cursor c1 = null;
        try {
            //select 쿼리문을 실행할 sql을 parameter로 받고 c1변수에 저장함
            c1 = db.rawQuery(SQL, null);
            println("cursor count : " + c1.getCount());
        } catch(Exception ex) {
            //오루 발생 시
            Log.e(TAG, "Exception in executeQuery", ex);
        }
        return c1;
    }

    //select가 아닌 나머지 쿼리문 수행
    public boolean execSQL(String SQL) {
        println("\nexecute called.\n");
        try {
            Log.d(TAG, "SQL : " + SQL);
            //sql 쿼리문을 받고 실행시킴
            db.execSQL(SQL);
        } catch(Exception ex) {
            //오류 발생 시
            Log.e(TAG, "Exception in executeQuery", ex);
            return false;
        }
        return true;
    }

    //DB를 관리하는데 필요한 DatabaseHelper 사용
    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        //DB table을 생성함
        public void onCreate(SQLiteDatabase db) {
            //이미 테이이 잇을 경우 drop 함
            String DROP_SQL = "drop table if exists " + TABLE_NODE;
            try {
                db.execSQL(DROP_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in DROP_SQL", ex);
            }

            //create table
            String CREATE_SQL = "create table " + TABLE_NODE + "("
                    + "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
                    + "  TITLE TEXT DEFAULT '', "
                    + "  CONTENTS TEXT DEFAULT '', "
                    + "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                    + "  MODIFY_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
                    + ")";
            try {
                //쿼리문 실행
                db.execSQL(CREATE_SQL);
            } catch(Exception ex) {
                Log.e(TAG, "Exception in CREATE_SQL", ex);
            }
        }

        @Override
        //기존 사용자가 DB를 사용하고 있어서 업그레이드 할 경우, 오버라이드
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }
}