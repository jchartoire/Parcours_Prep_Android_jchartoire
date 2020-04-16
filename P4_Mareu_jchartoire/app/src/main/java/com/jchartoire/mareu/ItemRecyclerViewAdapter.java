package com.jchartoire.mareu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.threeten.bp.format.DateTimeFormatter;

import com.jchartoire.mareu.di.DI;
import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.model.User;
import com.jchartoire.mareu.service.ApiService;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private List<Meeting> meetings;
    private Context context;
    private ApiService apiService;


    public ItemRecyclerViewAdapter(List<Meeting> items) {
        meetings = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_item_content, parent, false);
        context = view.getContext();
        apiService = DI.getNeighbourApiService();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        holder.meetingTitle.setText(meeting.getTitle() + " - " + meeting.getStartDate().format(dateTimeFormatter) + " - " + meeting.getLeader());
        for (int j = 0 ; j < meeting.getUsers().size(); j++)
        {
            if (j == 0)
            {
                holder.itemParticipants.setText(meeting.getUsers().get(j).getEmail());
            }
            else
            {
                holder.itemParticipants.setText(holder.itemParticipants.getText() + ", " +  meeting.getUsers().get(j).getEmail());
            }
        }
        holder.imageViewCircle.setColorFilter(meeting.getRoom().getColor());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo : ajouter fonction de suppression de l'item
                System.out.println("deleting : " + meeting.getTitle());
            }
        });

        /* adding an OnClickListener on each item, and launch DetailsActivity */
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int Id = meeting.getId();        // getting meeting's ID of the clicked item
                Intent myIntent = new Intent(context, DetailActivity.class);
                myIntent.putExtra("meetingId", Id);     // putting the meeting's ID to an intent
                context.startActivity(myIntent);               // then launching the activity with this intent
            }
        });
    }

    @Override
    public int getItemCount() {
        System.out.println("meeting size : " + meetings.size());
        return meetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_list_room_color)
        public ImageView imageViewCircle;
        @BindView(R.id.tv_item_title)
        public TextView meetingTitle;
        @BindView(R.id.tv_item_infos)
        public TextView itemParticipants;
        @BindView(R.id.iv_delete_button)
        public ImageButton deleteButton;
        @BindView(R.id.item)
        RelativeLayout item;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
