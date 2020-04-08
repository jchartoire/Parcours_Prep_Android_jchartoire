package com.jchartoire.mareu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jchartoire.mareu.model.Meeting;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

    private final List<Meeting> mMeetings;
    private Context mContext;

    public ItemRecyclerViewAdapter(List<Meeting> items) {
        mMeetings = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_content, parent, false);
        mContext = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Meeting meeting = mMeetings.get(position);
        holder.mMeetingTitle.setText(meeting.getTitle());
        holder.mImageViewCircle.setColorFilter(Color.parseColor(meeting.getColor()));


        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo : ajouter fonction de suppression de l'item
                System.out.println("deleting item :" + meeting.getTitle());

            }
        });

        /** adding an OnClickListener on each item, and launch DetailsActivity */
        holder.mItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int Id = meeting.getId();        // getting meeting's ID of the clicked item
//                Intent myIntent = new Intent(mContext, DetailActivity.class);
//                myIntent.putExtra("meetingId", Id);     // putting the meeting's ID to an intent
//                mContext.startActivity(myIntent);               // then launching the activity with this intent
                System.out.println("starting activity details for :" + meeting.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageViewCircle)
        public ImageView mImageViewCircle;
        @BindView(R.id.id_text_Title)
        public TextView mMeetingTitle;
        @BindView(R.id.imageViewDelete)
        public ImageButton mDeleteButton;
        @BindView(R.id.item)
        LinearLayout mItem;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

//            mMeetingTitle = view.findViewById(R.id.id_text_Title);
//            ImageButton = view.findViewById(R.id.imageViewDelete);
//            mItem = view.findViewById(R.id.item);
        }
    }
}
