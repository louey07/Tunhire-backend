package com.example.tunhire;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
        .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
        .importPackages("com.example.tunhire");

    @Test
    void jobsShouldNotDependOnApplicationsOrCompaniesInternals() {
        ArchRule rule = ArchRuleDefinition.noClasses()
            .that()
            .resideInAnyPackage("..jobs..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                "..applications.repository..",
                "..applications.controller..",
                "..companies.controller.."
            );
        rule.check(classes);
    }

    @Test
    void applicationsShouldNotDependOnJobsDirectly() {
        ArchRule rule = ArchRuleDefinition.noClasses()
            .that()
            .resideInAnyPackage("..applications..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..jobs.repository..", "..jobs.controller..");
        rule.check(classes);
    }

    @Test
    void applicationsShouldNotDependOnCandidateDirectly() {
        ArchRule rule = ArchRuleDefinition.noClasses()
            .that()
            .resideInAnyPackage("..applications..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..candidate.repository..", "..candidate.controller..", "..candidate.service..", "..candidate.entity..");
        rule.check(classes);
    }

    @Test
    void companiesShouldNotDependOnApplicationsDirectly() {
        ArchRule rule = ArchRuleDefinition.noClasses()
            .that()
            .resideInAnyPackage("..companies..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                "..applications.repository..",
                "..applications.controller.."
            );
        rule.check(classes);
    }

    @Test
    void controllersShouldNotImportRepositories() {
        ArchRule rule = ArchRuleDefinition.noClasses()
            .that()
            .resideInAnyPackage("..controller..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..repository..");
        rule.check(classes);
    }

    @Test
    void recruiterAndCandidateShouldNotAccessEachOtherInternals() {
        ArchRule rule = ArchRuleDefinition.noClasses()
            .that()
            .resideInAnyPackage("..recruiter..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage(
                "..candidate.repository..",
                "..candidate.controller.."
            )
            .allowEmptyShould(true);
        rule.check(classes);
    }
}
