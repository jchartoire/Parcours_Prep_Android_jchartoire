package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.di.DI;
import com.openclassrooms.entrevoisins.model.Neighbour;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Unit test on Neighbour service
 */
@RunWith(JUnit4.class)
public class NeighbourServiceTest {

    private NeighbourApiService service;
    List<Neighbour> expectedNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;


    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void getNeighbourWithSuccess() {
        Neighbour neighbour = expectedNeighbours.get(1);
        Neighbour neighbourToGet = service.getNeighbourById(neighbour.getId());
        assertEquals(neighbour, neighbourToGet);
    }

    @Test
    public void getNeighbourAvatarWithSuccess() {
        Neighbour neighbour = expectedNeighbours.get(0);
        assertNotNull(neighbour.getAvatarUrl());
    }

    @Test
    public void getNeighbourNameWithSuccess() {
        Neighbour dummyNeighbour = expectedNeighbours.get(1);
        String dummyneighbourName = dummyNeighbour.getName(); // get name of the second dummy neighbour of the generated list
        Neighbour neighbourToGet = service.getNeighbours().get(1);
        String neighbourToGetNameExpected = neighbourToGet.getName(); // get name of the second neighbour from the service
        assertEquals(dummyneighbourName, neighbourToGetNameExpected);
    }

    @Test
    public void setFavoriteWithSuccess() {
        /* get the second neighbour by the service and set it as favorite */
        Neighbour neighbour = service.getNeighbours().get(1);
        neighbour.setAsFavorite(true);
        /* assert that the favorite state is saved and is gettable */
        assertTrue(neighbour.getFavoriteStatus());
    }

    @Test
    public void getFavoritesWithSuccess() {
        assertEquals(0,service.getFavoritesNeighbours().size());      //first, assert that favorite list is empty
        Neighbour neighbour = service.getNeighbours().get(0);
        neighbour.setAsFavorite(true); //add a neighbour to favorite list
        List<Neighbour> favoritesNeighbours = service.getFavoritesNeighbours();
        assertEquals(1, favoritesNeighbours.size());     //then, assert that favorite list is filled with 1 favorite
    }
}
