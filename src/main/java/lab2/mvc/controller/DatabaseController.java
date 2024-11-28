package lab2.mvc.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import lombok.extern.slf4j.Slf4j;
import lab2.mvc.migrated.database.Database;
import lab2.mvc.migrated.database.DatabaseReader;
import lab2.mvc.migrated.database.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class DatabaseController {
    private Database database;

    private Path tempDir;

    public DatabaseController() {
        try {
            tempDir = Files.createTempDirectory("tempUploads");
            log.info("Temporary directory created at: " + tempDir.toString());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @GetMapping("/")
    public String queryForm(@RequestParam(name = "name", required = false, defaultValue = "guest") String name,
                            Model model) {
        model.addAttribute("name", name);
        return "database";
    }

    @GetMapping("/query")
    public String queryContent(@RequestParam(name = "name", required = false, defaultValue = "guest") String name,
                               Model model) {
        model.addAttribute("name", name);
        return "queries";
    }

    @PostMapping(value = "/database", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    public Result query(@RequestParam(name = "query") String query) {
        log.info(query);
        return database.query(query);
    }

    @PostMapping("/createFile")
    public ResponseEntity<String> createFile(@RequestParam("filePathCreated") String filePathCreated) {
        if (filePathCreated == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        if (!filePathCreated.endsWith(".json")) {
            filePathCreated += ".json";
        }

        File newDatabaseFile = new File(filePathCreated);
        System.out.println(newDatabaseFile.getAbsolutePath());

        if (!newDatabaseFile.exists()) {
            try {
                newDatabaseFile.createNewFile();
                this.database = new Database(filePathCreated);
                this.database.save();
                this.database = new DatabaseReader(filePathCreated).read();
                return ResponseEntity.status(HttpStatus.OK).body("File created successfully: " + filePathCreated);
            } catch (IOException e) {
                log.error("File creation failed", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File creation failed");            }
        } else {
            log.info("File already exists!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File already exists!");
        }
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }
        try {
            Path tempFilePath = Files.createTempFile(tempDir, "uploaded_", "_" + file.getOriginalFilename());

            Files.copy(file.getInputStream(), tempFilePath, StandardCopyOption.REPLACE_EXISTING);

            log.info("File uploaded to: " + tempFilePath);
            this.database = new DatabaseReader(tempFilePath.toFile().getAbsolutePath()).read();
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully: " + tempFilePath);
        } catch (IOException e) {
            log.error("File upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed");
        }
    }
}
