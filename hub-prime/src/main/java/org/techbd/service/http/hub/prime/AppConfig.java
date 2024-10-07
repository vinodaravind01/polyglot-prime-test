package org.techbd.service.http.hub.prime;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.techbd.conf.Configuration;

@org.springframework.context.annotation.Configuration
@ConfigurationProperties(prefix = "org.techbd.service.http.hub.prime")
@ConfigurationPropertiesScan
public class AppConfig {

    public static class Servlet {

        public static final String FHIR_CONTENT_TYPE_HEADER_VALUE = "application/fhir+json";

        public static class HeaderName {

            public static class Request {

                public static final String FHIR_STRUCT_DEFN_PROFILE_URI = Configuration.Servlet.HeaderName.PREFIX
                        + "FHIR-Profile-URI";
                public static final String FHIR_VALIDATION_STRATEGY = Configuration.Servlet.HeaderName.PREFIX
                        + "FHIR-Validation-Strategy";
                public static final String DATALAKE_API_URL = Configuration.Servlet.HeaderName.PREFIX
                        + "DataLake-API-URL";
                public static final String DATALAKE_API_CONTENT_TYPE = Configuration.Servlet.HeaderName.PREFIX
                        + "DataLake-API-Content-Type";
                public static final String HEALTH_CHECK_HEADER = Configuration.Servlet.HeaderName.PREFIX
                        + "HealthCheck";
            }

            public static class Response {
                // in case they're necessary
            }
        }
    }

    private String version;
    private String defaultSdohFhirProfileUrl;
    private String defaultDatalakeApiUrl;
    private Map<String, String> structureDefinitionsUrls;
    private Map<String, String> codeSystemUrls;
    private Map<String, String> valueSetUrls;
    private DefaultDataLakeApiAuthn defaultDataLakeApiAuthn;
    private String fhirVesrion;
    private Map<String, Map<String, String>> igPackages;

    public String getVersion() {
        return version;
    }

    /**
     * Spring Boot will retrieve required value from properties file which is
     * injected from pom.xml.
     *
     * @param version the version of the application
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public String getDefaultSdohFhirProfileUrl() {
        return defaultSdohFhirProfileUrl;
    }

    public void setDefaultSdohFhirProfileUrl(String fhirVesrion) {
        this.defaultSdohFhirProfileUrl = fhirVesrion;
    }

    public String getDefaultDatalakeApiUrl() {
        return defaultDatalakeApiUrl;
    }

    public void setDefaultDatalakeApiUrl(String defaultDatalakeApiUrl) {
        this.defaultDatalakeApiUrl = defaultDatalakeApiUrl;
    }

    public void setStructureDefinitionsUrls(Map<String, String> structureDefinitionsUrls) {
        this.structureDefinitionsUrls = structureDefinitionsUrls;
    }

    public Map<String, String> getStructureDefinitionsUrls() {
        return structureDefinitionsUrls;
    }

    public void setCodeSystemUrls(Map<String, String> codeSystemUrls) {
        this.codeSystemUrls = codeSystemUrls;
    }

    public void setValueSetUrls(Map<String, String> valueSetUrls) {
        this.valueSetUrls = valueSetUrls;
    }

    public Map<String, String> getCodeSystemUrls() {
        return codeSystemUrls;
    }

    public Map<String, String> getValueSetUrls() {
        return valueSetUrls;
    }

    public DefaultDataLakeApiAuthn getDefaultDataLakeApiAuthn() {
        return defaultDataLakeApiAuthn;
    }

    public void setDefaultDataLakeApiAuthn(DefaultDataLakeApiAuthn defaultDataLakeApiAuthn) {
        this.defaultDataLakeApiAuthn = defaultDataLakeApiAuthn;
    }

    public record DefaultDataLakeApiAuthn(
            String mTlsStrategy,
            MTlsAwsSecrets mTlsAwsSecrets,
            PostStdinPayloadToNyecDataLakeExternal postStdinPayloadToNyecDataLakeExternal,
            MTlsResources mTlsResources) {
    }

    public record MTlsResources(String mTlsKeyResourceName, String mTlsCertResourceName) {
    }

    public record MTlsAwsSecrets(String mTlsKeySecretName, String mTlsCertSecretName) {
    }

    public record PostStdinPayloadToNyecDataLakeExternal(String cmd, int timeout) {
    }

    public String getfhirVesrion() {
        return fhirVesrion;
    }

    public void setfhirVesrion(String fhirVesrion) {
        this.fhirVesrion = fhirVesrion;
    }

    public Map<String, Map<String, String>> getIgPackages() {
        return igPackages;
    }

    public void setIgPackages(Map<String, Map<String, String>> igPackages) {
        this.igPackages = igPackages;
    }
}
