package com.jchartoire.mareu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.jchartoire.mareu.databinding.ActivityListBinding;
import com.jchartoire.mareu.di.DI;
import com.jchartoire.mareu.events.DeleteMeetingEvent;
import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.service.ApiService;
import com.jchartoire.mareu.tools.DatePickerFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.jchartoire.mareu.tools.DateUtils.dateFormatter;

public class ItemListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private ActivityListBinding binding;
    private ApiService apiService;
    private ItemRecyclerViewAdapter adapter;
    private LinearLayout spinnerLayout;
    private List<Room> rooms;
    private int filterType = 0;
    private String filterPattern = null;
    private String selectedRoomFilterString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);
        Drawable drawable = getDrawable(R.drawable.ic_filter_list_black_24dp);
        binding.toolbar.setOverflowIcon(drawable);

        apiService = DI.getApiService();

        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemListActivity.this, DetailActivity.class);
                ItemListActivity.this.startActivity(intent);
            }
        });

        binding.resetFilterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetFilter();
            }
        });

        initList();
        initFilterInfoBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks
        int id = item.getItemId();
        DialogFragment newFragmentDate = new DatePickerFragment(this, new Date());
        switch (id) {
            case R.id.reset_filter:
                resetFilter();
                return true;

            case R.id.filter_date:
                // prompt date picker
                newFragmentDate.show(getSupportFragmentManager(), "datePicker");
                return true;

            case R.id.filter_room:
                // filter list by room
                setRoomFilter();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*=== Init the List of meetings ===*/
    private void initList() {
        List<Meeting> meetings = apiService.getFilteredMeetings(filterType, filterPattern);
        Collections.sort(meetings, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting m1, Meeting m2) {
                return m1.getStartDate().compareTo(m2.getStartDate());
            }
        });
        if (adapter == null) {
            rooms = apiService.getRooms();
            adapter = new ItemRecyclerViewAdapter(meetings);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            adapter.setData(meetings);
        }
    }

    /*=== Init info bar ===*/
    private void initFilterInfoBar() {
        // reset bottom filter info bar
        switch (filterType) {
            case 0:
                // reset bottom filter info bar
                binding.filterType.setText(R.string.no_Filter_Text);
                ((ViewGroup.MarginLayoutParams) binding.recyclerView.getLayoutParams()).bottomMargin = 0;
                binding.bottomBar.getLayoutParams().height = 0;
                break;

            case 1:
                // set bottom filter info bar
                binding.filterType.setText(String.format("%s%s", getString(R.string.filter_By_Date_Text), filterPattern));
                ((ViewGroup.MarginLayoutParams) binding.recyclerView.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.bottom_bar_height);
                binding.bottomBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.bottom_bar_height);
                break;

            case 2:
                // set bottom filter info bar
                binding.filterType.setText(String.format("%s%s", getString(R.string.filter_text), filterPattern));
                ((ViewGroup.MarginLayoutParams) binding.recyclerView.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.bottom_bar_height);
                binding.bottomBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.bottom_bar_height);
                break;
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // save current filter
        Calendar calendar = Calendar.getInstance();
        // get initial today's date and edit only Year, Month, Day
        calendar.set(year, month, day);
        String dateSet = dateFormatter.format(calendar.getTime());
        filterType = 1;
        filterPattern = dateSet;
        initList();
        // set bottom filter info bar
        binding.filterType.setText(String.format("%s%s", getString(R.string.filter_By_Date_Text), dateSet));
        ((ViewGroup.MarginLayoutParams) binding.recyclerView.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.bottom_bar_height);
        binding.bottomBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.bottom_bar_height);
    }

    void spinnerRoomSetup() {
        ArrayAdapter<Room> roomsAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, rooms);
        roomsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spnRoom = new Spinner(this);
        spnRoom.setAdapter(roomsAdapter);
        spinnerLayout = new LinearLayout(this);
        LinearLayout.LayoutParams parameter = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        parameter.setMargins(100, 20, 100, 20);
        spnRoom.setLayoutParams(parameter);
        spnRoom.setSelection(0);
        spinnerLayout.addView(spnRoom);
        spnRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                Room selectedRoom = (Room) (parent.getSelectedItem());
                long roomId = selectedRoom.getId();
                selectedRoomFilterString = apiService.getRoomById(roomId).getRoomName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void setRoomFilter() {
        spinnerRoomSetup();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (spinnerLayout.getParent() != null)
            ((ViewGroup) spinnerLayout.getParent()).removeView(spinnerLayout);
        builder.setView(spinnerLayout);
        builder.setTitle("Choisissez la salle : ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (selectedRoomFilterString != null) {
                    filterType = 2;
                    filterPattern = selectedRoomFilterString;
                    initList();
                    // set bottom filter info bar
                    binding.filterType.setText(String.format("%s%s", getString(R.string.filter_text), selectedRoomFilterString));
                    ((ViewGroup.MarginLayoutParams) binding.recyclerView.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.bottom_bar_height);
                    binding.bottomBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.bottom_bar_height);
                } else {
                    resetFilter();
                }
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // do nothing
            }
        });
        builder.show();
    }

    public void resetFilter() {
        // reset filter
        selectedRoomFilterString = "";
        filterType = 0;
        filterPattern = null;
        initList();
        // reset bottom filter info bar
        binding.filterType.setText(getString(R.string.no_Filter_Text));
        ((ViewGroup.MarginLayoutParams) binding.recyclerView.getLayoutParams()).bottomMargin = 0;
        binding.bottomBar.getLayoutParams().height = 0;
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
        initFilterInfoBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (! EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /*=== Fired if the user clicks on a delete button ===*/
    @Subscribe
    public void onDeleteMeeting(DeleteMeetingEvent event) {
        apiService.deleteMeeting(event.meeting);
        initList();
    }
}