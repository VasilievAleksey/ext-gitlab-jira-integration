package com.vasilievaleksey.plugin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryDto {
    private String url;
    private String accessToken;
}
