package com.example.mydiary;

public class Node {
    //Node 구성하는 id, title, contents, createDateStr
    int _id;
    String title;
    String contents;
    String createDateStr;

    //Node 구성하는 id, title, contents, createDateStr
    public Node(int _id, String title, String contents, String createDateStr) {
        this._id = _id;
        this.title = title;
        this.contents = contents;
        this.createDateStr = createDateStr;
    }

    //각 필드의 접근자, 생성자 선언
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }
}
