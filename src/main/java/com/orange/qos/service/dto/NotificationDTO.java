package com.orange.qos.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.orange.qos.domain.Notification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private Long id;

    private String type;

    private String message;

    private Instant dateEnvoi;

    private Boolean statutLecture;

    private DegradationDTO degradation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Instant dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Boolean getStatutLecture() {
        return statutLecture;
    }

    public void setStatutLecture(Boolean statutLecture) {
        this.statutLecture = statutLecture;
    }

    public DegradationDTO getDegradation() {
        return degradation;
    }

    public void setDegradation(DegradationDTO degradation) {
        this.degradation = degradation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", message='" + getMessage() + "'" +
            ", dateEnvoi='" + getDateEnvoi() + "'" +
            ", statutLecture='" + getStatutLecture() + "'" +
            ", degradation=" + getDegradation() +
            "}";
    }
}
