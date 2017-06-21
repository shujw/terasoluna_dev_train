package jp.co.tutorial.domain.customer.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import jp.co.tutorial.domain.customer.model.MSampleCustomer;

/**
 * 顧客情報テーブル操作リポジトリ.
 */
public interface MSampleCustomerRepository {

    /**
     * 登録件数の取得.
     * @return 登録件数
     */
    long getCount();

    /**
     * 顧客一覧の取得.
     * @param sortStr ソート順
     * @param rowBounds 取得範囲
     * @return 顧客情報リスト
     */
    List<MSampleCustomer> getCustomerData(@Param("sortStr") String sortStr, RowBounds rowBounds);

    /**
     * 顧客CDを条件に顧客情報を検索.
     * @param customerCd 顧客CD
     * @return 顧客情報
     */
    MSampleCustomer findByCustomerCd(String customerCd);

    /**
     * 顧客情報をの登録.
     * @param mSampleCustomer 顧客情報
     */
    void insert(MSampleCustomer mSampleCustomer);

    /**
     * 顧客情報の更新.
     * @param mSampleCustomer 顧客情報
     * @return 更新結果
     */
    boolean update(MSampleCustomer mSampleCustomer);

    /**
     * 顧客情報の削除.
     * @param mSampleCustomer 顧客情報
     */
    void delete(MSampleCustomer mSampleCustomer);

}
