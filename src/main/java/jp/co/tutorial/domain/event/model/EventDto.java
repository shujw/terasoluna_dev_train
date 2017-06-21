package jp.co.tutorial.domain.event.model;

/**
 * イベント情報DTOクラス.
 *
 * @author intra-mart
 */
public class EventDto {

    /** イベントID. */
    public String eventId;

    /** イベント名. */
    public String eventName;

    /** イベント内容. */
    public String eventDetail;

    /** 開催日. */
    public String eventDate;

    /** 参加者. */
    public String entryUser;

    /** 参加者名. */
    public String entryUserName;

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
