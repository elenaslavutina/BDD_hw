package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTests {

    @BeforeEach
    public void initializasion() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
    }

    @Test
    @Order(1)
    void shouldTransferMoneyFromFirstToSecond() {
        val dashboardPage = new DashboardPage();
        val amount = 500;
        val expectedBalance = dashboardPage.getNewBalanceSecondCard(amount);
        dashboardPage.completeTransferToCard2();
        val transferPage = new TransferPage();
        val transferInfo = DataHelper.getTransferInfoFirstCard(String.valueOf(amount));
        transferPage.transferMoney(transferInfo);
        val newBalance = dashboardPage.getCurrentBalanceSecondCard();

        assertEquals(expectedBalance, newBalance);
    }

    @Test
    @Order(2)
    void shouldTransferMoneyFromSecondToFirst() {
        val amount = 500;
        val dashboardPage = new DashboardPage();
        val expectedBalance = dashboardPage.getNewBalanceFirstCard(amount);
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = DataHelper.getTransferInfoSecondCard(String.valueOf(amount));
        transferPage.transferMoney(transferInfo);
        val newBalance = dashboardPage.getCurrentBalanceFirstCard();
        assertEquals(expectedBalance, newBalance);
    }

    @Test
    @Order(3)
    void shouldNotTransferWhenAmountEmpty() {
        val dashboardPage = new DashboardPage();
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = new DataHelper.TransferInfo("", "5559000000000002");
        transferPage.transferError(transferInfo);
    }

    @Test
    @Order(4)
    void shouldNotTransferWhenEmptyCardField() {
        val dashboardPage = new DashboardPage();
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = new DataHelper.TransferInfo("500", "");
        transferPage.transferError(transferInfo);
    }

    @Test
    @Order(5)
    void shouldNotTransferWhenUnknownCard() {
        val dashboardPage = new DashboardPage();
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = new DataHelper.TransferInfo("500", "5559000000000005");
        transferPage.transferError(transferInfo);
    }


    @Test
    @Order(6)
    void shouldNotTransferWhenNotEnoughMoneyOnSecondCard() {
        val dashboardPage = new DashboardPage();
        val currentAmount = dashboardPage.getCurrentBalanceSecondCard();
        val amount = currentAmount + 1;
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = DataHelper.getTransferInfoSecondCard(String.valueOf(amount));
        transferPage.transferError(transferInfo);
    }

    @Test
    @Order(7)
    void shouldNotTransferWhenNotEnoughMoneyOnFirstCard() {
        val dashboardPage = new DashboardPage();
        val currentAmount = dashboardPage.getCurrentBalanceFirstCard();
        val amount = currentAmount + 1;
        dashboardPage.completeTransferToCard2();
        val transferPage = new TransferPage();
        val transferInfo = DataHelper.getTransferInfoFirstCard(String.valueOf(amount));
        transferPage.transferError(transferInfo);
    }

    @Test
    @Order(8)
    void shouldTransferAllMoneyFromSecondCardToFirst() {
        val dashboardPage = new DashboardPage();
        val amount = dashboardPage.getCurrentBalanceSecondCard();
        val expectedBalance = dashboardPage.getNewBalanceFirstCard(amount);
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = DataHelper.getTransferInfoSecondCard(String.valueOf(amount));
        transferPage.transferMoney(transferInfo);
        val newBalance = dashboardPage.getCurrentBalanceFirstCard();
        assertEquals(expectedBalance, newBalance);
    }

}