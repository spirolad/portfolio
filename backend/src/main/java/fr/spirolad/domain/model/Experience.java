package fr.spirolad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Experience {
    private Long id;
    private String company;
    private String position;
    private List<String> mission;
    private LocalDate startDate;
    private LocalDate endDate;
}
