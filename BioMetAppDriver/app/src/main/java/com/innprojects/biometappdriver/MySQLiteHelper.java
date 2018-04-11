package com.innprojects.biometappdriver;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.innprojects.biometappdriver.models.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by simransarin on 24/06/17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String STUDENTS = "students";
    public static final String STUDENT_ID = "student_id";
    public static final String STUDENT_ADM_NO = "student_adm_no";
    public static final String STUDENT_NAME = "student_name";
    public static final String STUDENT_PARENT_NAME = "parent_name";
    public static final String STUDENT_CLASS = "class_number";
    public static final String STUDENT_SECTION = "section";
    public static final String STUDENT_PHONE = "phone_number";
    public static final String STUDENT_BUS_STOP = "student_bus_stop";

    public static final String DATABASE_NAME = "students.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + STUDENTS + "( " + STUDENT_ID
            + " TEXT PRIMARY KEY, " + STUDENT_ADM_NO
            + " TEXT, " + STUDENT_NAME
            + " TEXT, " + STUDENT_PARENT_NAME
            + " TEXT, " + STUDENT_CLASS
            + " TEXT, " + STUDENT_SECTION
            + " TEXT, " + STUDENT_PHONE
            + " TEXT, " + STUDENT_BUS_STOP
            + " TEXT);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + STUDENTS);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_ID, student.getStudent_id()); // Student id
        values.put(STUDENT_ADM_NO, student.getStudent_adm_no()); // Student adm no
        values.put(STUDENT_NAME, student.getStudent_name()); // Student Name
        values.put(STUDENT_PARENT_NAME, student.getParent_name()); // Student parent name
        values.put(STUDENT_CLASS, student.getClass_number()); // Student class
        values.put(STUDENT_SECTION, student.getSection()); // Student section
        values.put(STUDENT_PHONE, student.getPhone_number()); // Student phone no
        values.put(STUDENT_BUS_STOP, student.getStudent_bus_stop()); // Student bus stop

        // Inserting Row
        db.insert(STUDENTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting single student
    public Student getStudent(String id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(STUDENTS, new String[]{
                        STUDENT_ID, STUDENT_ADM_NO, STUDENT_NAME, STUDENT_PARENT_NAME,
                        STUDENT_CLASS, STUDENT_SECTION, STUDENT_PHONE,
                        STUDENT_BUS_STOP}, STUDENT_ID + "=?",
                new String[]{id}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Student student = new Student(cursor.getString(0), cursor.getString(1),
                cursor.getString(2), cursor.getString(3),
                cursor.getString(4), cursor.getString(5),
                cursor.getString(6), cursor.getString(7));
        // return student
        return student;
    }

    // Getting All Students
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<Student>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + STUDENTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            Student student = new Student();
            student.setStudent_id(cursor.getString(0));
            student.setStudent_adm_no(cursor.getString(1));
            student.setStudent_name(cursor.getString(2));
            student.setParent_name(cursor.getString(3));
            student.setClass_number(cursor.getString(4));
            student.setSection(cursor.getString(5));
            student.setPhone_number(cursor.getString(6));
            student.setStudent_bus_stop(cursor.getString(7));
            // Adding contact to list
            studentList.add(student);
        }
        while (cursor.moveToNext()) ;
    }
        // return student list
        return studentList;
    }

    // Updating single student
    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STUDENT_ID, student.getStudent_id()); // Student id
        values.put(STUDENT_ADM_NO, student.getStudent_adm_no()); // Student adm no
        values.put(STUDENT_NAME, student.getStudent_name()); // Student Name
        values.put(STUDENT_PARENT_NAME, student.getParent_name()); // Student parent name
        values.put(STUDENT_CLASS, student.getClass_number()); // Student class
        values.put(STUDENT_SECTION, student.getSection()); // Student section
        values.put(STUDENT_PHONE, student.getPhone_number()); // Student phone no
        values.put(STUDENT_BUS_STOP, student.getStudent_bus_stop()); // Student bus stop
        // updating row
        return db.update(STUDENTS, values, STUDENT_ID,
                new String[]{student.getStudent_id()});
    }

    // Deleting single student
    public void deleteStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(STUDENTS,STUDENT_ID,
                new String[]{String.valueOf(student.getStudent_id())});
        db.close();
    }

    public void deleteStudentTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + STUDENTS);
    }

        // Getting students Count
    public int getStudentsCount() {
        String countQuery = "SELECT  * FROM " + STUDENTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    public boolean dbHasData(String searchKey) {
        String query = "Select * from " + STUDENTS + " where " + STUDENT_ID + " = ?";
        return getReadableDatabase().rawQuery(query, new String[]{searchKey}).moveToFirst();
    }
}