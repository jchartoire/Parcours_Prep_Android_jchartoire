package com.cleanup.todoc.database.dao;

import com.cleanup.todoc.model.Project;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ProjectDao {

    @Insert
    void insertProject(Project project);

    @Query("SELECT * FROM project_table ORDER BY id DESC")
    LiveData<List<Project>> getAllProjects();
}
