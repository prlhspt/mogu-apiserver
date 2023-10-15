package com.mogu.apiserver.presentation.settlement.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GeneratePreSignedUrlsResponse {

    String url;

}
