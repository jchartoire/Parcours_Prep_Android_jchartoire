package com.jchartoire.mareu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.jchartoire.mareu.di.DI;
import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.model.User;
import com.jchartoire.mareu.service.ApiService;

import org.threeten.bp.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {

    // layout viewSwitcher
    ViewSwitcher viewSwitcher;
    View textViewView, editTextView;

    // Detail layout content editText view
    ImageView etvIvRoomColor;
    Spinner spnRoom;
    EditText etLeader, etDescription, etMeetingTitle;
    TextView tvClickableDate, tvClickableStartHour, tvClickableEndHour;
    MultiAutoCompleteTextView etParticipants;

    // Detail layout content textView view
    ImageView tvvIvRoomColor;
    TextView tvRoom, tvLeader, tvStartDate, tvStartHour, tvEndHour, tvParticipants, tvDescription, tvMeetingTitle;

    MenuItem ok_settings, edit_settings;

    private ApiService apiService;
    private Meeting meeting;
    private List<Meeting> meetings;
    private List<Room> rooms;
    private List<User> users;
    ArrayAdapter<Room> roomsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        apiService = DI.getNeighbourApiService();

        /* get the intent of the meeting sent from ItemListActivity */
        Intent intent = getIntent();
        int id = intent.getIntExtra("meetingId", 0);
        if (id != 0) {
            meeting = apiService.getMeetingById(id);
        }
        rooms = apiService.getRooms();
        users = apiService.getUsers();

        findViewByIdUpdate();

        /* Rooms spinner setup */
        roomsAdapter = new ArrayAdapter<Room>(this, R.layout.support_simple_spinner_dropdown_item, rooms);
        spnRoom.setAdapter(roomsAdapter);
        spnRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                Room room = (Room) parent.getSelectedItem();
                System.out.println(room.getRoomName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /* MultiAutoCompleteTextView */

        //dummy emails
        String[] emails = {"employé_1@boite.fr"};


        MultiAutoCompleteTextView simpleMultiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.et_participants);
        ArrayAdapter<User> mactvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);    // set adapter to fill the
        // data in suggestion list
        simpleMultiAutoCompleteTextView.setAdapter(mactvAdapter);
        simpleMultiAutoCompleteTextView.setThreshold(1);    // set threshold value 1 that help us to start the searching from first character
        simpleMultiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());   // set tokenizer that distinguish the various substrings by comma

        /* DatePicker configuration */
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };
    }

    void findViewByIdUpdate() {
        viewSwitcher = findViewById(R.id.viewSwitcher);
        textViewView = findViewById(R.id.textviewView);
        editTextView = findViewById(R.id.edittextView);

        // Detail layout content editText view
        etMeetingTitle = findViewById(R.id.et_meeting_title);
        etvIvRoomColor = findViewById(R.id.etv_iv_room_color);
        spnRoom = findViewById(R.id.spn_room);
        etLeader = findViewById(R.id.et_leader);
        tvClickableDate = findViewById(R.id.tv_clickable_date);
        tvClickableStartHour = findViewById(R.id.tv_clickable_start_hour);
        tvClickableEndHour = findViewById(R.id.tv_clickable_end_hour);
        etParticipants = findViewById(R.id.et_participants);
        etDescription = findViewById(R.id.et_description);

        // Detail layout content textView view
        tvMeetingTitle = findViewById(R.id.tv_meeting_title);
        tvvIvRoomColor = findViewById(R.id.tvv_iv_room_color);
        tvRoom = findViewById(R.id.tv_room);
        tvLeader = findViewById(R.id.tv_leader);
        tvStartDate = findViewById(R.id.tv_start_date);
        tvStartHour = findViewById(R.id.tv_start_hour);
        tvEndHour = findViewById(R.id.tv_end_hour);
        tvParticipants = findViewById(R.id.tv_participants);
        tvDescription = findViewById(R.id.tv_description);
    }

    void updateMeetingDetails() {
        if (meeting != null) {
            DateTimeFormatter TimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");

            tvMeetingTitle.setText(meeting.getTitle());
            tvLeader.setText(meeting.getLeader());
            for (int j = 0; j < meeting.getUsers().size(); j++) {
                if (j == 0) {
                    tvParticipants.setText(meeting.getUsers().get(j).getEmail());
                } else {
                    tvParticipants.setText(tvParticipants.getText() + ", " + meeting.getUsers().get(j).getEmail());
                }
            }
            tvDescription.setText(meeting.getDescription());
            etDescription.setText(meeting.getDescription());
            tvRoom.setText(meeting.getRoom().getRoomName());

            /* Update the spinner selected item, matching the room of the meeting  */
            for (int i = 0; i < rooms.size(); i++) {
                if (roomsAdapter.getItem(i) == meeting.getRoom()) {
                    spnRoom.setSelection(i);
                    break;
                }
            }

            tvStartDate.setText(meeting.getStartDate().format(dateFormatter));
            tvStartHour.setText(meeting.getStartDate().format(TimeFormatter));
            tvEndHour.setText(meeting.getEndDate().format(TimeFormatter));
            tvvIvRoomColor.setColorFilter(meeting.getRoom().getColor());
            setEditionMode(false);
        } else {
            setEditionMode(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_detail, menu);
        ok_settings = menu.findItem(R.id.ok_settings);
        edit_settings = menu.findItem(R.id.edit_settings);

        updateMeetingDetails();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();

        switch (itemId) {
            case R.id.edit_settings:
                if (viewSwitcher.getCurrentView() == textViewView) {
                    //todo: ajouter fonction pour mettre à jour les textview et l'API ici (ou le faire dans le oncreate de DetailActivity ??)
                    setEditionMode(true);
                }
                return true;

            case R.id.ok_settings:
                if (viewSwitcher.getCurrentView() == editTextView) {
                    if (meeting != null) {
                        setEditionMode(false);
                        //todo: ajouter fonction pour mettre à jour les textview et l'API ici
                    } else {
                        //todo: ajouter fonction pour mettre à jour les textview et l'API ici
                        finish();
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void setEditionMode(boolean editionMode) {
        if (editionMode == true) {
            // On transforme les champs en edittext
            viewSwitcher.setDisplayedChild(viewSwitcher.indexOfChild(editTextView));
            // paramétrage des boutons menu d'édition
            ok_settings.setVisible(true);
            edit_settings.setVisible(false);
        } else {
            // On transforme les champs en textview
            viewSwitcher.setDisplayedChild(viewSwitcher.indexOfChild(textViewView));
            // paramétrage des boutons menu d'édition
            ok_settings.setVisible(false);
            edit_settings.setVisible(true);
        }
    }

    /* TextView date and time OnClick setup */
    @OnClick({R.id.tv_clickable_date, R.id.tv_clickable_start_hour, R.id.tv_clickable_end_hour})
    public void onViewClicked(View view) {
        DialogFragment newFragmentTime = new TimePickerFragment();
        DialogFragment newFragmentDate = new DatePickerFragment();

        switch (view.getId()) {
            case R.id.tv_clickable_date:
                newFragmentDate.show(getSupportFragmentManager(), "datePicker");
                break;
            case R.id.tv_clickable_start_hour:
                newFragmentTime.show(getSupportFragmentManager(), "timePicker_start");
                break;
            case R.id.tv_clickable_end_hour:
                newFragmentTime.show(getSupportFragmentManager(), "timePicker_end");
                break;
        }
    }

    /**
     * Override onSupportNavigateUp is set to finish activity, when touching the back button in appbar
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
