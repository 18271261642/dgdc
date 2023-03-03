package com.jkcq.homebike.ali;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/10/12.
 */
public class BaseBean implements Parcelable {

    private int code;

    private String message;
    private String message_en;

    private long    timestamp;
    private String  error;
    private String  exception;
    private String  path;
    private boolean islastdata;
    private boolean isCache;
    private long    time;


    public String getMessage_en() {
        return message_en;
    }

    public void setMessage_en(String message_en) {
        this.message_en = message_en;
    }

    public BaseBean() {
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isIslastdata() {
        return islastdata;
    }

    public void setIslastdata(boolean islastdata) {
        this.islastdata = islastdata;
    }

    public boolean isCache() {
        return isCache;
    }

    public void setCache(boolean cache) {
        isCache = cache;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.message);
        dest.writeString(this.message_en);
        dest.writeLong(this.timestamp);
        dest.writeString(this.error);
        dest.writeString(this.exception);
        dest.writeString(this.path);
        dest.writeByte(this.islastdata ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCache ? (byte) 1 : (byte) 0);
        dest.writeLong(this.time);
    }

    protected BaseBean(Parcel in) {
        this.code = in.readInt();
        this.message = in.readString();
        this.message_en = in.readString();
        this.timestamp = in.readLong();
        this.error = in.readString();
        this.exception = in.readString();
        this.path = in.readString();
        this.islastdata = in.readByte() != 0;
        this.isCache = in.readByte() != 0;
        this.time = in.readLong();
    }

    public static final Creator<BaseBean> CREATOR = new Creator<BaseBean>() {
        @Override
        public BaseBean createFromParcel(Parcel source) {
            return new BaseBean(source);
        }

        @Override
        public BaseBean[] newArray(int size) {
            return new BaseBean[size];
        }
    };
}