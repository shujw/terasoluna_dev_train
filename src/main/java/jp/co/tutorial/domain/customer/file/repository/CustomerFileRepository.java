package jp.co.tutorial.domain.customer.file.repository;

import org.springframework.web.multipart.MultipartFile;

public interface CustomerFileRepository {

    /**
     * 参考資料のアップロード.
     *
     * @param customerCd 顧客情報
     * @param file 参考資料
     */
    void uploadFile(final String customerCd, final MultipartFile file);

    /**
     * 参考資料の削除.
     *
     * @param customerCd 顧客コード
     */
    void deleteFile(final String customerCd);

}
