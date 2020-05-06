package com.jchartoire.mareu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jchartoire.mareu.databinding.ItemContentBinding;
import com.jchartoire.mareu.events.DeleteMeetingEvent;
import com.jchartoire.mareu.model.Meeting;

import org.greenrobot.eventbus.EventBus;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Meeting> meetingList;
    private Context context;

    ItemRecyclerViewAdapter(List<Meeting> items) {
        this.meetingList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content, parent, false);
        context = view.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Meeting meeting = meetingList.get(position);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        holder.binding.tvItemTitle.setText(String.format("%s - %s - %s", meeting.getTitle(), timeFormatter.format(meeting.getStartDate()),
                meeting.getRoom().getRoomName()));
        for (int j = 0; j < meeting.getUsers().size(); j++) {
            if (j == 0) {
                holder.binding.tvItemInfos.setText(meeting.getUsers().get(j).getEmail());
            } else {
                holder.binding.tvItemInfos.setText(String.format("%s, %s", holder.binding.tvItemInfos.getText(), meeting.getUsers().get(j).getEmail()));
            }
        }
        holder.binding.ivListRoomColor.setColorFilter(meeting.getRoom().getColor());
        holder.binding.ivDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new DeleteMeetingEvent(meeting));
            }
        });

        /* adding an OnClickListener on each item, and launch DetailsActivity */
        holder.binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long Id = meeting.getId();        // getting meeting's ID of the clicked item
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("meetingId", Id);     // putting the meeting's ID to an intent
                context.startActivity(intent);               // then launching the activity with this intent
            }
        });
    }

    @Override
    public int getItemCount() {
        if (meetingList != null) {
            return meetingList.size();
        } else return 0;
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    ItemContentBinding binding;

    ViewHolder(View view) {
        super(view);
        binding = ItemContentBinding.bind(view);
    }
}