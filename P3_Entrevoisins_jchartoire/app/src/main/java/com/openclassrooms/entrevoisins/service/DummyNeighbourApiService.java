package com.openclassrooms.entrevoisins.service;

import com.openclassrooms.entrevoisins.model.Neighbour;

import java.util.ArrayList;
import java.util.List;

/**
 * Dummy mock for the Api
 */
public class DummyNeighbourApiService implements  NeighbourApiService {

    private List<Neighbour> neighbours = DummyNeighbourGenerator.generateNeighbours();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getNeighbours() {
        return neighbours;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Neighbour getNeighbourById(long Id)
    {
        for (Neighbour neighbour : neighbours) {
            if (neighbour.getId() == Id) {
                return neighbour;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Neighbour> getFavoritesNeighbours() {
        List<Neighbour> favoritesNeighboursList = new ArrayList<>();
        for (Neighbour neighbour : neighbours) {
            if(neighbour.getFavoriteStatus()) {
                favoritesNeighboursList.add(neighbour);
            }
        }
        return favoritesNeighboursList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteNeighbour(Neighbour neighbour) {
        neighbours.remove(neighbour);
    }

    /**
     * {@inheritDoc}
     * @param neighbour
     */
    @Override
    public void createNeighbour(Neighbour neighbour) {
        neighbours.add(neighbour);
    }
}
