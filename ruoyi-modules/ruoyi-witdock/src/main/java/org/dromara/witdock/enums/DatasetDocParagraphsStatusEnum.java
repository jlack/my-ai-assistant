package org.dromara.witdock.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author zyh
 */

public enum DatasetDocParagraphsStatusEnum implements IEnum<String> {
    ACTIVE("active","可用"),
    INACTIVE("inactive","禁用");

    DatasetDocParagraphsStatusEnum(final String value, final String desc) {
        this.value = value;
        this.desc = desc;
    }

    private String value;
    private String desc;

    @Override
    public String getValue() {
        return this.value;
    }

    @JsonValue
    public String getDesc(){
        return this.desc;
    }
}
