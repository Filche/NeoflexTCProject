package neoflex.calculator.service;

import neoflex.calculator.dto.LoanOfferDto;
import neoflex.calculator.dto.LoanStatementRequestDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Сервис для расчёта возможных условий кредита
 */
@Service
public class OffersService {

    private static final BigDecimal INSURANCE_AMOUNT = BigDecimal.valueOf(100_000);
    private static final BigDecimal RATE = BigDecimal.valueOf(10);
    private static final BigDecimal SALARY_CLIENT_DISCOUNT = BigDecimal.ONE;

    /**
     * Данный метод возвращает отсортированный список из 4-х предложений, зависящих от итоговой ставки.
     * (Чем меньше итоговая ставка, тем лучше)
     * @param loanStatementRequestDto (запрос о кредитной выписке)
     * @return (список кредитных предложений)
     */
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {
        List<LoanOfferDto> offers = new ArrayList<>();

        // Создание четырёх предложений на все 4 случая, в зависимости от значения переменных
        // isInsuranceEnabled (застрахован ли клиент) и isSalaryClient (платёжеспособен ли клиент)
        for (boolean isInsuranceEnabled : new boolean[]{true, false}) {
            for (boolean isSalaryClient : new boolean[]{true, false}) {
                LoanOfferDto offerDto = createLoanOfferDto(loanStatementRequestDto, isInsuranceEnabled, isSalaryClient);
                offers.add(offerDto);
            }
        }

        // Список сортируется от худшего предложения к худшему, в зависимости от итоговой ставки
        offers.sort(Comparator.comparing(LoanOfferDto::getRate));

        return offers;
    }

    /**
     *
     * @param loanStatementRequestDto (запрос о кредитной выписке)
     * @param isInsuranceEnabled (имеется ли страховка)
     * @param isSalaryClient (является ли зарплатным клиентом)
     * @return (сформированное кредитное предложение)
     */
    private LoanOfferDto createLoanOfferDto(LoanStatementRequestDto loanStatementRequestDto,
                                            boolean isInsuranceEnabled, boolean isSalaryClient) {
        LoanOfferDto offerDto = new LoanOfferDto();
        BigDecimal totalAmount = loanStatementRequestDto.getAmount();
        BigDecimal rate = RATE;

        if (isInsuranceEnabled){
            totalAmount = totalAmount.add(INSURANCE_AMOUNT);
            rate = rate.subtract(BigDecimal.valueOf(3));
        }

        if (isSalaryClient){
            rate = rate.subtract(SALARY_CLIENT_DISCOUNT);
        }

        offerDto.setRequestedAmount(loanStatementRequestDto.getAmount());
        offerDto.setTotalAmount(totalAmount);
        offerDto.setTerm(loanStatementRequestDto.getTerm());
        offerDto.setMonthlyPayment(calculateMonthlyPayment(totalAmount, rate, loanStatementRequestDto.getTerm()));
        offerDto.setRate(rate);
        offerDto.setIsInsuranceEnabled(isInsuranceEnabled);
        offerDto.setIsSalaryClient(isSalaryClient);

        return offerDto;
    }

    /**
     * В данном методе высчитывается сумма месячной выплаты по кредиту.
     * Сумма высчитывается по такой формуле:
     * {rate} / 12 = {monthlyRate} - месячная ставка
     * ({monthlyRate} + 1) ^ {term} = {powForAnnuity} - месячная ставка + 1 возведённая в степень равную сроку
     * {monthlyRate} × {powForAnnuity} / ({powForAnnuity} − 1) = {annuityCoefficient} - коэффициент аннуитета
     * {totalAmount} * {annuityCoefficient} = {monthlyPayment} - месячная выплата
     * @param totalAmount (текущая сумма)
     * @param rate (ставка)
     * @param term (срок)
     * @return (сумму месячной выплаты)
     */
    private BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, BigDecimal rate, Integer term){
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 3, RoundingMode.DOWN);
        BigDecimal powForAnnuity = monthlyRate.add(BigDecimal.ONE).pow(term);
        BigDecimal annuityCoefficient = monthlyRate.multiply(powForAnnuity)
                .divide(powForAnnuity.subtract(BigDecimal.ONE), 3, RoundingMode.DOWN);
        return totalAmount.multiply(annuityCoefficient);
    }
}
