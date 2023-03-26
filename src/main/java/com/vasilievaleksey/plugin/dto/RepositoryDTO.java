package com.vasilievaleksey.plugin.dto;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import com.vasilievaleksey.plugin.exception.PluginRestException;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.gitlab4j.api.models.Project;
import org.springframework.http.HttpStatus;

import java.util.Optional;

public enum RepositoryDTO {;

    public enum Request {;
        @Value @AllArgsConstructor public static class RepositoryInfo {
            String url;
            String accessToken;

            public String hostUrl() {
                return parseHostUrl(this.url);
            }

            public String repositoryName() {
                return parseRepositoryName(this.url);
            }
        }
    }

    public enum Response {;
        @Value public static class RepositoryInfo {
            Long id;
            String name;
            String description;

            public RepositoryInfo(Project project) {
                this.id = project.getId();
                this.name = project.getName();
                this.description = project.getDescription();
            }
        }

        @Value public static class Repository {
            int id;
            String name;
            String description;
            String url;
            String accessToken;
            String status;
        }
    }

    private static String parseRepositoryName(String repositoryUrl) {
        String[] urlParts = repositoryUrl.split("/");
        return urlParts[urlParts.length - 1].replace(".git", "");
    }

    private static String parseHostUrl(String repositoryUrl) {
        UrlDetector parser = new UrlDetector(repositoryUrl, UrlDetectorOptions.Default);
        Optional<Url> urlOptional = parser.detect().stream().findFirst();

        if (urlOptional.isPresent()) {
            Url url = urlOptional.get();
            return url.getScheme() + "://" + url.getHost();
        }

        throw new PluginRestException(HttpStatus.BAD_REQUEST, "Incorrect repository URL: " + repositoryUrl);
    }
}
