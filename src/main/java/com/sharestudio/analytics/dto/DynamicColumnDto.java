package com.sharestudio.analytics.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class DynamicColumnDto {

    private Collection<String> column;
    private Integer columnCount;
}
