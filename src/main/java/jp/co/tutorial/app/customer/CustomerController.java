package jp.co.tutorial.app.customer;

import java.io.IOException;

import javax.inject.Inject;

import jp.co.intra_mart.foundation.authz.annotation.Authz;
import jp.co.intra_mart.foundation.service.client.file.PublicStorage;
import jp.co.tutorial.domain.customer.model.CustomerDto;
import jp.co.tutorial.domain.customer.service.CustomerService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * 顧客情報のControllerクラス.
 *
 * @author intra-mart
 */
@Controller
//@Authz(uri="service://customer/master", action="execute")
@RequestMapping("customer")
public class CustomerController {

    /** 顧客一覧画面JSP. */
    private static final String LIST = "/customer/list.jsp";

    /** 顧客編集画面JSP. */
    private static final String EDIT = "/customer/edit.jsp";

    /** サービスの定義. */
    @Inject
    private CustomerService customerService;

    /** 独自入力チェックを行うValidatorの定義. */
    @Inject
    private CustomerFormValidator customerFormValidator;

    @InitBinder("customerForm")
    public final void initBinder(final WebDataBinder binder) {
        // 独自入力チェックを行うValidatorを追加
        binder.addValidators(customerFormValidator);
    }

    /**
     * 一覧画面表示処理.
     * @param form フォーム
     * @return 遷移先
     */
    @RequestMapping("list")
    public final String list(final CustomerListForm form) {

        // 初期表示時のページング・ソート条件設定
        form.setPage("1");
        form.setPagerRowList("10,20,30");
        form.setRowNum("10");
        form.setSortName("customerCd");
        form.setSortOrder("asc");

        return LIST;
    }

    /**
     * 登録画面表示処理.
     * @param form フォーム
     * @return 遷移先
     */
    @RequestMapping("createForm")
    public final String createForm(final CustomerForm form) {

        // 編集種別を設定
        form.setEditType("0");

        return EDIT;
    }

    /**
     * 更新・削除画面表示処理.
     * @param model モデル
     * @param customerCd 顧客コード
     * @return 遷移先
     */
    @RequestMapping(value = "editForm/{customerCd}")
    public final String editForm(final Model model, @PathVariable("customerCd") final String customerCd) {

        // 顧客情報の取得
        CustomerDto customerDto = customerService.searchCustomer(customerCd);

        if (customerDto == null) {
            // 取得件数が0
            throw new BusinessException(ResultMessages.error().add(
                    "IM.CST.ERR.003",
                    customerService.getMessage("IM.CST.ERR.003")));
        }

        // Formに顧客情報設定
        CustomerForm form = dtoToForm(customerDto);

        // 編集種別を設定
        form.setEditType("1");

        model.addAttribute("customerForm", form);

        return EDIT;
    }

    /**
     * 顧客情報の編集.
     * @param form フォーム
     * @param result エラーチェック結果
     * @return 遷移先
     */
    @RequestMapping(value = "edit", method = {RequestMethod.POST})
    public final String edit(@Validated final CustomerForm form, final BindingResult result) {

        // 入力エラーが存在する場合
        if (result.hasErrors()) {
            return EDIT;
        }

        // Dtoに顧客情報を設定
        final CustomerDto customerDto = formToDto(form);

        // 編集種別による分岐
        switch (form.getEditType()) {

        // 登録
        case "0":
            customerService.createCustomer(customerDto, form.getAttachmentFile());
            break;

        // 更新
        case "1":
            customerService.updateCustomer(customerDto, form.getAttachmentFile());
            break;

        // 削除
        case "2":
            customerService.deleteCustomer(customerDto.getCustomerCd());
            break;

        default:
            break;
        }

        return "redirect:/customer/list";
    }

    /**
     * 添付ファイルのダウンロード.
     *
     * @return 遷移しないので、nullを返却
     * @throws IOException
     */
    @RequestMapping(value = "download/{customerCd}")
    public final String download(
            Model model,
            @PathVariable("customerCd") final String customerCd) throws IOException {

        final CustomerDto customerDto = customerService.searchCustomer(customerCd);

        if (customerDto.getAttachmentFile() != null && !customerDto.getAttachmentFile().equals("")) {

            final PublicStorage storage = new PublicStorage(
                    "customer/" + customerCd + "/" + customerDto.getAttachmentFile());

            if (storage.isFile()) {
                // ファイルが存在する場合
                model.addAttribute("storage", storage);
                model.addAttribute("fileName", customerDto.getAttachmentFile());
                return "jp.co.tutorial.app.customer.DownloadView";
            }
        }
        return null;
    }

    /**
     * 顧客情報をDTOからFormに入れ替え.
     *
     * @param customerDto 顧客情報
     * @return CustomerForm 顧客情報
     */
    private CustomerForm dtoToForm(final CustomerDto customerDto) {

        final CustomerForm customerForm = new CustomerForm();

        // 顧客CD
        customerForm.setCustomerCd(customerDto.getCustomerCd());
        // 顧客名
        customerForm.setCustomerName(customerDto.getCustomerName());
        // 電話番号
        customerForm.setCustomerTelno(customerDto.getCustomerTelno());
        // 住所
        customerForm.setCustomerAddress(customerDto.getCustomerAddress());
        // 性別
        customerForm.setCustomerSex(customerDto.getCustomerSex());
        // 生年月日
        customerForm.setCustomerBirthday(customerDto.getCustomerBirthday());
        // 担当者コード
        customerForm.setChargeStfCd(customerDto.getChargeStfCd());
        // 担当者名
        customerForm.setChargeStf(customerService.getChargeStfName(customerDto.getChargeStfCd()));
        // 添付ファイル
        if (customerDto.getAttachmentFile() != null) {
            customerForm.setUploadedFileName(customerDto.getAttachmentFile());
        }

        return customerForm;
    }

    /**
     * 顧客情報をFormからDTOに入れ替え.
     *
     * @param customerForm 顧客情報
     * @return CustomerDto 顧客情報
     */
    private CustomerDto formToDto(final CustomerForm customerForm) {

        final CustomerDto customerDto = new CustomerDto();

        // 顧客CD
        customerDto.setCustomerCd(customerForm.getCustomerCd());
        // 顧客名
        customerDto.setCustomerName(customerForm.getCustomerName());
        // 電話番号
        customerDto.setCustomerTelno(customerForm.getCustomerTelno());
        // 住所
        customerDto.setCustomerAddress(customerForm.getCustomerAddress());
        // 性別
        customerDto.setCustomerSex(customerForm.getCustomerSex());
        // 生年月日
        customerDto.setCustomerBirthday(customerForm.getCustomerBirthday());
        // 担当者コード
        customerDto.setChargeStfCd(customerForm.getChargeStfCd());
        // 添付ファイル
        customerDto.setAttachmentFile(customerForm.getUploadedFileName());
        if (customerForm.getAttachmentFile().getOriginalFilename() != null
            && !customerForm.getAttachmentFile().getOriginalFilename().equals("")) {
            customerDto.setAttachmentFile(customerForm.getAttachmentFile().getOriginalFilename());
        }

        return customerDto;
    }

}
