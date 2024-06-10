package neoflex.calculator.controller;

import neoflex.calculator.dto.CreditDto;
import neoflex.calculator.dto.LoanOfferDto;
import neoflex.calculator.dto.LoanStatementRequestDto;
import neoflex.calculator.dto.ScoringDataDto;
import neoflex.calculator.service.CalcService;
import neoflex.calculator.service.OffersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    @Autowired
    private OffersService offersService;

    @Autowired
    private CalcService calcService;

    @PostMapping("/offers")
    public List<LoanOfferDto> getLoanOffers(@RequestParam LoanStatementRequestDto loanStatementRequestDto){
        return offersService.getLoanOffers(loanStatementRequestDto);
    }

    @PostMapping("/calc")
    public CreditDto calculate(@RequestParam ScoringDataDto scoringDataDto){
        return calcService.calculate(scoringDataDto);
    }
}
