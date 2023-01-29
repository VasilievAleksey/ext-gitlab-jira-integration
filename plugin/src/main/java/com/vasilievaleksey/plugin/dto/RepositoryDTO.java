package com.vasilievaleksey.plugin.dto;

import lombok.Value;

public enum RepositoryDTO {;

    public enum Request {;
        @Value public static class Credential {
            String url;
            String accessToken;
        }
    }

    public enum Response {;
        @Value public static class RepositoryInfo {
            Long id;
            String name;
            String description;
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
}
