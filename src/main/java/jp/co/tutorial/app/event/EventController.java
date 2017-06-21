package jp.co.tutorial.app.event;

import java.io.IOException;

import javax.inject.Inject;

import jp.co.intra_mart.foundation.authz.annotation.Authz;
import jp.co.intra_mart.foundation.service.client.information.Identifier;
import jp.co.tutorial.domain.event.model.EventDto;
import jp.co.tutorial.domain.event.service.EventService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.terasoluna.gfw.common.exception.BusinessException;
import org.terasoluna.gfw.common.message.ResultMessages;

/**
 * 顧客情報のControllerクラス.
 *
 * @author intra-mart
 */
@Controller
@Authz(uri="service://event/master", action="execute")
@RequestMapping("event")
public class EventController {

    /** イベント一覧画面JSP. */
    private static final String LIST = "/event/list.jsp";

    /** イベント登録画面JSP. */
    private static final String CREATE = "/event/create.jsp";

    /** イベント参照画面JSP. */
    private static final String DETAIL = "/event/detail.jsp";

    /** サービスの定義. */
    @Inject
    private EventService eventService;

    /**
     * 一覧画面表示処理.
     * @param form フォーム
     * @return 遷移先
     */
    @RequestMapping("list")
    public final String list(final EventListForm form) {

        /*  「3 一覧画面」を実装しましょう */
        // 初期表示時のページング・ソート条件設定
        form.setPage("1");
        form.setPagerRowList("10,20,30");
        form.setRowNum("10");
        form.setSortName("eventDate");
        form.setSortOrder("asc");

        return LIST;
    }

    /**
     * 登録画面表示処理.
     * @param form フォーム
     * @return 遷移先
     */
    @RequestMapping("createForm")
    public final String createForm(final EventForm form) {
        return CREATE;
    }

    /**
     * イベント情報の登録.
     * @param form フォーム
     * @param result エラーチェック結果
     * @return 遷移先
     */
    @RequestMapping(value = "create", method = {RequestMethod.POST})
    public final String create(@Validated final EventForm form, final BindingResult result) {

        // 入力エラーが存在する場合
        if (result.hasErrors()) {
            return CREATE;
        }

        /*  「4.1 登録画面」を実装しましょう */
        // イベントID採番
        try {
            form.setEventId(new Identifier().get());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Dtoにイベント情報を設定
        final EventDto eventDto = formToDto(form);

        eventService.createEvent(eventDto);

        return "redirect:/event/list";
    }

    /**
     * 参照画面表示処理.
     * @param model モデル
     * @param eventId イベントID
     * @return 遷移先
     */
    @RequestMapping(value = "detail/{eventId}")
    public final String editForm(final Model model, @PathVariable("eventId") final String eventId) {

        // 顧客情報の取得
        EventDto eventDto = eventService.searchEvent(eventId);

        if (eventDto == null) {
            // 取得件数が0
            throw new BusinessException(ResultMessages.error().add(
                    "IM.CST.ERR.003",
                    eventService.getMessage("IM.CST.ERR.003")));
        }

        // Formに顧客情報設定
        EventForm form = dtoToForm(eventDto);

        model.addAttribute("eventForm", form);

        return DETAIL;
    }

    /**
     * イベント情報の更新.
     * @param form フォーム
     * @param result エラーチェック結果
     * @return 遷移先
     */
    @RequestMapping(value = "update", method = {RequestMethod.POST})
    public final String update(@Validated final EventForm form, final BindingResult result) {

        // 入力エラーが存在する場合
        if (result.hasErrors()) {
            return DETAIL;
        }

        // Dtoに顧客情報を設定
        final EventDto eventDto = formToDto(form);

        eventService.updateEvent(eventDto);

        return "redirect:/event/list";
    }

    /**
     * イベント情報の削除.
     * @param form フォーム
     * @return 遷移先
     */
    @RequestMapping(value = "delete", method = {RequestMethod.POST})
    public final String delete(final EventForm form) {

        eventService.deleteEvent(form.getEventId());

        return "redirect:/event/list";
    }

    /**
     * イベント情報をDTOからFormに入れ替え.
     *
     * @param eventDto イベント情報
     * @return eventForm イベント情報
     */
    private EventForm dtoToForm(final EventDto eventDto) {

        final EventForm eventForm = new EventForm();

        // イベントID
        eventForm.setEventId(eventDto.getEventId());
        // イベント名
        eventForm.setEventName(eventDto.getEventName());
        // イベント内容
        eventForm.setEventDetail(eventDto.getEventDetail());
        // 開催日
        eventForm.setEventDate(eventDto.getEventDate());
        // 参加者
        eventForm.setEntryUser(eventDto.getEntryUser());
        // 参加者名
        eventForm.setEntryUserName(eventService.getEntryUserName(eventDto.getEntryUser()));

        return eventForm;
    }

    /**
     * イベント情報をFormからDTOに入れ替え.
     *
     * @param eventForm イベント情報
     * @return eventDto イベント情報
     */
    private EventDto formToDto(final EventForm eventForm) {

        final EventDto eventDto = new EventDto();

        // イベントID
        eventDto.setEventId(eventForm.getEventId());
        // イベント名
        eventDto.setEventName(eventForm.getEventName());
        // イベント内容
        eventDto.setEventDetail(eventForm.getEventDetail());
        // 開催日
        eventDto.setEventDate(eventForm.getEventDate());
        // 参加者
        eventDto.setEntryUser(eventForm.getEntryUser());

        return eventDto;
    }

}
