package jp.co.tutorial.domain.customer.service;

import java.util.List;

import jp.co.tutorial.domain.customer.model.CustomerDto;

import org.springframework.web.multipart.MultipartFile;

/**
 * customerServiceインターフェースクラス.
 *
 * @author intra-mart
 */
public interface CustomerService {

    /**
     * 顧客数の取得.
     *
     * @return 顧客数
     */
    int countCustomer();

    /**
     * 顧客情報リストの取得.
     *
     * @param sortIndex ソート対象
     * @param sortOrder 昇順/降順
     * @param offset 取得開始レコード
     * @param limit 取得件数
     * @return 顧客情報リスト
     */
    List<CustomerDto> getCustomerList(String sortIndex, String sortOrder, int offset, int limit);

    /**
     * 顧客CDを条件に顧客情報を取得.
     *
     * @param customerCd 顧客CD
     * @return 顧客情報DTO
     */
    CustomerDto searchCustomer(String customerCd);

    /**
     * 顧客情報の登録.
     *
     * @param customerDto 顧客情報
     * @param file 参考資料
     */
    void createCustomer(final CustomerDto customerDto, final MultipartFile file);

    /**
     * 顧客情報の更新.
     *
     * @param customerDto 顧客情報
     * @param file 参考資料
     */
    void updateCustomer(CustomerDto customerDto, MultipartFile file);

    /**
     * 顧客情報の削除.
     *
     * @param customerCd 顧客コード
     */
    void deleteCustomer(String customerCd);

    /**
     * 担当者の名前を取得.
     *
     * @param chargeStfCd 担当者コード
     * @return 担当者名
     */
    String getChargeStfName(String chargeStfCd);

    /**
     * 新規登録した顧客情報をIMBOXへ送信.
     * @param customerDto 顧客情報
     */
    void linkIMBox(CustomerDto customerDto);

    /**
     * メッセージの取得.
     *
     * @param key メッセージキー
     * @return メッセージ
     */
    String getMessage(final String key);

}
