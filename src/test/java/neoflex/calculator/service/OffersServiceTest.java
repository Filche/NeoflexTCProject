package neoflex.calculator.service;

import neoflex.calculator.dto.LoanOfferDto;
import neoflex.calculator.dto.LoanStatementRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OffersServiceTest {

    @InjectMocks
    private OffersService offerService;

    @Test
    void shouldBe4GeneratedOffers(){
        LoanStatementRequestDto loanStatementRequestDto = LoanStatementRequestDto.builder()
                .amount(BigDecimal.ONE)
                .term(1)
                .build();
        List<LoanOfferDto> loanOfferDto = offerService.getLoanOffers(loanStatementRequestDto);

        assertEquals(4, loanOfferDto.size());
    }
}