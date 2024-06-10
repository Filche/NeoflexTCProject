package neoflex.calculator.service;

import neoflex.calculator.dto.CreditDto;
import neoflex.calculator.dto.PaymentScheduleElementDto;
import neoflex.calculator.dto.ScoringDataDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис, отвечающий за полный расчёт параметров кредита
 */
@Service
public class CalcService {

    /**
     * Метод делает расчёт всех параметров кредита,
     * исходя из оценочных данных, переданных в него.
     * @param scoringDataDto (оценочные данные)
     * @return (параметры кредита)
     */
    public CreditDto calculate(ScoringDataDto scoringDataDto) {
        CreditDto credit = new CreditDto();

        BigDecimal rate = calculateRate();

        BigDecimal monthlyPayment = calculateMonthlyPayment(scoringDataDto.getAmount(), rate, scoringDataDto.getTerm());

        BigDecimal psk = calculatePsk(monthlyPayment, scoringDataDto.getTerm());

        List<PaymentScheduleElementDto> paymentSchedule = createPaymentSchedule(scoringDataDto.getAmount(), monthlyPayment, rate, scoringDataDto.getTerm());

        credit.setAmount(scoringDataDto.getAmount());
        credit.setTerm(scoringDataDto.getTerm());
        credit.setMonthlyPayment(monthlyPayment);
        credit.setRate(rate);
        credit.setPsk(psk);
        credit.setPaymentSchedule(paymentSchedule);
        credit.setIsInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled());
        credit.setIsSalaryClient(scoringDataDto.getIsSalaryClient());

        return credit;
    }

    /**
     * @return (значение процентной ставки кредита)
     */
    private BigDecimal calculateRate() {
        return new BigDecimal("12");
    }

    /**
     * Полная стоимость кредита получена путём
     * перемножения полной ежемесячного платежа на срок
     * @param monthlyPayment (полная месячная выплата)
     * @param term (срок выплаты кредита)
     * @return (полная стоимость кредита)
     */
    private BigDecimal calculatePsk(BigDecimal monthlyPayment, Integer term) {
        return monthlyPayment.multiply(BigDecimal.valueOf(term));
    }

    /**
     * P = (PV * r) / (1 - (1 + r)^(-n))
     * где:
     * P = ежемесячный платеж
     * PV = сумма кредита (500 000)
     * r = месячная процентная ставка (12% / 12 = 0,01)
     * n = срок кредита в месяцах (36 месяцев)
     * @param amount (сумма кредита)
     * @param rate (месячная процентная ставка)
     * @param term (срок кредита в месяцах)
     * @return (ежемесячный платеж)
     */
    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthRate = rate.divide(BigDecimal.valueOf(12), 3, RoundingMode.DOWN).divide(BigDecimal.valueOf(100), 3, RoundingMode.DOWN);
        BigDecimal numerator = amount.multiply(monthRate);
        BigDecimal onePlusR = monthRate.add(BigDecimal.ONE);
        BigDecimal powOfTerm = BigDecimal.ONE.divide(onePlusR.pow(term), 3, RoundingMode.DOWN);
        BigDecimal denominator = BigDecimal.ONE.subtract(powOfTerm);
        return numerator.divide(denominator,  3, RoundingMode.DOWN);
//        return (amount.multiply(rate))
//                .divide((((rate.divide(BigDecimal.valueOf(12), 3, RoundingMode.DOWN))
//                .add(BigDecimal.ONE)).pow(term))
//                .subtract(BigDecimal.ONE), 3, RoundingMode.DOWN);
    }

    /**
     * Метода создаёт List ежемесячных выплат по ранее рассчитанным данным
     * @param amount (сумма взятого кредита)
     * @param monthlyPayment (полная месячная
     * @param rate (процентная ставка)
     * @param term (срок)
     * @return (график ежемесячных платежей)
     */
    private List<PaymentScheduleElementDto> createPaymentSchedule(BigDecimal amount, BigDecimal monthlyPayment, BigDecimal rate, Integer term) {
        List<PaymentScheduleElementDto> paymentSchedule = new ArrayList<>();

        for (int i = 1; i <= term; i++) {
            PaymentScheduleElementDto element = new PaymentScheduleElementDto();
            // Номер выплаты соответствует порядковому номеру месяца
            element.setNumber(i);
            // Каждая дата выплаты - это текущая дата + 1 месяц
            element.setDate(LocalDate.now().plusMonths(i - 1));
            // Текущая выплата = полный месячный платёж
            element.setTotalPayment(monthlyPayment);
            // Выплата по процентам высчитывается по формуле Pm = (PV * r)
            // где:
            // Pm = платеж по процентам
            // PV = сумма кредита
            // r = месячная процентная ставка
            element.setInterestPayment(amount.multiply(rate.divide(BigDecimal.valueOf(12), 3, RoundingMode.DOWN)));
            // Выплаты основного долга высчитываются по формуле R = P - Pm
            // где:
            // R = выплаты основного долга
            // Pm = платёж по процентам
            // P = полный ежемесячный платёж
            element.setDebtPayment(element.getTotalPayment().subtract(element.getInterestPayment()));
            // Оставшаяся сумма долго
            amount = amount.subtract(element.getDebtPayment());
            element.setRemainingDebt(amount);

            paymentSchedule.add(element);
        }

        return paymentSchedule;
    }
}
