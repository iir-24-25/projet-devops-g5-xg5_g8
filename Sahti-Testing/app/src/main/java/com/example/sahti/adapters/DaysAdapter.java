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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DaysAdapter extends ListAdapter<Calendar, DaysAdapter.DayViewHolder> {

    public interface OnDaySelectedListener {
        void onDaySelected(Calendar day);
    }

    private OnDaySelectedListener onDaySelectedListener;
    private int selectedPosition = -1;

    public DaysAdapter(OnDaySelectedListener listener) {
        super(new DayDiffCallback());
        this.onDaySelectedListener = listener;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Calendar day = getItem(position);
        holder.bind(day);
    }

    class DayViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }

        public void bind(final Calendar day) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE d MMM", Locale.getDefault());
            textView.setText(dateFormat.format(day.getTime()));

            textView.setSelected(getAdapterPosition() == selectedPosition);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int oldPosition = selectedPosition;
                    selectedPosition = getAdapterPosition();

                    if (oldPosition != -1) {
                        notifyItemChanged(oldPosition);
                    }
                    notifyItemChanged(selectedPosition);

                    if (onDaySelectedListener != null) {
                        onDaySelectedListener.onDaySelected(day);
                    }
                }
            });
        }
    }

    public static class DayDiffCallback extends DiffUtil.ItemCallback<Calendar> {

        @Override
        public boolean areItemsTheSame(@NonNull Calendar oldItem, @NonNull Calendar newItem) {
            return oldItem.getTimeInMillis() == newItem.getTimeInMillis();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Calendar oldItem, @NonNull Calendar newItem) {
            return oldItem.getTimeInMillis() == newItem.getTimeInMillis();
        }
    }
}
