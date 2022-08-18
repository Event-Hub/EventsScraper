package com.event.scraper.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


class ArchTest {

    @SuppressWarnings("SpellCheckingInspection")
    public static final String MAIL_MODULE = "com.event.scraper.mailmodule";

    @SuppressWarnings("SpellCheckingInspection")
    public static final String ADMIN_API = "com.event.scraper.adminapi";
    public static final String AUTH = "com.event.scraper.auth";
    public static final String EVENTS = "com.event.scraper.events";
    public static final String NEWSLETTER = "com.event.scraper.newsletter";
    public static final String USER = "com.event.scraper.users";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String USER_API = "com.event.scraper.usersapi";
    public static final String STATS = "com.event.scraper.statistics";
    public static final String SCRAPERS = "com.event.scraper.scrapers";
    public static final String SCRAPERS_EBILET = "com.event.scraper.scrapers.ebilet";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String SCRAPERS_EMPIKBILET = "com.event.scraper.scrapers.empikbilet";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String SCRAPERS_GOINGAPP = "com.event.scraper.scrapers.goingapp";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String SCRAPERS_KUPBILECIK = "com.event.scraper.scrapers.kupbilecik";
    public static final String SCRAPERS_PROANIMA = "com.event.scraper.scrapers.proanima";
    public static final String JAVA = "..java..";
    public static final String SPRING = "..org.springframework..";
    public static final String JAVAX = "..javax..";
    public static final String HTMLUNIT = "..htmlunit..";
    public static final String JSOUP = "..jsoup..";


    @Test
    @DisplayName("Test for Scrapers Module")
    void givenScrapersModule_thenCheckDependencyOnlyOnEvents() {
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.event.scraper");

        ArchRule archRule = classes()
                .that().resideInAPackage(SCRAPERS).and().resideOutsideOfPackages(SCRAPERS_EBILET, SCRAPERS_EMPIKBILET, SCRAPERS_GOINGAPP,
                        SCRAPERS_KUPBILECIK, SCRAPERS_PROANIMA)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        EVENTS);

        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Scrapers Internal Implementation Modules")
    void givenScrapersImplementationModule_thenCheckDependencyOnlyOnAbstractScraperAndScrapingLibraries() {
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.event.scraper");

        ArchRule archRule = classes()
                .that()
                .resideInAnyPackage(SCRAPERS_EBILET, SCRAPERS_EMPIKBILET, SCRAPERS_GOINGAPP,
                        SCRAPERS_KUPBILECIK, SCRAPERS_PROANIMA)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        SCRAPERS, HTMLUNIT, JSOUP);

        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Event Module")
    void givenEventModule_thenCheckDoesNotDependOnAnyOtherModuleFromApp() {
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.event.scraper");

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(EVENTS)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for User Module")
    void givenUSerModule_thenCheckDependencies() {
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.event.scraper");

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(USER)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        AUTH);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for NewsLetter and Notifications Module")
    void givenNewsLetterModule_thenCheckDependencies() {
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.event.scraper");

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(NEWSLETTER)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        EVENTS, USER, MAIL_MODULE);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Mail Module")
    void givenMailModule_thenCheckDoesNotDependOnAnyOtherModuleFromApp() {
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.event.scraper");

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(MAIL_MODULE)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for User API Module")
    void givenUserAPIModule_thenCheckDependencies() {
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.event.scraper");

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(USER_API)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        NEWSLETTER,EVENTS,USER,AUTH);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Authentication Module")
    void givenAuthModule_thenCheckDoesNotDependOnAnyOtherModuleFromApp() {
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.event.scraper");

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(AUTH)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Admin API Module")
    void givenAdminAPIModule_thenCheckDependencies() {
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.event.scraper");

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(ADMIN_API)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        USER,AUTH,STATS);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Statistics Module")
    void givenStatsModule_thenCheckDependencies() {
        JavaClasses javaClasses = new ClassFileImporter().importPackages("com.event.scraper");

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(STATS)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        SCRAPERS,EVENTS,USER);
        archRule.check(javaClasses);

    }



}