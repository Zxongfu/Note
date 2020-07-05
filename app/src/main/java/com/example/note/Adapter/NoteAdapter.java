package com.example.note.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.R;
import com.example.note.data.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.Inner> implements Filterable {

    private Context mContext;
    private List<Note> mNoteList;
    private List<Note> mNoteListAll;
    private onItemClickListener itemClickListener=null;


    public NoteAdapter(Context context, List<Note> noteList) {
        this.mContext = context;
        this.mNoteList = noteList;
        this.mNoteListAll = new ArrayList<>(noteList);
    }

    @NonNull
    @Override
    public NoteAdapter.Inner onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_note, parent, false);
        return new Inner(v);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.Inner holder, final int position) {
        View itemView = holder.itemView;
        mContext.setTheme(R.style.DayTheme);
        TextView contentTv = itemView.findViewById(R.id.content_tv);
        TextView timeTv = itemView.findViewById(R.id.time_tv);

        final Note note = mNoteList.get(position);
        contentTv.setText(note.getContent());
        timeTv.setText(note.getTime());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.OnItemClick(note.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mNoteList != null) {
            return mNoteList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private Filter mFilter = new Filter() {
        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Note> filterList = new ArrayList<>();

            if (TextUtils.isEmpty(charSequence)) {
                filterList.addAll(mNoteListAll);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Note note : mNoteListAll) {
                    if (note.getContent().toLowerCase().contains(filterPattern)) {

                        filterList.add(note);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        //run on ui thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mNoteList.clear();

            mNoteList.addAll((List<Note>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class Inner extends RecyclerView.ViewHolder {
        public Inner(@NonNull View itemView) {
            super(itemView);
        }

    }
    public  void setonItemClickListener(onItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    public interface onItemClickListener{
        void OnItemClick(long position);
    }
}
