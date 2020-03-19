
package com.openclassrooms.entrevoisins.neighbour_list;

import com.openclassrooms.entrevoisins.R;
import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;
import com.openclassrooms.entrevoisins.service.NeighbourApiService;
import com.openclassrooms.entrevoisins.ui.neighbour_list.ListNeighbourActivity;
import com.openclassrooms.entrevoisins.utils.DeleteViewAction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;
/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

    // This is fixed
    private static int ITEMS_COUNT = 12;

    private ListNeighbourActivity mActivity;
    private NeighbourApiService mApiService;

    @Rule
    public ActivityTestRule<ListNeighbourActivity> mActivityRule =
            new ActivityTestRule(ListNeighbourActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();
        assertThat(mActivity, notNullValue());
        mApiService = DI.getNeighbourApiService();
    }

    /**
     * We ensure that our recyclerview is displaying at least one item
     */
    @Test
    public void myNeighboursList_shouldNotBeEmpty() {
        //First scroll to the position that needs to be matched and click on it.
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, there is one less item in the list
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        // Given : We remove the element at position 2
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(ITEMS_COUNT - 1));
    }

    @Test
    public void myNeighboursList_clickItem_shouldLaunchDetail_andShowNeihboursName() {
        // Given : we store the name of the neighbour at position 4 of the list
        Neighbour neighbour = mApiService.getNeighbours().get(3);
        String neighbourName = neighbour.getName();
        // When perform a click on the 4th item on view
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        // Then : The detail activity is launching and neighbour's name in the detail content is the same as neighbour's name previously selected
        onView(allOf(withId(R.id.textViewName), withText(neighbourName), isDisplayed())).check(matches(isDisplayed()));
    }

    /**
     * The favorite list only contains neighbours who are marked as favorite
     */
    @Test
    public void myFavoritesNeighboursList_onlyContainsFavorites() {
        // Given : there is no favorite at the beginning
        onView(allOf(withId(R.id.list_neighbours), withParentIndex(1))).check(withItemCount(0));
        // When going to detail and add to favorite
        onView(allOf(withId(R.id.list_neighbours), withParentIndex(0))).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.favoriteFAB)).perform(click());
        // Then : when going to favorite tab, there is just one new favorite added
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(allOf(withText("Favoris"),isDescendantOfA(withId(R.id.tabs)))).perform(click());
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(1));
    }
}