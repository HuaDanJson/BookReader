package com.example.jason.examination.data;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import cn.bmob.v3.BmobObject;

/**
 * Created by jason on 2018/3/17.
 */
@Entity
public class BookList extends BmobObject {

    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "BookList")
    private String bookName;
    private String BookIntroduce;
    private String BookWriter;
    private String BookValue;
    private String bookCover;
    private String classification;//书的分类
    private boolean isReadBefore;//是否阅读过
    @Generated(hash = 1692221099)
    public BookList(Long id, String bookName, String BookIntroduce,
            String BookWriter, String BookValue, String bookCover,
            String classification, boolean isReadBefore) {
        this.id = id;
        this.bookName = bookName;
        this.BookIntroduce = BookIntroduce;
        this.BookWriter = BookWriter;
        this.BookValue = BookValue;
        this.bookCover = bookCover;
        this.classification = classification;
        this.isReadBefore = isReadBefore;
    }
    @Generated(hash = 1714324117)
    public BookList() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getBookName() {
        return this.bookName;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
    public String getBookIntroduce() {
        return this.BookIntroduce;
    }
    public void setBookIntroduce(String BookIntroduce) {
        this.BookIntroduce = BookIntroduce;
    }
    public String getBookWriter() {
        return this.BookWriter;
    }
    public void setBookWriter(String BookWriter) {
        this.BookWriter = BookWriter;
    }
    public String getBookValue() {
        return this.BookValue;
    }
    public void setBookValue(String BookValue) {
        this.BookValue = BookValue;
    }
    public String getBookCover() {
        return this.bookCover;
    }
    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }
    public String getClassification() {
        return this.classification;
    }
    public void setClassification(String classification) {
        this.classification = classification;
    }
    public boolean getIsReadBefore() {
        return this.isReadBefore;
    }
    public void setIsReadBefore(boolean isReadBefore) {
        this.isReadBefore = isReadBefore;
    }

    @Override
    public String toString() {
        return "BookList{" +
                "id=" + id +
                ", bookName='" + bookName + '\'' +
                ", BookIntroduce='" + BookIntroduce + '\'' +
                ", BookWriter='" + BookWriter + '\'' +
                ", BookValue='" + BookValue + '\'' +
                ", bookCover='" + bookCover + '\'' +
                ", classification='" + classification + '\'' +
                ", isReadBefore=" + isReadBefore +
                '}';
    }
}
