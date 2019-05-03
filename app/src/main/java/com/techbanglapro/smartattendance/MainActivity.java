package com.techbanglapro.smartattendance;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techbanglapro.smartattendance.Adapter.ViewpagerAdapter;
import com.techbanglapro.smartattendance.Fragments.AllClassFragment;
import com.techbanglapro.smartattendance.Fragments.NewClassFragment;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;


    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("Dashboard");

        tabLayout = findViewById(R.id.tabl_layout);
        viewPager = findViewById(R.id.view_pager);

        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());

        viewpagerAdapter.addFragments(new AllClassFragment(),"All Class");
        viewpagerAdapter.addFragments(new NewClassFragment(),"All Subjects");

        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.new_class:
            {

       /*         AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(this);

                LayoutInflater inflater = getLayoutInflater();

                View view = inflater.inflate(R.layout.create_class_layout,null);
                final EditText subject_name = findViewById(R.id.subject_name);
                EditText semester = findViewById(R.id.semester);
                EditText shift = findViewById(R.id.shift);
                Button create_button = findViewById(R.id.create_button);
                Button cancle_button = findViewById(R.id.cancle_button);

                builder.setView(view);
                builder.setCancelable(false);

                create_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String subjcet = subject_name.getText().toString();
                        Toast.makeText(MainActivity.this, "Subject name : " + subjcet, Toast.LENGTH_SHORT).show();


                    }
                });

              AlertDialog alertDialog = builder.create();
              alertDialog.getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
                builder.show();*/

                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.create_class_layout,null);
                final EditText subject_name = findViewById(R.id.subject_name);
                final EditText semester = findViewById(R.id.semester);
                final EditText shift = findViewById(R.id.shift);



                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //, R.style.AlertDilog
              //  builder.setTitle("");

                builder.setMessage("")
                        .setView(view)

                /*final EditText creatNewClass = new EditText(MainActivity.this);
                creatNewClass.setHint("e.g., Programming");
                builder.setView(creatNewClass);*/

                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                       // String subjectName = creatNewClass.getText().toString();
                        String subject_Name = subject_name.getText().toString();
                        String student_Semester = semester.getText().toString();
                        String student_shift = shift.getText().toString();


                        if(TextUtils.isEmpty(subject_Name) || TextUtils.isEmpty(student_Semester) || TextUtils.isEmpty(student_shift) ){
                            Toast.makeText(MainActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            CreatNewSubject(subject_Name, student_Semester, student_shift);
                        }
                    }
                })

              .setNegativeButton("Cancle",null)
              .setCancelable(false);

              AlertDialog alert = builder.create();
                alert.show();



                break;
            }
            case  R.id.logout:
            {
                LogoutUser();
                break;
            }

        }

        return false;
    }

    private void LogoutUser() {
     FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
        startActivity(intent);
        finish();

    }

    private void CreatNewSubject(String student_Subject, String student_Semester, String student_shift) {

          firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
          databaseReference = FirebaseDatabase.getInstance().getReference("ClassName").child(firebaseUser.getUid());

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Subject",student_Subject);
        hashMap.put("Semester",student_Semester);
        hashMap.put("Shift",student_shift);
        databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Class create successfull", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
