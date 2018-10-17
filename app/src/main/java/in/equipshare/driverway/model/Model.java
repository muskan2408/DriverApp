package in.equipshare.driverway.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Model implements Parcelable{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lon")
    @Expose

    private double lon;
    @SerializedName("user")
    @Expose
    private User user;


    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }



    protected Model(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        token = in.readString();
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
//

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        dest.writeString(token);
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
















//package in.equipshare.driverway.model;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//import java.util.Collections;
//
//public class Model implements Parcelable{
////    @SerializedName("result")
////    @Expose
////    private Result result;
//    @SerializedName("token")
//    @Expose
//    private String token;
//    @SerializedName("success")
//    @Expose
//    private Boolean success;
//    @SerializedName("msg")
//    @Expose
//    private String msg;
//    @SerializedName("name")
//    @Expose
//    private String name;
//    @SerializedName("email")
//    @Expose
//    private String email;
//    @SerializedName("password")
//    @Expose
//    private String password;
//    @SerializedName("contact")
//    @Expose
//    private String contact;
//    @SerializedName("driverId")
//    @Expose
//    private Integer driverId;
//
//
//    protected Model(Parcel in) {
//      //  result = in.readParcelable(Result.class.getClassLoader());
//        token = in.readString();
//        byte tmpSuccess = in.readByte();
//        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
//        msg = in.readString();
//        name = in.readString();
//        email = in.readString();
//        password = in.readString();
//        contact = in.readString();
//        if (in.readByte() == 0) {
//            driverId = null;
//        } else {
//            driverId = in.readInt();
//        }
//    }
//
//    public static final Creator<Model> CREATOR = new Creator<Model>() {
//        @Override
//        public Model createFromParcel(Parcel in) {
//            return new Model(in);
//        }
//
//        @Override
//        public Model[] newArray(int size) {
//            return new Model[size];
//        }
//    };
//
//    public Boolean getSuccess() {
//        return success;
//    }
//
//    public void setSuccess(Boolean success) {
//        this.success = success;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
////    public Result getResult() {
////        return result;
////    }
////
////    public void setResult(Result result) {
////        this.result = result;
////    }
//
//    public String getToken() {
//        return token;
//    }
//
//    public void setToken(String token) {
//        this.token = token;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getContact() {
//        return contact;
//    }
//
//    public void setContact(String contact) {
//        this.contact = contact;
//    }
//
//    public Integer getDriverId() {
//        return driverId;
//    }
//
//    public void setDriverId(Integer driverId) {
//        this.driverId = driverId;
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeValue(this.success);
//        dest.writeString(this.token);
//       // dest.writeParcelable(this.result,flags);
//        dest.writeString(this.msg);
//        dest.writeValue(this.name);
//        dest.writeString(this.email);
//        dest.writeList(Collections.singletonList(this.contact));
//
//    }
//}
