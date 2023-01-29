package com.vasilievaleksey.plugin.dto;

import com.linkedin.urls.Url;
import com.linkedin.urls.detection.UrlDetector;
import com.linkedin.urls.detection.UrlDetectorOptions;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

public enum RepositoryDTO {;

    public enum Request {;
        public record Credential(String url, String accessToken) {
            public String repositoryName() {
                return parseRepositoryName(url);
            }

            public String hostUrl() {
                return parseHostUrl(url);
            }
        }
    }

    public enum Response {;
        public record RepositoryInfo(Long id, String name, String description) {}
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

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect repository URL: " + repositoryUrl);
    }
}


