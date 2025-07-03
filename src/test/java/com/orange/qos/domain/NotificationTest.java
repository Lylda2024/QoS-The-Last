package com.orange.qos.domain;

import static com.orange.qos.domain.DegradationTestSamples.*;
import static com.orange.qos.domain.NotificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.orange.qos.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = getNotificationSample1();
        Notification notification2 = new Notification();
        assertThat(notification1).isNotEqualTo(notification2);

        notification2.setId(notification1.getId());
        assertThat(notification1).isEqualTo(notification2);

        notification2 = getNotificationSample2();
        assertThat(notification1).isNotEqualTo(notification2);
    }

    @Test
    void degradationTest() {
        Notification notification = getNotificationRandomSampleGenerator();
        Degradation degradationBack = getDegradationRandomSampleGenerator();

        notification.setDegradation(degradationBack);
        assertThat(notification.getDegradation()).isEqualTo(degradationBack);

        notification.degradation(null);
        assertThat(notification.getDegradation()).isNull();
    }
}
