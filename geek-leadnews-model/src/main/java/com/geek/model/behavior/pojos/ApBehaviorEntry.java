package com.geek.model.behavior.pojos;

import lombok.Data;
import lombok.Getter;
import org.apache.ibatis.type.Alias;

import java.util.Date;

@Data
public class ApBehaviorEntry {

    public String burst;
    private Integer id;
    private Short type;
    private Integer entryId;
    private Date createdTime;

    public boolean isUser() {
        return this.getType() != null && this.getType() == Type.USER.getCode();
    }

    @Alias("ApBehaviorEntryEnumType")
    public enum Type {
        USER((short) 1), EQUIPMENT((short) 0);
        @Getter
        short code;

        Type(short code) {
            this.code = code;
        }
    }

}
