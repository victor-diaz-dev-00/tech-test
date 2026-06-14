package com.gft.techtest;

import com.gft.techtest.prices.application.port.in.ApplicablePricePort;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.gft.techtest",
        importOptions = { ImportOption.DoNotIncludeTests.class })
public class ArchitectureTest {

    @ArchTest
    static final ArchRule domain_should_not_depend_on_infrastructure = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
            .because("Domain layer should not depend on infrastructure in hexagonal architecture");

    @ArchTest
    static final ArchRule ports_should_be_interfaces = classes()
            .that().resideInAPackage("..application.port..")
            .and().haveSimpleNameEndingWith("Port")
            .should().beInterfaces()
            .because("Ports in hexagonal architecture should be interfaces");

    @ArchTest
    static final ArchRule application_should_not_depend_on_infrastructure = noClasses()
            .that().resideInAPackage("..application..")
            .should().dependOnClassesThat()
            .resideInAnyPackage("..infrastructure.out.persistence..", "org.springframework.data..", "jakarta.persistence..")
            .because("Application layer should not depend on infrastructure details");

    @ArchTest
    static final ArchRule controllers_should_only_depend_on_use_cases = classes()
            .that().resideInAPackage("..infrastructure.in.rest.controller..")
            .and().haveSimpleNameEndingWith("Controller")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                    "..application.port.in..",
                    "..infrastructure.in.rest..",
                    "..domain.model..",
                    "java..",
                    "org.springframework..",
                    "io.swagger..",
                    "jakarta.validation.."
            )
            .because("Controllers should depend on inbound ports, DTOs and domain model only");

    @ArchTest
    static final ArchRule use_cases_should_implement_ports = classes()
            .that().resideInAPackage("..application.usecases..")
            .and().haveSimpleNameEndingWith("UseCase")
            .should().implement(ApplicablePricePort.class)
            .because("Use case classes should implement their inbound port interfaces");

    @ArchTest
    static final ArchRule adapters_should_depend_on_ports = classes()
            .that().resideInAPackage("..infrastructure.out.persistence.adapter..")
            .and().haveSimpleNameEndingWith("Adapter")
            .should().dependOnClassesThat().resideInAPackage("..application.port.out..")
            .because("Outbound adapters should depend on outbound port interfaces");

    // --- Onion Architecture validation ---

    @ArchTest
    static final ArchRule domain_models_should_only_depend_on_java = classes()
            .that().resideInAPackage("..domain.model..")
            .should().onlyDependOnClassesThat().resideInAnyPackage("..domain.model..", "java..")
            .because("Domain models should be independent of everything except Java libraries");

    @ArchTest
    static final ArchRule use_cases_should_only_depend_on_domain_ports_and_minimal_framework = classes()
            .that().resideInAPackage("..application.usecases..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                    "..domain.model..",
                    "..domain.exception..",
                    "..application.port..",
                    "java..",
                    "org.springframework.stereotype.."
            )
            .because("Use cases should only depend on domain, ports, and minimal framework");

    @ArchTest
    static final ArchRule ports_should_not_depend_on_infrastructure = noClasses()
            .that().resideInAPackage("..application.port..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
            .because("Ports should not depend on infrastructure");

    // --- Naming Conventions ---

    @ArchTest
    static final ArchRule controllers_should_follow_naming_convention = classes()
            .that().resideInAPackage("..infrastructure.in.rest.controller..")
            .and().areAnnotatedWith(RestController.class)
            .should().haveSimpleNameEndingWith("Controller");

    @ArchTest
    static final ArchRule use_cases_should_follow_naming_convention = classes()
            .that().resideInAPackage("..application.usecases..")
            .and().areAnnotatedWith(Service.class)
            .should().haveSimpleNameEndingWith("UseCase");
}