package com.jeko.mynote;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {

    private NoteClickListener mListener;
    private final LayoutInflater mInflater;
    private List<Note> mNotes; // Cached copy of Notes

    NoteListAdapter(Context context, NoteClickListener listener) {
        this.mInflater = LayoutInflater.from(context);
        this.mListener = listener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recycle_view_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        if (mNotes != null) {
            Note current = mNotes.get(position);
            holder.titleItemView.setText(current.getTitle());
            holder.dateItemView.setText(current.getDate());
        }
    }

    void setNotes(List<Note> notes) {
        mNotes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mNotes != null)
            return mNotes.size();
        else return 0;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView titleItemView;
        private final TextView dateItemView;


        private NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            titleItemView = itemView.findViewById(R.id.title);
            dateItemView = itemView.findViewById(R.id.date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onNoteClick(getAdapterPosition());
            }
        }
    }
}
