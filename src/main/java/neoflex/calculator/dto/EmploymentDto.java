package neoflex.calculator.dto;

import lombok.Data;
import neoflex.calculator.enums.EmploymentStatus;
import neoflex.calculator.enums.Position;

import java.math.BigDecimal;

@Data
public class EmploymentDto {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
