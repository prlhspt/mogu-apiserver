package com.mogu.apiserver.domain.settlement;

import com.mogu.apiserver.domain.BaseEntity;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class SettlementImage extends BaseEntity {

    private String imagePath;

    public SettlementImage(String imagePath) {
        this.imagePath = imagePath;
    }

    public static SettlementImage create(String imagePath) {
        SettlementImage settlementImage = new SettlementImage(imagePath);
        return settlementImage;
    }
}
