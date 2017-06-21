package jp.co.tutorial.domain.customer.file.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jp.co.intra_mart.common.aid.jdk.java.io.IOUtil;
import jp.co.intra_mart.foundation.security.exception.AccessSecurityException;
import jp.co.intra_mart.foundation.security.message.MessageManager;
import jp.co.intra_mart.foundation.service.client.file.PublicStorage;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.SystemException;
import org.terasoluna.gfw.common.message.ResultMessages;

@Repository
public class CustomerFileRepositoryImpl implements CustomerFileRepository{

    /**
     * 参考資料のアップロード.
     *
     * @param customerCd 顧客情報
     * @param file 参考資料
     */
    @Override
    public void uploadFile(final String customerCd, final MultipartFile file) {

        // ファイル名取得
        final String fileName = file.getOriginalFilename();
        // ディレクトリパス
        final String dir = "customer/" + customerCd;

        try {
            final PublicStorage dirStorage = new PublicStorage(dir);
            if (!dirStorage.exists() || !dirStorage.isDirectory()) {
                if (!dirStorage.makeDirectories()) {
                    // ディレクトリ作成失敗
                    throw new BusinessException(ResultMessages.error().add(
                            "IM.CST.ERR.000", getMessage("IM.CST.ERR.000")));
                }
            }

            final PublicStorage fileStorage =
                    new PublicStorage(dirStorage, fileName);

            try(OutputStream os = fileStorage.create(); 
                  InputStream is = file.getInputStream()){
                IOUtil.transfer(is, os);
            }
        } catch (final IOException e) {
            // ファイルアップロード処理失敗
            throw new BusinessException(ResultMessages.error().add(
                    "IM.CST.ERR.000", getMessage("IM.CST.ERR.000")), e);
        }

        return;
    }

    /**
     * 参考資料の削除.
     *
     * @param customerCd 顧客コード
     */
    @Override
    public void deleteFile(final String customerCd) {

        // ディレクトリパス
        final String dir = "customer/" + customerCd;
        try {
            final PublicStorage dirStorage = new PublicStorage(dir);
            if (dirStorage.exists()) {
                dirStorage.remove(true);
            }
        } catch (final IOException e) {

            // ファイル削除処理失敗
            throw new BusinessException(ResultMessages.error().add(
                    "IM.CST.ERR.000", getMessage("IM.CST.ERR.000")), e);
        }
    }

    /**
     * メッセージの取得.
     *
     * @param key メッセージキー
     * @return メッセージ
     */
    public final String getMessage(final String key) {

        try {
            return MessageManager.getInstance().getMessage(key);
        } catch (final AccessSecurityException e) {
            throw new SystemException(null, e);
        }
    }
}
