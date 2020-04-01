package it.bz.davinci.innovationscoreboard.stats.csv;

import it.bz.davinci.innovationscoreboard.stats.FileImportLogService;
import it.bz.davinci.innovationscoreboard.stats.dto.FileImportLogDto;
import it.bz.davinci.innovationscoreboard.stats.es.InnovationInCompaniesWithAtLeast10EmployeesEsDao;
import it.bz.davinci.innovationscoreboard.stats.jpa.FileImportRepository;
import it.bz.davinci.innovationscoreboard.stats.model.FileImport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import({FileImportLogService.class})
public class InnovationInCompaniesWithAtLeast10EmployeesCsvImporterTest {

    private InnovationInCompaniesWithAtLeast10EmployeesCsvImporter innovationInCompaniesWithAtLeast10EmployeesCsvImporter;

    @Autowired
    private FileImportRepository fileImportRepository;

    @Autowired
    private FileImportLogService fileImportLogService;

    @Mock
    private InnovationInCompaniesWithAtLeast10EmployeesEsDao esDao;

    @Mock
    private ApplicationEventPublisher publisher;

    @Before
    public void setup() {
        when(esDao.cleanIndex()).thenReturn(true);
        innovationInCompaniesWithAtLeast10EmployeesCsvImporter = new InnovationInCompaniesWithAtLeast10EmployeesCsvImporter(fileImportLogService, esDao, publisher);
        fileImportRepository.deleteAll();
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldFailIfNoDBEntryIsPresent() {
        innovationInCompaniesWithAtLeast10EmployeesCsvImporter.importFile("src/test/resources/csv/validResearchAndDevelopment2.csv", 1);
    }

    @Test
    public void shouldCleanAndUploadDataToIndex() throws IOException {
        final FileImportLogDto uploadedFile = createUploadedFile("validEmploymentDemographic.csv");
        when(esDao.bulkIndex(anyList())).thenReturn(true);

        innovationInCompaniesWithAtLeast10EmployeesCsvImporter.importFile("src/test/resources/csv/validEmploymentDemographic.csv", uploadedFile.getId());
        final FileImportLogDto fileImport = fileImportLogService.getById(uploadedFile.getId());

        assertThat(fileImport.getStatus(), equalTo(FileImport.Status.PROCESSED_WITH_SUCCESS));
    }

    @Test
    public void shouldMarkUploadAsProcessedWithWarnings() throws IOException {
        final FileImportLogDto uploadedFile = createUploadedFile("employmentDemographicWithFaultyRows.csv");
        when(esDao.bulkIndex(anyList())).thenReturn(true);

        innovationInCompaniesWithAtLeast10EmployeesCsvImporter.importFile("src/test/resources/csv/employmentDemographicWithFaultyRows.csv", uploadedFile.getId());
        final FileImportLogDto fileImport = fileImportLogService.getById(uploadedFile.getId());

        assertThat(fileImport.getStatus(), equalTo(FileImport.Status.PROCESSED_WITH_WARNINGS));
        assertThat(fileImport.getLogs(), equalTo("Line 3: Number of data fields does not match number of headers.\n"));
    }

    @Test
    public void giveFileWithoutASingleProcessableRow_thenMarkUploadAsProcessedWithErrors() throws IOException {
        final FileImportLogDto uploadedFile = createUploadedFile("employmentDemographicWithAllFaultyRows.csv");

        innovationInCompaniesWithAtLeast10EmployeesCsvImporter.importFile("src/test/resources/csv/employmentDemographicWithAllFaultyRows.csv", uploadedFile.getId());
        final FileImportLogDto fileImport = fileImportLogService.getById(uploadedFile.getId());

        assertThat(fileImport.getStatus(), equalTo(FileImport.Status.PROCESSED_WITH_ERRORS));
        assertThat(fileImport.getLogs(), equalTo("Line 2: Conversion of 544a to java.math.BigDecimal failed.\n" +
                "Line 3: Number of data fields does not match number of headers.\n"));
    }

    private FileImportLogDto createUploadedFile(String s) throws IOException {
        MultipartFile multipartFile = createFile(s);
        return fileImportLogService.save(FileImportLogDto.builder()
                .source(multipartFile.getName())
                .importDate(LocalDateTime.now())
                .status(FileImport.Status.UPLOADED).build());
    }

    private MockMultipartFile createFile(String fileName) throws IOException {
        return new MockMultipartFile(fileName, new FileInputStream(new File("src/test/resources/csv/" + fileName)));
    }
}
