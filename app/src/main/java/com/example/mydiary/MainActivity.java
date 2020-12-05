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
* date: `20.11.28.~12.5.
 */
public class MainActivity extends AppCompatActivity implements OnSelectedListener {
    //Fragment1, 2 선언
    Fragment1 fragment1;
    Fragment2 fragment2;

    //bottomNavigation 선언
    BottomNavigationView bottomNavigation;

    //db instance 선언과 초기화
    public static NodeDatabase Database = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity_main을 화면에 뿌려줌
        setContentView(R.layout.activity_main);

        //fragment 객체 생성성
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();

        //getSupportFragmentManager의 beginTransaction을 사용하여 FragmentTransaction을 가지고 옴,
        //replace을 사요하여 Fragment 변경, commit()을 호출로 적용
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();


        //아래 Navigation button
        bottomNavigation = findViewById(R.id.bottom_navigation);
        //아래 Navigation button을 누를 때, 원하는 Fragment로 화면이 출력되는 listener
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
            //DB가 null이 아니면 DB를 닫고 새로운 DB를 만듦
            Database.close();
            Database = null;
        }

        //DB instance 가져오기
        Database = NodeDatabase.getInstance(this);
        //DB open
        Database.open();
    }

    //OnSelectedListener onTabSelected 매소드 오버라이드
    public void onTabSelected(int position) {
        if (position == 0) {
            //position이 0일 때 Fragment1을 호출
            bottomNavigation.setSelectedItemId(R.id.tab1);
        } else if (position == 1) {
            //position이 1일 때 Fragment2를 호출
            fragment2 = new Fragment2();
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
        }
    }

    //OnSelectedListener showFragment2 매소드 오버라이드
    public void showFragment2(Node item) {
        fragment2 = new Fragment2();
        fragment2.setItem(item);
        //두 번째 탭을 선택할 때 fragment1 호출
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commit();
    }
}