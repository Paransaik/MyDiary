package com.example.mydiary;

import android.os.Bundle;
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
public class MainActivity extends AppCompatActivity implements Fragment1.OnSelectedListener {
    Fragment1 fragment1;
    Fragment2 fragment2;

    //bottomNavigation 생성
    BottomNavigationView bottomNavigation;

    //db instance 생성
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

        //DB 열기
        openDatabase();
    }

    //DB가 없으면 만들기
    public void openDatabase() {
        //open database
        if (Database != null) {
            Database.close();
            Database = null;
        }

        //DB instance 가져오기
        Database = NodeDatabase.getInstance(this);
    }

    //Fragment1.OnSelectedListener onTabSelected 매소드 오버라이드
    public void onTabSelected(int position) {
        if (position == 0) {
            bottomNavigation.setSelectedItemId(R.id.tab1);
        } else if (position == 1) {
            fragment2 = new Fragment2();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
        }
    }

    //Fragment1.OnSelectedListener showFragment2 매소드 오버라이드
    public void showFragment2(Node item) {
        fragment2 = new Fragment2();
        fragment2.setItem(item);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
    }
}