package kz.bitlab.taskManager.G136.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Connection;
import java.sql.PreparedStatement;

@Controller
@RequiredArgsConstructor
public class ProjectController {
    private final Connection connection;

    @GetMapping("/add-project")
    public String addProject() {
        return "add-project";
    }

    @PostMapping("/add-project")
    public String addProjectPost(
            @RequestParam(name = "name") String name,
            @RequestParam(name = "description") String description
    ) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into projects (name, description) values (?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
