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
import android.widget.Spinner;

import com.jchartoire.mareu.databinding.ActivityListBinding;
import com.jchartoire.mareu.di.DI;
import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.service.ApiService;
import com.jchartoire.mareu.tools.DatePickerFragment;
import com.jchartoire.mareu.tools.filterParams;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.jchartoire.mareu.tools.DateUtils.dateFormatter;

public class ItemListActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private List<Meeting> meetings;
    private List<Room> rooms;
    private ApiService apiService;
    private ActivityListBinding binding;
    ItemRecyclerViewAdapter adapter;
    Spinner spnRoom;
    View spinnerView;
    String selectedRoomFilterString;
    ArrayAdapter<Room> roomsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setSupportActionBar(binding.toolbar);
        Drawable drawable = getDrawable(R.drawable.ic_filter_list_black_24dp);
        binding.toolbar.setOverflowIcon(drawable);

        apiService = DI.getNeighbourApiService();

        initList();
        initFilterInfoBar();

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

        initRecyclerView();
        spinnerRoomSetup();
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
        DialogFragment newFragmentDate = new DatePickerFragment(this);

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

    private void initRecyclerView() {
        adapter = new ItemRecyclerViewAdapter(meetings);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setFilter(filterParams.getFilterType(), filterParams.getFilterPattern());
        adapter.notifyDataSetChanged();
    }

    /*=== Init the List of meetings ===*/
    private void initList() {
        meetings = apiService.getMeetings();
        Collections.sort(meetings, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting m1, Meeting m2) {
                return m1.getStartDate().compareTo(m2.getStartDate());
            }
        });
        rooms = apiService.getRooms();
    }

    /*=== Init the List of meetings ===*/
    private void initFilterInfoBar() {
        // reset bottom filter info bar
        int filterType = filterParams.getFilterType();
        String filterPattern = filterParams.getFilterPattern();
        switch (filterType) {
            case 0:
                // reset bottom filter info bar
                binding.filterType.setText(R.string.no_Filter_Text);
                ((ViewGroup.MarginLayoutParams) binding.itemLayout.getLayoutParams()).bottomMargin = 0;
                binding.bottomBar.getLayoutParams().height = 0;
                break;

            case 1:
                // set bottom filter info bar
                binding.filterType.setText(String.format("%s%s", getString(R.string.filter_By_Date_Text), filterPattern));
                ((ViewGroup.MarginLayoutParams) binding.itemLayout.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.bottom_bar_height);
                binding.bottomBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.bottom_bar_height);
                break;

            case 2:
                // set bottom filter info bar
                binding.filterType.setText(String.format("%s%s", getString(R.string.filter_text), filterPattern));
                ((ViewGroup.MarginLayoutParams) binding.itemLayout.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.bottom_bar_height);
                binding.bottomBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.bottom_bar_height);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initList();
        initFilterInfoBar();
        adapter.notifyDataSetChanged();
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // save current filter
        filterParams.setFilterType(1);
        Calendar calendar = Calendar.getInstance();
        // get initial today's date and edit only Year, Month, Day
        calendar.set(year, month, day);
        String dateSet = dateFormatter.format(calendar.getTime());
        filterParams.setFilterPattern(dateSet);
        adapter.setFilter(filterParams.getFilterType(), filterParams.getFilterPattern());
        adapter.notifyDataSetChanged();
        // set bottom filter info bar
        binding.filterType.setText(String.format("%s%s", getString(R.string.filter_By_Date_Text), dateSet));
        ((ViewGroup.MarginLayoutParams) binding.itemLayout.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.bottom_bar_height);
        binding.bottomBar.getLayoutParams().height = (int) getResources().getDimension(R.dimen.bottom_bar_height);
    }

    void spinnerRoomSetup() {
        spinnerView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
        spnRoom = spinnerView.findViewById(R.id.spinner_room);
        roomsAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, rooms);
        roomsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnRoom.setAdapter(roomsAdapter);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (spinnerView.getParent() != null)
            ((ViewGroup) spinnerView.getParent()).removeView(spinnerView);
        builder.setView(spinnerView);
        builder.setTitle("Choisissez la salle : ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (selectedRoomFilterString != null) {
                    adapter.setFilter(2, selectedRoomFilterString);
                    filterParams.setFilterType(2);
                    filterParams.setFilterPattern(selectedRoomFilterString);
                    adapter.notifyDataSetChanged();
                    // set bottom filter info bar
                    binding.filterType.setText(String.format("%s%s", getString(R.string.filter_text), selectedRoomFilterString));
                    ((ViewGroup.MarginLayoutParams) binding.itemLayout.getLayoutParams()).bottomMargin = (int) getResources().getDimension(R.dimen.bottom_bar_height);
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
        adapter.setFilter(0, null);
        filterParams.setFilterType(0);
        filterParams.setFilterPattern(null);
        adapter.notifyDataSetChanged();
        // reset bottom filter info bar
        binding.filterType.setText(getString(R.string.no_Filter_Text));
        ((ViewGroup.MarginLayoutParams) binding.itemLayout.getLayoutParams()).bottomMargin = 0;
        binding.bottomBar.getLayoutParams().height = 0;
    }
}
