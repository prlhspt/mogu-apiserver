package com.mogu.apiserver.application.settlement.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GenerateSettlementPreSignedUrlsServiceRequest {

    private Integer fileCount;

}
