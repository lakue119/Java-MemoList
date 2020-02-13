package com.lakue.linememolist.Model;

import java.util.ArrayList;

import io.realm.RealmObject;

public class DataMemo extends RealmObject {
    int idx = 0;
    String title = "";
    String content = "";
    //ArrayList<String> imgs = new ArrayList<>();

    public DataMemo(){

    }

    public DataMemo(int idx, String title, String content){
        this.idx = idx;
        this.title = title;
        this.content = content;
    }


    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

//    public ArrayList<String> getImgs() {
//        return imgs;
//    }
//
//    public void setImgs(ArrayList<String> imgs) {
//        this.imgs = imgs;
//    }

//    @Override
//    public String toString() {
//        return "DataMemo{" +
//                "idx=" + idx +
//                ", title='" + title + '\'' +
//                ", content='" + content + '\'' +
//                ", imgs=" + imgs +
//                '}';
//    }
}
