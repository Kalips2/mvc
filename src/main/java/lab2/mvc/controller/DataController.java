package lab2.mvc.controller;

import lab2.mvc.service.DatabaseService;
import lombok.RequiredArgsConstructor;
import lab2.mvc.migrated.database.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mvc/rows")
@RequiredArgsConstructor
public class DataController {
    private final DatabaseService databaseService;
    public static final String REDIRECT_ROWS = "redirect:/mvc/rows";
    public static final String ROWS_VIEW = "data";
    public static final String RESULT = "result";

    @GetMapping
    public String getRowsPage(Model model) {
        return ROWS_VIEW;
    }

    @GetMapping("/select")
    public String selectRows(@RequestParam String tableName,
                             Model model) {
        String query = String.format("select * from %s", tableName);

        final var database = databaseService.getDatabase();
        Result result = database.query(query);

        model.addAttribute("rows", result);
        return ROWS_VIEW;
    }

    @PostMapping("/insert")
    public String insertRow(@RequestParam String columns,
                            @RequestParam String tableName,
                            @RequestParam String values,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        String query = String.format("insert into %s (%s) values (%s)", tableName, columns, values);

        final var database = databaseService.getDatabase();
        Result result = database.query(query);

        model.addAttribute(RESULT, result);
        if (result.getStatus() == Result.Status.FAIL) {
            redirectAttributes.addFlashAttribute("errorMessage", result.getReport());
            return "redirect:/mvc/rows";
        }

        return String.format("redirect:/mvc/rows/select?columns=%s&tableName=%s", columns, tableName);
    }

    @PostMapping("/delete")
    public String deleteRow(@RequestParam String tableName,
                            @RequestParam String condition,
                            Model model) {
        String query = String.format("delete from %s where %s", tableName, condition);
        final var database = databaseService.getDatabase();
        Result result = database.query(query);
        model.addAttribute(RESULT, result);
        return REDIRECT_ROWS;
    }
}
