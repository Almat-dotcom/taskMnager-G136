package kz.bitlab.taskManager.G136.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Project {
    private Long id;
    private String name;
    private String description;
}
