package ut.com.vasilievaleksey.plugin.service;

import com.vasilievaleksey.plugin.client.GitlabApiClient;
import com.vasilievaleksey.plugin.dto.RepositoryDto;
import com.vasilievaleksey.plugin.dto.RepositoryInfoDto;
import com.vasilievaleksey.plugin.service.RepositoryService;
import org.gitlab4j.api.models.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class RepositoryServiceTest {
    @Mock
    private GitlabApiClient gitlabApiClient;
    private RepositoryService repositoryService;

    @BeforeEach
    void setUp() {
        repositoryService = new RepositoryService(gitlabApiClient);
    }

    @Test
    void getInfo_IncorrectRepositoryUrl_ThrowException() {
        var incorrectRepositoryUrl = "test";

        var exception = assertThrows(ResponseStatusException.class, () -> repositoryService.getInfo(new RepositoryDto(incorrectRepositoryUrl, "123")));

        assertEquals("Incorrect repository URL: "+ incorrectRepositoryUrl, exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void getInfo() {
        var url = "https://github.com/VasilievAleksey/ext-git-jira-integration";
        var hostUrl = "https://github.com";
        var repositoryName = "ext-git-jira-integration";
        var accessToken = "123456";
        var projectID = 1L;
        var testDescription = "test description";

        var project = new Project();
        project.setId(projectID);
        project.setName(repositoryName);

        project.setDescription(testDescription);

        var build = RepositoryInfoDto.builder()
                .id(projectID)
                .name(repositoryName)
                .description(testDescription)
                .build();

        Mockito.when(gitlabApiClient.findProjectByName(hostUrl, accessToken, repositoryName)).thenReturn(project);

        var obj = repositoryService.getInfo(new RepositoryDto(url, accessToken));

        assertEquals(build, obj);
    }
}