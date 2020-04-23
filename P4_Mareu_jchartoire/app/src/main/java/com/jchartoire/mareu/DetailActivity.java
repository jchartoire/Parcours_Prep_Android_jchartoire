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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ViewSwitcher;

import com.google.android.material.snackbar.Snackbar;
import com.jchartoire.mareu.databinding.ActivityDetailBinding;
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
    private View textViewView, editTextView;

    // Detail layout content editText view
    private ImageView etvIvRoomColor;
    private Spinner spnRoom;
    private EditText etDescription, etMeetingTitle;
    private TextView tvClickableDate, tvClickableStartHour, tvClickableEndHour;
    private MultiAutoCompleteTextView mactvParticipants;
    private AutoCompleteTextView actvLeader;

    // Detail layout content textView view
    private ImageView tvvIvRoomColor;
    private TextView tvRoom, tvLeader, tvStartDate, tvStartHour, tvEndHour, tvParticipants, tvDescription, tvMeetingTitle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        /*=== Binding / findViewById ===*/
        //viewSwitcher content
        viewSwitcherBinding = binding.viewSwitcher;
        textViewView = findViewById(R.id.textviewView);
        editTextView = findViewById(R.id.edittextView);

        //Detail layout content editText view
        etMeetingTitle = binding.edittextView.etMeetingTitle;
        etvIvRoomColor = binding.edittextView.etvIvRoomColor;
        spnRoom = binding.edittextView.spnRoom;
        actvLeader = binding.edittextView.actvLeader;
        tvClickableDate = binding.edittextView.tvClickableDate;
        tvClickableStartHour = binding.edittextView.tvClickableStartHour;
        tvClickableEndHour = binding.edittextView.tvClickableEndHour;
        mactvParticipants = binding.edittextView.mactvParticipants;
        etDescription = binding.edittextView.etDescription;

        //Detail layout content textView view
        tvMeetingTitle = binding.textviewView.tvMeetingTitle;
        tvvIvRoomColor = binding.textviewView.tvvIvRoomColor;
        tvRoom = binding.textviewView.tvRoom;
        tvLeader = binding.textviewView.tvLeader;
        tvStartDate = binding.textviewView.tvStartDate;
        tvStartHour = binding.textviewView.tvStartHour;
        tvEndHour = binding.textviewView.tvEndHour;
        tvParticipants = binding.textviewView.tvParticipants;
        tvDescription = binding.textviewView.tvDescription;

        /*=== Get service ===*/
        apiService = DI.getNeighbourApiService();

        /*=== init meeting details ===*/
        initDetails();

        /*=== Set textView OnClickListener ===*/
        tvClickableDate.setOnClickListener(this);
        tvClickableStartHour.setOnClickListener(this);
        tvClickableEndHour.setOnClickListener(this);

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
            tvClickableDate.setText(dateFormatter.format(selectedStartDate));
            tvClickableStartHour.setText(timeFormatter.format(selectedStartDate));
            // set default meeting duration to 2 hours
            calendar.add(Calendar.HOUR_OF_DAY, 2);
            selectedEndDate = calendar.getTime();
            // update textView
            tvClickableEndHour.setText(timeFormatter.format(selectedEndDate));
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
        spnRoom.setAdapter(roomsAdapter);
        spnRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the value selected by the user
                selectedRoom = (Room) (parent.getSelectedItem());
                etvIvRoomColor.setColorFilter(selectedRoom.getColor());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /*=== MultiAutoCompleteTextView setup , set adapter to fill the data in suggestion list ===*/
        ArrayAdapter<User> mactvAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);    // set adapter to fill the
        // data in suggestion list
        mactvParticipants.setAdapter(mactvAdapter);
        mactvParticipants.setThreshold(1);    // set threshold value 1 that help us to start the searching from first character
        mactvParticipants.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());   // set tokenizer that distinguish the various substrings by comma

        /*=== AutoCompleteTextView setup, set adapter to fill the data in suggestion list ===*/
        AutoCompleteTextView actv_leader = actvLeader;
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
            tvClickableStartHour.setText(timeFormatter.format(selectedStartDate));
            calendar.add(Calendar.HOUR_OF_DAY, 1);
        } else if (pickerTag.equals("timePicker_end")) {
            calendar.setTime(selectedEndDate);
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
        }
        selectedEndDate = calendar.getTime();
        tvClickableEndHour.setText(timeFormatter.format(selectedEndDate));
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        // get initial StartDate and edit only Year, Month, Day
        calendar.setTime(selectedStartDate);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        selectedStartDate = calendar.getTime();
        tvClickableDate.setText(dateFormatter.format(selectedStartDate));
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
            viewSwitcherBinding.setDisplayedChild(viewSwitcherBinding.indexOfChild(editTextView));
            // setup visible menu buttons
            ok_settings.setVisible(true);
            edit_settings.setVisible(false);
        } else {
            // switch to TextView view
            viewSwitcherBinding.setDisplayedChild(viewSwitcherBinding.indexOfChild(textViewView));
            // setup visible menu buttons
            ok_settings.setVisible(false);
            edit_settings.setVisible(true);
        }
    }

    void saveMeetingDetails() {
        /*=== Get EditText content and save it to meeting ===*/
        meeting.setTitle(etMeetingTitle.getText().toString());
        meeting.setLeader(apiService.getUserByEmail(actvLeader.getText().toString()));
        String[] usersEmailList = mactvParticipants.getText().toString().split("\\s*,\\s*");
        List<User> usersList = new ArrayList<>();
        for (String email : usersEmailList) {
            usersList.add(apiService.getUserByEmail(email));
        }
        meeting.setUsers(usersList);
        meeting.setDescription(etDescription.getText().toString());
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
        tvMeetingTitle.setText(meeting.getTitle());
        tvLeader.setText(meeting.getLeader().getEmail());
        for (int j = 0; j < meeting.getUsers().size(); j++) {
            if (j == 0) {
                tvParticipants.setText(meeting.getUsers().get(j).getEmail());
            } else {
                tvParticipants.setText(getString(R.string.Comma_separator, tvParticipants.getText(), meeting.getUsers().get(j).getEmail()));
            }
        }
        tvDescription.setText(meeting.getDescription());
        tvRoom.setText(meeting.getRoom().getRoomName());
        tvStartDate.setText(dateFormatter.format(meeting.getStartDate()));
        tvStartHour.setText(timeFormatter.format(meeting.getStartDate()));
        tvEndHour.setText(timeFormatter.format(meeting.getEndDate()));
        tvvIvRoomColor.setColorFilter(meeting.getRoom().getColor());
    }

    void updateEditText() {
        /*=== update EditTextView content ===*/
        etMeetingTitle.setText(meeting.getTitle());
        actvLeader.setText(meeting.getLeader().toString());

        for (int j = 0; j < meeting.getUsers().size(); j++) {
            if (j == 0) {
                mactvParticipants.setText(meeting.getUsers().get(j).getEmail());
            } else {
                mactvParticipants.setText(getString(R.string.Comma_separator, mactvParticipants.getText(), meeting.getUsers().get(j).getEmail()));
            }
        }
        etDescription.setText(meeting.getDescription());

        /*=== Update the spinner selected item, matching the room of the meeting ===*/
        for (int i = 0; i < rooms.size(); i++) {
            if (roomsAdapter.getItem(i) == meeting.getRoom()) {
                spnRoom.setSelection(i);
                break;
            }
        }
        tvClickableDate.setText(dateFormatter.format(meeting.getStartDate()));
        tvClickableStartHour.setText(timeFormatter.format(meeting.getStartDate()));
        tvClickableEndHour.setText(timeFormatter.format(meeting.getEndDate()));
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
                if (viewSwitcherBinding.getCurrentView() == textViewView) {
                    updateEditText();
                    setEditionMode(true);
                }
                return true;

            case R.id.ok_settings:
                if (viewSwitcherBinding.getCurrentView() == editTextView) {
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
        DialogFragment newFragmentTime = new TimePickerFragment(this);
        DialogFragment newFragmentDate = new DatePickerFragment(this);

        switch (v.getId()) {
            case R.id.tv_clickable_date:
                pickerTag = "datePicker";
                newFragmentDate.show(getSupportFragmentManager(), pickerTag);
                break;
            case R.id.tv_clickable_start_hour:
                pickerTag = "timePicker_start";
                newFragmentTime.show(getSupportFragmentManager(), pickerTag);
                break;
            case R.id.tv_clickable_end_hour:
                pickerTag = "timePicker_end";
                newFragmentTime.show(getSupportFragmentManager(), pickerTag);
                break;
        }
    }

    public boolean editTextValidator() {
        if (TextUtils.isEmpty(etMeetingTitle.getText())) {
            Snackbar.make(viewSwitcherBinding.getCurrentView(), getResources().getString(R.string.title_missing),
                    Snackbar.LENGTH_LONG).setAction("Error", null).show();
            return true;
        }
        if (TextUtils.isEmpty(actvLeader.getText())) {
            Snackbar.make(viewSwitcherBinding.getCurrentView(), getResources().getString(R.string.leader_missing), Snackbar.LENGTH_LONG).setAction("Error",
                    null).show();
            return true;
        }
        if (TextUtils.isEmpty(mactvParticipants.getText())) {
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

    public void dialogConfirmation()
    {
        if (ok_settings.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.save_message));
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(!editTextValidator())
                    {
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

    /*=== Override onSupportNavigateUp is set to get back to previous tab, not the home tab, when touching the back button in appbar layout ===*/
    @Override
    public boolean onSupportNavigateUp() {
        dialogConfirmation();
        return true;
    }

    @Override
    public void onBackPressed() { //todo: je voudrais aussi avoir un handle sur le navigateUp
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
