package com.cleanup.todoc.repositories;

import android.app.Application;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ProjectRepository {

    private LiveData<List<Project>> allProjects;

    public ProjectRepository(Application application) {
        TodocDatabase database = TodocDatabase.getDatabase(application);
        ProjectDao projectDao = database.projectDao();
        allProjects = projectDao.getAllProjects();
    }

    public LiveData<List<Project>> getAllProjects() {
        return allProjects;
    }
}
