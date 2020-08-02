package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;
import lombok.val;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");
    private SelenideElement replenishCard1Button = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0'] .button");
    private SelenideElement replenishCard2Button = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d'] .button");
    private SelenideElement balance = $(".list__item [data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']");
    private SelenideElement balance2 = $(".list__item [data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']");

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    public TransferPage completeTransferToCard1() {
        replenishCard1Button.click();
        return new TransferPage();
    }

    public TransferPage completeTransferToCard2() {
        replenishCard2Button.click();
        return new TransferPage();
    }

    public int getCurrentBalanceFirstCard() {
        String balancePart = balance.getText().split(":")[1];
        int value = Integer.parseInt(balancePart.substring(0, balancePart.indexOf("р")).strip());
        return value;
    }

    public int getCurrentBalanceSecondCard() {
        String balancePart = balance2.getText().split(":")[1];
        int value = Integer.parseInt(balancePart.substring(0, balancePart.indexOf("р")).strip());
        return value;
    }

    public int getNewBalanceFirstCard(int transfer) {
        int currentBalance = getCurrentBalanceFirstCard();
        return currentBalance + transfer;
    }

    public int getNewBalanceSecondCard(int transfer) {
        int currentBalance = getCurrentBalanceSecondCard();
        return currentBalance + transfer;
    }
}