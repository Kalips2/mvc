package lab2.mvc.migrated.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class DatabaseReader {
    private final String filePath;
    private static Database database;

    public static Database getDatabase() {
        return database;
    }

    public static void setDatabase(Database database) {
        DatabaseReader.database = database;
    }

    public DatabaseReader(String filePath) {
        this.filePath = filePath;
    }

    public Database read() throws FileNotFoundException, JsonSyntaxException, JsonIOException {
        FileReader reader = new FileReader(filePath);
        Gson gson = new GsonBuilder().create();
        Database parsedDB = gson.fromJson(reader, Database.class);
        setDatabase(parsedDB);
        return parsedDB;
    }
}
