
package com.lakue.linememolist.Model;

import java.util.Arrays;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DataMemoImg extends RealmObject {
    @PrimaryKey
    long img_idx = 0;
    long memo_idx = 0;
    byte[] img_file;

    public DataMemoImg(){

    }

    public DataMemoImg(long img_idx, long  memo_idx, byte[] img_file){
        this.img_idx = img_idx;
        this.memo_idx = memo_idx;
        this.img_file = img_file;
    }

    public long getImg_idx() {
        return img_idx;
    }

    public void setImg_idx(long img_idx) {
        this.img_idx = img_idx;
    }

    public long getMemo_idx() {
        return memo_idx;
    }

    public void setMemo_idx(long memo_idx) {
        this.memo_idx = memo_idx;
    }

    public byte[] getImg_file() {
        return img_file;
    }

    public void setImg_file(byte[] img_file) {
        this.img_file = img_file;
    }

    @Override
    public String toString() {
        return "DataMemoImg{" +
                "img_idx=" + img_idx +
                ", memo_idx=" + memo_idx +
                ", img_file=" + Arrays.toString(img_file) +
                '}';
    }
}

