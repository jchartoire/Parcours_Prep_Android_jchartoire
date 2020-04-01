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
import static androidx.test.espresso.contrib.ViewPagerActions.scrollRight;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static com.openclassrooms.entrevoisins.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.IsNull.notNullValue;
/**
 * Test class for list of neighbours
 */
@RunWith(AndroidJUnit4.class)
public class NeighboursListTest {

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
        // scroll to the position that needs to be matched and click on it.
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(matches(hasMinimumChildCount(1)));
    }

    /**
     * When we delete an item, there is one less item in the list
     */
    @Test
    public void myNeighboursList_deleteAction_shouldRemoveItem() {
        int ITEMS_COUNT = mApiService.getNeighbours().size();
        // Given : We remove the element at position 2
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(ITEMS_COUNT));
        // When perform a click on a delete icon
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(1, new DeleteViewAction()));
        // Then : the number of element is 11
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(ITEMS_COUNT - 1));
    }

    /**
     * When we click on an item, a new detail page should launching
     */
    @Test
    public void myNeighboursList_clickItem_shouldLaunchDetail_andShowNeihboursName() {
        // we store the name of the neighbour at position 4 of the list
        Neighbour neighbour = mApiService.getNeighbours().get(3);
        String neighbourName = neighbour.getName();
        // perform a click on the 4th item displayed
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
        // Then check if the textViewName field in detail activity contains neighbour's name
        onView(allOf(withId(R.id.textViewName), isDisplayed())).check(matches(withText(neighbourName)));
    }

    /**
     * The favorite list only contains neighbours who are marked as favorite
     */
    @Test
    public void myFavoritesNeighboursList_onlyContainsFavorites() {
        // first, check there is no favorite at the beginning
        onView(allOf(withId(R.id.list_neighbours), withParentIndex(1))).check(withItemCount(0));
        // going to detail and add to favorite
        onView(allOf(withId(R.id.list_neighbours), withParentIndex(0))).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.favoriteFAB)).perform(click());
        // going to favorite tab, and check there is just one new favorite added
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());
        onView(allOf(withText(R.string.tab_favorites_title),isDescendantOfA(withId(R.id.tabs)))).perform(click());
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(1));
        // reset favorite to false
        mApiService.setNeighbourAsFavorite(mApiService.getNeighbours().get(0), false);
    }

    /**
     * When we delete an item on favorite list, the item is removed in favorite AND full list
     */
    @Test
    public void myFavoritesNeighboursList_deleteAction_shouldRemoveNeighbourEverywhere() {
        int neighboursCount = mApiService.getNeighbours().size();

        /*** Add one neighbour to favorite, in order to delete it from the favorite tab ***/
        // going to detail and add to favorite
        onView(allOf(withId(R.id.list_neighbours), withParentIndex(0))).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.favoriteFAB)).perform(click());
        // going to favorite tab, and check there is just one new favorite added
        onView(withContentDescription(R.string.abc_action_bar_up_description)).perform(click());

        /*** Count before delete ***/
        // check that neighbours list contains 12 elements
        onView(allOf(withId(R.id.list_neighbours), withParentIndex(0))).check(withItemCount(neighboursCount));
        // going to favorite tab
        onView(allOf(withText(R.string.tab_favorites_title),isDescendantOfA(withId(R.id.tabs)))).perform(click());
        // and check that favorite list contains 1 element
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(1));

        /*** perform a click on a delete icon in favorite list ***/
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        // check favorite list contains 0 item after delete

        /*** Count after delete ***/
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(0));
        // going to neighbour tab
        onView(allOf(withText(R.string.tab_neighbour_title),isDescendantOfA(withId(R.id.tabs)))).perform(click());
        // check that neighbours list contains 11 elements
        onView(allOf(withId(R.id.list_neighbours), isDisplayed())).check(withItemCount(neighboursCount - 1));
    }
}