package org.dromara.witdock.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author zyh
 */

public enum DocStatusEnum implements IEnum<String> {
    STATUS_0("active","可用"),
    STATUS_1("inactive","禁用"),
    STATUS_2("archived","归档");

    DocStatusEnum(final String value, final String desc) {
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
