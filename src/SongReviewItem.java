package com.cookandroid.songreviewapp;
public class SongReviewItem {
    private int id;             // 게시글 고유 ID
    private String singer;     // 가수명
    private String title;       // 노래제목
    private int star;           // 별점
    private String review;      // 한줄평
    private String writeDate;   // 작성날짜

    // alt+Ins -> Getter and Setter -> ctrl+a(전체선택) -> ok
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }
}