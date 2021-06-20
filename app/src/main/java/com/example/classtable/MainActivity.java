package com.example.classtable;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    private ImageView imageAdd;
    private DatabaseHelper databaseHelper = new DatabaseHelper
            (this, "database.db", null, 1);
    private int maxCoursesNumber = 11;
    private RelativeLayout day;
    private int currentCoursesNumber = 0;
    String[] startClass = {"8:00", "8:55", "10:00", "10:55", "14:30", "15:20", "16:25", "17:15", "19:40", "20:35"};
    Map<String, Integer> map = new HashMap<>();
    int color[] = {R.color.blue_139, R.color.yellow_255, R.color.blue_152, R.color.red_255, R.color.green_115, R.color.pink_115};
    int hours[] = {8, 8, 10, 10, 14, 15, 16, 17, 19, 20};
    int minutes[] = {0, 55, 0, 55, 30, 20, 25, 15, 40, 35};
    int i = 0;
    float heightPixels;
    float weightPixels;
    Calendar c = Calendar.getInstance();
    int weekday;
    double nLenStart = 0;
    ImageView addCourses;
    private static Context self;
    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //工具条
        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        final float scale = this.getResources().getDisplayMetrics().density;
        heightPixels = outMetrics.heightPixels - 100 - (56 * scale + 0.5f);
        weightPixels = outMetrics.widthPixels;
        self = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //获取窗口区域
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            //设置状态栏颜色
//            window.setStatusBarColor(Color.parseColor("#ffffff"));

            //设置显示为白色背景，黑色字体
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //从数据库读取数据
        loadData();
        weekday = c.get(Calendar.DAY_OF_WEEK);
        TextView textView = null;
        switch (weekday) {
            case SUNDAY:
                textView = findViewById(R.id.week7);
                break;
            case MONDAY:
                textView = findViewById(R.id.week1);
                break;
            case TUESDAY:
                textView = findViewById(R.id.week2);
                break;
            case WEDNESDAY:
                textView = findViewById(R.id.week3);
                break;
            case THURSDAY:
                textView = findViewById(R.id.week4);
                break;
            case FRIDAY:
                textView = findViewById(R.id.week5);
                break;
            case SATURDAY:
                textView = findViewById(R.id.week6);
                break;
        }
        textView.setBackgroundColor(getResources().getColor(R.color.teal_200));
        /*LinearLayout linearLayout = findViewById(R.id.root);
        linearLayout.setOnTouchListener(new MulitPointTouchListener());*/
        addCourses = findViewById(R.id.addCourses);
        addCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            /*public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
                startActivityForResult(intent, 0);
            }*/
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("请输入课程信息");
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_course, null);
                builder.setView(view);
                final EditText courseName = (EditText) view.findViewById(R.id.course_name);
                final EditText teacher = (EditText) view.findViewById(R.id.course_teacher);
                final EditText classroom = (EditText) view.findViewById(R.id.course_location);
                final EditText courseStart = (EditText) view.findViewById(R.id.course_start);
                final EditText courseEnd = (EditText) view.findViewById(R.id.course_end);
                final EditText courseWeekStart = (EditText) view.findViewById(R.id.course_start_week);
                final EditText courseWeekEnd = (EditText) view.findViewById(R.id.course_end_week);
                final EditText start = (EditText) view.findViewById(R.id.course_time);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Name = courseName.getText().toString();
                        String Teacher = teacher.getText().toString();
                        String Classroom = classroom.getText().toString();
                        String Day = start.getText().toString();
                        String CourseStart = courseStart.getText().toString();
                        String CourseEnd = courseEnd.getText().toString();
                        String CourseWeekStart = courseWeekStart.getText().toString();
                        String CourseWeekEnd = courseWeekEnd.getText().toString();
                        if (Name.equals("") || Teacher.equals("") || Classroom.equals("") || Day.equals("") || CourseStart.equals("") || CourseEnd.equals("") || CourseWeekStart.equals("") || CourseWeekEnd.equals("")) {
                            Toast.makeText(MainActivity.this, "基本课程信息未填写", Toast.LENGTH_SHORT).show();
                        } else {
                            Course course = new Course(Name, Teacher, Classroom, Integer.parseInt(Day), Integer.parseInt(CourseStart), Integer.parseInt(CourseEnd), Integer.parseInt(CourseWeekStart), Integer.parseInt(CourseWeekEnd));
                            saveData(course);
                        }
                        loadData();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

    }

    //从数据库加载数据
    private void loadData() {
        ArrayList<Course> coursesList = new ArrayList<>(); //课程列表
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from courses", null);
        if (cursor.moveToFirst()) {
            do {
                coursesList.add(new Course(
                        cursor.getString(cursor.getColumnIndex("courseName")),
                        cursor.getString(cursor.getColumnIndex("teacher")),
                        cursor.getString(cursor.getColumnIndex("classRoom")),
                        cursor.getInt(cursor.getColumnIndex("day")),
                        cursor.getInt(cursor.getColumnIndex("classStart")),
                        cursor.getInt(cursor.getColumnIndex("classEnd")),
                        cursor.getInt(cursor.getColumnIndex("weekStart")),
                        cursor.getInt(cursor.getColumnIndex("weekEnd"))));
            } while (cursor.moveToNext());
        }
        cursor.close();
        createLeftView();
        //使用从数据库读取出来的课程信息来加载课程表视图
        for (Course course : coursesList) {
            createItemCourseView(course);
        }
    }


    //保存数据到数据库
    private void saveData(Course course) {
        SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
        sqLiteDatabase.execSQL
                ("insert into courses(courseName, teacher, classRoom, day, classStart, classEnd,weekStart,weekEnd) " + "values(?, ?, ?, ?, ?, ?,?,?)",
                        new String[]{course.getCourseName(),
                                course.getTeacher(),
                                course.getClassRoom(),
                                course.getDay() + "",
                                course.getClassStart() + "",
                                course.getClassEnd() + "",
                                course.getWeekStart() + "",
                                course.getWeekEnd() + ""
                        }
                );
    }

    //创建"第几节数"视图
    private void createLeftView() {

        final float scale = this.getResources().getDisplayMetrics().density;

        for (int i = 0; i < maxCoursesNumber - 1; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.left_view, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(130, (int) (heightPixels / 11));
            view.setLayoutParams(params);
            TextView text = view.findViewById(R.id.class_number_text);
            text.setText(String.valueOf(++currentCoursesNumber));
            TextView classStartTime = view.findViewById(R.id.time_start);
            classStartTime.setText(startClass[i]);
            LinearLayout leftViewLayout = findViewById(R.id.left_view_layout);
            leftViewLayout.addView(view);
            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (heightPixels / 11));
            View viewLine = LayoutInflater.from(this).inflate(R.layout.line_view, null);
            viewLine.setLayoutParams(params1);
            LinearLayout mainLayout = findViewById(R.id.leftTop_view_layout);
            mainLayout.addView(viewLine);
        }
        for (int i = 0; i < 10; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (heightPixels / 11));
            View viewLine = LayoutInflater.from(this).inflate(R.layout.line_view, null);
            viewLine.setLayoutParams(params);
            LinearLayout mainLayout = findViewById(R.id.main);
            mainLayout.addView(viewLine);
        }
    }

    //创建单个课程视图
    private void createItemCourseView(final Course course) {
        int getDay = course.getDay();
        if ((getDay < 1 || getDay > 7) || course.getClassStart() > course.getClassEnd())
            Toast.makeText(this, "时间填写有误", Toast.LENGTH_LONG).show();
        else {
            int dayId = 0;
            switch (getDay) {
                case 1:
                    dayId = R.id.monday;
                    break;
                case 2:
                    dayId = R.id.tuesday;
                    break;
                case 3:
                    dayId = R.id.wednesday;
                    break;
                case 4:
                    dayId = R.id.thursday;
                    break;
                case 5:
                    dayId = R.id.friday;
                    break;
                case 6:
                    dayId = R.id.saturday;
                    break;
                case 7:
                    dayId = R.id.weekday;
                    break;
            }
            day = findViewById(dayId);
            CardView card = findViewById(R.id.card);
            int height = (int) (heightPixels / maxCoursesNumber);
            String courseName = course.getCourseName();
            final View v = LayoutInflater.from(this).inflate(R.layout.course_card, null); //加载单个课程布局
            if (!map.containsKey(courseName)) {
                map.put(courseName, color[i++]);
                v.setBackgroundColor(getResources().getColor(color[i - 1]));
            } else {
                v.setBackgroundColor(getResources().getColor(map.get(courseName)));
            }
            if ((getDay == weekday - 1) || (getDay == 6 + weekday)) {
                //小时
                int hour = c.get(Calendar.HOUR_OF_DAY);
                //分钟
                int minute = c.get(Calendar.MINUTE);
                int i = 0;
                for (int hour_c : hours) {
                    if (hour_c >= hour && (hour_c * 60 + minutes[i++] - hour * 60 - minute) > 30) {
                        v.setBackgroundColor(getResources().getColor(R.color.blue_13));
                        break;
                    }
                }
            }
            v.setY(height * (course.getClassStart() - 1)); //设置开始高度,即第几节课开始
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (ViewGroup.LayoutParams.MATCH_PARENT, (course.getClassEnd() - course.getClassStart() + 1) * height - 8); //设置布局高度,即跨多少节课
            v.setLayoutParams(params);
            TextView text = v.findViewById(R.id.text_view);
            text.setText(course.getCourseName() + "\n"  + course.getClassRoom() + "\n"  + course.getTeacher()); //显示课程名
            day.addView(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("课程信息");
                    View view2 = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_course, null);
                    builder.setView(view2);
                    final EditText courseName = view2.findViewById(R.id.course_name);
                    final EditText courseTeacher = view2.findViewById(R.id.course_teacher);
                    final EditText courseLocation = view2.findViewById(R.id.course_location);
                    final EditText courseTime = view2.findViewById(R.id.course_time);
                    final EditText courseStart = view2.findViewById(R.id.course_start);
                    final EditText courseEnd = view2.findViewById(R.id.course_end);
                    final EditText courseStartWeek = view2.findViewById(R.id.course_start_week);
                    final EditText courseEndWeek = view2.findViewById(R.id.course_end_week);
                    final AlertDialog alertDialog = builder.create();
                    courseName.setText(course.getCourseName());
                    courseTeacher.setText(course.getTeacher());
                    courseLocation.setText(course.getClassRoom());
                    courseTime.setText(String.valueOf(course.getDay()));
                    courseStart.setText(String.valueOf(course.getClassStart()));
                    courseEnd.setText(String.valueOf(course.getClassEnd()));
                    courseStartWeek.setText(String.valueOf(course.getWeekStart()));
                    courseEndWeek.setText(String.valueOf(course.getWeekEnd()));
                    builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String Name = courseName.getText().toString();
                            String Teacher = courseTeacher.getText().toString();
                            String Classroom = courseLocation.getText().toString();
                            String Day = courseTime.getText().toString();
                            String CourseStart = courseStart.getText().toString();
                            String CourseEnd = courseEnd.getText().toString();
                            String CourseWeekStart = courseStartWeek.getText().toString();
                            String CourseWeekEnd = courseEndWeek.getText().toString();
                            if (Name.equals("") || Teacher.equals("") || Classroom.equals("") || Day.equals("") || CourseStart.equals("") || CourseEnd.equals("") || CourseWeekStart.equals("") || CourseWeekEnd.equals("")) {
                                Toast.makeText(MainActivity.this, "基本课程信息未填写", Toast.LENGTH_SHORT).show();
                            } else {
                                Course course = new Course(Name, Teacher, Classroom, Integer.parseInt(Day), Integer.parseInt(CourseStart), Integer.parseInt(CourseEnd), Integer.parseInt(CourseWeekStart), Integer.parseInt(CourseWeekEnd));
                                saveData(course);
                            }
                            loadData();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            });
            //长按删除课程
            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    v.setVisibility(View.GONE);//先隐藏
                    day.removeView(v);//再移除课程视图
                    SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                    sqLiteDatabase.execSQL("delete from courses where courseName = ?", new String[]{course.getCourseName()});
                    return true;
                }

            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Course course = (Course) data.getSerializableExtra("course");
            //创建课程表左边视图(节数)
            //createLeftView(course);
            //创建课程表视图
            createItemCourseView(course);
            //存储数据到数据库
            saveData(course);
        }
    }

}
