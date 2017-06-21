package jp.co.tutorial.domain.event.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import jp.co.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContext;
import jp.co.intra_mart.common.aid.jsdk.javax.servlet.http.HTTPContextManager;
import jp.co.intra_mart.common.platform.log.Logger;
import jp.co.intra_mart.foundation.BaseUrl;
import jp.co.intra_mart.foundation.context.Contexts;
import jp.co.intra_mart.foundation.context.model.AccountContext;
import jp.co.intra_mart.foundation.exception.BizApiException;
import jp.co.intra_mart.foundation.i18n.datetime.format.AccountDateTimeFormatter;
import jp.co.intra_mart.foundation.i18n.datetime.format.DateTimeFormatIds;
import jp.co.intra_mart.foundation.i18n.datetime.format.DateTimeFormatterException;
import jp.co.intra_mart.foundation.master.user.UserManager;
import jp.co.intra_mart.foundation.master.user.model.User;
import jp.co.intra_mart.foundation.master.user.model.UserBizKey;
import jp.co.intra_mart.foundation.security.exception.AccessSecurityException;
import jp.co.intra_mart.foundation.security.message.MessageManager;
import jp.co.intra_mart.imbox.exception.IMBoxException;
import jp.co.intra_mart.imbox.model.Entry4NoticeMessage;
import jp.co.intra_mart.imbox.service.ApplicationBoxService;
import jp.co.intra_mart.imbox.service.Services;
import jp.co.tutorial.domain.event.model.EventDto;
import jp.co.tutorial.domain.event.model.MSampleEvent;
import jp.co.tutorial.domain.event.repository.MSampleEventRepository;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.exception.SystemException;

/**
 * eventService実装クラス.
 *
 * @author intra-mart
 */
@Service
public class EventServiceImpl implements EventService {

    /** リポジトリの定義. */
    @Inject
    private MSampleEventRepository eventRepository;

    /** ロガーの生成. */
    private static final Logger EVENT_LOGGER = Logger.getLogger("EVENT_LOG");

    /**
     * イベント数の取得.
     *
     * @return イベント数
     */
    @Override
    public final int countEvent() {
        return (int) eventRepository.getCount();
    }

    /**
     * イベント情報リストの取得.
     *
     * @param sortIndex ソート対象
     * @param sortOrder 昇順/降順
     * @param offset 取得開始レコード
     * @param limit 取得件数
     * @return イベント情報リスト
     */
    @Override
    public final List<EventDto> getEventList(
        final String sortIndex, final String sortOrder,
        final int offset, final int limit) {

        /*  「3 一覧画面」を実装しましょう */
        RowBounds rowBounds = new RowBounds(offset, limit);
        final List<MSampleEvent> resultList
            = eventRepository.getEventData(
              sortIndex + "  " + sortOrder, rowBounds);
        if (resultList == null) {
            return Collections.emptyList();
        }

        final List<EventDto> eventList = new ArrayList<EventDto>();
        // イベント情報をDtoに設定
        for (final MSampleEvent event : resultList) {
            final EventDto eventDto = entityToDto(event);
            eventList.add(eventDto);
        }
        return eventList;
    }

    /**
     * イベントIDを条件にイベント情報を取得.
     *
     * @param customerCd イベントID
     * @return イベント情報DTO
     */
    @Override
    public final EventDto searchEvent(final String eventId) {

        final MSampleEvent mSampleEvent =
                eventRepository.findByEventId(eventId);

        if (mSampleEvent != null) {

            // イベント情報をDtoに設定
            final EventDto eventDto = entityToDto(mSampleEvent);

            return eventDto;
        }
        return null;
    }

    /**
     * イベント情報の登録.
     *
     * @param eventDto イベント情報
     */
    @Override
    @Transactional
    public final void createEvent(final EventDto eventDto) {

        /*  「4.1 登録画面」を実装しましょう */
        // イベント情報をEntityに設定
        final MSampleEvent mSampleEvent = dtoToEntity(eventDto);

        // 登録
        eventRepository.insert(mSampleEvent);

        // IMBOX連携
        if(eventDto.getEntryUser() != null && !eventDto.getEntryUser().equals("")) {
            linkIMBox(eventDto);
        }

        // ログの出力
        EVENT_LOGGER.info("イベント名:{}を登録しました。", eventDto.eventName);
        EVENT_LOGGER.debug("エラー:なし");

        return;
    }

    /**
     * イベント情報の更新.
     *
     * @param eventDto イベント情報
     */
    @Override
    @Transactional
    public final void updateEvent(final EventDto eventDto) {

        // イベント情報をEntityに設定
        final MSampleEvent mSampleEvent = dtoToEntity(eventDto);

        // 更新
        eventRepository.update(mSampleEvent);

    }

    /**
     * イベント情報の削除.
     *
     * @param eventId イベントID
     */
    @Override
    @Transactional
    public final void deleteEvent(final String eventId) {

        final MSampleEvent mSampleEvent = new MSampleEvent();
        mSampleEvent.setEventId(eventId);

        // 削除
        eventRepository.delete(mSampleEvent);

    }

