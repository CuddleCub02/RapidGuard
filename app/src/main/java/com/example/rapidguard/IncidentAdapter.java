package com.example.rapidguard;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class IncidentAdapter
        extends RecyclerView.Adapter<IncidentAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onClick(IncidentItem item);
    }

    private List<IncidentItem> items;
    private final OnItemClickListener listener;

    public IncidentAdapter(List<IncidentItem> items,
                           OnItemClickListener listener) {
        this.items    = items;
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<IncidentItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_incident_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        IncidentItem item = items.get(pos);

        h.ivIcon.setImageResource(item.iconRes);
        h.tvType.setText(item.type);
        h.tvDateTime.setText(item.dateTime);
        h.tvLocation.setText(item.location);
        h.tvStatus.setText(item.status);
        h.tvRisk.setText(item.riskLevel);

        // Dynamic status badge color
        int statusColor;
        int statusBg;
        switch (item.status) {
            case "Resolved":
                statusColor = R.color.status_success;
                statusBg    = R.drawable.bg_status_success;
                break;
            case "In Progress":
                statusColor = R.color.status_warning;
                statusBg    = R.drawable.bg_status_warning;
                break;
            default:
                statusColor = R.color.status_danger;
                statusBg    = R.drawable.bg_status_danger;
                break;
        }
        h.tvStatus.setTextColor(
                ContextCompat.getColor(h.itemView.getContext(), statusColor));
        h.tvStatus.setBackgroundResource(statusBg);

        // Dynamic risk chip color
        int riskColor;
        int riskBg;
        switch (item.riskLevel) {
            case "Low":
                riskColor = R.color.status_success;
                riskBg    = R.drawable.bg_status_success;
                break;
            case "Medium":
                riskColor = R.color.status_warning;
                riskBg    = R.drawable.bg_status_warning;
                break;
            default:
                riskColor = R.color.status_danger;
                riskBg    = R.drawable.bg_status_danger;
                break;
        }
        h.tvRisk.setTextColor(
                ContextCompat.getColor(h.itemView.getContext(), riskColor));
        h.tvRisk.setBackgroundResource(riskBg);

        h.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // ✅ FIX: must be PUBLIC so RecyclerView can access it externally
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView  tvType, tvDateTime, tvLocation, tvStatus, tvRisk;

        public ViewHolder(@NonNull View v) {
            super(v);
            ivIcon     = v.findViewById(R.id.ivIncidentIcon);
            tvType     = v.findViewById(R.id.tvIncidentType);
            tvDateTime = v.findViewById(R.id.tvIncidentDateTime);
            tvLocation = v.findViewById(R.id.tvIncidentLocation);
            tvStatus   = v.findViewById(R.id.tvStatusBadge);
            tvRisk     = v.findViewById(R.id.tvRiskChip);
        }
    }
}