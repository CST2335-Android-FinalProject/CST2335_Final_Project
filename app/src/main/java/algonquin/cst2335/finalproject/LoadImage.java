package algonquin.cst2335.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

public class LoadImage implements Parcelable{

    private String image;
    private long height;
    private long width;

    private String imageId;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeLong(height);
        dest.writeLong(width);
        dest.writeString(imageId); // Add imageId to the parcel
    }


    protected LoadImage(Parcel in) {
        image = in.readString();
        height = in.readInt();
        width = in.readInt();
    }

    public String getImage() {
        return image;
    }

    public long getHeight() {
        return height;
    }

    public long getWidth() {
        return width;
    }

    public static final Creator<LoadImage> CREATOR = new Creator<LoadImage>() {
        @Override
        public LoadImage createFromParcel(Parcel source) {
            return new LoadImage(source);
        }

        @Override
        public LoadImage[] newArray(int size) {
            return new LoadImage[size];
        }
    };

    public LoadImage(String imageId, String image, int height, int width) {
        this.imageId = imageId;
        this.image = image;
        this.height = height;
        this.width = width;
    }

}
