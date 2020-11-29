package com.example.mydiary;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
* Department of Cybersecurity
* Smart Device Programming
* Made by Taeyeong, Jeong
* github: Paransaik
* url: https://github.com/Paransaik
* date: `20.11.28.~xx.
 */
public class MainActivity extends AppCompatActivity implements OnTabItemSelectedListener {
    Fragment1 fragment1;
    Fragment2 fragment2;

    BottomNavigationView bottomNavigation;

    public static NodeDatabase Database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fragment 객체 생성성
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();

        //아래 네비게이션 버튼 연결
        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab1:
                        //첫 번째 탭을 선택할 때 fragment1 호출
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();
                        return true;
                    case R.id.tab2:
                        //두 번째 탭을 선택할 때 fragment1 호출
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
                        return true;
                }
                return false;
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);

        //데이터베이스 열기
        openDatabase();
    }

    //DB 만들기
    public void openDatabase() {
        //open database
        if (Database != null) {
            Database.close();
            Database = null;
        }

        //DB instance 가져오기
        Database = NodeDatabase.getInstance(this);
        boolean isOpen = Database.open();
        if (isOpen) {
            Log.d(TAG, "Note database is open.");
        } else {
            Log.d(TAG, "Note database is not open.");
        }
    }
}