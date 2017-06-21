package jp.co.tutorial.app.customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import jp.co.intra_mart.foundation.ui.ajax.AjaxServiceException;
import jp.co.intra_mart.foundation.ui.ajax.component.listtable.ListTableProcessor;
import jp.co.intra_mart.foundation.ui.ajax.component.listtable.ListTableResult;
import jp.co.intra_mart.foundation.ui.ajax.component.listtable.ParameterBean;
import jp.co.intra_mart.framework.extension.spring.context.ApplicationContextProvider;
import jp.co.tutorial.domain.customer.model.CustomerDto;
import jp.co.tutorial.domain.customer.service.CustomerService;

/**
 * 顧客一覧の取得プロセッサ.
 *
 * @author intra-mart
 *
 */
public class CustomerListTableProcessor implements ListTableProcessor {

    /** サービスの定義. */
    @Inject
    private CustomerService customerService;

    /** コンストラクタ. */
    public CustomerListTableProcessor() {
        ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
    }

    /**
     * 顧客一覧取得.
     *
     * @param parameter ページング情報
     * @throws AjaxServiceException 処理例外
     */
    public ListTableResult getList(final ParameterBean parameter) throws AjaxServiceException {

        final ListTableResult result = new ListTableResult();

        try {
            // ページング情報
            final int page = parameter.getPage().intValue();
            final int rowNum = parameter.getRowNum().intValue();
            // ソート情報
            final String sortIndex = camelToSnake(parameter.getSortIndex());
            final String sortOrder = parameter.getSortOrder();

            final int start = (page < 0 || rowNum < 0) ? 0 : (page - 1) * rowNum;

            // 顧客件数の取得
            final int count = (int) customerService.countCustomer();

            // 顧客情報の取得
            final List<CustomerDto> customersList = customerService
                    .getCustomerList(sortIndex, sortOrder, start, rowNum);

            // 顧客情報の設定
            List<Map<String, Object>> data =
                    new ArrayList<Map<String, Object>>();
            for (CustomerDto customer : customersList) {

                HashMap<String, Object> map = new HashMap<String, Object>();

                map.put("customerCd", customer.getCustomerCd());
                map.put("customerName", customer.getCustomerName());
                final String chargeStfName = customerService.
                        getChargeStfName(customer.getChargeStfCd());
                map.put("chargeStf", chargeStfName);
                map.put("edit",
                        "<div class='im-ui-icon-common-16-update' " + "width=\'20\' height=\'20\' />");

                data.add(map);
            }

            // 表示情報の設定
            result.setPage(page);
            result.setTotal(count);
            result.setData(data);
        } catch (final Exception e) {
            throw new AjaxServiceException(e.getMessage(), e);
        }

        return result;

    }

    /**
     * キャメルケースをスネークケースに変換します.
     * @param targetStr キャメルケース
     * @return スネークケース
     */
    private String camelToSnake(final String targetStr) {
        String convertedStr = targetStr
                .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
                .replaceAll("([a-z])([A-Z])", "$1_$2");
        return convertedStr.toLowerCase();
    }

}
