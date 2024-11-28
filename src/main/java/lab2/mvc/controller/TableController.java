package lab2.mvc.controller;

import lab2.mvc.migrated.database.Result;
import lab2.mvc.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/mvc")
@RequiredArgsConstructor
public class TableController {
    public static final String RESULT = "result";
    public static final String TABLES = "tables";
    private final DatabaseService databaseService;

    @GetMapping("/tables")
    public String listTables(Model model) {
        final var database = databaseService.getDatabase();
        Result result = database.query("list tables");
        model.addAttribute(TABLES, result);
        return TABLES;
    }

    @PostMapping("/tables/create")
    public String createTable(@RequestParam String tableName,
                              @RequestParam String columns,
                              Model model) {
        final var database = databaseService.getDatabase();
        Result result = database.query(String.format("create table %s (%s)", tableName, columns));
        model.addAttribute(RESULT, result);
        return String.format("redirect:/mvc/tables");
    }

    @PostMapping("/tables/delete")
    public String dropTable(@RequestParam String tableName,
                            Model model) {
        final var database = databaseService.getDatabase();
        Result result = database.query(String.format("delete table %s", tableName));
        model.addAttribute(RESULT, result);
        return String.format("redirect:/mvc/tables");
    }
}
