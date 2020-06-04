package com.cleanup.todoc.database.dao;

import com.cleanup.todoc.model.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TaskDao {

    /*=== Read Task from database ===*/
    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllTasks();

    /*=== Create Task in database ===*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    /*=== Delete Task from database ===*/
    @Delete
    void deleteTask(Task task);

    @Query("DELETE FROM task_table")
    void deleteAll();
}
