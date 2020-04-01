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
    List<Neighbour> dummyNeighbours = DummyNeighbourGenerator.DUMMY_NEIGHBOURS;


    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
    }

    @Test
    public void getNeighboursWithSuccess() {
        List<Neighbour> neighbours = service.getNeighbours();
        assertThat(neighbours, IsIterableContainingInAnyOrder.containsInAnyOrder(dummyNeighbours.toArray()));
    }

    @Test
    public void deleteNeighbourWithSuccess() {
        Neighbour neighbourToDelete = service.getNeighbours().get(0);
        service.deleteNeighbour(neighbourToDelete);
        assertFalse(service.getNeighbours().contains(neighbourToDelete));
    }

    @Test
    public void getSpecificNeighbourWithSuccess() {
        /* get a specific neighbour from the list of Neighbours by the service */
        Neighbour neighbourToGet = dummyNeighbours.get(1);
        Neighbour neighbour = service.getNeighbourById(neighbourToGet.getId());
        assertEquals(neighbour, neighbourToGet);
    }

    @Test
    public void getNeighbourAvatarWithSuccess() {
        /* assert that avatar's url we get by the service is not null */
        Neighbour neighbour = service.getNeighbours().get(0);
        assertNotNull(neighbour.getAvatarUrl());
    }

    @Test
    public void getNeighbourNameWithSuccess() {
        /* get the name of the second dummy neighbour of the generated list */
        Neighbour dummyNeighbour = dummyNeighbours.get(1);
        String dummyNeighbourName = dummyNeighbour.getName();
        Neighbour neighbourToGet = service.getNeighbours().get(1);
        /* get the name of the second neighbour by the service */
        String neighbourToGetNameExpected = neighbourToGet.getName();
        assertEquals(dummyNeighbourName, neighbourToGetNameExpected);
    }

    @Test
    public void setFavoriteWithSuccess() {
        /* get a specific neighbour from the list of Neighbours by the service */
        Neighbour neighbourToGet = dummyNeighbours.get(1);
        Neighbour neighbour = service.getNeighbourById(neighbourToGet.getId());
        /* get the first neighbour by the service and set it as favorite */
        service.setNeighbourAsFavorite(neighbour,true);
        /* assert that the favorite state is saved and is gettable from DummyNeighbourApiService*/
        assertTrue(service.getNeighbourFavoriteStatus(neighbourToGet));
    }

    @Test
    public void getFavoritesWithSuccess() {
        /* first, assert that favorite list is empty */
        assertEquals(0,service.getFavoritesNeighbours().size());
        /* add a neighbour to favorite list */
        Neighbour neighbour = service.getNeighbours().get(0);
        neighbour.setAsFavorite(true);
        List<Neighbour> favoritesNeighbours = service.getFavoritesNeighbours();
        /* then, assert that favorite list is filled with 1 favorite */
        assertEquals(1, favoritesNeighbours.size());
    }
}