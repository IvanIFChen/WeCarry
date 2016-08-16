package space.wecarry.wecarryapp;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;
import android.util.Log;

import java.util.ArrayList;

import space.wecarry.wecarryapp.item.TaskItem;
import space.wecarry.wecarryapp.sqlite.DBHelper;

public class DatabaseTest extends AndroidTestCase {
    private DBHelper dbHelper;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");
        dbHelper = new DBHelper(context);
    }

    @Override
    public void tearDown() throws Exception {
        dbHelper.close();
        super.tearDown();
    }

    public void testAddEntry(){
        // DOESN'T WORK.
        ArrayList<TaskItem> taskList = dbHelper.getAllTasks();
        for (TaskItem ti : taskList) {
            Log.d("getAllTasks", ti.getTitle() + " ");
        }
    }
}