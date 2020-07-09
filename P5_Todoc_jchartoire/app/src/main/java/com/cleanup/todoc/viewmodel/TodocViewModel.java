package com.cleanup.todoc.viewmodel;

import android.app.Application;

import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;
import com.cleanup.todoc.repositories.ProjectRepository;
import com.cleanup.todoc.repositories.TaskRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TodocViewModel extends AndroidViewModel {

    /*=== REPOSITORY ===*/
    private TaskRepository taskRepository;
    private ProjectRepository projectRepository;

    public TodocViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        projectRepository = new ProjectRepository(application);
    }

    public void insertTask(Task task) {
        taskRepository.insertTask(task);
    }

    public void deleteTask(Task task) {
        taskRepository.deleteTask(task);
    }

    public LiveData<List<Task>> getAllTasks() {
        return taskRepository.getAllTasks();
    }

    public LiveData<List<Project>> getAllProjects() {
        return projectRepository.getAllProjects();
    }
}