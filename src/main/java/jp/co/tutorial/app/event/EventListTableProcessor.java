package jp.co.tutorial.app.event;

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
import jp.co.tutorial.domain.event.model.EventDto;
import jp.co.tutorial.domain.event.service.EventService;

/**
 * 顧客一覧の取得プロセッサ.
 *
 * @author intra-mart
 *
 */
public class EventListTableProcessor implements ListTableProcessor {

    /** サービスの定義. */
    @Inject
    private EventService eventService;

    /** コンストラクタ. */
    public EventListTableProcessor() {
        ApplicationContextProvider.getApplicationContext().getAutowireCapableBeanFactory().autowireBean(this);
    }

    /**
     * イベント一覧取得.
     *
     * @param parameter ページング情報
     * @throws AjaxServiceException 処理例外
     */
    public ListTableResult getList(final ParameterBean parameter)
            throws AjaxServiceException {

        final ListTableResult result = new ListTableResult();

        try {
            // ページング情報
            final int page = parameter.getPage().intValue();
            final int rowNum = parameter.getRowNum().intValue();
            // ソート情報
            final String sortIndex = camelToSnake(parameter.getSortIndex());
            final String sortOrder = parameter.getSortOrder();

            final int start = (page < 0 || rowNum < 0) ? 0 : (page - 1) * rowNum;

            // イベント件数の取得
            final int count = (int) eventService.countEvent();

            /*  「3 一覧画面」を実装しましょう */
            // イベント情報の取得
            final List<EventDto> eventList = eventService
                    .getEventList(sortIndex, sortOrder, start, rowNum);

            // イベント情報の設定
            List<Map<String, Object>> data =
                    new ArrayList<Map<String, Object>>();
            for (EventDto event : eventList) {

                HashMap<String, Object> map = new HashMap<String, Object>();

                map.put("eventId", event.getEventId());
                map.put("eventName", event.getEventName());
                map.put("eventDate", event.getEventDate());
                map.put("detail",
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
