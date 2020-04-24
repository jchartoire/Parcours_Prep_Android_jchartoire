package com.jchartoire.mareu;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;

import com.google.android.material.snackbar.Snackbar;
import com.jchartoire.mareu.databinding.ActivityDetailBinding;
import com.jchartoire.mareu.databinding.FragmentDetailContentEdittextBinding;
import com.jchartoire.mareu.databinding.FragmentDetailContentTextviewBinding;
import com.jchartoire.mareu.di.DI;
import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.model.User;
import com.jchartoire.mareu.service.ApiService;
import com.jchartoire.mareu.tools.DatePickerFragment;
import com.jchartoire.mareu.tools.TimePickerFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import static com.jchartoire.mareu.tools.DateUtils.DateInRange;
import static com.jchartoire.mareu.tools.DateUtils.dateFormatter;
import static com.jchartoire.mareu.tools.DateUtils.timeFormatter;

public class DetailActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener, View.OnClickListener {

    // layout viewSwitcher
    private ViewSwitcher viewSwitcherBinding;

    private MenuItem ok_settings, edit_settings;

    private String pickerTag;
    private ApiService apiService;
    private Room selectedRoom;
    private List<Room> rooms;
    private ArrayAdapter<Room> roomsAdapter;
    private Meeting meeting;
    private List<Meeting> meetings;
    private List<User> users;
    private Date selectedStartDate, selectedEndDate;
    private boolean createNewMeeting;

    private ActivityDetailBinding binding;
    private FragmentDetailContentEdittextBinding edittextViewBinding;
    private FragmentDetailContentTextviewBinding textviewViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*=== Binding ===*/
        //viewSwitcher content
        viewSwitcherBinding = binding.viewSwitcher;
        edittextViewBinding = binding.edittextView;
        textviewViewBinding = binding.textviewView;

        /*=== Get service ===*/
        apiService = DI.getNeighbourApiService();

        /*=== init meeting details ===*/
        initDetails();

        /*=== Set textView OnClickListener ===*/
        edittextViewBinding.tvClickableDate.setOnClickListener(this);
        edittextViewBinding.tvClickableStartHour.setOnClickListener(this);
        edittextViewBinding.tvClickableEndHour.setOnClickListener(this);

