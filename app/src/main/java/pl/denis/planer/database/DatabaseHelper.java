package pl.denis.planer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Collection;
import java.util.LinkedList;

import pl.denis.planer.model.project.Project;
import pl.denis.planer.model.task.Task;
import pl.denis.planer.model.user.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    /**
     * current app state
     */
    private static Context context;
    public static void setContext(Context newContext){
        context = newContext;
    }
    /**
     * name of the database
     */
    private static final String DATABASE_NAME = "PlanedDatabase.db";
    /**
     * version of the database
     */
    private static final int DATABASE_VERSION = 4;


    public DatabaseHelper() {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * gives last  used id in given table
     * @param tableName name of the table who's id we are checking
     * @return  Primary key of the last element in given table;
     * -1 if there is no elements or the table did not exists
     */
    public int getFirstFreeIndex(String tableName){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT seq  FROM sqlite_sequence " +
                "WHERE name = \"" + tableName + "\"; ",
                null);
        if(res.getCount()==0){
            return 0;
        }
        res.moveToFirst();
        return res.getInt(0)+1;
    }

    /**
     * Resets tables in database
     */
    public void resetDatabase(){
        SQLiteDatabase db  = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + ProjectTableInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UserTableInfo.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TaskTableInfo.TABLE_NAME);
        onCreate(db);
    }


    public boolean checkIfLoginIsUsed(String login){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT *  FROM " + UserTableInfo.TABLE_NAME +
                        " WHERE " + UserTableInfo.LOGIN + " = '" +  login + "'; ",
                null);
        if(res.getCount()!=0) {
            return true;
        }
        return  false;
    }

    /**
     * Adds project to the database projects table
     * @param project Project to add
     */
    public void addProject(Project project){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProjectTableInfo.TITLE,project.getTitle());
        contentValues.put(ProjectTableInfo.DESCRIPTION,project.getDescription());
        contentValues.put(ProjectTableInfo.USER,project.getUser().getId());
        contentValues.put(ProjectTableInfo.COMPLETE, project.getCompleted() ? 1 : 0);
        sqLiteDatabase.insert(ProjectTableInfo.TABLE_NAME,null,contentValues);
    }

    /**
     * Adds task to the database tasks table
     * @param task  Task to add
     */
    public void addTask(Task task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskTableInfo.TITLE,task.getTitle());
        contentValues.put(TaskTableInfo.DESCRIPTION,task.getDescription());
        contentValues.put(TaskTableInfo.USER,task.getUser().getId());
        contentValues.put(TaskTableInfo.PROJECT,task.getProject().getId());
        contentValues.put(TaskTableInfo.PRIORITY,task.getPriority());
        contentValues.put(ProjectTableInfo.COMPLETE, task.getCompleted() ? 1 : 0);
        sqLiteDatabase.insert(TaskTableInfo.TABLE_NAME,null,contentValues);
    }

    /**
     * updates all fields in given project
     * @param project project to update in database
     */
    public void updateProject(Project project){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE " + ProjectTableInfo.TABLE_NAME +
                        " SET " + ProjectTableInfo.TITLE + " = '" + project.getTitle() + "', " +
                        ProjectTableInfo.DESCRIPTION + " = '" + project.getDescription() + "', " +
                        ProjectTableInfo.COMPLETE + " = " + (project.getCompleted() ? 1 : 0) + " " +
                        " WHERE " +
                        ProjectTableInfo.ID + " = " + project.getId() + ";"
        );
    }

    /**
     * updates all fields in given task
     * @param task task to update
     */
    public void updateTask(Task task){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("UPDATE " + TaskTableInfo.TABLE_NAME +
                        " SET " + TaskTableInfo.TITLE + " = '" + task.getTitle() + "', " +
                        TaskTableInfo.DESCRIPTION + " = '" + task.getDescription() + "', " +
                        TaskTableInfo.PRIORITY + " = " + task.getPriority() + ", " +
                        TaskTableInfo.COMPLETE + " = " + (task.getCompleted() ? 1 : 0) + " " +
                        " WHERE " +
                        TaskTableInfo.ID + " = " + task.getId() + ";"
        );
    }

    /**
     * gets all project of given user from the database
     * @param user  user  who's projects you want
     * @return collections of projects
     */
    private Collection<Project> getProjects(User user){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT *  FROM " + ProjectTableInfo.TABLE_NAME +
                        " WHERE " + ProjectTableInfo.USER + "= \"" +  user.getId() + "\"; ",
                null);
        if(res.getCount()==0){
            return new LinkedList<>();
        }
        Collection<Project> collection = new LinkedList<Project>();
        while(res.moveToNext()) {
            collection.add(Project.getExistingProject(
                    res.getInt(res.getColumnIndexOrThrow(ProjectTableInfo.ID)),
                    user,
                    res.getString(res.getColumnIndexOrThrow(ProjectTableInfo.TITLE)),
                    res.getString(res.getColumnIndexOrThrow(ProjectTableInfo.DESCRIPTION)),
                    res.getInt(res.getColumnIndexOrThrow(ProjectTableInfo.COMPLETE)) != 0
            ));
        }
        for (Project project:collection) {
            Collection<Task> tasks = getTasks(project);
            for (Task task:tasks) {
                project.addTask(task);
            }
        }
        return collection;
    }

    /**
     * gets all tasks in given project
     * @param project  project which task's we are getting
     * @return task in given project
     */
    private Collection<Task> getTasks(Project project){
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT *  FROM " + TaskTableInfo.TABLE_NAME +
                        " WHERE " + TaskTableInfo.PROJECT + "= \"" +  project.getId() + "\"; ",
                null);
        if(res.getCount()==0){
            return new LinkedList<>();
        }
        Collection<Task> collection = new LinkedList<Task>();
        while(res.moveToNext()) {
            collection.add(Task.getExistingTask(
                    res.getInt(res.getColumnIndexOrThrow(TaskTableInfo.ID)),
                    project,
                    project.getUser(),
                    res.getString(res.getColumnIndexOrThrow(TaskTableInfo.TITLE)),
                    res.getString(res.getColumnIndexOrThrow(TaskTableInfo.DESCRIPTION)),
                    res.getInt(res.getColumnIndexOrThrow(TaskTableInfo.PRIORITY)),
                    res.getInt(res.getColumnIndexOrThrow(TaskTableInfo.COMPLETE)) != 0
            ));
        }
        return collection;
    }


    /**
     * Create tables in the Database
     * @param db database to initialize
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query  =
                "CREATE TABLE " + TaskTableInfo.TABLE_NAME + " ( " +
                        TaskTableInfo.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TaskTableInfo.TITLE + " TEXT, " +
                        TaskTableInfo.DESCRIPTION + " TEXT, " +
                        TaskTableInfo.USER + " INTEGER, " +
                        TaskTableInfo.PROJECT + " INTEGER, " +
                        TaskTableInfo.PRIORITY + " INTEGER, " +
                        TaskTableInfo.COMPLETE + " INTEGER);";
        db.execSQL(query);
        query  =
                "CREATE TABLE " + UserTableInfo.TABLE_NAME + " ( " +
                        UserTableInfo.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        UserTableInfo.EMAIL_ADDRESS + " TEXT UNIQUE, " +
                        UserTableInfo.LOGIN + " TEXT UNIQUE, " +
                        UserTableInfo.PASSWORD + " TEXT);";
        db.execSQL(query);
        query  =
                "CREATE TABLE " + ProjectTableInfo.TABLE_NAME + " ( " +
                        ProjectTableInfo.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ProjectTableInfo.TITLE + " TEXT, " +
                        ProjectTableInfo.DESCRIPTION + " TEXT, " +
                        ProjectTableInfo.USER + " INTEGER, " +
                        ProjectTableInfo.COMPLETE + " INTEGER);";
        db.execSQL(query);
    }

    /**
     * Upgrades database to new a veriosn
     * @param db database to update
     * @param oldVersion not used
     * @param newVersion not used
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS projects");
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

}
