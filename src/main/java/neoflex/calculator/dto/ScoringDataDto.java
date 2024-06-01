package neoflex.calculator.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import neoflex.calculator.enums.Gender;
import neoflex.calculator.enums.MaritalStatus;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ScoringDataDto {
    private BigDecimal amount;
    private Integer term;
    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 30)
    private String lastName;
    @NotBlank
    @Size(min = 2, max = 30)
    private String middleName;
    private Gender gender;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate birthdate;
    @NotBlank
    @Size(min = 4, max = 4)
    private String passportSeries;
    @NotBlank
    @Size(min = 6, max = 6)
    private String passportNumber;
    private String passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private EmploymentDto employment;
    private String accountNumber;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}
