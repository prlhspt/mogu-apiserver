package com.mogu.apiserver.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AwsS3Property {

    @Value("${cloud.aws.credentials.access-key}")
    public String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    public String secretKey;

    @Value("${cloud.aws.region.static}")
    public String region;

    @Value("${cloud.aws.expire-time}")
    public int expireTime;

    @Value("${cloud.aws.bucket-name}")
    public String bucketName;

}
