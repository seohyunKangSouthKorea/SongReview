package com.cookandroid.songreviewapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<SongReviewItem> mSongReviewItems;
    private Context mContext;
    private DBHelper mDBHelper;

    // alt+Ins -> Constructor -> ctrl+a -> enter
    public Adapter(ArrayList<SongReviewItem> mSongReviewItems, Context mContext) { // 적어놓은 노래평가목록들, MainActivity의 context
        this.mSongReviewItems = mSongReviewItems;
        this.mContext = mContext;
        mDBHelper = new DBHelper(mContext); // MainActivity를 context로 하는 DBHelper객체 생성
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 어뎁터에 item_list.xml 연동
        // 리사이클러 아이템 하나하나에 대한 "뷰 연결"을 여기서 해줘야함 onCreateViewHolder에서
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        return new ViewHolder(holder); // 아까 만든 밑의 ViewHolder에  윗줄의 holder객체를 던져준다.  뷰홀더연동끝
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // onBindViewHolder메소드는 실제 리스틀이 쭈루룩 로드가 될 때 setText행위를 해줘야한다.
        // 건들 - item_list부분 건들이면됨
        holder.tv_singer.setText(mSongReviewItems.get(position).getSinger());
        holder.tv_title.setText(mSongReviewItems.get(position).getTitle());
        holder.rtb_star.setRating((float)mSongReviewItems.get(position).getStar());
        holder.tv_review.setText(mSongReviewItems.get(position).getReview());
        holder.tv_writeDate.setText(mSongReviewItems.get(position).getWriteDate());
    }

    @Override
    public int getItemCount() {
        return mSongReviewItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder에서는 아까 item_list.xml에서 만들어둔 제목,내용,작성날짜를 구성해야함
        // 건들
        private TextView tv_singer; // 가수명
        private TextView tv_title;  // 노래제목
        private RatingBar rtb_star; // 별점
        private TextView tv_review; // 한줄평
        private TextView tv_writeDate; // 작성날짜


        public ViewHolder(@NonNull View oneOfSongReviews) {
            super(oneOfSongReviews);

            // 건들
            tv_singer = oneOfSongReviews.findViewById(R.id.tv_singer);
            tv_title = oneOfSongReviews.findViewById(R.id.tv_title); // oneOfSongReviews.findViewById  oneOfSongReviews객체로 가져와야함
            rtb_star = oneOfSongReviews.findViewById(R.id.rtb_star);
            tv_review = oneOfSongReviews.findViewById(R.id.tv_review);
            tv_writeDate = oneOfSongReviews.findViewById(R.id.tv_date);

            // 화면에 나타나는 리스트 클릭시 해당 게시글의 수정이나 삭제를 가능하게 하기위해 처리해주는 부분이 필요
            // 팝업(다이어로그)이 뜨도록 해야함
            oneOfSongReviews.setOnClickListener(new View.OnClickListener() { // oneOfSongReviews는 노래평가 목록들중에서 하나임
                @Override
                public void onClick(View view) {
                    int curPos = getAdapterPosition(); //현재리스트클릭한아이템위치 (getAdapterPosition은 0부터시작이 됨)
                    SongReviewItem curPosItem = mSongReviewItems.get(curPos);  // 아이템위치와 array리스트로 저장해놓은 위치가 같으니까 curPos가 인자로잡힘

                    String[] choice = {"수정?", "삭제?","재생?"}; // 수정하기와 삭제하기 를 만들꺼다 뭐로?
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext); // AlertDialog라는 안드로이드에서 기본으로 제공하는 다이어로그로
//                    builder.setTitle("원하는 작업을 선택 해주세요"); // 팝업의 제목을 써야함
                    builder.setItems(choice, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int position) {
                            if(position == 0) {
                                // 수정하기를 누른 경우
                                //------------------------------------MainActivity.java의 Dialog부분 떼와서 일부 고쳐야함-----------------------------------
                                // 다이어로그 == 팝업 팝업창 띄우기 (게시글 작성을 위한 팝업창 띄우기)
                                Dialog dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_Dialog);
                                dialog.setContentView(R.layout.dialog_input); // 다이어로그 xml과 연결
                                // 만든 다이어로그xml속 뷰들을 findViewById로 가져올것이다.
                                // 필요한건 EditText가 필요함 그거 가져오면됨
                                // 대신 쌩으로 findViewById()하면 안됨.
                                // dialog.findViewByID()로 해야함. 즉, dialog객체를 통해 findViewById수행해야함
                                // 건들 - 다이어로그
                                EditText et_singer = dialog.findViewById(R.id.et_singer);
                                EditText et_title = dialog.findViewById(R.id.et_title);
                                RatingBar dialog_star = dialog.findViewById(R.id.dialog_star);
                                EditText et_review = dialog.findViewById(R.id.et_review);
                                Button btn_ok = dialog.findViewById(R.id.btn_ok);

                                // 건들
                                et_singer.setText(curPosItem.getSinger());
                                et_title.setText(curPosItem.getTitle()); // 수정하려고 열어보면 이전에 기입했던 값이 등장해야하기 때문에
                                dialog_star.setRating((float)curPosItem.getStar());
                                et_review.setText(curPosItem.getReview());

                                // 커서 이동
                                // 건들
                                et_singer.setSelection(et_singer.getText().length());
                                et_title.setSelection(et_title.getText().length()); // 제목에 적어놓은 글자 만큼 커서가 이동돼있어야 수정하기도 편리하고 눈에도 예쁨
                                et_review.setSelection(et_review.getText().length());

                                // 확인버튼을 눌렀을 때 -> UPDATE행위가 이뤄져야한다.
                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // update database
                                        // 건들
                                        String singer = et_singer.getText().toString();
                                        String title = et_title.getText().toString();
                                        float s = dialog_star.getRating();
                                        int star = (int)s;
                                        String review = et_review.getText().toString();
                                        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()); // 수정하는시점의 현재시간 연월일시분초 받아오기
                                        String beforeTime = curPosItem.getWriteDate(); // 클릭한 리스트에대해 (게시물을 올렸었을때의 시간)이전에 등록됐던 시간을 가져옴

                                        // DB에 업데이트 update table
                                        // 건들
                                        mDBHelper.updateSongReview(singer, title, star,review,currentTime, beforeTime);

                                        // update UI
                                        // 건들
                                        curPosItem.setSinger(singer);
                                        curPosItem.setTitle(title); // 갱신된 데이터로 다시 넣음
                                        curPosItem.setStar(star);
                                        curPosItem.setReview(review);
                                        curPosItem.setWriteDate(currentTime);
                                        notifyItemChanged(curPos, curPosItem); // 현재 클릭한 지점에서 아이템이 갱신됐다는 수정
                                        dialog.dismiss(); // 다이어로그 꺼야함
                                        Toast.makeText(mContext,"!!!'\"수정 완료'\"!!!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                dialog.show(); // 이게 있어야 다이어로그가 보인다.
                                //----------------------------------------------------------------------------------------------------
                            }
                            else if(position == 1) {
                                // 삭제하기를 누른 경우
                                String beforeTime = curPosItem.getWriteDate();

                                // DB에서 삭제 Delete table
                                mDBHelper.deleteSongReview(beforeTime);

                                // delete UI
                                mSongReviewItems.remove(curPos); // 현재클릭한위치의 인덱스인 ArrayList도 제거를 해줘야함
                                notifyItemRemoved(curPos); // 현재위치에대해서 바뀜을 갱신해주면됨
                                Toast.makeText(mContext,"!!!'\"제거 완료'\"!!!",Toast.LENGTH_SHORT).show();
                            }
                            else if (position == 2) {
                                // 재생을 누른경우
                                String title = curPosItem.getTitle();
                                String singer = curPosItem.getSinger();
                                String keyword = singer+" "+title+"\n"; // YouTube에서 검색할 키워드
                                Toast.makeText(mContext,keyword+"검색을 시작합니다.", Toast.LENGTH_SHORT).show();
                                searchYouTube(keyword);

                            }
                        }
                    });
                    builder.show();
                }
            });
        }
    }
    public void searchYouTube(String query) {
        Intent intent = new Intent(Intent.ACTION_SEARCH);
        intent.setPackage("com.google.android.youtube");
        intent.putExtra("query", query);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    // 액티비티에서 호출되는 함수이며, 현재 어댑터에 새로운 게시글 아이템을 전달받아 추가하는 목적이다.
    public void addItem(SongReviewItem _item) {
        mSongReviewItems.add(0,_item); // 최근에 올린게시물에 상단에 있도록 하기위해 의도적으로 0으로 설정
        notifyItemInserted(0); // notify가 들어간 것들은 새로고침이다.
    }
}