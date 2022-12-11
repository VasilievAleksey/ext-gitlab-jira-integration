package com.vasilievaleksey.plugin.model;

import net.java.ao.Entity;
import net.java.ao.Preload;
import net.java.ao.schema.Table;

import java.util.Date;

@Preload
@Table("GIT_REPOSITORY")
public interface Repository extends Entity {
    String getName();

    void setName(String name);

    String getUrl();

    void setUrl(String url);

    RepositoryStatus getStatus();

    void setStatus(RepositoryStatus status);

    Date getLastUpdateTime();

    void setLastUpdateTime(Date lastUpdateTime);

    String getAccessToken();

    void setAccessToken(String accessToken);

    String getDescription();

    void setDescription(String description);
}
