package com.socialuni.social.common.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author qinkaiyuan 查询结果可以没有set和空构造，前台传值可以没有get
 * @date 2019-08-13 23:34
 */
@Data
@NoArgsConstructor
@SuperBuilder
public class SocialuniContentIdRO {
    private String id;

    public SocialuniContentIdRO(SocialuniContentIdRO contentIdRO) {
        this.id = contentIdRO.getId();
    }
}
