package jp.co.tutorial.app.customer;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.intra_mart.foundation.http.ResponseUtil;
import jp.co.intra_mart.foundation.service.client.file.Storage;

import org.springframework.stereotype.Component;
import org.terasoluna.gfw.common.exception.SystemException;
import org.terasoluna.gfw.web.download.AbstractFileDownloadView;

/**
 * ダウンロードviewクラス.
 */
@Component("jp.co.tutorial.app.customer.DownloadView")
public class DownloadView extends AbstractFileDownloadView {

    @Override
    protected final void addResponseHeader(
            final Map<String, Object> model,
            final HttpServletRequest request,
            final HttpServletResponse response) {

        final String disposition;
        try {
            final String fileName = (String) model.get("fileName");
            disposition = ResponseUtil.encodeFileName(request, "UTF-8", fileName);
        } catch (final UnsupportedEncodingException e) {
            throw new SystemException("download view error code", e);
        }
        response.setHeader("Content-Disposition", "attachment;" + disposition);
        response.setContentType("text/plain");
    }

    @Override
    protected final InputStream getInputStream(
            final Map<String, Object> model,
            final HttpServletRequest request) throws IOException {

        // modelに設定した storage を取得し、InputStreamを返却
        final Storage<?> storage = (Storage<?>) model.get("storage");
        return storage.open();
   }
}
