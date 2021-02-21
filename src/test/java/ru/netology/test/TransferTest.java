package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.page.DashboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;
import static ru.netology.data.DataHelper.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class TransferTest {
    @BeforeEach
    void setUp() {
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor();
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @Order(1)
    void shouldTransferMoneyFromSecondToFirst() {
        val dashboardPage = new DashboardPage();
        val amount = 1000;
        val expectedBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val expectedBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        val transferPage = dashboardPage.transferToFirstCard();
        val transferInfo = getSecondCardNumber();
        transferPage.moneyTransfer(transferInfo, amount);
        val balanceOfFirstCard = getExpectedBalanceIfBalanceIncreased(expectedBalanceOfFirstCard, amount);
        val balanceOfSecondCard = getExpectedBalanceIfBalanceDecreased(expectedBalanceOfSecondCard, amount);
        val finalBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val finalBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        assertEquals(balanceOfFirstCard, finalBalanceOfFirstCard);
        assertEquals(balanceOfSecondCard, finalBalanceOfSecondCard);
    }

    @Test
    @Order(2)
    void shouldTransferMoneyFromFirstToSecond() {
        val dashboardPage = new DashboardPage();
        val amount = 600;
        val expectedBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        val expectedBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val transferPage = dashboardPage.transferToSecondCard();
        val transferInfo = getFirstCardNumber();
        transferPage.moneyTransfer(transferInfo, amount);
        val balanceOfSecondCard = getExpectedBalanceIfBalanceIncreased(expectedBalanceOfSecondCard, amount);
        val balanceOfFirstCard = getExpectedBalanceIfBalanceDecreased(expectedBalanceOfFirstCard, amount);
        val finalBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        val finalBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        assertEquals(balanceOfSecondCard, finalBalanceOfSecondCard);
        assertEquals(balanceOfFirstCard, finalBalanceOfFirstCard);

    }

    @Test
    @Order(3)
    void shouldBeErrorWhenAmountEmpty() {
        val dashboardPage = new DashboardPage();
        val amount = "";
        val transferPage = dashboardPage.transferToFirstCard();
        val transferInfo = getSecondCardNumber();
        transferPage.moneyTransfer(transferInfo, Integer.parseInt(amount));
        transferPage.invalidMoneyTransfer();
    }

    @Test
    @Order(4)
    void shouldBeErrorWhenCardFieldEmpty() {
        val dashboardPage = new DashboardPage();
        val amount = 1000;
        val transferPage = dashboardPage.transferToFirstCard();
        val transferInfo = getEmptyCardNumber();
        transferPage.moneyTransfer(transferInfo, amount);
        transferPage.invalidMoneyTransfer();
    }

    @Test
    @Order(5)
    void shouldBeErrorWhenCardIrrelevant() {
        val dashboardPage = new DashboardPage();
        val amount = 1000;
        val transferPage = dashboardPage.transferToFirstCard();
        val transferInfo = getIrrelevantCardNumber();
        transferPage.moneyTransfer(transferInfo, amount);
        transferPage.invalidMoneyTransfer();
    }

    @Test
    @Order(6)
    void shouldTransferNothingWhenAmountZero() {
        val dashboardPage = new DashboardPage();
        val amount = 0;
        val expectedBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        val expectedBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val transferPage = dashboardPage.transferToSecondCard();
        val transferInfo = getFirstCardNumber();
        transferPage.moneyTransfer(transferInfo, amount);
        val balanceOfSecondCard = getExpectedBalanceIfBalanceIncreased(expectedBalanceOfSecondCard, amount);
        val balanceOfFirstCard = getExpectedBalanceIfBalanceDecreased(expectedBalanceOfFirstCard, amount);
        val finalBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        val finalBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        assertEquals(balanceOfSecondCard, finalBalanceOfSecondCard);
        assertEquals(balanceOfFirstCard, finalBalanceOfFirstCard);
    }

    @Test
    @Order(7)
    void shouldTransferAllMoney() {
        val dashboardPage = new DashboardPage();
        val amount = dashboardPage.getCurrentBalanceOfSecondCard();
        val expectedBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val expectedBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        val transferPage = dashboardPage.transferToFirstCard();
        val transferInfo = getSecondCardNumber();
        transferPage.moneyTransfer(transferInfo, amount);
        val balanceOfFirstCard = getExpectedBalanceIfBalanceIncreased(expectedBalanceOfFirstCard, amount);
        val balanceOfSecondCard = getExpectedBalanceIfBalanceDecreased(expectedBalanceOfSecondCard, amount);
        val finalBalanceOfFirstCard = dashboardPage.getCurrentBalanceOfFirstCard();
        val finalBalanceOfSecondCard = dashboardPage.getCurrentBalanceOfSecondCard();
        assertEquals(balanceOfFirstCard, finalBalanceOfFirstCard);
        assertEquals(balanceOfSecondCard, finalBalanceOfSecondCard);
    }

    @Test
    @Order(8)
    void shouldBeErrorWhenNotEnoughMoneyForTransfer() {
        val dashboardPage = new DashboardPage();
        val amount = dashboardPage.getCurrentBalanceOfSecondCard() + 10000;
        val transferPage = dashboardPage.transferToFirstCard();
        val transferInfo = getSecondCardNumber();
        transferPage.moneyTransfer(transferInfo, amount);
        transferPage.invalidMoneyTransfer();
    }
}
