package jp.co.tutorial.domain.event.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import jp.co.tutorial.domain.event.model.MSampleEvent;

/**
 * イベント情報テーブル操作リポジトリ.
 */
public interface MSampleEventRepository {

    /**
     * 登録件数の取得.
     * @return 登録件数
     */
    long getCount();

    /**
     * イベント一覧の取得.
     * @param sortStr ソート順
     * @param rowBounds 取得範囲
     * @return イベント情報リスト
     */
    /*  「3 一覧画面」を実装しましょう */
    List<MSampleEvent> getEventData(@Param("sortStr") String sortStr, RowBounds rowBounds);

    /**
     * イベントIDを条件にイベント情報を検索.
     * @param eventId イベントID
     * @return イベント情報
     */
    MSampleEvent findByEventId(String eventId);

    /**
     * イベント情報を登録.
     * @param mSampleEvent イベント情報
     */
    /*  「4.1 登録画面」を実装しましょう */
    void insert(MSampleEvent mSampleEvent);

    /**
     * イベント情報の更新.
     * @param mSampleEvent イベント情報
     * @return 更新結果
     */
    boolean update(MSampleEvent mSampleEvent);

    /**
     * イベント情報の削除.
     * @param mSampleEvent イベント情報
     */
    void delete(MSampleEvent mSampleEvent);

}
