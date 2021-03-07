package com.eviware.soapui.impl.wsdl;

import com.eviware.soapui.config.*;
import org.apache.xmlbeans.XmlException;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.eviware.soapui.impl.wsdl.CompositingUtils.pathProofName;

public class Compositor {

    public InputStream loadCompositeProject(File projectFile) throws IOException, XmlException {

        ProjectConfig project = ProjectConfig.Factory.parse(Paths.get(projectFile.getAbsolutePath(),"composite-project.xml").toFile());
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(projectFile.getAbsolutePath(), "eventHandlers"),"*.xml")) {
            List<EventHandlerTypeConfig> eventHandlerTypeConfigList = new ArrayList<>();
            for(Path file: stream){
                eventHandlerTypeConfigList.add(EventHandlerTypeConfig.Factory.parse(file.toFile()));
            }
            project.setEventHandlersArray(eventHandlerTypeConfigList.toArray(new EventHandlerTypeConfig[0]));
        }

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(projectFile.getAbsolutePath(),"environments"),"*.xml")) {
            List<EnvironmentConfig> environmentConfigList = new ArrayList<>();
            for(Path file: stream){
                environmentConfigList.add(EnvironmentConfig.Factory.parse(file.toFile()));
            }
            project.setEnvironmentArray(environmentConfigList.toArray(new EnvironmentConfig[0]));
        }

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(projectFile.getAbsolutePath(),"mockServices"),"*.xml")) {
            List<MockServiceConfig> mockServiceConfigList = new ArrayList<>();
            for(Path file: stream){
                mockServiceConfigList.add(MockServiceConfig.Factory.parse(file.toFile()));
            }
            project.setMockServiceArray(mockServiceConfigList.toArray(new MockServiceConfig[0]));
        }

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(projectFile.getAbsolutePath(),"restMockServices"),"*.xml")) {
            List<RESTMockServiceConfig> restMockServiceConfigList = new ArrayList<>();
            for(Path file: stream){
                restMockServiceConfigList.add(RESTMockServiceConfig.Factory.parse(file.toFile()));
            }
            project.setRestMockServiceArray(restMockServiceConfigList.toArray(new RESTMockServiceConfig[0]));
        }

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(projectFile.getAbsolutePath(),"interfaces"),"*.xml")) {
            List<InterfaceConfig> interfaceConfigList = new ArrayList<>();
            for(Path file: stream){
                interfaceConfigList.add(InterfaceConfig.Factory.parse(file.toFile()));
            }
            project.setInterfaceArray(interfaceConfigList.toArray(new InterfaceConfig[0]));
        }

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(projectFile.getAbsolutePath(),"testSuites"),"*.xml")) {
            List<TestSuiteConfig> testSuiteConfigList = new ArrayList<>();
            for(Path file: stream){
                TestSuiteConfig config = TestSuiteConfig.Factory.parse(file.toFile());
                testSuiteConfigList.add(config);
                Path testCasesDir = Paths.get( file.toFile().getParentFile().getAbsolutePath() ,pathProofName(config.getName()) + "_testCases");
                try(DirectoryStream<Path> testCases = Files.newDirectoryStream(testCasesDir,"*.xml")){
                    List<TestCaseConfig> testCaseConfigList = new ArrayList<>();
                    for(Path caseFile: testCases) {
                        testCaseConfigList.add(TestCaseConfig.Factory.parse(caseFile.toFile()));
                    }
                    if(!testCaseConfigList.isEmpty()){
                        config.setTestCaseArray(testCaseConfigList.toArray(new TestCaseConfig[0]));
                    }
                }
            }
            project.setTestSuiteArray(testSuiteConfigList.toArray(new TestSuiteConfig[0]));
        }

        File file;
        if((file = Paths.get(projectFile.getAbsolutePath(), "afterLoadScript.xml").toFile()).exists()) {
            project.setAfterLoadScript(ScriptConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "afterRunScript.xml").toFile()).exists()) {
            project.setAfterRunScript(ScriptConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "beforeSaveScript.xml").toFile()).exists()) {
            project.setBeforeSaveScript(ScriptConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "beforeRunScript.xml").toFile()).exists()) {
            project.setBeforeRunScript(ScriptConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "sensitiveInformation.xml").toFile()).exists()) {
            project.setSensitiveInformation(SensitiveInformationConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "endpointStrategy.xml").toFile()).exists()) {
            project.setEndpointStrategy(EndpointStrategyConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "oauth1ProfileContainer.xml").toFile()).exists()) {
            project.setOAuth1ProfileContainer(OAuth1ProfileContainerConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "oauth2ProfileContainer.xml").toFile()).exists()) {
            project.setOAuth2ProfileContainer(OAuth2ProfileContainerConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "properties.xml").toFile()).exists()) {
            project.setProperties(PropertiesTypeConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "wssContainer.xml").toFile()).exists()) {
            project.setWssContainer(WssContainerConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "databaseConnectionContainer.xml").toFile()).exists()) {
            project.setDatabaseConnectionContainer(DatabaseConnectionContainerConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "reporting.xml").toFile()).exists()) {
            project.setReporting(ReportingTypeConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "reportScript.xml").toFile()).exists()) {
            project.setReportScript(ScriptConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "requirements.xml").toFile()).exists()) {
            project.setRequirements(RequirementsTypeConfig.Factory.parse(file));
        }
        if((file = Paths.get(projectFile.getAbsolutePath(), "settings.xml").toFile()).exists()) {
            project.setSettings(SettingsConfig.Factory.parse(file));
        }

        SoapuiProjectDocumentConfig projectDocument = SoapuiProjectDocumentConfig.Factory.newInstance();
        projectDocument.setSoapuiProject(project);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        projectDocument.save(bao);
        byte[] content = bao.toByteArray();
        return new ByteArrayInputStream(content);
    }


}
