package neoflex.calculator.dto;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
public class LoanStatementRequestDto {

    @NotNull
    private BigDecimal amount;
    @NotNull
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
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate birthdate;
    @NotBlank
    @Size(min = 4, max = 4)
    private String passportSeries;
    @NotBlank
    @Size(min = 6, max = 6)
    private String passportNumber;
}
