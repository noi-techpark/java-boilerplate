package it.bz.davinci.innovationscoreboard.stats;

import com.google.common.collect.ImmutableMap;
import it.bz.davinci.innovationscoreboard.stats.csv.ParserResult;
import it.bz.davinci.innovationscoreboard.stats.csv.ResearchAndDevelopmentCsv;
import it.bz.davinci.innovationscoreboard.stats.csv.StatsCsvParser;
import it.bz.davinci.innovationscoreboard.stats.dto.StatisticsResponseDto;
import it.bz.davinci.innovationscoreboard.stats.dto.StatisticsResponseGroupDto;
import it.bz.davinci.innovationscoreboard.stats.dto.StatisticsResponsePerYearDto;
import it.bz.davinci.innovationscoreboard.stats.es.ResearchAndDevelopmentEs;
import it.bz.davinci.innovationscoreboard.stats.es.ResearchAndDevelopmentEsDao;
import it.bz.davinci.innovationscoreboard.stats.mapper.ResearchAndDevelopmentMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResearchAndDevelopmentAggregatorTest {

    @Mock
    private ResearchAndDevelopmentEsDao researchAndDevelopmentEsDao;

    private ResearchAndDevelopmentAggregator researchAndDevelopmentAggregator;
    private final StatsCsvParser<ResearchAndDevelopmentCsv> researchAndDevelopmentStatsCsvParser = new StatsCsvParser<>(ResearchAndDevelopmentCsv.class);

    @Before
    public void setup() {
        researchAndDevelopmentAggregator = new ResearchAndDevelopmentAggregator(researchAndDevelopmentEsDao);
    }

    @Test
    public void givenEmptyList_returnEmptyStatistics() {
        when(researchAndDevelopmentEsDao.getResearchAndDevelopmentPersonnelInHouseDividedByTerritory()).thenReturn(emptyList());
        final StatisticsResponseDto result = researchAndDevelopmentAggregator.getResearchAndDevelopmentPersonnelInHouseDividedByTerritory();

        assertThat(result.getStatistics().size(), equalTo(0));
    }

    @Test
    public void giveListOfData_returnGroupedStatistics() throws IOException {
        when(researchAndDevelopmentEsDao.getResearchAndDevelopmentPersonnelInHouseDividedByTerritory())
                .thenReturn(generateData("src/test/resources/csv/research-and-development/personel-in-house-divided-by-territory.csv"));

        final StatisticsResponseDto result = researchAndDevelopmentAggregator.getResearchAndDevelopmentPersonnelInHouseDividedByTerritory();

        final Collection<StatisticsResponsePerYearDto> resultsForSardegna = result.getStatistics().get("ITG2");
        assertThat(resultsForSardegna, contains(
                StatisticsResponsePerYearDto.builder()
                        .year("2015")
                        .total(BigDecimal.valueOf(6848))
                        .groups(createGroups(
                                ImmutableMap.of("1", BigDecimal.valueOf(3858), "2", BigDecimal.valueOf(2990)),
                                ImmutableMap.of("60", BigDecimal.valueOf(3492), "others", BigDecimal.valueOf(3356)),
                                ImmutableMap.of("S20", BigDecimal.valueOf(620), "S22", BigDecimal.valueOf(4738), "S16", BigDecimal.valueOf(1452), "S19", BigDecimal.valueOf(38))))
                        .build(),
                StatisticsResponsePerYearDto.builder()
                        .year("2014")
                        .total(BigDecimal.valueOf(6111))
                        .groups(createGroups(
                                ImmutableMap.of("1", BigDecimal.valueOf(3342), "2", BigDecimal.valueOf(2769)),
                                ImmutableMap.of("60", BigDecimal.valueOf(3334), "others", BigDecimal.valueOf(2777)),
                                ImmutableMap.of("S20", BigDecimal.valueOf(401), "S22", BigDecimal.valueOf(4281), "S16", BigDecimal.valueOf(1409), "S19", BigDecimal.valueOf(20))))
                        .build(),
                StatisticsResponsePerYearDto.builder()
                        .year("2013")
                        .total(BigDecimal.valueOf(6281))
                        .groups(createGroups(
                                ImmutableMap.of("1", BigDecimal.valueOf(3393), "2", BigDecimal.valueOf(2888)),
                                ImmutableMap.of("60", BigDecimal.valueOf(3134), "others", BigDecimal.valueOf(3147)),
                                ImmutableMap.of("S20", BigDecimal.valueOf(370), "S22", BigDecimal.valueOf(4564), "S16", BigDecimal.valueOf(1325), "S19", BigDecimal.valueOf(22))))
                        .build(),
                StatisticsResponsePerYearDto.builder()
                        .year("2012")
                        .total(BigDecimal.valueOf(6217))
                        .groups(createGroups(
                                ImmutableMap.of("1", BigDecimal.valueOf(3438), "2", BigDecimal.valueOf(2779)),
                                ImmutableMap.of("60", BigDecimal.valueOf(3343), "others", BigDecimal.valueOf(2874)),
                                ImmutableMap.of("S20", BigDecimal.valueOf(415), "S22", BigDecimal.valueOf(4463), "S16", BigDecimal.valueOf(1319), "S19", BigDecimal.valueOf(20))))
                        .build()
        ));
    }

    @Test
    public void givenDataWithEmptyEntryValue_returnNullAsTotalValue() throws IOException {
        when(researchAndDevelopmentEsDao.getDomesticResearchAndDevelopmentExpenditureInHouseDividedByTerritory())
                .thenReturn(generateData("src/test/resources/csv/research-and-development/domestic-research-and-development-with-empty-value.csv"));

        final StatisticsResponseDto result = researchAndDevelopmentAggregator.getDomesticResearchAndDevelopmentExpenditureInHouseDividedByTerritory();

        final StatisticsResponsePerYearDto itf2015 = result.getStatistics().get("ITF").stream()
                .filter(statisticsResponsePerYearDto -> statisticsResponsePerYearDto.getYear().equals("2015"))
                .findFirst()
                .get();

        assertThat(itf2015.getTotal(), equalTo(null));
    }

    @Test
    public void givenDataValidEntryValue_returnTotalValue() throws IOException {
        when(researchAndDevelopmentEsDao.getDomesticResearchAndDevelopmentExpenditureInHouseDividedByTerritory())
                .thenReturn(generateData("src/test/resources/csv/research-and-development/domestic-research-and-development-with-empty-value.csv"));

        final StatisticsResponseDto result = researchAndDevelopmentAggregator.getDomesticResearchAndDevelopmentExpenditureInHouseDividedByTerritory();

        final StatisticsResponsePerYearDto itf2014 = result.getStatistics().get("ITF").stream()
                .filter(statisticsResponsePerYearDto -> statisticsResponsePerYearDto.getYear().equals("2014"))
                .findFirst()
                .get();

        assertThat(itf2014.getTotal(), equalTo(BigDecimal.valueOf(2622460000L)));
    }

    private List<StatisticsResponseGroupDto> createGroups(Map<String, BigDecimal> sexistatValues, Map<String, BigDecimal> profiloProfValues, Map<String, BigDecimal> SETTISTSEC2010Values) {
        List<StatisticsResponseGroupDto> result = new ArrayList<>();
        StatisticsResponseGroupDto sexistat = new StatisticsResponseGroupDto();
        sexistat.setId("SEXISTAT1");
        sexistat.setValues(sexistatValues);

        StatisticsResponseGroupDto profiloProf = new StatisticsResponseGroupDto();
        profiloProf.setId("PROFILO_PROF");
        profiloProf.setValues(profiloProfValues);

        StatisticsResponseGroupDto SETTISTSEC2010 = new StatisticsResponseGroupDto();
        SETTISTSEC2010.setId("SETTISTSEC2010");
        SETTISTSEC2010.setValues(SETTISTSEC2010Values);

        result.add(sexistat);
        result.add(profiloProf);
        result.add(SETTISTSEC2010);
        return result;
    }

    private List<ResearchAndDevelopmentEs> generateData(String path) throws IOException {
        File file = new File(path);
        final ParserResult<ResearchAndDevelopmentCsv> parserResult = researchAndDevelopmentStatsCsvParser.parse(file);
        return parserResult.getData().stream().map(ResearchAndDevelopmentMapper.INSTANCE::toEs).collect(Collectors.toList());
    }
}