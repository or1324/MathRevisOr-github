package or.nevet.orgeneralhelpers.graphical.recycler_view;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import or.nevet.orgeneralhelpers.graphical.activity_types.ButtonsListActivity;
import or.nevet.orgeneralhelpers.graphical.activity_types.ListActivity;

public class RecyclOrViewAdaptOr extends RecyclerView.Adapter<RecyclOrViewAdaptOr.ViewHoldOr> {

    ListActivity activity;
    List<CharSequence> texts;
    int recyclerViewRowId;
    int recyclerViewTextViewId;

    public RecyclOrViewAdaptOr(ListActivity activity, List<CharSequence> texts, int recyclerViewRowId, int recyclerViewTextViewId) {
        this.activity = activity;
        this.texts = texts;
        this.recyclerViewRowId = recyclerViewRowId;
        this.recyclerViewTextViewId = recyclerViewTextViewId;
    }

    @NonNull
    @Override
    public ViewHoldOr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(recyclerViewRowId, parent, false);
        return new ViewHoldOr(view, recyclerViewTextViewId);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoldOr holder, int position) {
        holder.listRow.setText(texts.get(position));
        if (activity instanceof ButtonsListActivity && holder.listRow instanceof Button) {
            holder.listRow.setOnClickListener(((ButtonsListActivity) activity).getOnClickListener(holder.listRow.getText().toString()));
            holder.listRow.setOnLongClickListener(((ButtonsListActivity) activity).getOnLongClickListener(holder.listRow.getText().toString()));
        }
    }

    @Override
    public int getItemCount() {
        return texts.size();
    }

    public static class ViewHoldOr extends RecyclerView.ViewHolder {

        TextView listRow;

        public ViewHoldOr(@NonNull View itemView, int recyclerViewTextViewId) {
            super(itemView);
            listRow = itemView.findViewById(recyclerViewTextViewId);
        }
    }
}
