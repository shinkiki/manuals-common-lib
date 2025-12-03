package kr.co.manuals.common.api.application.event;

import kr.co.manuals.common.api.application.dto.ApiResult;

import java.util.UUID;

public record ApiSavedEvent(
        UUID apiId,
        ApiResult api
) {}
