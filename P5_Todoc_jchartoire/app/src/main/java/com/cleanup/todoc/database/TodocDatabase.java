package com.cleanup.todoc.database;

import android.content.Context;

import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class TodocDatabase extends RoomDatabase {

    static final Executor executor = Executors.newSingleThreadExecutor();

    /*=== SINGLETON ===*/
    private static TodocDatabase INSTANCE;
    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            executor.execute(() -> {
                // Populate the database in the background.
                ProjectDao projectDao = INSTANCE.projectDao();
                TaskDao taskDao = INSTANCE.taskDao();
                projectDao.insertProject(new Project(1, "Projet Tartampion", 0xFF4183CC));
                projectDao.insertProject(new Project(2, "Projet Lucidia", 0xFF119D58));
                projectDao.insertProject(new Project(3, "Projet Circus", 0xFFFFD455));

                taskDao.insertTask(new Task(0, 1, "testA", System.currentTimeMillis()));
                taskDao.insertTask(new Task(0, 2, "testB", System.currentTimeMillis()));
                taskDao.insertTask(new Task(0, 3, "testC", System.currentTimeMillis()));
                taskDao.insertTask(new Task(0, 3, "testD", System.currentTimeMillis()));
            });
        }
    };

    public static TodocDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TodocDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        TodocDatabase.class, "TodocDatabase.db")
                        .addCallback(roomCallback)
                        .build();
            }
        }
        return INSTANCE;
    }

    /*=== DAO ===*/
    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();
}