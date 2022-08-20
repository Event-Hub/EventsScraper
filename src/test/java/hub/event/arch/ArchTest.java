package hub.event.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.ImportOptions;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;


class ArchTest {

    public static final String HUB_EVENT = "hub.event";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String MAIL_MODULE = HUB_EVENT + ".mailmodule..";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String ADMIN_API = HUB_EVENT + ".adminapi..";
    public static final String AUTH = HUB_EVENT + ".auth..";
    public static final String EVENTS = HUB_EVENT + ".events..";
    public static final String NEWSLETTER = HUB_EVENT + ".newsletter..";
    public static final String USER = HUB_EVENT + ".users..";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String USER_API = HUB_EVENT + ".usersapi..";
    public static final String STATS = HUB_EVENT + ".statistics..";
    public static final String SCRAPERS = HUB_EVENT + ".scrapers..";
    public static final String SCRAPERS_EBILET = HUB_EVENT + ".scrapers.ebilet..";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String SCRAPERS_EMPIKBILET = HUB_EVENT + ".scrapers.empikbilet..";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String SCRAPERS_GOINGAPP = HUB_EVENT + ".scrapers.goingapp..";
    @SuppressWarnings("SpellCheckingInspection")
    public static final String SCRAPERS_KUPBILECIK = HUB_EVENT + ".scrapers.kupbilecik..";
    public static final String SCRAPERS_PROANIMA = HUB_EVENT + ".scrapers.proanima..";
    public static final String JAVA = "..java..";
    public static final String SPRING = "..org.springframework..";
    public static final String JAVAX = "..javax..";
    public static final String HTMLUNIT = "..htmlunit..";
    public static final String JSOUP = "..jsoup..";


    @Test
    @DisplayName("Test for Scrapers Module")
    void givenScrapersModule_thenCheckDependencyOnlyOnEvents() {
        //Import classes from application and omit test classes
        JavaClasses javaClasses = new ClassFileImporter(new ImportOptions()
                .with(new ImportOption.DoNotIncludeTests())).importPackages(HUB_EVENT);

        ArchRule archRule = classes()
                .that().resideInAPackage(SCRAPERS).and().resideOutsideOfPackages(SCRAPERS_EBILET, SCRAPERS_EMPIKBILET, SCRAPERS_GOINGAPP,
                        SCRAPERS_KUPBILECIK, SCRAPERS_PROANIMA)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        EVENTS, SCRAPERS);

        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Scrapers Internal Implementation Modules")
    void givenScrapersImplementationModule_thenCheckDependencyOnlyOnAbstractScraperAndScrapingLibraries() {
        //Import classes from application and omit test classes
        JavaClasses javaClasses = new ClassFileImporter(new ImportOptions()
                .with(new ImportOption.DoNotIncludeTests())).importPackages(HUB_EVENT);

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
        //Import classes from application and omit test classes
        JavaClasses javaClasses = new ClassFileImporter(new ImportOptions()
                .with(new ImportOption.DoNotIncludeTests())).importPackages(HUB_EVENT);

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(EVENTS)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        EVENTS);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for User Module")
    void givenUSerModule_thenCheckDependencies() {
        //Import classes from application and omit test classes
        JavaClasses javaClasses = new ClassFileImporter(new ImportOptions()
                .with(new ImportOption.DoNotIncludeTests())).importPackages(HUB_EVENT);

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(USER)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        AUTH, USER);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for NewsLetter and Notifications Module")
    void givenNewsLetterModule_thenCheckDependencies() {
        //Import classes from application and omit test classes
        JavaClasses javaClasses = new ClassFileImporter(new ImportOptions()
                .with(new ImportOption.DoNotIncludeTests())).importPackages(HUB_EVENT);

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(NEWSLETTER)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        EVENTS, USER, MAIL_MODULE, NEWSLETTER);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Mail Module")
    void givenMailModule_thenCheckDoesNotDependOnAnyOtherModuleFromApp() {
        //Import classes from application and omit test classes
        JavaClasses javaClasses = new ClassFileImporter(new ImportOptions()
                .with(new ImportOption.DoNotIncludeTests())).importPackages(HUB_EVENT);

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(MAIL_MODULE)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        MAIL_MODULE);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for User API Module")
    void givenUserAPIModule_thenCheckDependencies() {
        //Import classes from application and omit test classes
        JavaClasses javaClasses = new ClassFileImporter(new ImportOptions()
                .with(new ImportOption.DoNotIncludeTests())).importPackages(HUB_EVENT);

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(USER_API)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        NEWSLETTER,EVENTS,USER,AUTH, USER_API);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Authentication Module")
    void givenAuthModule_thenCheckDoesNotDependOnAnyOtherModuleFromApp() {
        //Import classes from application and omit test classes
        JavaClasses javaClasses = new ClassFileImporter(new ImportOptions()
                .with(new ImportOption.DoNotIncludeTests())).importPackages(HUB_EVENT);

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(AUTH)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        AUTH);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Admin API Module")
    void givenAdminAPIModule_thenCheckDependencies() {
        //Import classes from application and omit test classes
        JavaClasses javaClasses = new ClassFileImporter(new ImportOptions()
                .with(new ImportOption.DoNotIncludeTests())).importPackages(HUB_EVENT);

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(ADMIN_API)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        USER,AUTH,STATS, ADMIN_API);
        archRule.check(javaClasses);

    }

    @Test
    @DisplayName("Test for Statistics Module")
    void givenStatsModule_thenCheckDependencies() {
        //Import classes from application and omit test classes
        JavaClasses javaClasses = new ClassFileImporter(new ImportOptions()
                .with(new ImportOption.DoNotIncludeTests())).importPackages(HUB_EVENT);

        ArchRule archRule = classes()
                .that()
                .resideInAPackage(STATS)
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(JAVA, JAVAX, SPRING,
                        SCRAPERS,EVENTS,USER, STATS);
        archRule.check(javaClasses);

    }



}