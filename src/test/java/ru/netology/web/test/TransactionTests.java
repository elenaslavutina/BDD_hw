package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;
import ru.netology.web.page.TransferPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionTests {

    @Test
    void shouldTransferMoneyFromFirstToSecond() {
        open("http://localhost:9999");
        val amount = 500;
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
        val dashboardPage = new DashboardPage();

        val currentBalance2 = dashboardPage.getCurrentBalanceSecondCard();
        val currentBalance1 = dashboardPage.getCurrentBalanceFirstCard();

        val expectedBalance = dashboardPage.getNewBalanceSecondCard(amount);
        dashboardPage.completeTransferToCard2();
        val transferPage = new TransferPage();
        val transferInfo = DataHelper.getTransferInfoFirstCard(String.valueOf(amount));
        transferPage.transferMoney(transferInfo);
        val newBalance = dashboardPage.getCurrentBalanceSecondCard();

        assertEquals(expectedBalance,newBalance);
    }

    @Test
    void shouldTransferMoneyFromSecondToFirst() {
        open("http://localhost:9999");
        val amount = 500;
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
        val dashboardPage = new DashboardPage();

        val currentBalance2 = dashboardPage.getCurrentBalanceSecondCard();
        val currentBalance1 = dashboardPage.getCurrentBalanceFirstCard();

        val expectedBalance = dashboardPage.getNewBalanceFirstCard(amount);
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = DataHelper.getTransferInfoSecondCard(String.valueOf(amount));
        transferPage.transferMoney(transferInfo);
        val newBalance = dashboardPage.getCurrentBalanceFirstCard();
        assertEquals(expectedBalance,newBalance);
    }

    @Test
    void shouldNotTransferWhenAmountEmpty() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
        val dashboardPage = new DashboardPage();
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = new DataHelper.TransferInfo("","5559000000000002");
        transferPage.transferError(transferInfo);
    }

    @Test
    void shouldNotTransferWhenEmptyCardField() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
        val dashboardPage = new DashboardPage();
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = new DataHelper.TransferInfo("500","");
        transferPage.transferError(transferInfo);
    }

    @Test
    void shouldNotTransferWhenUnknownCard() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
        val dashboardPage = new DashboardPage();
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = new DataHelper.TransferInfo("500","5559000000000005");
        transferPage.transferError(transferInfo);
    }


    @Test
    void shouldTransferAllMoneyFromSecondCardToFirst() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
        val dashboardPage = new DashboardPage();
        val amount = dashboardPage.getCurrentBalanceSecondCard();
        val expectedBalance = dashboardPage.getNewBalanceFirstCard(amount);
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = DataHelper.getTransferInfoSecondCard(String.valueOf(amount));
        transferPage.transferMoney(transferInfo);
        val newBalance = dashboardPage.getCurrentBalanceFirstCard();
        assertEquals(expectedBalance,newBalance);
    }

    @Test
    void shouldNotTransferWhenNotEnoughMoneyOnSecondCard() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
        val dashboardPage = new DashboardPage();
        val currentAmount = dashboardPage.getCurrentBalanceSecondCard();
        val amount = currentAmount+1;
        dashboardPage.completeTransferToCard1();
        val transferPage = new TransferPage();
        val transferInfo = DataHelper.getTransferInfoSecondCard(String.valueOf(amount));
        transferPage.transferError(transferInfo);
    }

    @Test
    void shouldNotTransferWhenNotEnoughMoneyOnFirstCard() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.validVerify(verificationCode);
        val dashboardPage = new DashboardPage();
        val currentAmount = dashboardPage.getCurrentBalanceFirstCard();
        val amount = currentAmount + 1;
        dashboardPage.completeTransferToCard2();
        val transferPage = new TransferPage();
        val transferInfo = DataHelper.getTransferInfoFirstCard(String.valueOf(amount));
        transferPage.transferError(transferInfo);
    }
}