    /**
     * 参加者の名前を取得.
     *
     * @param entryUserCd 参加者コード
     * @return 参加者名
     */
    @Override
    public final String getEntryUserName(final String entryUser) {

        StringBuffer entryUserName = new StringBuffer();
        if (entryUser != null && !entryUser.equals("")) {
            try {
                String[] entryUserList = entryUser.split(",");
                for (int i = 0; i < entryUserList.length; i++) {
                    final AccountContext accountContext = Contexts.get(AccountContext.class);
                    final UserBizKey bizKey = new UserBizKey();
                    bizKey.setUserCd(entryUserList[i]);
                    final UserManager userManager = new UserManager();
                    final User user = userManager.getUser(bizKey, new Date(), accountContext.getLocale());
                    if (user != null) {
                        entryUserName.append(user.getUserName());
                    }
                    if (i < entryUserList.length - 1) {
                        entryUserName.append(",");
                    }
                }
            } catch (final BizApiException e) {
                throw new BusinessException(null, e);
            }
        }
        return entryUserName.toString();
    }

    /**
     * 新規登録したイベント情報をIMBoxへ送信.
     * @param eventDto イベント情報
     */
    @Override
    public final void linkIMBox(final EventDto eventDto) {

        Entry4NoticeMessage entry4NoticeMessage = new Entry4NoticeMessage();
        entry4NoticeMessage.setMessageText("新規イベントを登録しました."
                 + "イベント名：" + eventDto.eventName);
        entry4NoticeMessage.setMessageTypeCd("MESSAGE_TYPE_MESSAGE");
        entry4NoticeMessage.setSendUserCd(Contexts.get(AccountContext.class).getUserCd());
        entry4NoticeMessage.setApplicationCd("exercise");
        /* 「4.2 参照画面」を実装済みの場合、URI・URITitleを設定しましょう */
        entry4NoticeMessage.setUri(getBaseUrl() + "/event/detail/" + eventDto.eventId);
        entry4NoticeMessage.setUriTitle("イベント詳細");

        ApplicationBoxService applicationBoxService = Services.get(ApplicationBoxService.class);
        try {
            /* 「5.2 複数選択できる検索画面」を実装していない場合には、以下のように実装しましょう */
            /*
            String[] entryUser = {eventDto.entryUser};
            applicationBoxService.sendNoticeMessage(entry4NoticeMessage, entryUser);
            */
            /* 「5.2 複数選択できる検索画面」を実装済みの場合には、以下のように実装しましょう */
            String[] entryUserList = eventDto.entryUser.split(",");
            applicationBoxService.sendNoticeMessage(entry4NoticeMessage, entryUserList);
        } catch (final IMBoxException e) {
            System.out.println("IMBoxの登録に失敗しました");
        }

    }

    /**
     * イベント情報をDTOからEntityに入れ替え.
     *
     * @param eventDto イベント情報
     * @return MSampleEvent イベント情報
     */
    private MSampleEvent dtoToEntity(final EventDto eventDto) {

        final MSampleEvent mSampleEvent = new MSampleEvent();

        // イベントID
        mSampleEvent.setEventId(eventDto.getEventId());
        // イベント名
        mSampleEvent.setEventName(eventDto.getEventName());
        // イベント内容
        mSampleEvent.setEventDetail(eventDto.getEventDetail());
        // 開催日
        if (eventDto.getEventDate() != null
                && !eventDto.getEventDate().equals("")) {
            try {
                mSampleEvent.setEventDate(
                                AccountDateTimeFormatter.parse(
                                eventDto.getEventDate(), Date.class,
                                DateTimeFormatIds.IM_DATETIME_FORMAT_DATE_INPUT));
            } catch (DateTimeFormatterException e) {
                e.printStackTrace();
            }
        }
        // 参加者
        mSampleEvent.setEntryUser(eventDto.getEntryUser());

        return mSampleEvent;

    }

    /**
     * イベント情報をEntityからDTOに入れ替え.
     *
     * @param mSampleEvent イベント情報
     * @return EventDto イベント情報
     */
    private EventDto entityToDto(
            final MSampleEvent mSampleEvent) {

        final EventDto eventDto = new EventDto();

        // イベントID
        eventDto.setEventId(mSampleEvent.getEventId());
        // イベント名
        eventDto.setEventName(mSampleEvent.getEventName());
        // イベント内容
        eventDto.setEventDetail(mSampleEvent.getEventDetail());
        // 開催日
        if (mSampleEvent.getEventDate() != null) {
            eventDto.setEventDate(AccountDateTimeFormatter.format(
                     mSampleEvent.getEventDate(),
                     DateTimeFormatIds.IM_DATETIME_FORMAT_DATE_INPUT));
        }
            // 参加者
        eventDto.setEntryUser(mSampleEvent.getEntryUser());

        return eventDto;

    }


    /**
     * メッセージの取得.
     *
     * @param key メッセージキー
     * @return メッセージ
     */
    @Override
    public final String getMessage(final String key) {

        try {
            return MessageManager.getInstance().getMessage(key);
        } catch (final AccessSecurityException e) {
            throw new SystemException(null, e);
        }
    }

    /**
     * ベースURLの取得.
     *
     * @return ベースURL
     */
    public final String getBaseUrl() {

        HTTPContext httpContext = HTTPContextManager.getInstance().getCurrentContext();
        HttpServletRequest request = httpContext.getRequest();
        String baseUrl = BaseUrl.get(request);

        return baseUrl;
    }

}
