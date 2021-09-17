package com.codept.whatsappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference userRef;

    //
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    FragmentAdapter adapter;
    //
    FloatingActionButton toContactsFab;

    @Override
    protected void onStart() {
        super.onStart();
        if(!isLoggedIn())
        {
            toStartScreen();
        }
        else{
            if (hasProfileInfo())
                toSetupProfile();
        }
    }

    private void toSetupProfile() {
        startActivity(new Intent(MainActivity.this,ProfileSetup.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialising
        tabLayout=findViewById(R.id.tabLayout);
        viewPager2=findViewById(R.id.viewPager);

        FragmentManager fm=getSupportFragmentManager();
        adapter=new FragmentAdapter(fm,getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("CHATS"));
        tabLayout.addTab(tabLayout.newTab().setText("STATUS"));
        tabLayout.addTab(tabLayout.newTab().setText("CALLS"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));

            }
        });
        toContactsFab=findViewById(R.id.fab);
        toContactsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MyContacts.class));
            }
        });




    }
    private boolean hasProfileInfo(){
        final boolean[] bool = new boolean[1];
        bool[0]=false;

        userRef= FirebaseDatabase.getInstance().getReference().child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    if(snapshot.hasChild("username"))
                    {
                        bool[0] =true;

                    }
                    else
                        bool[0] =false;

                }else
                    bool[0] =false;
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        return bool[0];

    }
    private boolean isLoggedIn()
    {
        boolean bool;
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            bool=true;
//            Toast.makeText(MainActivity.this, "is authenticated", Toast.LENGTH_SHORT).show();
        }
        else{
            bool=false;
//            Toast.makeText(MainActivity.this, "is not authenticated", Toast.LENGTH_SHORT).show();
        }
        return bool;

    }
    private void toStartScreen()
    {
        startActivity(new Intent(MainActivity.this,StartScreen.class));
        finish();
    }
}