package it.bz.davinci.innovationscoreboard.stats;

import com.google.common.collect.ImmutableMap;
import it.bz.davinci.innovationscoreboard.stats.csv.InnovationInCompaniesWithAtLeast10EmployeesCsv;
import it.bz.davinci.innovationscoreboard.stats.csv.ParserResult;
import it.bz.davinci.innovationscoreboard.stats.csv.StatsCsvParser;
import it.bz.davinci.innovationscoreboard.stats.dto.StatisticsResponseDto;
import it.bz.davinci.innovationscoreboard.stats.dto.StatisticsResponseGroupDto;
import it.bz.davinci.innovationscoreboard.stats.dto.StatisticsResponsePerYearDto;
import it.bz.davinci.innovationscoreboard.stats.es.InnovationInCompaniesWithAtLeast10EmployeesEs;
import it.bz.davinci.innovationscoreboard.stats.es.InnovationInCompaniesWithAtLeast10EmployeesEsDao;
import it.bz.davinci.innovationscoreboard.stats.mapper.EmploymentDemographicMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnterprisesWithInnovationAggregatorTest {

    private final StatsCsvParser<InnovationInCompaniesWithAtLeast10EmployeesCsv> csvParser = new StatsCsvParser<>(InnovationInCompaniesWithAtLeast10EmployeesCsv.class);

    @Mock
    private InnovationInCompaniesWithAtLeast10EmployeesEsDao innovationInCompaniesWithAtLeast10EmployeesEsDao;

    private EnterprisesWithInnovationAggregator enterprisesWithInnovationAggregator;

    @Before
    public void setup() {
        enterprisesWithInnovationAggregator = new EnterprisesWithInnovationAggregator(innovationInCompaniesWithAtLeast10EmployeesEsDao);
    }

    @Test
    public void givenValidEnterprisesWithInnovationActivitiesDividedByTerritory_returnGroupedData() throws IOException {
        when(innovationInCompaniesWithAtLeast10EmployeesEsDao.getEnterprisesWithInnovationActivitiesDividedByTerritory())
                .thenReturn(generateData("src/test/resources/csv/employment-demographic/enterprises-with-innovation-activities-divided-by-territory.csv"));

        final StatisticsResponseDto result = enterprisesWithInnovationAggregator.getEnterprisesWithInnovationActivitiesDividedByTerritory();

        final StatisticsResponsePerYearDto it2016 = result.getStatistics().get("IT").stream()
                .filter(entry -> entry.getYear().equals("2016"))
                .findFirst()
                .get();

        assertThat(it2016.getTotal(), equalTo(BigDecimal.valueOf(76895)));
        assertThat(it2016.getGroups(), contains(StatisticsResponseGroupDto.builder()
                .id("FORMA_INNOVAZ")
                .values(ImmutableMap.of("PTCON", BigDecimal.valueOf(19268), "OMKON", BigDecimal.valueOf(16757), "PTCOMK", BigDecimal.valueOf(40870)))
                .build()
        ));
    }

    @Test
    public void giveValidData_groupByATECO_2007() throws IOException {
        when(innovationInCompaniesWithAtLeast10EmployeesEsDao.getEnterprisesThatHaveIntroducedProductOrProcessInnovationsInItalyDividedByNace())
                .thenReturn(generateData("src/test/resources/csv/employment-demographic/enterprises-with-innovation-activities-in-italy-divided-by-NACE.csv"));

        final StatisticsResponseDto result = enterprisesWithInnovationAggregator.getEnterprisesThatHaveIntroducedProductOrProcessInnovationsInItalyDividedByNace();

        final StatisticsResponsePerYearDto it2016 = result.getStatistics().get("IT").stream()
                .filter(entry -> entry.getYear().equals("2016"))
                .findFirst()
                .get();

        assertThat(it2016.getTotal(), equalTo(BigDecimal.valueOf(341388)));

        HashMap<String, BigDecimal> values = new HashMap<String, BigDecimal>() {{
            put("45", BigDecimal.valueOf(2089));
            put("46", BigDecimal.valueOf(9293));
            put("52_53", BigDecimal.valueOf(2177));
            put("47", BigDecimal.valueOf(5371));
            put("14_15", BigDecimal.valueOf(3718));
            put("70_74", BigDecimal.valueOf(2974));
            put("13", BigDecimal.valueOf(1315));
            put("16", BigDecimal.valueOf(941));
            put("17", BigDecimal.valueOf(795));
            put("18", BigDecimal.valueOf(1124));
            put("19", BigDecimal.valueOf(63));
            put("0020", BigDecimal.valueOf(41203));
            put("00110", BigDecimal.valueOf(29769));
            put("0011", BigDecimal.valueOf(47126));
            put("24_25", BigDecimal.valueOf(7635));
            put("61", BigDecimal.valueOf(198));
            put("62", BigDecimal.valueOf(2663));
            put("63", BigDecimal.valueOf(743));
            put("20", BigDecimal.valueOf(1311));
            put("64", BigDecimal.valueOf(643));
            put("21", BigDecimal.valueOf(232));
            put("65", BigDecimal.valueOf(116));
            put("22", BigDecimal.valueOf(2400));
            put("66", BigDecimal.valueOf(699));
            put("23", BigDecimal.valueOf(1133));
            put("B", BigDecimal.valueOf(112));
            put("C", BigDecimal.valueOf(39730));
            put("D", BigDecimal.valueOf(260));
            put("26", BigDecimal.valueOf(1300));
            put("E", BigDecimal.valueOf(1101));
            put("27", BigDecimal.valueOf(1698));
            put("F", BigDecimal.valueOf(5923));
            put("28", BigDecimal.valueOf(6184));
            put("G", BigDecimal.valueOf(16753));
            put("29", BigDecimal.valueOf(674));
            put("H", BigDecimal.valueOf(4553));
            put("J", BigDecimal.valueOf(4033));
            put("00100", BigDecimal.valueOf(76895));
            put("K", BigDecimal.valueOf(1457));
            put("58_60", BigDecimal.valueOf(429));
            put("70", BigDecimal.valueOf(814));
            put("71", BigDecimal.valueOf(618));
            put("72", BigDecimal.valueOf(242));
            put("73", BigDecimal.valueOf(553));
            put("30", BigDecimal.valueOf(423));
            put("74", BigDecimal.valueOf(747));
            put("CA", BigDecimal.valueOf(4489));
            put("31", BigDecimal.valueOf(1504));
            put("32", BigDecimal.valueOf(889));
            put("33", BigDecimal.valueOf(1901));
            put("49_51", BigDecimal.valueOf(2375));
        }};

        assertThat(it2016.getGroups(), contains(StatisticsResponseGroupDto.builder()
                .id("ATECO_2007")
                .values(values)
                .build()
        ));
    }

    private List<InnovationInCompaniesWithAtLeast10EmployeesEs> generateData(String path) throws IOException {
        File file = new File(path);
        final ParserResult<InnovationInCompaniesWithAtLeast10EmployeesCsv> parserResult = csvParser.parse(file);
        return parserResult.getData().stream().map(EmploymentDemographicMapper.INSTANCE::toEs).collect(Collectors.toList());
    }
}