package it.bz.davinci.innovationscoreboard.stats.csv;

import org.junit.Before;
import org.junit.Test;

import static it.bz.davinci.innovationscoreboard.stats.model.StatsType.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class StatsCsvImporterFactoryTest {

    private StatsCsvImporterFactory statsCsvImporterFactory;
    private final ResearchAndDevelopmentCsvImporter researchAndDevelopmentDataImporter = new ResearchAndDevelopmentCsvImporter(null, null, null);
    private final IctInCompaniesWithAtLeast10EmployeesCsvImporter ictInCompaniesWithAtLeast10EmployeesCsvImporter = new IctInCompaniesWithAtLeast10EmployeesCsvImporter(null, null, null);
    private final InnovationInCompaniesWithAtLeast10EmployeesCsvImporter innovationInCompaniesWithAtLeast10EmployeesCsvImporter = new InnovationInCompaniesWithAtLeast10EmployeesCsvImporter(null, null, null);


    @Before
    public void setUp() {
        statsCsvImporterFactory = new StatsCsvImporterFactory(researchAndDevelopmentDataImporter, ictInCompaniesWithAtLeast10EmployeesCsvImporter, innovationInCompaniesWithAtLeast10EmployeesCsvImporter);
    }

    @Test
    public void returnsResearchAndDevelopmentDataImporter() {
        final StatsCsvImporter csvDataImporter = statsCsvImporterFactory.getCsvDataImporter(RESEARCH_AND_DEVELOPMENT);
        assertThat(csvDataImporter, equalTo(researchAndDevelopmentDataImporter));
    }

    @Test
    public void returnsInnovationDataImporter() {
        final StatsCsvImporter csvDataImporter = statsCsvImporterFactory.getCsvDataImporter(INNOVATION_IN_COMPANIES_WITH_AT_LEAST_10_EMPLOYEES);
        assertThat(csvDataImporter, equalTo(innovationInCompaniesWithAtLeast10EmployeesCsvImporter));
    }

    @Test
    public void returnsEmploymentDemographicDataImporter() {
        final StatsCsvImporter csvDataImporter = statsCsvImporterFactory.getCsvDataImporter(ICT_IN_COMPANIES_WITH_AT_LEAST_10_EMPLOYEES);
        assertThat(csvDataImporter, equalTo(ictInCompaniesWithAtLeast10EmployeesCsvImporter));
    }

}