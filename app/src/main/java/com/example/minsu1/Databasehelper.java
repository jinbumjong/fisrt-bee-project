package com.example.minsu1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;

public class Databasehelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "product_data.db"; // 저장하는 파일 이름
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "product_data"; // 저장 테이블의 이름
    private static final String COLUMN_ID ="_id"; // ID열의 이름
    private static final String COLUMN_PRODUCT_NAME ="product_name";//열의 이름
    private static final String COLUMN_PRODUCT_NUMBER ="product_number";//열의 이름
    private static final String COLUMN_SELL_BY_DATA ="sell_by_date ";//열의 이름
    private static final String COLUMN_SAVE_SPACE ="save_space ";//열의 이름

    // 생성자
    public Databasehelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // SQL파일 생성
        String query =
                "CREATE TABLE " + TABLE_NAME +
                        " ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_PRODUCT_NAME + " TEXT," +
                        COLUMN_PRODUCT_NUMBER + " TEXT," +
                        COLUMN_SELL_BY_DATA + " TEXT,"+
                        COLUMN_SAVE_SPACE + "TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // sql데이터를 추가하는 함수
    void addBook(String product_name, String product_number, String sell_by_date, String save_space){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_PRODUCT_NAME, product_name);
        cv.put(COLUMN_PRODUCT_NUMBER, product_number);
        cv.put(COLUMN_SELL_BY_DATA, sell_by_date);
        cv.put(COLUMN_SAVE_SPACE, save_space);
        long result = db.insert(TABLE_NAME, null , cv);
        // 데이터를 제대로 넣었는지에 대한 확인을 하는 조건문
        if (result == -1){
            Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Soccessfull", Toast.LENGTH_SHORT).show();
        }

    }

    // sql데이터를 읽어오는 함수 / 검색도 여기서함
    Cursor readAllData(String name, String space){
        SQLiteDatabase db;
        Cursor cursor;
        db = this.getReadableDatabase();
        if(space != null){ // 전체 리스트를 보여줄때 space에 null을 넘겨 조건문을 통해 거름
            if(name != null){ // 검색기능을 이용시 데이터를 필터링 하도록 조건문을 사용
                cursor = db.rawQuery("SELECT * FROM product_data WHERE product_name LIKE ?", new String[]{"%"+name+"%"});
            }
            else {
                cursor = db.rawQuery("SELECT * FROM product_data WHERE save_space =" + space,null);
            }
        }
        else { // 전체리스트에대한 필터
            if (name != null) {
                cursor = db.rawQuery("SELECT * FROM product_data WHERE product_name LIKE ? ", new String[]{"%"+name+"%"});
            }
            else {
                cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
            }
        }
        return cursor;
    }

    // 정렬하기위한 함수 #위 부분과 합치지 않은 부분은 코드의 혼돈이 발생하여 분리
    Cursor read_to_date(String name, int i ){
        SQLiteDatabase db;
        Cursor cursor;
        db = this.getReadableDatabase();
        if (name != null) { // 검색어 필터
            cursor = db.rawQuery("SELECT * FROM product_data WHERE product_name LIKE ? ", new String[]{"%"+name+"%"});
            return cursor;
        }
        if( i == 1) // 오름차순
            cursor = db.rawQuery("SELECT * FROM product_data  ORDER BY 4  ",null);
        else if(i == 0) // 내림차순
            cursor = db.rawQuery("SELECT * FROM product_data  ORDER BY 4 DESC",null);
        else // 기본
            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME,null);
        return cursor;
    }
    // sql데이터를 수정하는 함수
    void updateData(String row_id, String product_name, String product_number, String sell_by_date, String save_space){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv =new ContentValues();
        cv.put(COLUMN_PRODUCT_NAME, product_name);
        cv.put(COLUMN_PRODUCT_NUMBER, product_number);
        cv.put(COLUMN_SELL_BY_DATA, sell_by_date);
        cv.put(COLUMN_SAVE_SPACE, save_space);
        long result = db.update(TABLE_NAME, cv,"_id=?",new String[]{row_id});
        if(result == -1){
            Toast.makeText(context,"FAILED UPDATE",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"SUCCESSFULL UPDATE",Toast.LENGTH_SHORT).show();
        }

    }

    //sql데이터를 삭제하는 함수
    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME,"_id=?",new String[]{row_id});
        if(result == -1){
            Toast.makeText(context,"FAILED DELETE",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"SUCCESSFULL DELETE",Toast.LENGTH_SHORT).show();
        }
    }
}

