package pl.denis.planer.model.project;

import pl.denis.planer.database.DatabaseHelper;

public class SQLiteProject extends Project {
    DatabaseHelper databaseHelper= new DatabaseHelper();

    SQLiteProject(){
        super();
    }



}
