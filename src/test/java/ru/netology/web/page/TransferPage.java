package ru.netology.web.page;

import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement heading = $(byText("Пополнение карты"));
    private SelenideElement amount = $("[data-test-id = amount] input");
    private SelenideElement from = $("[data-test-id = from] input");
    private SelenideElement transferButton = $("[data-test-id = action-transfer]");
    private SelenideElement errorNotification = $("[data-test-id = error-notification]");

    public TransferPage() {
        heading.shouldBe(visible);
    }

    public DashboardPage transferMoney(DataHelper.TransferInfo info) {
        amount.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        amount.doubleClick().sendKeys(Keys.DELETE);
        amount.setValue(info.getAmount());
        from.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        from.doubleClick().sendKeys(Keys.DELETE);
        from.setValue(info.getCard());
        transferButton.click();
        return new DashboardPage();
    }

    public void transferError(DataHelper.TransferInfo info) {
        amount.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        amount.doubleClick().sendKeys(Keys.DELETE);
        amount.setValue(info.getAmount());
        from.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        from.doubleClick().sendKeys(Keys.DELETE);
        from.setValue(info.getCard());
        transferButton.click();
        errorNotification.shouldBe(visible);
    }
}