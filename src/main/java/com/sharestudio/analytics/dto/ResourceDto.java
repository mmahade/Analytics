package com.sharestudio.analytics.dto;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
@Getter
@Setter
public class ResourceDto {
    private String resourceId;
    private String title;
    public boolean isAllFieldNull() throws IllegalAccessException {
        for (Field f : getClass().getDeclaredFields())
            if (f.get(this) != null)
                return false;
        return true;
    }

}
