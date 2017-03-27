package cn.xdysite.duanzi.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/23.
 */

public class Book  implements Parcelable{
    private int id;
    private String name;


    public Book(String name, int id) {
        this.name = name;
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel source) {
            int id = source.readInt();
            String str = source.readString();
            return new Book(str, id);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };
}
