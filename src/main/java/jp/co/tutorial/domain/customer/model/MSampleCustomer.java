package jp.co.tutorial.domain.customer.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 * MSampleCustomerクラス.
 *
 * @author intra-mart
 */
public class MSampleCustomer implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** customerCdプロパティ. */
    private String customerCd;

    /** customerNameプロパティ. */
    private String customerName;

    /** customerTelnoプロパティ. */
    private String customerTelno;

    /** customerAddressプロパティ. */
    private String customerAddress;

    /** customerSexプロパティ. */
    private String customerSex;

    /** customerBirthdayプロパティ. */
    private Date customerBirthday;

    /** attachmentFileプロパティ.*/
    private String attachmentFile;

    /** chargeStfCdプロパティ. */
    private String chargeStfCd;

    /** updateDateプロパティ. */
    private Timestamp updateDate;

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

    public Date getCustomerBirthday() {
        return customerBirthday;
    }

    public void setCustomerBirthday(Date customerBirthday) {
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

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }
}
