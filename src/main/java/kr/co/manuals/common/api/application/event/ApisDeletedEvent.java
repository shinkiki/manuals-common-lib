package kr.co.manuals.common.api.application.event;

import java.util.List;
import java.util.UUID;

public record ApisDeletedEvent(
        List<UUID> apiIds
) {}
