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
        val expectedBalance1 = dashboardPage.getNewBalanceSecondCard(amount);
        val expectedBalance2 = dashboardPage.getNewBalanceFirstCard(-amount);

        val transferPage = dashboardPage.completeTransferToCard2();
        val transferInfo = DataHelper.getTransferInfoFirstCard(String.valueOf(amount));
        transferPage.transferMoney(transferInfo);
        val newBalance1 = dashboardPage.getCurrentBalanceSecondCard();
        val newBalance2 = dashboardPage.getCurrentBalanceFirstCard();

        assertEquals(expectedBalance1, newBalance1);
        assertEquals(expectedBalance2, newBalance2);


    }

    @Test
    @Order(2)
    void shouldTransferMoneyFromSecondToFirst() {
        val amount = 500;
        val dashboardPage = new DashboardPage();
        val expectedBalance1 = dashboardPage.getNewBalanceFirstCard(amount);
        val expectedBalance2 = dashboardPage.getNewBalanceSecondCard(-amount);

        val transferPage = dashboardPage.completeTransferToCard1();
        val transferInfo = DataHelper.getTransferInfoSecondCard(String.valueOf(amount));
        transferPage.transferMoney(transferInfo);
        val newBalance1 = dashboardPage.getCurrentBalanceFirstCard();
        val newBalance2 = dashboardPage.getCurrentBalanceSecondCard();

        assertEquals(expectedBalance1, newBalance1);
        assertEquals(expectedBalance2, newBalance2);
    }

    @Test
    @Order(3)
    void shouldNotTransferToCard1WhenAmountEmpty() {
        val dashboardPage = new DashboardPage();
        val transferPage = dashboardPage.completeTransferToCard1();
        val transferInfo = new DataHelper.TransferInfo("", "5559000000000002");
        transferPage.transferError(transferInfo);
    }

    @Test
    @Order(4)
    void shouldNotTransferToCard2WhenAmountEmpty() {
        val dashboardPage = new DashboardPage();
        val transferPage = dashboardPage.completeTransferToCard2();
        val transferInfo = new DataHelper.TransferInfo("", "5559000000000001");
        transferPage.transferError(transferInfo);
    }

    @Test
    @Order(5)
    void shouldNotTransferToCard1WhenEmptyCardField() {
        val dashboardPage = new DashboardPage();
        val transferPage = dashboardPage.completeTransferToCard1();
        val transferInfo = new DataHelper.TransferInfo("500", "");
        transferPage.transferError(transferInfo);
    }


    @Test
    @Order(6)
    void shouldNotTransferToCard2WhenEmptyCardField() {
        val dashboardPage = new DashboardPage();
        val transferPage = dashboardPage.completeTransferToCard2();
        val transferInfo = new DataHelper.TransferInfo("500", "");
        transferPage.transferError(transferInfo);
    }

    @Test
    @Order(7)
    void shouldNotTransferToCard1WhenUnknownCard() {
        val dashboardPage = new DashboardPage();
        val transferPage = dashboardPage.completeTransferToCard1();
        val transferInfo = new DataHelper.TransferInfo("500", "5559000000000005");
        transferPage.transferError(transferInfo);
    }

    @Test
    @Order(8)
    void shouldNotTransferToCard2WhenUnknownCard() {
        val dashboardPage = new DashboardPage();
        val transferPage = dashboardPage.completeTransferToCard2();
        val transferInfo = new DataHelper.TransferInfo("500", "5559000000000005");
        transferPage.transferError(transferInfo);
    }


    @Test
    @Order(9)
    void shouldNotTransferWhenNotEnoughMoneyOnSecondCard() {
        val dashboardPage = new DashboardPage();
        val currentAmount = dashboardPage.getCurrentBalanceSecondCard();
        val amount = currentAmount + 1;
        val transferPage = dashboardPage.completeTransferToCard1();
        val transferInfo = DataHelper.getTransferInfoSecondCard(String.valueOf(amount));
        transferPage.transferError(transferInfo);
    }

    @Test
    @Order(10)
    void shouldNotTransferWhenNotEnoughMoneyOnFirstCard() {
        val dashboardPage = new DashboardPage();
        val currentAmount = dashboardPage.getCurrentBalanceFirstCard();
        val amount = currentAmount + 1;
        val transferPage = dashboardPage.completeTransferToCard2();
        val transferInfo = DataHelper.getTransferInfoFirstCard(String.valueOf(amount));
        transferPage.transferError(transferInfo);
    }

    @Test
    @Order(11)
    void shouldTransferAllMoneyFromSecondCardToFirst() {
        val dashboardPage = new DashboardPage();
        val amount = dashboardPage.getCurrentBalanceSecondCard();
        val expectedBalance1 = dashboardPage.getNewBalanceFirstCard(amount);
        val expectedBalance2 = dashboardPage.getNewBalanceSecondCard(-amount);

        val transferPage = dashboardPage.completeTransferToCard1();
        val transferInfo = DataHelper.getTransferInfoSecondCard(String.valueOf(amount));
        transferPage.transferMoney(transferInfo);
        val newBalance1 = dashboardPage.getCurrentBalanceFirstCard();
        val newBalance2 = dashboardPage.getCurrentBalanceSecondCard();

        assertEquals(expectedBalance1, newBalance1);
        assertEquals(expectedBalance2, newBalance2);

    }


    @Test
    @Order(12)
    void shouldTransferAllMoneyFromFirstCardToSecond() {
        val dashboardPage = new DashboardPage();
        val amount = dashboardPage.getCurrentBalanceFirstCard();
        val expectedBalance1 = dashboardPage.getNewBalanceSecondCard(amount);
        val expectedBalance2 = dashboardPage.getNewBalanceFirstCard(-amount);

        val transferPage = dashboardPage.completeTransferToCard2();
        val transferInfo = DataHelper.getTransferInfoFirstCard(String.valueOf(amount));
        transferPage.transferMoney(transferInfo);
        val newBalance1 = dashboardPage.getCurrentBalanceSecondCard();
        val newBalance2 = dashboardPage.getCurrentBalanceFirstCard();
        assertEquals(expectedBalance1, newBalance1);
        assertEquals(expectedBalance2, newBalance2);

    }

}