package com.lakue.linememolist.Model;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DataMemo extends RealmObject {
    @PrimaryKey
    long idx = 0;
    String title = "";
    String content = "";
    byte[] thumbnail = null;

    public DataMemo(){

    }

    public DataMemo(long idx, String title, String content, byte[] thumbnail){
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public long getIdx() {
        return idx;
    }

    public void setIdx(long idx) {
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

    @Override
    public String toString() {
        return "DataMemo{" +
                "idx=" + idx +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
