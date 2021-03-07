package com.eviware.soapui.impl.wsdl;

import com.eviware.soapui.config.*;
import com.eviware.soapui.model.project.SaveStatus;
import org.apache.xmlbeans.XmlObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.eviware.soapui.impl.wsdl.CompositingUtils.pathProofName;

public class Decompositor {
    public SaveStatus saveComposite(SoapuiProjectDocumentConfig projectDocument, File projectFile) throws IOException {
        if(!projectFile.isDirectory() || !createSaveStructure(projectFile)){
            return SaveStatus.FAILED;
        }

        ProjectConfig project = projectDocument.getSoapuiProject();
        Path testSuites = Paths.get(projectFile.getAbsolutePath(),"testSuites");
        Path interfaces = Paths.get(projectFile.getAbsolutePath(), "interfaces");
        Path mockServices = Paths.get(projectFile.getAbsolutePath(), "mockServices");
        Path restMockServices = Paths.get(projectFile.getAbsolutePath(), "restMockServices");
        Path environments = Paths.get(projectFile.getAbsolutePath(), "environments");
        Path eventHandlers = Paths.get(projectFile.getAbsolutePath(), "eventHandlers");
        Path afterLoadScript = Paths.get(projectFile.getAbsolutePath(),"afterLoadScript.xml");
        Path afterRunScript = Paths.get(projectFile.getAbsolutePath(),"afterRunScript.xml");
        Path beforeSaveScript = Paths.get(projectFile.getAbsolutePath(),"beforeSaveScript.xml");
        Path beforeRunScript = Paths.get(projectFile.getAbsolutePath(),"beforeRunScript.xml");
        Path sensitiveInformation = Paths.get(projectFile.getAbsolutePath(),"sensitiveInformation.xml");
        Path endpointStrategy = Paths.get(projectFile.getAbsolutePath(),"endpointStrategy.xml");
        Path oauth1ProfileContainer = Paths.get(projectFile.getAbsolutePath(),"oauth1ProfileContainer.xml");
        Path oauth2ProfileContainer = Paths.get(projectFile.getAbsolutePath(),"oauth2ProfileContainer.xml");
        Path properties = Paths.get(projectFile.getAbsolutePath(),"properties.xml");
        Path wssContainer = Paths.get(projectFile.getAbsolutePath(),"wssContainer.xml");
        Path databaseConnectionContainer = Paths.get(projectFile.getAbsolutePath(),"databaseConnectionContainer.xml");
        Path reporting = Paths.get(projectFile.getAbsolutePath(),"reporting.xml");
        Path reportScript = Paths.get(projectFile.getAbsolutePath(),"reportScript.xml");
        Path requirements = Paths.get(projectFile.getAbsolutePath(),"requirements.xml");
        Path settings = Paths.get(projectFile.getAbsolutePath(),"settings.xml");

        List<TestSuiteConfig> testSuiteConfigList = project.getTestSuiteList();
        List<InterfaceConfig> interfaceConfigList = project.getInterfaceList();
        List<MockServiceConfig> mockServiceConfigList = project.getMockServiceList();
        List<RESTMockServiceConfig> restMockServiceConfigList = project.getRestMockServiceList();
        List<EnvironmentConfig> environmentConfigList = project.getEnvironmentList();
        List<EventHandlerTypeConfig> eventHandlerTypeConfigList = project.getEventHandlersList();
        ScriptConfig afterLoadScriptConfig = project.getAfterLoadScript();
        ScriptConfig afterRunScriptConfig = project.getAfterRunScript();
        ScriptConfig beforeRunScriptConfig = project.getBeforeRunScript();
        ScriptConfig beforeSaveScriptConfig = project.getBeforeSaveScript();
        SensitiveInformationConfig sensitiveInformationConfig = project.getSensitiveInformation();
        EndpointStrategyConfig endpointStrategyConfig = project.getEndpointStrategy();
        OAuth1ProfileContainerConfig oAuth1ProfileContainerConfig = project.getOAuth1ProfileContainer();
        OAuth2ProfileContainerConfig oAuth2ProfileContainerConfig = project.getOAuth2ProfileContainer();
        PropertiesTypeConfig propertiesTypeConfig = project.getProperties();
        WssContainerConfig wssContainerConfig = project.getWssContainer();
        DatabaseConnectionContainerConfig databaseConnectionContainerConfig = project.getDatabaseConnectionContainer();
        ReportingTypeConfig reportingTypeConfig = project.getReporting();
        ScriptConfig reportScriptConfig = project.getReportScript();
        RequirementsTypeConfig requirementsTypeConfig = project.getRequirements();
        SettingsConfig settingsConfig = project.getSettings();


        for( TestSuiteConfig testSuiteConfig: testSuiteConfigList)  {
            String name = pathProofName(testSuiteConfig.getName());

            if(!testSuiteConfig.getTestCaseList().isEmpty()) {
                Paths.get(testSuites.toFile().getAbsolutePath(),name + "_testCases").toFile().mkdir();
            }
            for (TestCaseConfig testCaseConfig: testSuiteConfig.getTestCaseList()){
                if(!testCaseConfig.getTestStepList().isEmpty()) {
                    Paths.get(testSuites.toFile().getAbsolutePath(), name + "_testCases", pathProofName(testCaseConfig.getName()) + "_steps").toFile().mkdir();
                    for(int x = 0; x < testCaseConfig.getTestStepList().size(); x++) {
                        File step = Paths.get(testSuites.toFile().getAbsolutePath(), name + "_testCases",
                                pathProofName(testCaseConfig.getName()) + "_steps",
                                "step_" + x + "_" + pathProofName(testCaseConfig.getTestStepArray(x).getName()) + ".xml")
                                .toFile();
                        testCaseConfig.getTestStepArray(x).save(step);
                    }
                }
                /*
                TestCaseConfig testCase = TestCaseConfig.Factory.newInstance();
                testCase.setAmfAuthorisation(testCaseConfig.getAmfAuthorisation());
                testCase.setAmfEndpoint(testCaseConfig.getAmfEndpoint());
                testCase.setDisabled(testCaseConfig.getDisabled());
                testCase.setAmfLogin(testCaseConfig.getAmfLogin());
                testCase.setAmfPassword(testCaseConfig.getAmfPassword());
                testCase.setBreakPointsArray(testCaseConfig.getBreakPointsList().toArray(new BreakPointConfig[0]));
                testCase.setDiscardOkResults(testCaseConfig.getDiscardOkResults());
                testCase.setFailOnError(testCaseConfig.getFailOnError());
                testCase.setFailTestCaseOnErrors(testCaseConfig.getFailTestCaseOnErrors());
                testCase.setKeepSession(testCaseConfig.getKeepSession());
                testCase.setMaxResults(testCaseConfig.getMaxResults());
                testCase.setProperties(testCaseConfig.getProperties());
                testCase.setLoadTestArray(testCaseConfig.getLoadTestList().toArray(new LoadTestConfig[0]));
                testCase.setReportParameters(testCaseConfig.getReportParameters());
                testCase.setReportScript(testCaseConfig.getReportScript());
                testCase.setReportTemplatesArray(testCaseConfig.getReportTemplatesList().toArray(new ReportTemplateConfig[0]));
                testCase.setRequirements(testCaseConfig.getRequirements());
                testCase.setSearchProperties(testCaseConfig.getSearchProperties());
                testCase.setSecurityTestArray(testCaseConfig.getSecurityTestList().toArray(new SecurityTestConfig[0]));
                testCase.setSetupScript(testCaseConfig.getSetupScript());
                testCase.setTearDownScript(testCaseConfig.getTearDownScript());
                testCase.setTimeout(testCaseConfig.getTimeout());
                testCase.setWsrmAckTo(testCaseConfig.getWsrmAckTo());
                testCase.setWsrmEnabled(testCaseConfig.getWsrmEnabled());
                testCase.setWsrmExpires(testCaseConfig.getWsrmExpires());
                testCase.setWsrmVersion(testCaseConfig.getWsrmVersion());
                testCase.setDescription(testCaseConfig.getDescription());
                testCase.setId(testCaseConfig.getId());
                testCase.setName(testCaseConfig.getName());
                testCase.setSettings(testCaseConfig.getSettings());
                testCase.setTimestamp(testCaseConfig.getTimestamp());
                */
                testCaseConfig.setTestStepArray(null);
                testCaseConfig.save(Paths.get(testSuites.toFile().getAbsolutePath(), name +  "_testCases", pathProofName(testCaseConfig.getName()) + ".xml" ).toFile());
            }
            testSuiteConfig.setTestCaseArray(null);
            testSuiteConfig.save(Paths.get(testSuites.toFile().getAbsolutePath(),name + ".xml").toFile());
        }
        for(InterfaceConfig interfaceConfig: interfaceConfigList){
            String name = pathProofName(interfaceConfig.getName()) + "_interface.xml";
            interfaceConfig.save(Paths.get(interfaces.toFile().getAbsolutePath(), name).toFile());
        }
        for(MockServiceConfig mockServiceConfig: mockServiceConfigList){
            String name = pathProofName(mockServiceConfig.getName()) + "_mockServiceConfig.xml";
            mockServiceConfig.save(Paths.get(mockServices.toFile().getAbsolutePath(), name).toFile());
        }
        for(RESTMockServiceConfig restMockServiceConfig: restMockServiceConfigList){
            String name = pathProofName(restMockServiceConfig.getName()) + "_restMockServiceConfig.xml";
            restMockServiceConfig.save(Paths.get(restMockServices.toFile().getAbsolutePath(), name).toFile());
        }
        for(EnvironmentConfig environmentConfig: environmentConfigList){
            String name = pathProofName(environmentConfig.getName()) + "_environment.xml";
            environmentConfig.save(Paths.get(environments.toFile().getAbsolutePath(), name).toFile());
        }
        for(EventHandlerTypeConfig eventHandlerTypeConfig: eventHandlerTypeConfigList){
            String name = pathProofName(eventHandlerTypeConfig.getName()) + "_eventHandler.xml";
            eventHandlerTypeConfig.save(Paths.get(eventHandlers.toFile().getAbsolutePath(), name).toFile());
        }

        saveIfNotNull(afterLoadScriptConfig, afterLoadScript.toFile());
        saveIfNotNull(afterRunScriptConfig, afterRunScript.toFile());
        saveIfNotNull(beforeRunScriptConfig, beforeRunScript.toFile());
        saveIfNotNull(beforeSaveScriptConfig, beforeSaveScript.toFile());
        saveIfNotNull(sensitiveInformationConfig, sensitiveInformation.toFile());
        saveIfNotNull(endpointStrategyConfig, endpointStrategy.toFile());
        saveIfNotNull(oAuth1ProfileContainerConfig, oauth1ProfileContainer.toFile());
        saveIfNotNull(oAuth2ProfileContainerConfig, oauth2ProfileContainer.toFile());
        saveIfNotNull(propertiesTypeConfig , properties.toFile());
        saveIfNotNull(wssContainerConfig, wssContainer.toFile());
        saveIfNotNull(databaseConnectionContainerConfig, databaseConnectionContainer.toFile());
        saveIfNotNull(reportingTypeConfig, reporting.toFile());
        saveIfNotNull(reportScriptConfig, reportScript.toFile());
        saveIfNotNull(requirementsTypeConfig, requirements.toFile());
        saveIfNotNull(settingsConfig, settings.toFile());

        project.save(Paths.get(projectFile.getPath(),"composite-project.xml").toFile());

        return SaveStatus.SUCCESS;
    }

