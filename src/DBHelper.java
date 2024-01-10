package com.cookandroid.songreviewapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{ // alt+enter로 필요한 생성자, 메소드 추가
    // 전역변수
    private static final int DB_VERSION=1;               // DBHelper클래스에서만 쓰기 위해 private으로 지정
    private static final String DB_NAME="koreankang.db"; // 앱 내부 db에 저장될 이름 지정
    public DBHelper(@Nullable Context context) { // 상속받은 클래스의 생성자가 필요 context이후로는 지웠음
        super(context, DB_NAME, null, DB_VERSION); // MainActivity,DB_NAME,null,DB_VERSION
    }

    @Override
    public void onCreate(SQLiteDatabase db) { // 상속받은 클래스의 메소드가 필요
        // MainActivity의 onCreate()와 상당히 비슷
        // DBHelper가 처음으로 생성될 때 즉, DB가 처음으로 생성됐을 때 상태값을 가짐.

        // 실제로 데이터 베이스가 생성이 될 때 호출
        // 건들
        db.execSQL("CREATE TABLE IF NOT EXISTS SongReview (id1 INTEGER PRIMARY KEY AUTOINCREMENT, singer TEXT NOT NULL, title TEXT NOT NULL, star INTEGER NOT NULL, review TEXT NOT NULL , writeDate TEXT NOT NULL);");
        // IF NOT EXISTS : 테이블이 존재하면 생성을 못하도록 처리
        // AUTOINCREMENT : 데이터가 INSERT될 때마다 자동으로 id가 1씩증가. 즉, 데이터 삽입 시 키값이 자동증가
        // TEXT : String 문자열을 넣어야함

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // 상속받은 클래스의 메소드가 필요
        // DB가 업그레이드 될때의 관리
        onCreate(db); // 그냥 onCreate(db);하나 호출
    }

    // SELECT 문 (평가목록들을 조회) ArrayList 형태로 리턴
    // public인 이유? DBHelper클래스를 다른 곳(ex)MainActivity)에서 가져올 것이기 때문 마음껏 DML을 하기 위함
    public ArrayList<SongReviewItem> getSongReview(){
        ArrayList<SongReviewItem> songReviewItems = new ArrayList<>(); // 노래평가목록들이 검색 시 쌓일 예정으로 arraylist로 한다.
        SQLiteDatabase db = getReadableDatabase(); // select문을 수행할 예정으로 읽기가능으로 db객체 생성
        // 건들
        Cursor cursor = db.rawQuery("SELECT * FROM SongReview ORDER BY writeDate DESC;", null); // db검색후 커서사용을 위해 커서생성
        if(cursor.getCount()!=0) { // 커서가 릴레이션을 세보았을때 0개가아닌경우 즉, 1개이상존재하는경우 실행
            // 즉, 조회 해온 데이터가 있을 때 내부 수행
            while(cursor.moveToNext()) { // 조회한 테이블 데이터의 한 행 씩 커서 이동
                // 건들
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id1"));
                String singer = cursor.getString(cursor.getColumnIndexOrThrow("singer"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int star = cursor.getInt(cursor.getColumnIndexOrThrow("star"));
                String review = cursor.getString(cursor.getColumnIndexOrThrow("review"));
                String writeDate = cursor.getString(cursor.getColumnIndexOrThrow("writeDate"));

                SongReviewItem tmpItem = new SongReviewItem(); // songReviewItem객체 생성
                // 건들
                tmpItem.setId(id);                 // setter이용해 하나씩 넣음
                tmpItem.setSinger(singer);
                tmpItem.setTitle(title);
                tmpItem.setStar(star);
                tmpItem.setReview(review);
                tmpItem.setWriteDate(writeDate);

                songReviewItems.add(tmpItem);            // ArrayList에 방금 설정한 객체를 add
            }
        }
        cursor.close();   // 커서 이용이 끝났으니 종료해줌
        return songReviewItems; // 최종적으로 만들어진 ArrayList를 리턴
    }


    // INSERT 문 (평가 목록을 DB에 넣는다.)
    // public인 이유? DBHelper클래스를 다른 곳(ex)MainActivity)에서 가져올 것이기 때문 마음껏 DML을 하기 위함
    public void insertSongReview(String _singer, String _title,int _star, String _review, String _writeDate) { // activity_main.xml같은 화면에서의 값들을 DBHelper로 보내서 여기 인자로 받는다.
        SQLiteDatabase db = getWritableDatabase(); // 삽입 할것이기에 쓰기가능으로 db객체 생성
        // 건들
        db.execSQL("INSERT INTO SongReview (singer,title,star,review,writeDate) VALUES('"+_singer+"', '"+_title+"','"+_star+"' ,'"+_review+"','"+_writeDate+"');");
        // id값을 안넣을거라서 사전에 뭘넣을건지명시해둠
    }

    // UPDATE 문 (평가 목록을 수정한다.) - 업데이트
    // public인 이유? DBHelper클래스를 다른 곳(ex)MainActivity)에서 가져올 것이기 때문 마음껏 DML을 하기 위함
    public void updateSongReview(String _singer, String _title, int _star, String _review, String _writeDate, String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase(); // 쓰기가능으로 db객체생성
        // 건들
        db.execSQL("UPDATE SongReview SET singer='"+_singer+"', title='"+_title+"', star='"+_star+"', review='"+_review+"' ,writeDate='"+_writeDate+"' WHERE writeDate='"+_beforeDate+"';");
    }

    // DELETE 문 (평가 목록을 제거한다.) - 제거
    // public인 이유? DBHelper클래스를 다른 곳(ex)MainActivity)에서 가져올 것이기 때문 마음껏 DML을 하기 위함
    public void deleteSongReview(String _beforeDate) {
        SQLiteDatabase db = getWritableDatabase(); // 쓰기가능으로 db객체 생성
        db.execSQL("DELETE FROM SongReview WHERE writeDate='"+_beforeDate+"';");
    }
}