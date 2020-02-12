package com.lakue.linememolist;

import java.util.ArrayList;

public class DataMemo {
    int idx = 0;
    String title = "";
    String content = "";
    ArrayList<String> imgs = new ArrayList<>();

    public DataMemo(String title, String content){
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

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    @Override
    public String toString() {
        return "DataMemo{" +
                "idx=" + idx +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", imgs=" + imgs +
                '}';
    }
}