    private void saveIfNotNull(XmlObject object, File file) throws IOException {
        if(object != null){
            object.save(file);
        }
    }

    private boolean createSaveStructure(File projectFile){
        if(!projectFile.isDirectory()){
            return false;
        }

        Path testSuites = Paths.get(projectFile.getAbsolutePath(),"testSuites");
        Path interfaces = Paths.get(projectFile.getAbsolutePath(), "interfaces");
        Path mockServices = Paths.get(projectFile.getAbsolutePath(), "mockServices");
        Path restMockServices = Paths.get(projectFile.getAbsolutePath(), "restMockServices");
        Path environments = Paths.get(projectFile.getAbsolutePath(), "environments");
        Path eventHandlers = Paths.get(projectFile.getAbsolutePath(), "eventHandlers");

        boolean success = true;
        if(!testSuites.toFile().exists()){
            success = testSuites.toFile().mkdir();
        }
        if(!interfaces.toFile().exists()){
            success &= interfaces.toFile().mkdir();
        }
        if(!mockServices.toFile().exists()){
            success &= mockServices.toFile().mkdir();
        }
        if(!restMockServices.toFile().exists()){
            success &= restMockServices.toFile().mkdir();
        }
        if(!environments.toFile().exists()){
            success &= environments.toFile().mkdir();
        }
        if(!eventHandlers.toFile().exists()){
            success &= eventHandlers.toFile().mkdir();
        }

        return success;
    }
}
