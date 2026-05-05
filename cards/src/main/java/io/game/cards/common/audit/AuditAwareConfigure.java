package io.game.cards.common.audit;

import jakarta.annotation.Nonnull;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component("auditWareConfigure")
public class AuditAwareConfigure implements AuditorAware<String> {
    @Nonnull
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("CARDS_MS");
    }
}
