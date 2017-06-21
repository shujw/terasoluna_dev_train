package jp.co.tutorial.app.event;

import java.io.Serializable;
import javax.validation.constraints.Size;

/**
 * 顧客情報のパラメータプロパティを持つFormクラス.
 *
 * @author intra-mart
 */
public class EventForm implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** イベントID. */
    @Size(max = 20)
    private String eventId;

    /** イベント名. */
    @Size(max = 25)
    private String eventName;

    /** イベント内容. */
    @Size(max = 50)
    private String eventDetail;

    /** 開催日. */
    private String eventDate;

    /** 参加者. */
    @Size(max = 100)
    private String entryUser;

    /** 参加者名. */
    @Size(max = 100)
    private String entryUserName;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDetail() {
        return eventDetail;
    }

    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEntryUser() {
        return entryUser;
    }

    public void setEntryUser(String entryUser) {
        this.entryUser = entryUser;
    }

    public String getEntryUserName() {
        return entryUserName;
    }

    public void setEntryUserName(String entryUserName) {
        this.entryUserName = entryUserName;
    }

}
