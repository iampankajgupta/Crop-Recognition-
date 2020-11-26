package in.my.cropmldetection;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CropNames implements Parcelable {

    ArrayList<String> result;

    public CropNames(){
        super();
    }

    protected CropNames(Parcel in) {
        result = in.createStringArrayList();
    }

    public static final Creator<CropNames> CREATOR = new Creator<CropNames>() {
        @Override
        public CropNames createFromParcel(Parcel in) {
            return new CropNames(in);
        }

        @Override
        public CropNames[] newArray(int size) {
            return new CropNames[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(result);
    }

    public ArrayList<String> getResult() {
        return result;
    }

    public void setResult(ArrayList<String> result) {
        this.result = result;
    }
}
