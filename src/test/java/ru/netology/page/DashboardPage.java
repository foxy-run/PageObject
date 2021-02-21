package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import lombok.val;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class DashboardPage {
    private final SelenideElement FirstCardButton = $("[data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0'] .button");
    private final SelenideElement SecondCardButton = $("[data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d'] .button");

    public DashboardPage() {
        SelenideElement heading = $("[data-test-id=dashboard]");
        heading.shouldBe(visible);
    }

    public TransferPage transferToFirstCard() {
        FirstCardButton.click();
        return new TransferPage();
    }

    public TransferPage transferToSecondCard() {
        SecondCardButton.click();
        return new TransferPage();
    }

    public int getCurrentBalanceOfFirstCard() {
        val text = $(".list__item [data-test-id='92df3f1c-a033-48e6-8390-206f6b1f56c0']").getText();
        return extractBalance(text);
    }

    public int getCurrentBalanceOfSecondCard() {
        val text = $(".list__item [data-test-id='0f3f5c2a-249e-4c3d-8287-09f7a039391d']").getText();
        return extractBalance(text);
    }

    public int extractBalance(String text) {
        val substring = text.split(",");
        val getArraysLength = substring[substring.length - 1];
        val value = getArraysLength.replaceAll("\\D+", "");
        return Integer.parseInt(value);
    }
}
