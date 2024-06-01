package neoflex.calculator.service;

import neoflex.calculator.dto.CreditDto;
import neoflex.calculator.dto.ScoringDataDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CalcServiceTest {

    @InjectMocks
    private CalcService calcService;

    private static ScoringDataDto scoringDataDto;

    @BeforeAll
    static void generate(){
        scoringDataDto = ScoringDataDto.builder()
                .amount(BigDecimal.valueOf(500_000))
                .term(36)
                .build();
    }

    @Test
    void countOfPaymentScheduleShouldBe36() {
        CreditDto creditDto = calcService.calculate(scoringDataDto);
        assertEquals(36, creditDto.getPaymentSchedule().size());
    }

    @Test
    void correctPSKValueFromCredit() {
        CreditDto creditDto = calcService.calculate(scoringDataDto);
        assertEquals(596026.476, creditDto.getPsk().doubleValue());
    }
}