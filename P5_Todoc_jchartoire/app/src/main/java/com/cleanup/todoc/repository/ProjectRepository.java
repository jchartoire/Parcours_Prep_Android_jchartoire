package com.cleanup.todoc.repository;

import android.app.Application;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.model.Project;

import java.util.List;

import androidx.lifecycle.LiveData;

public class ProjectRepository {

    private static LiveData<List<Project>> allProjects;

    public ProjectRepository(Application application) {
        TodocDatabase database = TodocDatabase.getDatabase(application);
        ProjectDao projectDao = database.projectDao();
        allProjects = projectDao.getAllProjects();
    }

    public static LiveData<List<Project>> getAllProjects() {
        return allProjects;
    }
}
