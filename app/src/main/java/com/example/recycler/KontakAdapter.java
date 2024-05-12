package com.example.recycler;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class KontakAdapter extends RecyclerView.Adapter<KontakAdapter.VH> {

    private final Context context;
    private final List<Kontak> kontaks;
    private SparseBooleanArray selectedItems;

    public KontakAdapter(Context context, List<Kontak> kontaks) {
        this.context = context;
        this.kontaks = kontaks;
        this.selectedItems = new SparseBooleanArray();
    }

    public List<Kontak> getSelectedContacts() {
        List<Kontak> selectedContacts = new ArrayList<>();
        for (int i = 0; i < kontaks.size(); i++) {
            if (selectedItems.get(i, false)) {
                selectedContacts.add(kontaks.get(i));
            }
        }
        return selectedContacts;
    }

    public class VH extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvNama;
        private final RadioButton radioButton;

        private final ImageView ivAvatar;
        public VH(@NonNull View itemView) {
            super(itemView);
            this.tvNama = itemView.findViewById(R.id.tvNama);
            this.radioButton = itemView.findViewById(R.id.btShow);
            this.ivAvatar = itemView.findViewById(R.id.ivAvatar);
            itemView.setOnClickListener(this);
            radioButton.setOnClickListener(this);
        }

        public void bindKontak(Kontak k, int position) {
            this.tvNama.setText(k.nama);
            this.radioButton.setChecked(selectedItems.get(position, false));
            this.ivAvatar.setImageResource(k.avatarId);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                selectedItems.put(adapterPosition, !selectedItems.get(adapterPosition, false));
                notifyItemChanged(adapterPosition);
            }
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vh = LayoutInflater.from(this.context).inflate(R.layout.list_kontak, parent, false);
        return new VH(vh);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Kontak k = this.kontaks.get(position);
        holder.bindKontak(k, position);
    }

    @Override
    public int getItemCount() {
        return this.kontaks.size();
    }
}
