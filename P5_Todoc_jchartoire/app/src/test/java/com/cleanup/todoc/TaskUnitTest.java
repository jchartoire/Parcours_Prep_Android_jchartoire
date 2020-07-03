package com.cleanup.todoc;

import android.content.Context;
import android.os.Build;

import com.cleanup.todoc.database.TodocDatabase;
import com.cleanup.todoc.database.dao.ProjectDao;
import com.cleanup.todoc.database.dao.TaskDao;
import com.cleanup.todoc.model.Project;
import com.cleanup.todoc.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.LOLLIPOP}, manifest = Config.NONE)
public class TaskUnitTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private TaskDao taskDao;
    private ProjectDao projectDao;
    private TodocDatabase todocDatabase;

    @Before
    public void initDb() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        this.todocDatabase = Room.inMemoryDatabaseBuilder(context, TodocDatabase.class).allowMainThreadQueries().build();
        taskDao = this.todocDatabase.taskDao();
        projectDao = this.todocDatabase.projectDao();
        projectDao.insertProject(new Project(1L, "Projet Tartampion", 0xFF4183CC));
        projectDao.insertProject(new Project(2L, "Projet Lucidia", 0xFF119D58));
        projectDao.insertProject(new Project(3L, "Projet Circus", 0xFFFFD455));
    }

    @After
    public void closeDb() {
        taskDao.deleteAll();
        this.todocDatabase.close();
    }

    @Test
    @DisplayName("insert a task in database, read the task inserted, then delete it")
    public void insert_read_and_delete_task() throws InterruptedException {
        List<Task> tasksList = LiveDataTestUtil.getValue(taskDao.getAllTasks());
        assertEquals(0, tasksList.size());

        Task task = new Task(1L, 1L, "task", System.currentTimeMillis());
        taskDao.insertTask(task);
        tasksList = LiveDataTestUtil.getValue(taskDao.getAllTasks());
        assertEquals(1, tasksList.size());
        assertEquals("task", tasksList.get(0).getName());

        taskDao.deleteTask(task);
        tasksList = LiveDataTestUtil.getValue(taskDao.getAllTasks());
        assertEquals(0, tasksList.size());
    }

    @Test
    @DisplayName("insert a task with unexisting project should fail")
    public void insert_task_with_unexisting_project() {
        assertThrows(android.database.sqlite.SQLiteConstraintException.class,
                () -> {
                    Task task = new Task(1L, 4L, "task", System.currentTimeMillis());
                    taskDao.insertTask(task);
                });
    }

    @Test
    @DisplayName("read projects in database")
    public void read_project() throws InterruptedException {
        List<Project> projectsList = LiveDataTestUtil.getValue(projectDao.getAllProjects());
        assertEquals(3, projectsList.size());
        assertEquals("Projet Circus", projectsList.get(0).getName());
        assertEquals("Projet Lucidia", projectsList.get(1).getName());
        assertEquals("Projet Tartampion", projectsList.get(2).getName());
    }

    @Test
    @DisplayName("sort method by task name AZ")
    public void az_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskAZComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task2);
    }

    @Test
    @DisplayName("sort method by task name ZA")
    public void za_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskZAComparator());

        assertSame(tasks.get(0), task2);
        assertSame(tasks.get(1), task3);
        assertSame(tasks.get(2), task1);
    }

    @Test
    @DisplayName("sort method by creation date, recent first")
    public void recent_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskRecentComparator());

        assertSame(tasks.get(0), task3);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task1);
    }

    @Test
    @DisplayName("sort method by creation date, old first")
    public void old_comparator() {
        final Task task1 = new Task(1, 1, "aaa", 123);
        final Task task2 = new Task(2, 2, "zzz", 124);
        final Task task3 = new Task(3, 3, "hhh", 125);

        final ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        Collections.sort(tasks, new Task.TaskOldComparator());

        assertSame(tasks.get(0), task1);
        assertSame(tasks.get(1), task2);
        assertSame(tasks.get(2), task3);
    }
}