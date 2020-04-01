package it.bz.davinci.innovationscoreboard.stats;

import it.bz.davinci.innovationscoreboard.stats.dto.UploadHistoryResponseDto;
import it.bz.davinci.innovationscoreboard.stats.jpa.FileImportRepository;
import it.bz.davinci.innovationscoreboard.stats.model.FileImport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(FileImportLogService.class)
public class FileImportLogServiceTest {

    @Autowired
    FileImportRepository fileImportRepository;

    @Autowired
    FileImportLogService fileImportLogService;

    @Before
    public void setUp() {
        fileImportRepository.deleteAll();
    }

    @Test
    public void findEmpty() {
        UploadHistoryResponseDto result = fileImportLogService.findAll();
        assertThat(result.getUploadedStats(), is(empty()));
    }

    @Test
    public void findAll() {
        fileImportRepository.save(FileImport.builder()
                .status(FileImport.Status.UPLOADED)
                .importDate(LocalDateTime.now())
                .source("test.csv")
                .build()
        );

        UploadHistoryResponseDto result = fileImportLogService.findAll();
        assertThat(result.getUploadedStats(), hasSize(1));
        assertThat(result.getUploadedStats().get(0).getStatus(), is(equalTo(FileImport.Status.UPLOADED)));
    }
}