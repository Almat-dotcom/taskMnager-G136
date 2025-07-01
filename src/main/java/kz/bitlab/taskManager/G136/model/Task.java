package kz.bitlab.taskManager.G136.model;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
    private Long id;

    private String name;

    private String description;

    private LocalDate deadlineDate;

    private boolean isCompleted;

    private Project project;
}
