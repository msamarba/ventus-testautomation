package bdd;

import org.junit.platform.suite.api.*;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameters({
        @ConfigurationParameter(
                key = io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME,
                value = "pretty, summary, html:target/cucumber-report.html, json:target/cucumber.json, junit:target/cucumber-junit.xml, io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
        ),
        @ConfigurationParameter(
                key = io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME,
                value = "bdd"
        ),
        @ConfigurationParameter(
                key = io.cucumber.junit.platform.engine.Constants.FILTER_TAGS_PROPERTY_NAME,
                value = "not @wip"
        )
})
public class CucumberTestSuite {}