package it.bz.davinci.innovationscoreboard.stats.storage;

import it.bz.davinci.innovationscoreboard.stats.FileImportLogService;
import it.bz.davinci.innovationscoreboard.stats.dto.FileImportLogDto;
import it.bz.davinci.innovationscoreboard.utils.io.InMemoryFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FileImportStorageServiceTest {

    @Mock
    private FileImportStorageS3 fileImportStorageS3;

    @Mock
    private FileImportLogService fileImportLogService;

    FileImportStorageService fileImportStorageService;

    @Before
    public void setup() {
        fileImportStorageService = new FileImportStorageService(fileImportStorageS3, fileImportLogService);
    }

    @Test(expected = NoLinkToExternalStorageFoundException.class)
    public void givenFileImportLogWithoutExternalStorageLink_thenThrow() {
        when(fileImportLogService.getById(anyInt())).thenReturn(FileImportLogDto.builder()
                .build());

        fileImportStorageService.download(1);
    }

    @Test
    public void givenFileImportLogWithExternalLink_thenTryToDownload() {

        final String externalStorageLocation = "test.csv";
        when(fileImportLogService.getById(anyInt())).thenReturn(FileImportLogDto.builder()
                .externalStorageLocation(externalStorageLocation)
                .build());

        fileImportStorageService.download(1);
        verify(fileImportStorageS3, times(1)).download(externalStorageLocation);
    }
}