        /*=== setup all interactive text fields and datePickers ===*/
        widgetsSetup();
    }

    private void initDetails() {
        meetings = apiService.getMeetings();

        /*=== get the intent of the meeting sent from ItemRecyclerViewAdapter ===*/
        Intent intent = getIntent();
        long id = intent.getLongExtra("meetingId", 0);
        if (id != 0) {
            createNewMeeting = false;
            meeting = apiService.getMeetingById(id);

            /*=== Get start and end date of the meeting ===*/
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(meeting.getStartDate());
            selectedStartDate = calendar.getTime();
            calendar.setTime(meeting.getEndDate());
            selectedEndDate = calendar.getTime();
        } else {
            createNewMeeting = true;

            /*=== init new meeting ===*/
            meeting = new Meeting(System.currentTimeMillis(), "", null, null, null, null, null, null);

            /*=== init start and end date based on today's date ===*/
            //get the date of the day
            Calendar calendar = Calendar.getInstance();
            calendar.getTimeInMillis();
            // set default meeting at 10:00
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            calendar.set(Calendar.MINUTE, 0);
            selectedStartDate = calendar.getTime();
            // update textView
            edittextViewBinding.tvClickableDate.setText(dateFormatter.format(selectedStartDate));
            edittextViewBinding.tvClickableStartHour.setText(timeFormatter.format(selectedStartDate));
            // set default meeting duration to 2 hours
            calendar.add(Calendar.HOUR_OF_DAY, 2);
            selectedEndDate = calendar.getTime();
            // update textView
            edittextViewBinding.tvClickableEndHour.setText(timeFormatter.format(selectedEndDate));
            // save new date to the meeting
            meeting.setStartDate(selectedStartDate);
            meeting.setEndDate(selectedEndDate);
        }

        /*=== Get rooms and users lists ===*/
        rooms = apiService.getRooms();
        users = apiService.getUsers();
    }

    void widgetsSetup() {
        /*=== Rooms spinner setup===*/
        roomsAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, rooms);
        edittextViewBinding.spnRoom.setAdapter(roomsAdapter);
        edittextViewBinding.spnRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                selectedRoom = (Room) (parent.getSelectedItem());
                edittextViewBinding.etvIvRoomColor.setColorFilter(selectedRoom.getColor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*=== MultiAutoCompleteTextView setup , set adapter to fill the data in suggestion list ===*/
        ArrayAdapter<User> mactvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);    // set adapter to fill the
        // data in suggestion list
        edittextViewBinding.mactvParticipants.setAdapter(mactvAdapter);
        edittextViewBinding.mactvParticipants.setThreshold(1);    // set threshold value 1 that help us to start the searching from first character
        edittextViewBinding.mactvParticipants.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());   // set tokenizer that distinguish the various substrings by comma

        /*=== AutoCompleteTextView setup, set adapter to fill the data in suggestion list ===*/
        AutoCompleteTextView actv_leader = edittextViewBinding.actvLeader;
        ArrayAdapter<User> actvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, users);
        actv_leader.setAdapter(actvAdapter);
        // set threshold value that help us to start the searching from first character
        actv_leader.setThreshold(1);
        actv_leader.setValidator(new Validator());
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (pickerTag.equals("timePicker_start")) {
            calendar.setTime(selectedStartDate);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            selectedStartDate = calendar.getTime();
            edittextViewBinding.tvClickableStartHour.setText(timeFormatter.format(selectedStartDate));
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        } else if (pickerTag.equals("timePicker_end")) {
            calendar.setTime(selectedEndDate);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
        }
        selectedEndDate = calendar.getTime();
        edittextViewBinding.tvClickableEndHour.setText(timeFormatter.format(selectedEndDate));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        // get initial StartDate and edit only Year, Month, Day
        calendar.setTime(selectedStartDate);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        selectedStartDate = calendar.getTime();
        edittextViewBinding.tvClickableDate.setText(dateFormatter.format(selectedStartDate));
        // set EndDate same as StartDate, whithout modifying Hour and Minute end
        calendar.setTime(selectedEndDate);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        selectedEndDate = calendar.getTime();
    }

    void setEditionMode(boolean editionMode) {
        if (editionMode) {
            // switch to EditText view
            viewSwitcherBinding.setDisplayedChild(viewSwitcherBinding.indexOfChild(edittextViewBinding.getRoot()));
            // setup visible menu buttons
            ok_settings.setVisible(true);
            edit_settings.setVisible(false);
        } else {
            // switch to TextView view
            viewSwitcherBinding.setDisplayedChild(viewSwitcherBinding.indexOfChild(binding.textviewView.getRoot()));
            // setup visible menu buttons
            ok_settings.setVisible(false);
            edit_settings.setVisible(true);
        }
    }

    void saveMeetingDetails() {
        /*=== Get EditText content and save it to meeting ===*/
        meeting.setTitle(edittextViewBinding.etMeetingTitle.getText().toString());
        meeting.setLeader(apiService.getUserByEmail(edittextViewBinding.actvLeader.getText().toString()));
        String[] usersEmailList = edittextViewBinding.mactvParticipants.getText().toString().split("\\s*,\\s*");
        List<User> usersList = new ArrayList<>();
        for (String email : usersEmailList) {
            usersList.add(apiService.getUserByEmail(email));
        }
        meeting.setUsers(usersList);
        meeting.setDescription(edittextViewBinding.etDescription.getText().toString());
        meeting.setRoom(selectedRoom);
        meeting.setStartDate(selectedStartDate);
        meeting.setEndDate(selectedEndDate);

        /*=== Create a new meeting ===*/
        if (createNewMeeting) {
            apiService.createMeeting(meeting);
        }
    }

    void updateTextView() {
        /*=== Update TextView content ===*/
        textviewViewBinding.tvMeetingTitle.setText(meeting.getTitle());
        textviewViewBinding.tvLeader.setText(meeting.getLeader().getEmail());
        for (int j = 0; j < meeting.getUsers().size(); j++) {
            if (j == 0) {
                textviewViewBinding.tvParticipants.setText(meeting.getUsers().get(j).getEmail());
            } else {
                textviewViewBinding.tvParticipants.setText(getString(R.string.Comma_separator, textviewViewBinding.tvParticipants.getText(), meeting.getUsers().get(j).getEmail()));
            }
        }
        textviewViewBinding.tvDescription.setText(meeting.getDescription());
        textviewViewBinding.tvRoom.setText(meeting.getRoom().getRoomName());
        textviewViewBinding.tvStartDate.setText(dateFormatter.format(meeting.getStartDate()));
        textviewViewBinding.tvStartHour.setText(timeFormatter.format(meeting.getStartDate()));
        textviewViewBinding.tvEndHour.setText(timeFormatter.format(meeting.getEndDate()));
        textviewViewBinding.tvvIvRoomColor.setColorFilter(meeting.getRoom().getColor());
    }

    void updateEditText() {
        /*=== update EditTextView content ===*/
        edittextViewBinding.etMeetingTitle.setText(meeting.getTitle());
        edittextViewBinding.actvLeader.setText(meeting.getLeader().toString());

        for (int j = 0; j < meeting.getUsers().size(); j++) {
            if (j == 0) {
                edittextViewBinding.mactvParticipants.setText(meeting.getUsers().get(j).getEmail());
            } else {
                edittextViewBinding.mactvParticipants.setText(getString(R.string.Comma_separator, edittextViewBinding.mactvParticipants.getText(), meeting.getUsers().get(j).getEmail()));
            }
        }
        edittextViewBinding.etDescription.setText(meeting.getDescription());

        /*=== Update the spinner selected item, matching the room of the meeting ===*/
        for (int i = 0; i < rooms.size(); i++) {
            if (roomsAdapter.getItem(i) == meeting.getRoom()) {
                edittextViewBinding.spnRoom.setSelection(i);
                break;
            }
        }
        edittextViewBinding.tvClickableDate.setText(dateFormatter.format(meeting.getStartDate()));
        edittextViewBinding.tvClickableStartHour.setText(timeFormatter.format(meeting.getStartDate()));
        edittextViewBinding.tvClickableEndHour.setText(timeFormatter.format(meeting.getEndDate()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_detail, menu);
        ok_settings = menu.findItem(R.id.ok_settings);
        edit_settings = menu.findItem(R.id.edit_settings);
        if (createNewMeeting) {
            setEditionMode(true);
        } else {
            updateTextView();
            setEditionMode(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // action bar will automatically handle clicks on the Home/Up button, so long as you specify a parent activity in AndroidManifest.xml.
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.edit_settings:
                if (viewSwitcherBinding.getCurrentView() == textviewViewBinding.getRoot()) {
                    updateEditText();
                    setEditionMode(true);
                }
                return true;

            case R.id.ok_settings:
                if (viewSwitcherBinding.getCurrentView() == edittextViewBinding.getRoot()) {
                    // remove focus from editText
                    this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    if (editTextValidator()) {
                        return true;
                    }
                    if (createNewMeeting) {
                        saveMeetingDetails();
                        finish();
                    } else {
                        saveMeetingDetails();
                        updateTextView();
                        setEditionMode(false);
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*=== TextView date and time OnClick setup ===*/
    @Override
    public void onClick(View v) {
        DialogFragment newFragmentTime;
        DialogFragment newFragmentDate;

        switch (v.getId()) {
            case R.id.tv_clickable_date:
                pickerTag = "datePicker";
                newFragmentDate = new DatePickerFragment(this, selectedStartDate);
                newFragmentDate.show(getSupportFragmentManager(), pickerTag);
                break;
            case R.id.tv_clickable_start_hour:
                pickerTag = "timePicker_start";
                newFragmentTime = new TimePickerFragment(this, selectedStartDate);
                newFragmentTime.show(getSupportFragmentManager(), pickerTag);
                break;
            case R.id.tv_clickable_end_hour:
                pickerTag = "timePicker_end";
                newFragmentTime = new TimePickerFragment(this, selectedEndDate);
                newFragmentTime.show(getSupportFragmentManager(), pickerTag);
                break;
        }
    }

    public boolean editTextValidator() {
        if (TextUtils.isEmpty(edittextViewBinding.etMeetingTitle.getText())) {
            Snackbar.make(viewSwitcherBinding.getCurrentView(), getResources().getString(R.string.title_missing),
                    Snackbar.LENGTH_LONG).setAction("Error", null).show();
            return true;
        }
        if (TextUtils.isEmpty(edittextViewBinding.actvLeader.getText())) {
            Snackbar.make(viewSwitcherBinding.getCurrentView(), getResources().getString(R.string.leader_missing), Snackbar.LENGTH_LONG).setAction("Error",
                    null).show();
            return true;
        }
        if (TextUtils.isEmpty(edittextViewBinding.mactvParticipants.getText())) {
            Snackbar.make(viewSwitcherBinding.getCurrentView(), getResources().getString(R.string.participants_missing), Snackbar.LENGTH_LONG).setAction("Error",
                    null).show();
            return true;
        }
        for (Meeting meetingToTest : meetings) {
            Date TestStartDate = meetingToTest.getStartDate();
            Date testEndDate = meetingToTest.getEndDate();
            if (meetingToTest != meeting && meetingToTest.getRoom() == selectedRoom && DateInRange(TestStartDate, testEndDate, selectedStartDate, selectedEndDate)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getResources().getString(R.string.wrong_start_date));
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.show();
                return true;
            }
        }
        return false;
    }

    public void dialogConfirmation() {
        if (ok_settings.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.save_message));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (! editTextValidator()) {
                        saveMeetingDetails();
                        finish();
                    }
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.negative_button_text), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            builder.show();
        } else {
            finish();
        }
    }

    /*=== handle back button in menu bar ===*/
    @Override
    public boolean onSupportNavigateUp() {
        dialogConfirmation();
        return true;
    }

    @Override
    public void onBackPressed() {
        dialogConfirmation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    /*=== AutoCompleteTextView validator ===*/
    class Validator implements AutoCompleteTextView.Validator, View.OnFocusChangeListener {

        @Override
        public boolean isValid(CharSequence text) {
            String[] valideUsersList = new String[users.size()];
            for (int i = 0; i < users.size(); i++) {
                valideUsersList[i] = users.get(i).getEmail();
            }
            Arrays.sort(valideUsersList);
            return Arrays.asList(valideUsersList).contains(text.toString());
        }

        @Override
        public CharSequence fixText(CharSequence invalidText) {
            Snackbar.make(viewSwitcherBinding.getCurrentView(), getResources().getString(R.string.wrong_email), Snackbar.LENGTH_LONG).setAction(
                    "Erreur", null).show();
            return "";
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getId() == R.id.actv_leader && ! hasFocus) {
                ((AutoCompleteTextView) v).performValidation();
            }
        }
    }
}