package jp.co.tutorial.domain.customer.model;

/**
 * 顧客情報DTOクラス.
 *
 * @author intra-mart
 */
public class CustomerDto {

    /** 顧客CD. */
    private String customerCd;

    /** 顧客名. */
    private String customerName;

    /** 電話番号. */
    private String customerTelno;

    /** 住所. */
    private String customerAddress;

    /** 性別. */
    private String customerSex;

    /** 生年月日. */
    private String customerBirthday;

    /** 参考資料. */
    private String attachmentFile;

    /** 担当者. */
    private String chargeStfCd;

    public String getCustomerCd() {
        return customerCd;
    }

    public void setCustomerCd(String customerCd) {
        this.customerCd = customerCd;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerTelno() {
        return customerTelno;
    }

    public void setCustomerTelno(String customerTelno) {
        this.customerTelno = customerTelno;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getCustomerSex() {
        return customerSex;
    }

    public void setCustomerSex(String customerSex) {
        this.customerSex = customerSex;
    }

    public String getCustomerBirthday() {
        return customerBirthday;
    }

    public void setCustomerBirthday(String customerBirthday) {
        this.customerBirthday = customerBirthday;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public String getChargeStfCd() {
        return chargeStfCd;
    }

    public void setChargeStfCd(String chargeStfCd) {
        this.chargeStfCd = chargeStfCd;
    }
}
