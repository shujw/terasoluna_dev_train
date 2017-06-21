package jp.co.tutorial.app.customer;

import java.io.Serializable;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

/**
 * 顧客情報のパラメータプロパティを持つFormクラス.
 *
 * @author intra-mart
 */
public class CustomerForm implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** 顧客CD. */
    @NotEmpty
    @Size(max = 20)
    private String customerCd;

    /** 顧客名. */
    @Size(max = 25)
    private String customerName;

    /** 電話番号. */
    @Size(max = 20)
    private String customerTelno;

    /** 住所. */
    @Size(max = 50)
    private String customerAddress;

    /** 性別. */
    @Size(max = 1)
    private String customerSex;

    /** 生年月日. */
    private String customerBirthday;

    /** 参考資料. */
    private MultipartFile attachmentFile;

    /** アップロード済みファイル名. */
    private String uploadedFileName;

    /** 担当者コード. */
    @Size(max = 100)
    private String chargeStfCd;

    /** 担当者. */
    @Size(max = 100)
    private String chargeStf;

    /** 編集種別. */
    private String editType;

    public final String getCustomerCd() {
        return customerCd;
    }

    public final void setCustomerCd(String customerCd) {
        this.customerCd = customerCd;
    }

    public final String getCustomerName() {
        return customerName;
    }

    public final void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public final String getCustomerTelno() {
        return customerTelno;
    }

    public final void setCustomerTelno(String customerTelno) {
        this.customerTelno = customerTelno;
    }

    public final String getCustomerAddress() {
        return customerAddress;
    }

    public final void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public final String getCustomerSex() {
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

    public MultipartFile getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(MultipartFile attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public String getUploadedFileName() {
        return uploadedFileName;
    }

    public void setUploadedFileName(String uploadedFileName) {
        this.uploadedFileName = uploadedFileName;
    }

    public String getChargeStfCd() {
        return chargeStfCd;
    }

    public void setChargeStfCd(String chargeStfCd) {
        this.chargeStfCd = chargeStfCd;
    }

    public String getChargeStf() {
        return chargeStf;
    }

    public void setChargeStf(String chargeStf) {
        this.chargeStf = chargeStf;
    }

    public String getEditType() {
        return editType;
    }

    public void setEditType(String editType) {
        this.editType = editType;
    }

}
