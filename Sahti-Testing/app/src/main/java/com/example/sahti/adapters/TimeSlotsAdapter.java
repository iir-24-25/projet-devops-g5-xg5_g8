package com.example.sahti.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sahti.R;

public class TimeSlotsAdapter extends ListAdapter<String, TimeSlotsAdapter.TimeSlotViewHolder> {

    private int selectedPosition = -1;
    private OnTimeSlotSelectedListener onTimeSlotSelectedListener;

    public TimeSlotsAdapter(OnTimeSlotSelectedListener listener) {
        super(new TimeSlotDiffCallback());
        this.onTimeSlotSelectedListener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        String timeSlot = getItem(position);
        holder.bind(timeSlot);
    }

    class TimeSlotViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            // On suppose que item_time_slot.xml est un TextView ou que le TextView racine est directement itemView
            textView = (TextView) itemView;
        }

        public void bind(final String timeSlot) {
            textView.setText(timeSlot);
            textView.setSelected(getAdapterPosition() == selectedPosition);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int oldPosition = selectedPosition;
                    selectedPosition = getAdapterPosition();

                    notifyItemChanged(oldPosition);
                    notifyItemChanged(selectedPosition);

                    if (onTimeSlotSelectedListener != null) {
                        onTimeSlotSelectedListener.onTimeSlotSelected(timeSlot);
                    }
                }
            });
        }
    }

    public interface OnTimeSlotSelectedListener {
        void onTimeSlotSelected(String timeSlot);
    }

    static class TimeSlotDiffCallback extends DiffUtil.ItemCallback<String> {

        @Override
        public boolean areItemsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull String oldItem, @NonNull String newItem) {
            return oldItem.equals(newItem);
        }
    }
}
