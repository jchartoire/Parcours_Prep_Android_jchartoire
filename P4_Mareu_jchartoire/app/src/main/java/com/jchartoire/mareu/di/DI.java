package com.jchartoire.mareu.di;

import com.jchartoire.mareu.service.ApiService;
import com.jchartoire.mareu.service.DummyApiService;

/**
 * Dependency injector to get instance of services
 */
public class DI {

    private static ApiService service = new DummyApiService();

    /**
     * Get an instance on @{@link ApiService}
     *
     * @return return the dummy ApiService
     */
    public static ApiService getApiService() {
        return service;
    }

    /**
     * Get always a new instance on @{@link ApiService}. Useful for tests, so we ensure the context is clean.
     *
     * @return return a new instance of the dummy ApiService
     */
    public static ApiService getNewInstanceApiService() {
        return new DummyApiService();
    }
}
