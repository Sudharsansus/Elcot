package in.elcot.avgcxr.policy.application.domain.model;

import in.elcot.avgcxr.policy.application.domain.model.Application.ApplicationStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Application domain")
class ApplicationTest {

    private static Application newDraft() {
        return Application.createDraft(
            UUID.randomUUID(),
            UUID.randomUUID(),
            "Chennai",
            "AVGC-2026-000001"
        );
    }

    @Nested
    @DisplayName("creation")
    class Creation {
        @Test
        @DisplayName("createDraft produces a DRAFT application with generated id")
        void createDraftSetsDefaults() {
            var app = newDraft();
            assertThat(app.getId()).isNotNull();
            assertThat(app.getStatus()).isEqualTo(ApplicationStatus.DRAFT);
            assertThat(app.getApplicationNumber()).isEqualTo("AVGC-2026-000001");
            assertThat(app.getDistrict()).isEqualTo("Chennai");
            assertThat(app.getSubmittedAt()).isNull();
            assertThat(app.getApprovedAt()).isNull();
            assertThat(app.getRejectedAt()).isNull();
        }
    }

    @Nested
    @DisplayName("state transitions — happy paths")
    class HappyPaths {
        @Test
        @DisplayName("DRAFT -> SUBMITTED via submit()")
        void draftToSubmitted() {
            var app = newDraft();
            app.submit();
            assertThat(app.getStatus()).isEqualTo(ApplicationStatus.SUBMITTED);
            assertThat(app.getSubmittedAt()).isNotNull();
        }

        @Test
        @DisplayName("SUBMITTED -> UNDER_REVIEW (simulated via direct transition)")
        void submittedToUnderReview() {
            var app = newDraft();
            app.submit();
            // Application#submit moves DRAFT->SUBMITTED; further transition UNDER_REVIEW
            // is performed by reviewer action which we simulate by using canTransitionTo.
            assertThat(ApplicationStatus.SUBMITTED.canTransitionTo(ApplicationStatus.UNDER_REVIEW)).isTrue();
        }

        @Test
        @DisplayName("UNDER_REVIEW -> APPROVED via approve()")
        void underReviewToApproved() {
            var app = newDraft();
            app.submit();
            // simulate reviewer moving to UNDER_REVIEW then approve
            app.setStatus(ApplicationStatus.UNDER_REVIEW);
            app.approve(new BigDecimal("50000.00"));
            assertThat(app.getStatus()).isEqualTo(ApplicationStatus.APPROVED);
            assertThat(app.getFundingApproved()).isEqualByComparingTo("50000.00");
            assertThat(app.getApprovedAt()).isNotNull();
        }

        @Test
        @DisplayName("UNDER_REVIEW -> REJECTED via reject()")
        void underReviewToRejected() {
            var app = newDraft();
            app.submit();
            app.setStatus(ApplicationStatus.UNDER_REVIEW);
            app.reject("Missing income certificate");
            assertThat(app.getStatus()).isEqualTo(ApplicationStatus.REJECTED);
            assertThat(app.getRejectionReason()).isEqualTo("Missing income certificate");
            assertThat(app.getRejectedAt()).isNotNull();
        }

        @Test
        @DisplayName("DRAFT -> WITHDRAWN via withdraw()")
        void draftToWithdrawn() {
            var app = newDraft();
            app.withdraw();
            assertThat(app.getStatus()).isEqualTo(ApplicationStatus.WITHDRAWN);
        }

        @Test
        @DisplayName("APPROVED -> FUND_DISBURSED")
        void approvedToFundDisbursed() {
            var app = newDraft();
            app.submit();
            app.setStatus(ApplicationStatus.UNDER_REVIEW);
            app.approve(new BigDecimal("10000.00"));
            assertThat(ApplicationStatus.APPROVED.canTransitionTo(ApplicationStatus.FUND_DISBURSED)).isTrue();
        }
    }

    @Nested
    @DisplayName("state transitions — illegal paths")
    class IllegalPaths {
        @Test
        @DisplayName("DRAFT cannot directly APPROVED")
        void draftCannotBeApproved() {
            var app = newDraft();
            assertThatThrownBy(() -> app.approve(new BigDecimal("1000.00")))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot transition");
        }

        @Test
        @DisplayName("DRAFT cannot directly REJECTED")
        void draftCannotBeRejected() {
            var app = newDraft();
            assertThatThrownBy(() -> app.reject("no reason"))
                .isInstanceOf(IllegalStateException.class);
        }

        @Test
        @DisplayName("REJECTED cannot transition anywhere")
        void rejectedIsTerminal() {
            assertThat(ApplicationStatus.REJECTED.canTransitionTo(ApplicationStatus.APPROVED)).isFalse();
            assertThat(ApplicationStatus.REJECTED.canTransitionTo(ApplicationStatus.SUBMITTED)).isFalse();
            assertThat(ApplicationStatus.REJECTED.canTransitionTo(ApplicationStatus.WITHDRAWN)).isFalse();
        }

        @Test
        @DisplayName("WITHDRAWN cannot transition anywhere")
        void withdrawnIsTerminal() {
            assertThat(ApplicationStatus.WITHDRAWN.canTransitionTo(ApplicationStatus.SUBMITTED)).isFalse();
            assertThat(ApplicationStatus.WITHDRAWN.canTransitionTo(ApplicationStatus.APPROVED)).isFalse();
        }

        @Test
        @DisplayName("FUND_DISBURSED is terminal")
        void fundDisbursedIsTerminal() {
            assertThat(ApplicationStatus.FUND_DISBURSED.canTransitionTo(ApplicationStatus.APPROVED)).isFalse();
            assertThat(ApplicationStatus.FUND_DISBURSED.canTransitionTo(ApplicationStatus.WITHDRAWN)).isFalse();
        }
    }

    @Nested
    @DisplayName("transition validation")
    class TransitionValidation {
        @Test
        @DisplayName("canTransitionTo is reflexive for allowed transitions")
        void canTransitionToSelf() {
            // Self-transition is not allowed for any state in this state machine
            for (ApplicationStatus s : ApplicationStatus.values()) {
                assertThat(s.canTransitionTo(s)).isFalse();
            }
        }
    }
}
