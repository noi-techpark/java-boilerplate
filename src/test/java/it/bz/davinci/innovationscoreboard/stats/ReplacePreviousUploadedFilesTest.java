package it.bz.davinci.innovationscoreboard.stats;

import it.bz.davinci.innovationscoreboard.stats.jpa.FileImportRepository;
import it.bz.davinci.innovationscoreboard.stats.model.FileImport;
import it.bz.davinci.innovationscoreboard.stats.model.StatsType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({ReplacePreviousUploadedFiles.class, FileImportLogService.class})
public class ReplacePreviousUploadedFilesTest {

    @Autowired
    private FileImportRepository fileImportRepository;

    @Autowired
    private ReplacePreviousUploadedFiles replacePreviousUploadedFiles;

    @Test
    public void replaceAllPreviouslyUploadedFilesOfSameType() {
        final FileImport prevImport = fileImportRepository.save(FileImport.builder().type(StatsType.RESEARCH_AND_DEVELOPMENT).status(FileImport.Status.PROCESSED_WITH_SUCCESS).build());
        final FileImport currentImport = fileImportRepository.save(FileImport.builder().type(StatsType.RESEARCH_AND_DEVELOPMENT).status(FileImport.Status.PROCESSED_WITH_SUCCESS).build());

        replacePreviousUploadedFiles.replacePreviouslyUploadedFilesStatus(currentImport.getId());

        assertThat(prevImport.getStatus(), equalTo(FileImport.Status.REPLACED));
        assertThat(currentImport.getStatus(), equalTo(FileImport.Status.PROCESSED_WITH_SUCCESS));
    }
}