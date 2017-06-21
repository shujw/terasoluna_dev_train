package jp.co.tutorial.domain.event.model;

import java.io.Serializable;
import java.util.Date;

/**
 * MSampleCustomerクラス.
 *
 * @author intra-mart
 */
public class MSampleEvent implements Serializable {

    /** serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** eventIdプロパティ. */
    private String eventId;

    /** eventNameプロパティ. */
    private String eventName;

    /** eventDetailプロパティ. */
    private String eventDetail;

    /** eventDateプロパティ. */
    private Date eventDate;

    /** entryUserプロパティ. */
    private String entryUser;

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

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEntryUser() {
        return entryUser;
    }

    public void setEntryUser(String entryUser) {
        this.entryUser = entryUser;
    }

}
