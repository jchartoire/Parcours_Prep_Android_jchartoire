package com.jchartoire.mareu;

import android.content.Context;

import com.jchartoire.mareu.di.DI;
import com.jchartoire.mareu.model.Meeting;
import com.jchartoire.mareu.model.Room;
import com.jchartoire.mareu.model.User;
import com.jchartoire.mareu.service.ApiService;
import com.jchartoire.mareu.service.DummyGenerator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
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
    public ActivityTestRule<ItemListActivity> activityTestRule =
            new ActivityTestRule(ItemListActivity.class);
    private ItemListActivity itemListActivity;
    private ApiService apiService;
    private List<Meeting> dummyMeetings = DummyGenerator.dummyMeetings;
    private List<User> dummyUsers = DummyGenerator.dummyUsers;
    private List<Room> dummyRooms = DummyGenerator.dummyRooms;

    // Convenience helper
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    @Before
    public void setUp() {
        itemListActivity = activityTestRule.getActivity();
        assertThat(itemListActivity, notNullValue());
        apiService = DI.getApiService();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.jchartoire.mareu", appContext.getPackageName());
    }

    /**
     * We ensure that our recyclerview is displaying at least one item
     */
    @Test
    public void RecyclerView_shouldNotBeEmpty() {
        String dummyTitle = dummyMeetings.get(0).getTitle();
        // check RecyclerView is not empty
        onView(withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(1)));
        // check item is not void and contains meeting title
        onView(withRecyclerView(R.id.recycler_view).atPosition(0)).check(matches(hasDescendant(withText(containsString(dummyTitle)))));

        // perform click on first item
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        //onView(withRecyclerView(R.id.recycler_view).atPosition(0)).perform(click());
    }

    @Test
    public void myMeetingsList_deleteAction_shouldRemoveItem() {
        int ITEMS_COUNT = apiService.getMeetings().size();
        // Given : We remove the element at position 2
        //  onView(allOf(withId(R.id.recycler_view), isDisplayed())).check(withItemCount(Matchers.is(expectedCount)(ITEMS_COUNT));
        // When perform a click on a delete icon
        // Then : the number of element is 11
        //  onView(allOf(withId(R.id.recycler_view), isDisplayed())).check(withItemCount(ITEMS_COUNT - 1));
    }

    /**
     * When we click on an item, a new detail page should launching
     */
    @Test
    public void MeetingsList_clickItem_shouldLaunchDetail_andShowNeihboursName() {
        // we store the name of the meeting at position 4 of the list
        Meeting meeting = apiService.getMeetings().get(3);
        String meetingName = meeting.getTitle();
        // perform a click on the 4th item displayed
        onView(allOf(withId(R.id.recycler_view), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        // Then check if the textViewName field in detail activity contains meeting's name
        onView(allOf(withId(R.id.tv_meeting_title), isDisplayed())).check(matches(withText(meetingName)));
    }

    /**
     * When we click on an item, a new detail page should launching
     */
    @Test
    public void DetailActivity_newMeetinEditor() {
        onView(withId(R.id.add_fab)).perform(click());
        onView(withId(R.id.et_meeting_title)).perform(ViewActions.typeText("Meeting Test"), closeSoftKeyboard());
        onView(withId(R.id.mactv_Participants)).perform(ViewActions.typeText("amy.hall@lamzone.com"), closeSoftKeyboard());
        onView(withId(R.id.actv_leader)).perform(ViewActions.typeText("adam.cook@lamzone.com"), closeSoftKeyboard());
        onView(withId(R.id.ok_settings)).perform(click());
    }
}
