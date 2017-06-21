package jp.co.tutorial.app.customer;

import javax.inject.Inject;

import jp.co.tutorial.domain.customer.service.CustomerService;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * CustomerForm用のValidator実装クラス.
 */
@Component
public class CustomerFormValidator implements Validator {

    /** サービスの定義. */
    @Inject
    private CustomerService customerService;

    /** チェック対象フォームの定義 . */
    @Override
    public final boolean supports(Class<?> clazz) {
        return CustomerForm.class.isAssignableFrom(clazz);
    }

    /** 独自実装チェック . */
    @Override
    public final void validate(final Object target, final Errors errors) {

        // 単項目チェックにエラーが発生しているか判定
        if (errors.hasFieldErrors("customerCd")) {
            return;
        }

        // フォームの取得
        CustomerForm form = (CustomerForm) target;

        if (form.getEditType().equals("0")) {
            // 顧客CDの重複チェック
            if (null != customerService.searchCustomer(form.getCustomerCd())) {
                // エラーメッセージ設定
                errors.rejectValue("customerCd", "IM.CST.ERR.004", "顧客コードが重複しています.");
            }
        }
    }

}
