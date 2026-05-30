package fr.spirolad.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    private Long id;
    private String institution;
    private String degree;
    private LocalDate startDate;
    private LocalDate endDate;

}
