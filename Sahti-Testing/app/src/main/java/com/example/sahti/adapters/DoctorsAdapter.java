package com.example.sahti.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sahti.R;
import com.example.sahti.activities.DoctorDetailActivity;
import com.example.sahti.activities.DoctorModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> {

    private List<DoctorModel> doctors = new ArrayList<>();

    public void updateDoctors(List<DoctorModel> newDoctors) {
        doctors = newDoctors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        holder.bind(doctors.get(position));
    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class DoctorViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText;
        private final TextView specialityText;
        private final TextView addressText;
        private final ImageView imageView;
        private final View btnDetails;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.doctorName);
            specialityText = itemView.findViewById(R.id.doctorSpeciality);
            addressText = itemView.findViewById(R.id.doctorAddress);
            imageView = itemView.findViewById(R.id.doctorImage);
            btnDetails = itemView.findViewById(R.id.btnDetails);

            itemView.setOnClickListener(view -> openDoctorDetail(view.getContext()));
            btnDetails.setOnClickListener(view -> openDoctorDetail(view.getContext()));
        }

        private void openDoctorDetail(Context context) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                DoctorModel doctor = doctors.get(position);
                Intent intent = new Intent(context, DoctorDetailActivity.class);
                intent.putExtra("DOCTOR_ID", doctor.getId());
                intent.putExtra("DOCTOR_NAME", doctor.getFullName());
                intent.putExtra("DOCTOR_SPECIALITY", doctor.getSpecialty());
                intent.putExtra("DOCTOR_PHOTO", doctor.getPhoto());
                context.startActivity(intent);
            }
        }

        public void bind(DoctorModel doctor) {
            nameText.setText(doctor.getFullName());
            specialityText.setText(doctor.getSpecialty());
            addressText.setText(doctor.getAddress());

            String photoData = doctor.getPhoto();
            if (photoData != null && !photoData.isEmpty()) {
                try {
                    if (photoData.startsWith("data:image")) {
                        String base64Image = photoData.substring(photoData.indexOf("base64,") + 7);
                        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        imageView.setImageBitmap(bitmap);
                    } else {
                        imageView.setImageResource(R.drawable.default_profile); // or load URL if needed
                    }
                } catch (Exception e) {
                    Log.e("DoctorsAdapter", "Erreur chargement image: " + e.getMessage());
                    imageView.setImageResource(R.drawable.default_profile);
                }
            } else {
                imageView.setImageResource(R.drawable.default_profile);
            }
        }
    }
}
