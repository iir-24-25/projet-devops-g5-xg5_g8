package com.example.sahti.activities;

import android.os.Parcel;
import android.os.Parcelable;

public class DoctorModel implements Parcelable {

    private Long id;
    private String fullName;
    private String inpe;
    private String specialty;
    private String address;
    private String email;
    private String password;
    private String phone;
    private String about;
    private String photo;

    public DoctorModel() {
    }

    public DoctorModel(Long id, String fullName, String inpe, String specialty, String address,
                       String email, String password, String phone, String about, String photo) {
        this.id = id;
        this.fullName = fullName;
        this.inpe = inpe;
        this.specialty = specialty;
        this.address = address;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.about = about;
        this.photo = photo;
    }

    protected DoctorModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        fullName = in.readString();
        inpe = in.readString();
        specialty = in.readString();
        address = in.readString();
        email = in.readString();
        password = in.readString();
        phone = in.readString();
        about = in.readString();
        photo = in.readString();
    }

    public static final Creator<DoctorModel> CREATOR = new Creator<DoctorModel>() {
        @Override
        public DoctorModel createFromParcel(Parcel in) {
            return new DoctorModel(in);
        }

        @Override
        public DoctorModel[] newArray(int size) {
            return new DoctorModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(fullName);
        dest.writeString(inpe);
        dest.writeString(specialty);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(phone);
        dest.writeString(about);
        dest.writeString(photo);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getInpe() { return inpe; }

    public void setInpe(String inpe) { this.inpe = inpe; }

    public String getSpecialty() { return specialty; }

    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getAbout() { return about; }

    public void setAbout(String about) { this.about = about; }

    public String getPhoto() { return photo; }

    public void setPhoto(String photo) { this.photo = photo; }
}
