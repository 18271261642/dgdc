package com.jkcq.homebike.ali;


import android.os.Parcel;
import android.os.Parcelable;


public class UpdatePhotoBean extends BaseBean {

    public String headUrl;
    public String headUrlTiny;

    @Override
    public String toString() {
        return "UpdatePhotoBean{" +
                "headUrl='" + headUrl + '\'' +
                ", headUrlTiny='" + headUrlTiny + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.headUrl);
        dest.writeString(this.headUrlTiny);
    }

    public UpdatePhotoBean() {
    }

    protected UpdatePhotoBean(Parcel in) {
        super(in);
        this.headUrl = in.readString();
        this.headUrlTiny = in.readString();
    }

    public static final Parcelable.Creator<UpdatePhotoBean> CREATOR = new Parcelable.Creator<UpdatePhotoBean>() {
        @Override
        public UpdatePhotoBean createFromParcel(Parcel source) {
            return new UpdatePhotoBean(source);
        }

        @Override
        public UpdatePhotoBean[] newArray(int size) {
            return new UpdatePhotoBean[size];
        }
    };
}
