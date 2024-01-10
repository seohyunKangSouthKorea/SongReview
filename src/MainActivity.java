package com.cookandroid.songreviewapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    // m을 붙이는 이유 멤버변수라는 뜻 멤버변수 == 전역변수
    private RecyclerView mRv_songReview; // 전체
    private FloatingActionButton mBtn_plus; // 플러스버튼
    private ArrayList<SongReviewItem> mSongReviewItems; // 노래평가한 목록들
    private DBHelper mDBHelper; // 만든 DBHelper 사용할 때
    private Adapter mAdapter; // 만든 Adapter 사용할 때
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInit();
    }

    private void setInit() {
        // 객체 생성 부분
        mDBHelper = new DBHelper(this); // DBHelper의 생성자에 MainActivity의 context를 보냄
        mRv_songReview = findViewById(R.id.rv_songReview); // 목록이 보여질 전체 UI 가져옴 리사이클러뷰
        mBtn_plus = findViewById(R.id.btn_plus); // 플러스버튼 (게시글게시하려면 눌러야함)
        mSongReviewItems = new ArrayList<>(); // 노래평가목록들

        // load recent DB 최근DB를 가져옴
        loadRecentDB(); // 이게 있어야 계속 저장이 돼서 앱을 껐다 실행해도 전에 쓴 흔적이 남아있어야함 사라지면 안됨

        // 플러스 버튼을 누르면?
        mBtn_plus.setOnClickListener(new View.OnClickListener() { 
            @Override
            public void onClick(View view) {
                // 다이어로그 == 팝업 팝업창 띄우기 (게시글 작성을 위한 팝업창 띄우기)
                Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_Material_Light_Dialog);
                dialog.setContentView(R.layout.dialog_input); // 다이어로그 xml과 연결
                // 만든 다이어로그xml속 뷰들을 findViewById로 가져올것이다.
                // 필요한건 EditText가 필요함 그거 가져오면됨
                // 대신 쌩으로 findViewById()하면 안됨.
                // dialog.findViewByID()로 해야함. 즉, dialog객체를 통해 findViewById수행해야함
                // 건들 - in 다이어로그꺼
                EditText et_singer = dialog.findViewById(R.id.et_singer);
                EditText et_title = dialog.findViewById(R.id.et_title);
                RatingBar dialog_star = dialog.findViewById(R.id.dialog_star);
                EditText et_review = dialog.findViewById(R.id.et_review);
                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                // 입력완료버튼을 눌렀을 때 -> INSERT행위가 이뤄져야한다.
                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 건들
                        String singer = et_singer.getText().toString(); // 에디트텍스트에 입력된 가수명 가져오기
                        String title = et_title.getText().toString(); // 에디트텍스트에 입력된 노래제목 가져오기
                        float s = dialog_star.getRating();
                        int star = (int)s;
                        String review = et_review.getText().toString();
                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // 현재시간(작성시간) 알아내기

                        // INSERT DB (DB에 넣기) - 사용자에게 안보임
                        // 건들
                        mDBHelper.insertSongReview(singer,title,star,review,currentTime); // DB에 INSERT 하기

                        // INSERT UI (UI RecyclerView에 보이게 넣기) - 사용자에게 보임
                        // RecyclerView와 연동하기 위해 어뎁터가 필요하다.
                        // 건들
                        SongReviewItem item = new SongReviewItem();
                        item.setSinger(singer);
                        item.setTitle(title);
                        item.setStar(star);
                        item.setReview(review);
                        item.setWriteDate(currentTime);

                        // 다이어로그로 부터 입력값을 받고 받은 값을 가져와서 그걸 어뎁터쪽으로 넘겨서 리사이크러뷰를 갱신하는 것이다.
                        mAdapter.addItem(item);

                        // 데이터가 쌓일 때 마다 스크롤이 예쁘게 올라가게끔 스크롤도 이동을 해준다.
                        mRv_songReview.smoothScrollToPosition(0);

                        dialog.dismiss(); // 할 거 다해서 다이어로그 끄기
                        Toast.makeText(MainActivity.this,"!!!평가목록에 '\"추가 성공'\"!!!",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog.show(); // 이게 있어야 다이어로그가 보인다.
            }
        });
    }

    // 앱을 실행하면 기존에 insert가 돼있는 데이터가 있는지 검사를 해야한다.
    // 만약에 데이터가 DB에 있으면 DB로 부터 로드를 해서 예쁘게 뿌려줘야한다.
    // 데이터가 없다면 Adapter를 새로 생성을 해줘야된다.
    private void loadRecentDB() {
        // 저장돼있던 DB를 가져온다.
        mSongReviewItems = mDBHelper.getSongReview(); // 앱실행마다 흔적이 없어지지 않는 이유가 바로 이것 할때마다 select로 찾아오기 때문
        if(mAdapter==null) { // Adapter객체생성 어뎁터는 한번만생성한다.
            mAdapter = new Adapter(mSongReviewItems, this);
            mRv_songReview.setHasFixedSize(true); // 리사이클러뷰 성능강화?
            mRv_songReview.setAdapter(mAdapter);  // 어뎁터에서 다 처리해온것을 리사이클러뷰가 받는다고생각 리사이클러뷰는 함수요청만으로 처리다 해서 받기만하면됨 리사이클러 뷰에 어댑터 연결
        }
    }
}