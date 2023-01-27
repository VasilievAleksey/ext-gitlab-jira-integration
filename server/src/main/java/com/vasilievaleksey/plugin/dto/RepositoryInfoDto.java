package com.vasilievaleksey.plugin.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RepositoryInfoDto {
    private Long id;
    private String name;
    private String description;
}
