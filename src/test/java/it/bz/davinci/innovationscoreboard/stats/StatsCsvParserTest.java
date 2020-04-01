package it.bz.davinci.innovationscoreboard.stats;

import it.bz.davinci.innovationscoreboard.stats.csv.ResearchAndDevelopmentCsv;
import it.bz.davinci.innovationscoreboard.stats.csv.StatsCsvParser;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class StatsCsvParserTest {

    private final StatsCsvParser<ResearchAndDevelopmentCsv> researchAndDevelopmentStatsCsvParser = new StatsCsvParser<>(ResearchAndDevelopmentCsv.class);


    @Test
    public void successfulParsingByType() throws IOException {

        File file = new File("src/test/resources/csv/validResearchAndDevelopment2.csv");

        researchAndDevelopmentStatsCsvParser.parse(file);

    }

    @Test
    public void unsuccessfulParsingByType() throws IOException {

        File file = new File("src/test/resources/csv/invalidResearchAndDevelopment.csv");

        researchAndDevelopmentStatsCsvParser.parse(file);

    }
}