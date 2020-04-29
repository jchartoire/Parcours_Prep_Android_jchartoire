package com.jchartoire.mareu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.DatePicker;

import com.jchartoire.mareu.di.DI;
import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.service.ApiService;
import com.jchartoire.mareu.service.DummyGenerator;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.jchartoire.mareu.tools.DateUtils.dateFormatter;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    @Rule
    public ActivityTestRule<ItemListActivity> activityTestRule = new ActivityTestRule<>(ItemListActivity.class);
    private ItemListActivity itemListActivity;
    private ApiService apiService;
    private List<Meeting> dummyMeetings = DummyGenerator.dummyMeetings;

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    public void resetDummyList() {
        apiService.getMeetings().clear();
        apiService.getMeetings().addAll(dummyMeetings);
    }

    @Before
    public void setUp() {
        itemListActivity = activityTestRule.getActivity();
        assertThat(itemListActivity, notNullValue());
        apiService = DI.getApiService();
        // restore dummy generated list in case of previous test modifications
        resetDummyList();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.jchartoire.mareu", appContext.getPackageName());
        //TODO: le test crée par défaut est utile ?
    }

    /**
     * We ensure that our recyclerview is displaying at least one item with correct info
     */
    @Test
    public void recyclerView_shouldNotBeEmpty() {
        String dummyTitle = apiService.getMeetings().get(0).getTitle();
        // check RecyclerView is not empty
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(1)));
        // check item is not void and contains meeting title
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText(containsString(dummyTitle)))));
    }

    /**
     * We ensure that when we delete an item, recyclerview is updating and displays one item less
     */
    @Test
    public void meetingsList_deleteAction_shouldRemoveItem() {
        // count meetings elements
        int meetingCount = apiService.getMeetings().size();
        // compare meetings count and recyclerView item count
        RecyclerView recyclerView = activityTestRule.getActivity().findViewById(R.id.recycler_view);
        assertEquals(meetingCount, Objects.requireNonNull(recyclerView.getAdapter()).getItemCount());
        // click on delete button of the first item
        onView(allOf(withId(R.id.iv_delete_button), withParent(allOf(withId(R.id.item),
                childAtPosition(withId(R.id.recycler_view), 0))), isDisplayed())).perform(click());
        // check that item list contains one item less
        assertEquals(meetingCount - 1, Objects.requireNonNull(recyclerView.getAdapter()).getItemCount());
    }

    /**
     * When we click on an item, a new detail page should launching
     */
    @Test
    public void meetingsList_clickItem_shouldLaunchDetail_andShowNeighboursName() {
        // store the name of the meeting at first position of the list
        Meeting meeting = apiService.getMeetings().get(0);
        String meetingName = meeting.getTitle();
        // perform a click on first item
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // Then check if the textViewName field in detail activity contains meeting's name
        onView(allOf(withId(R.id.tv_meeting_title), isDisplayed())).check(matches(withText(meetingName)));
    }

    /**
     * When we click on add fab, a new void detail page should launching. We first check that meeting collision detector works.
     * Then we change the room so that the meeting can be created.
     */
    @Test
    public void detailActivity_newMeetingEditor() {
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeInMillis();
        // fill up required fields
        onView(withId(R.id.add_fab)).perform(click());
        onView(withId(R.id.et_meeting_title)).perform(scrollTo(), ViewActions.typeText("New Meeting Test"), closeSoftKeyboard());
        onView(withId(R.id.actv_leader)).perform(scrollTo(), ViewActions.typeText("adam.cook@lamzone.com"), closeSoftKeyboard());
        onView(withId(R.id.mactv_Participants)).perform(scrollTo(), ViewActions.typeText("amy.hall@lamzone.com"), closeSoftKeyboard());
        // Set a date on the date picker dialog (same as first dummyMeeting, today, 8:00, room 1)
        onView(withId(R.id.tv_clickable_date)).perform(scrollTo(), click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        // try to save the new meeting
        onView(withId(R.id.ok_settings)).perform(click());
        // Meeting exist so error dialog show up. We click OK
        onView(withText(R.string.meeting_already_exist)).check(matches(isDisplayed()));
        onView(withText("OK")).perform(click());
        // change the room to room 10 (room 5 to 10 aren't used in dummyGenerator)
        onView(withId(R.id.spn_room)).perform(scrollTo(), click());
        onData(anything()).atPosition(9).perform(click());
        onView(withId(R.id.ok_settings)).perform(click());
        // check that new meeting is showing up in meetings list
        onView(withId(R.id.recycler_view)).check(matches(hasDescendant(withText(containsString("New Meeting Test")))));
    }

    /**
     * check if filter show up only matching room
     */
    @Test
    public void roomFilter_show_only_matching_room() {
        // count the number of meetings registered in room 1
        int itemCount = 0;
        for (int i = 0; i < apiService.getMeetings().size(); i++) {
            if (apiService.getMeetings().get(i).getRoom().toString().equals("Salle 1")) {
                itemCount++;
            }
        }
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(1)));
        // set filter by Room 1
        openActionBarOverflowOrOptionsMenu(itemListActivity.getBaseContext());
        onView(withText(R.string.filter_room_item)).perform(click());
        onView(withText("OK")).perform(click());
        // assert that there is only room matching meetings in list
        RecyclerView recyclerView = activityTestRule.getActivity().findViewById(R.id.recycler_view);
        assertEquals(itemCount, Objects.requireNonNull(recyclerView.getAdapter()).getItemCount());
        // set filter by Room 10
        openActionBarOverflowOrOptionsMenu(itemListActivity.getBaseContext());
        onView(withText(R.string.filter_room_item)).perform(click());
        onView(allOf(childAtPosition(childAtPosition(withId(android.R.id.custom), 0), 0), isDisplayed())).perform(click());
        // select 9th item (room 10)
        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(9).perform(click());
        onView(withText("OK")).perform(click());
        // assert that there is no room matching in list
        assertEquals(0, Objects.requireNonNull(recyclerView.getAdapter()).getItemCount());
    }

    /**
     * check if filter show up only matching date
     */
    @Test
    public void dateFilter_show_only_matching_date() {
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeInMillis();
        String currentDate = dateFormatter.format(calendar.getTimeInMillis());
        int itemCount = 0;
        for (int i = 0; i < apiService.getMeetings().size(); i++) {
            if (dateFormatter.format(apiService.getMeetings().get(i).getStartDate()).equals(currentDate)) {
                itemCount++;
            }
        }
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(1)));
        // set filter by Date
        openActionBarOverflowOrOptionsMenu(itemListActivity.getBaseContext());
        onView(withText(R.string.filter_date_item)).perform(click());
        // Set a date on the date picker dialog
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        // assert that there is only current date matching in list
        RecyclerView recyclerView = activityTestRule.getActivity().findViewById(R.id.recycler_view);
        assertEquals(itemCount, Objects.requireNonNull(recyclerView.getAdapter()).getItemCount());
    }

    /**
     * When filtering is active, check if new meeting is showing up in filtered list
     */
    @Test
    public void filteredList_shouldBeUpdated() {
        Calendar calendar = Calendar.getInstance();
        calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        // set filter by Date
        openActionBarOverflowOrOptionsMenu(itemListActivity.getBaseContext());
        onView(withText(R.string.filter_date_item)).perform(click());
        // Set a date on the date picker dialog
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(0)));
        // create a new meeting
        onView(withId(R.id.add_fab)).perform(click());
        onView(withId(R.id.et_meeting_title)).perform(scrollTo(), ViewActions.typeText("Meeting Test"), closeSoftKeyboard());
        onView(withId(R.id.actv_leader)).perform(scrollTo(), ViewActions.typeText("adam.cook@lamzone.com"), closeSoftKeyboard());
        onView(withId(R.id.mactv_Participants)).perform(scrollTo(), ViewActions.typeText("amy.hall@lamzone.com"), closeSoftKeyboard());
        // Set a date on the date picker dialog
        onView(withId(R.id.tv_clickable_date)).perform(scrollTo(), click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.ok_settings)).perform(click());
        onView(withId(R.id.recycler_view)).check(matches(hasChildCount(1)));
    }
}