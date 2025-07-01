package kz.bitlab.taskManager.G136.controller;

import kz.bitlab.taskManager.G136.model.Project;
import kz.bitlab.taskManager.G136.model.Task;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TaskController {
    private final Connection connection;

    @GetMapping("/")
    public String start(Model model,
                        @RequestParam(name = "name", required = false) String filterName) {
        List<Task> tasks = new ArrayList<>();
        List<Project> projects = new ArrayList<>();
        //Новый код по задаче
        //Еще один код
        //Еще один код
        //Другой код

        try {
            PreparedStatement preparedStatement = null;
            if (filterName == null) {
                preparedStatement = connection.prepareStatement("" +
                        "select t.id as task_id, t.name as task_name,t.description, t.deadline_date, t.is_completed, p.id as project_id, p.name as project_name\n" +
                        "from tasks t\n" +
                        "join projects p on t.project_id=p.id;");
            } else {
                preparedStatement = connection.prepareStatement("" +
                        "SELECT t.id as task_id, t.name as task_name,t.description, t.deadline_date, t.is_completed, p.id as project_id, p.name as project_name\n" +
                        "from tasks t join projects p on t.project_id=p.id\n" +
                        "where lower(t.name) like lower(?)");
                preparedStatement.setString(1, "%" + filterName + "%");
                model.addAttribute("filter", filterName);
            }

            PreparedStatement preparedStatement2 = connection.prepareStatement("Select * from projects");

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Task task = Task.builder()
                        .id(resultSet.getLong("task_id"))
                        .name(resultSet.getString("task_name"))
                        .description(resultSet.getString("description"))
                        .deadlineDate(resultSet.getDate("deadline_date").toLocalDate())
                        .isCompleted(resultSet.getBoolean("is_completed"))
                        .project(Project.builder()
                                .id(resultSet.getLong("project_id"))
                                .name(resultSet.getString("project_name"))
                                .build())
                        .build();
                tasks.add(task);
            }
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            while (resultSet2.next()) {
                Project project = Project.builder()
                        .id(resultSet2.getLong("id"))
                        .name(resultSet2.getString("name"))
                        .description(resultSet2.getString("description"))
                        .build();

                projects.add(project);
            }
            preparedStatement.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        model.addAttribute("tasks", tasks);
        model.addAttribute("projects", projects);
        return "task-main";
    }

    @PostMapping("/add-task")
    public String addTask(@RequestParam(name = "name") String name,
                          @RequestParam(name = "description") String description,
                          @RequestParam(name = "projectId") Long project_id,
                          @RequestParam(name = "date") LocalDate date) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tasks(name, description, deadline_date, is_completed, project_id) values (?,?,?,?,?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDate(3, Date.valueOf(date));
            preparedStatement.setBoolean(4, false);
            preparedStatement.setLong(5, project_id);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/task-view/{id}")
    public String taskView(@PathVariable Long id, Model model) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("" +
                    "SELECT t.id as task_id, t.name as task_name,t.description, t.deadline_date, t.is_completed, p.id as project_id, p.name as project_name\n" +
                    "from tasks t\n" +
                    "join projects p on t.project_id=p.id " +
                    "where t.id=?");
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Task task = Task.builder()
                        .id(resultSet.getLong("task_id"))
                        .name(resultSet.getString("task_name"))
                        .description(resultSet.getString("description"))
                        .deadlineDate(resultSet.getDate("deadline_date").toLocalDate())
                        .isCompleted(resultSet.getBoolean("is_completed"))
                        .project(Project.builder()
                                .id(resultSet.getLong("project_id"))
                                .name(resultSet.getString("project_name"))
                                .build())
                        .build();
                model.addAttribute("task", task);


                List<Project> projects = new ArrayList<>();
                PreparedStatement preparedStatement2 = connection.prepareStatement("Select * from projects");

                ResultSet resultSet2 = preparedStatement2.executeQuery();
                while (resultSet2.next()) {
                    Project project = Project.builder()
                            .id(resultSet2.getLong("id"))
                            .name(resultSet2.getString("name"))
                            .description(resultSet2.getString("description"))
                            .build();

                    projects.add(project);
                }
                model.addAttribute("projects", projects);
            } else {
                return "404";
            }
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "task-edit";
    }

    @PostMapping("/edit-task")
    public String editTask(@RequestParam(name = "id") Long id,
                           @RequestParam(name = "name") String name,
                           @RequestParam(name = "description") String description,
                           @RequestParam(name = "date") LocalDate date,
                           @RequestParam(name = "projectId") Long projectId,
                           @RequestParam(name = "isCompleted") Boolean isCompleted) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE tasks set name=?, description=?, deadline_date=?, is_completed=?, project_id=? where id=?");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setDate(3, Date.valueOf(date));
            preparedStatement.setBoolean(4, isCompleted);
            preparedStatement.setLong(5, projectId);
            preparedStatement.setLong(6, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @PostMapping("/delete-task")
    public String deleteTask(@RequestParam(name = "id") Long id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM tasks where id=?");
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @PostMapping("/delete-completed")
    public String deleteByIsCompleted(@RequestParam(name = "isCompleted") Boolean isCompleted) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from tasks where is_completed=?");
            preparedStatement.setBoolean(1, isCompleted);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @PostMapping("/update-names")
    public String updateNames(@RequestParam(name = "oldName") String oldName,
                              @RequestParam(name = "newName") String newName) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update tasks set name=? where name=?");
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, oldName);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }
}
