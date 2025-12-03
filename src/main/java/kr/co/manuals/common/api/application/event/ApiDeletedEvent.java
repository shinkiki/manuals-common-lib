package kr.co.manuals.common.api.application.event;

import java.util.UUID;

public record ApiDeletedEvent(
        UUID apiId
) {}
