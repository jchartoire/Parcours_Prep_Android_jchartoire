package com.jchartoire.mareu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jchartoire.mareu.databinding.ItemContentBinding;
import com.jchartoire.mareu.di.DI;
import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.service.ApiService;
import com.jchartoire.mareu.tools.filterParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.jchartoire.mareu.tools.DateUtils.dateFormatter;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Meeting> meetings, meetingsFiltered;
    private Context context;
    private ApiService apiService;

    ItemRecyclerViewAdapter(List<Meeting> items) {
        this.meetings = items;
        this.meetingsFiltered = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content, parent, false);
        context = view.getContext();
        apiService = DI.getNeighbourApiService();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        ItemContentBinding binding;
        binding = ItemContentBinding.bind(holder.itemView);

        final Meeting meeting = meetingsFiltered.get(position);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        binding.tvItemTitle.setText(String.format("%s - %s - %s", meeting.getTitle(), timeFormatter.format(meeting.getStartDate()), meeting.getLeader().getFirstName()));
        for (int j = 0; j < meeting.getUsers().size(); j++) {
            if (j == 0) {
                binding.tvItemInfos.setText(meeting.getUsers().get(j).getEmail());
            } else {
                binding.tvItemInfos.setText(String.format("%s, %s", binding.tvItemInfos.getText(), meeting.getUsers().get(j).getEmail()));
            }
        }
        binding.ivListRoomColor.setColorFilter(meeting.getRoom().getColor());
        binding.ivDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService.deleteMeeting(meeting);
                meetings = apiService.getMeetings();
                setFilter(filterParams.getFilterType(), filterParams.getFilterPattern());
                notifyDataSetChanged();
            }
        });

        /* adding an OnClickListener on each item, and launch DetailsActivity */
        binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long Id = meeting.getId();        // getting meeting's ID of the clicked item
                Intent myIntent = new Intent(context, DetailActivity.class);
                myIntent.putExtra("meetingId", Id);     // putting the meeting's ID to an intent
                context.startActivity(myIntent);               // then launching the activity with this intent
            }
        });
    }

    @Override
    public int getItemCount() {
        return meetingsFiltered.size();
    }

    void setFilter(int type, String param) {
        switch (type) {
            case 0: // Reset filter
                meetingsFiltered = meetings;
                break;
            case 1: // filter by date
                meetingsFiltered = new ArrayList<>(); //TODO:
                for (Meeting meeting : meetings) {
                    if (dateFormatter.format(meeting.getStartDate()).equals(param)) {
                        meetingsFiltered.add(meeting);
                    }
                }
                break;
            case 2: // filter by room
                meetingsFiltered = new ArrayList<>();
                for (Meeting meeting : meetings) {
                    if (meeting.getRoom().getRoomName().equals(param)) {
                        meetingsFiltered.add(meeting);
                    }
                }
                break;
        }
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    //TODO: la classe doit restée sortie ? avant elle était en inner class, dans l'arborescence elle crée un sous fichier
    ViewHolder(View view) {
        super(view);
    }
}
