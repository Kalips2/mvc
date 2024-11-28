package lab2.mvc.service;

import lab2.mvc.migrated.database.Database;
import lab2.mvc.migrated.database.DatabaseReader;
import lab2.mvc.service.DatabaseService;
import org.springframework.stereotype.Service;

@Service
public class DatabaseServiceImpl implements DatabaseService {
    @Override
    public Database getDatabase() {
        var database = DatabaseReader.getDatabase();
        if (database == null) {
            throw new RuntimeException("No database");
        }
        return database;
    }
}
