package jp.co.tutorial.domain.event.service;

import java.util.List;

import jp.co.tutorial.domain.event.model.EventDto;

/**
 * customerServiceインターフェースクラス.
 *
 * @author intra-mart
 */
public interface EventService {

    /**
     * イベント数の取得.
     *
     * @return イベント数
     */
    int countEvent();

    /**
     * イベント情報リストの取得.
     *
     * @param sortIndex ソート対象
     * @param sortOrder 昇順/降順
     * @param offset 取得開始レコード
     * @param limit 取得件数
     * @return イベント情報リスト
     */
    List<EventDto> getEventList(String sortIndex,
            String sortOrder, int offset, int limit);

    /**
     * イベントIDを条件にイベント情報を取得.
     *
     * @param eventId イベントId
     * @return イベント情報DTO
     */
    EventDto searchEvent(String eventId);

    /**
     * イベント情報の登録.
     *
     * @param eventDto イベント情報
     */
    void createEvent(final EventDto eventDto);

    /**
     * イベント情報の更新.
     *
     * @param eventDto イベント情報
     */
    void updateEvent(EventDto eventDto);

    /**
     * イベント情報の削除.
     *
     * @param eventId イベントID
     */
    void deleteEvent(String eventId);

    /**
     * 参加者の名前を取得.
     *
     * @param entryUserCd 参加者コード
     * @return 参加者名
     */
    String getEntryUserName(String entryUserCd);

    /**
     * 新規登録したイベント情報をIMBOXへ送信.
     * @param eventDto イベント情報
     */
    void linkIMBox(EventDto eventDto);

    /**
     * メッセージの取得.
     *
     * @param key メッセージキー
     * @return メッセージ
     */
    String getMessage(String key);

}
