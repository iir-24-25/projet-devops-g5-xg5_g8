package com.example.sahti.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahti.R;
import com.example.sahti.models.AppointmentWithDoctor;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoctorAppointmentsAdapter extends RecyclerView.Adapter<DoctorAppointmentsAdapter.AppointmentViewHolder> {

    public interface OnValidateClickListener {
        void onValidateClick(AppointmentWithDoctor appointment);
    }

    public interface OnRejectClickListener {
        void onRejectClick(AppointmentWithDoctor appointment);
    }

    private List<AppointmentWithDoctor> appointments;
    private final OnValidateClickListener onValidateClickListener;
    private final OnRejectClickListener onRejectClickListener;

    public DoctorAppointmentsAdapter(OnValidateClickListener onValidateClickListener,
                                     OnRejectClickListener onRejectClickListener) {
        this.onValidateClickListener = onValidateClickListener;
        this.onRejectClickListener = onRejectClickListener;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_doctor_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        if (appointments == null || appointments.size() <= position) return;

        AppointmentWithDoctor appointment = appointments.get(position);

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.FRANCE);

        holder.patientNameText.setText("Patient : " + appointment.getPatientId());  // Assuming getPatientName() exists in AppointmentWithDoctor
        holder.dateText.setText("Date : " + dateFormat.format(new Date(appointment.getDate())));
        holder.timeText.setText("Heure : " + appointment.getTimeSlot());

        String status;
        switch (appointment.getStatus()) {
            case "pending":
                status = "En attente";
                break;
            case "validated":
                status = "Validé";
                break;
            case "rejected":
                status = "Refusé";
                break;
            default:
                status = appointment.getStatus();
        }
        holder.statusText.setText("Statut : " + status);

        if ("pending".equals(appointment.getStatus())) {
            holder.validateButton.setVisibility(View.VISIBLE);
            holder.rejectButton.setVisibility(View.VISIBLE);
        } else {
            holder.validateButton.setVisibility(View.GONE);
            holder.rejectButton.setVisibility(View.GONE);
        }

        holder.validateButton.setOnClickListener(v -> {
            if (onValidateClickListener != null) {
                onValidateClickListener.onValidateClick(appointment);
            }
        });

        holder.rejectButton.setOnClickListener(v -> {
            if (onRejectClickListener != null) {
                onRejectClickListener.onRejectClick(appointment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (appointments == null) ? 0 : appointments.size();
    }

    public void updateAppointments(List<AppointmentWithDoctor> newAppointments) {
        this.appointments = newAppointments;
        notifyDataSetChanged();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView patientNameText;
        TextView dateText;
        TextView timeText;
        MaterialButton validateButton;
        MaterialButton rejectButton;
        TextView statusText;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            patientNameText = itemView.findViewById(R.id.patientNameText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            validateButton = itemView.findViewById(R.id.validateButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            statusText = itemView.findViewById(R.id.statusText);
        }
    }
}
