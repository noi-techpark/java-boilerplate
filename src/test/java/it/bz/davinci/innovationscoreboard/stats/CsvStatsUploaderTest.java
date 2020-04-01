package it.bz.davinci.innovationscoreboard.stats;

import it.bz.davinci.innovationscoreboard.stats.dto.FileImportLogDto;
import it.bz.davinci.innovationscoreboard.stats.model.StatsType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CsvStatsUploaderTest {

    @Mock
    private FileImportLogService fileImportLogService;

    @Mock
    private CsvStatsProcessor csvStatsProcessor;

    private CsvStatsUploader csvStatsUploader;

    @Before
    public void setUp() {
        csvStatsUploader = new CsvStatsUploader(csvStatsProcessor, fileImportLogService);
    }


    @Test
    public void givenSupportedFile_startUpload() throws IOException {

        final FileImportLogDto fileImportDto = FileImportLogDto.builder().id(1).build();
        when(fileImportLogService.save(any())).thenReturn(fileImportDto);
        MultipartFile multipartFile = new MockMultipartFile("validResearchAndDevelopment2.csv", new FileInputStream(new File("src/test/resources/csv/validResearchAndDevelopment2.csv")));
        csvStatsUploader.importFile(multipartFile);

        verify(csvStatsProcessor, times(1)).process(anyString(), anyInt(), eq(StatsType.RESEARCH_AND_DEVELOPMENT));
    }

    @Test
    public void givenValidInnovationFile_startUpload() throws IOException {

        when(fileImportLogService.save(any())).thenReturn(FileImportLogDto.builder().id(1).build());
        MultipartFile multipartFile = new MockMultipartFile("validInnovation.csv", new FileInputStream(new File("src/test/resources/csv/validInnovation.csv")));
        csvStatsUploader.importFile(multipartFile);

        verify(csvStatsProcessor, times(1)).process(anyString(), anyInt(), eq(StatsType.ICT_IN_COMPANIES_WITH_AT_LEAST_10_EMPLOYEES));
    }

    @Test
    public void givenValidEmploymentDemographicFile_startUpload() throws IOException {

        when(fileImportLogService.save(any())).thenReturn(FileImportLogDto.builder().id(1).build());
        MultipartFile multipartFile = new MockMultipartFile("validEmploymentDemographic.csv", new FileInputStream(new File("src/test/resources/csv/validEmploymentDemographic.csv")));
        csvStatsUploader.importFile(multipartFile);

        verify(csvStatsProcessor, times(1)).process(anyString(), anyInt(), eq(StatsType.INNOVATION_IN_COMPANIES_WITH_AT_LEAST_10_EMPLOYEES));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void givenUnsupportedFile_throwException() throws IOException {

        MultipartFile multipartFile = new MockMultipartFile("invalidResearchAndDevelopmentHeader.csv", new FileInputStream(new File("src/test/resources/csv/invalidResearchAndDevelopmentHeader.csv")));
        csvStatsUploader.importFile(multipartFile);
    }
}