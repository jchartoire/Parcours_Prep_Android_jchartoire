package com.cleanup.todoc.database;

import android.content.Context;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    /*=== SINGLETON ===*/
    private static TodocDatabase INSTANCE;
    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                // Populate the database in the background.
                ProjectDao projectDao = INSTANCE.projectDao();
                TaskDao taskDao = INSTANCE.taskDao();
                projectDao.insertProject(new Project(1L, "Projet Tartampion", 0xFF4183CC));
                projectDao.insertProject(new Project(2L, "Projet Lucidia", 0xFF119D58));
                projectDao.insertProject(new Project(3L, "Projet Circus", 0xFFFFD455));

                taskDao.insertTask(new Task(1L,1L, "test1", System.currentTimeMillis()));
                taskDao.insertTask(new Task(2L,2L, "test2", System.currentTimeMillis()));
                taskDao.insertTask(new Task(3L,3L, "test3", System.currentTimeMillis()));
                taskDao.insertTask(new Task(4L,3L, "test4", System.currentTimeMillis()));
            });
        }
    };

    public static TodocDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TodocDatabase.class, "TodocDatabase.db")
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /*=== DAO ===*/
    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();
}