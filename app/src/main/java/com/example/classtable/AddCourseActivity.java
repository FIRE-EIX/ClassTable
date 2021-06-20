package com.example.classtable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class AddCourseActivity extends AppCompatActivity {
    private Button btn_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course);
        EditText courseName=findViewById(R.id.course_name);
        EditText teacher=findViewById(R.id.course_teacher);
        EditText classroom=findViewById(R.id.course_location);
        EditText courseStart=findViewById(R.id.course_start);
        EditText courseEnd=findViewById(R.id.course_end);
        EditText courseWeekStart=findViewById(R.id.course_start_week);
        EditText courseWeekEnd=findViewById(R.id.course_end_week);
        EditText start=findViewById(R.id.course_time);
//        btn_add=findViewById(R.id.button_addCourse);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = courseName.getText().toString();
                String Teacher = teacher.getText().toString();
                String Classroom = classroom.getText().toString();
                String Day = start.getText().toString();
                String CourseStart = courseStart.getText().toString();
                String CourseEnd = courseEnd.getText().toString();
                String CourseWeekStart = courseWeekStart.getText().toString();
                String CourseWeekEnd = courseWeekEnd.getText().toString();
                if (Name.equals("") || Teacher.equals("") || Classroom.equals("") || Day.equals("")||CourseStart.equals("")||CourseEnd.equals("")||CourseWeekStart.equals("")||courseWeekEnd.equals("")) {
                    Toast.makeText(AddCourseActivity.this, "基本课程信息未填写", Toast.LENGTH_SHORT).show();
                } else {
                    Course course = new Course(Name,Teacher,Classroom,Integer.parseInt(Day),Integer.parseInt(CourseStart),Integer.parseInt(CourseEnd),Integer.parseInt(CourseWeekStart),Integer.parseInt(CourseWeekEnd));
                    Intent intent = new Intent(AddCourseActivity.this, MainActivity.class);
                    intent.putExtra("course",course);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
