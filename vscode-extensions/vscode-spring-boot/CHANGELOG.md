## 2023-06-14 (4.19.0 RELEASE, incl. language servers version 1.47.0)

* _(Spring Boot)_ fixed: Spring Data Repositories: Validate ID type ([#457](https://github.com/spring-projects/sts4/issues/457))
* _(Spring Boot)_ fixed: Add support for spring potential injection points discovery while indexing ([#994](https://github.com/spring-projects/sts4/issues/994))
* _(Spring Boot)_ fixed: replace internals of symbols machinery with more comprehensive model of spring components ([#1006](https://github.com/spring-projects/sts4/issues/1006))
* _(Spring Boot)_ fixed: [spring index] optimize empty array objects in internal index storage structure ([#1040](https://github.com/spring-projects/sts4/issues/1040))
* _(Spring Boot)_ fixed: [spring index] add request method to lsp extension to identify matching beans ([#1049](https://github.com/spring-projects/sts4/issues/1049))
* _(Spring Boot)_ fixed: upgrading from Spring Boot 3.0.x to 3.1.0 doesn't work ([#1051](https://github.com/spring-projects/sts4/issues/1051))

## 2023-05-05 (4.18.1 RELEASE, incl. language servers version 1.46.0)

* _(Spring Boot)_ fixed: [vscode-spring-boot] Support navigating to a Spring property file when inspecting on @Value annotations ([#761](https://github.com/spring-projects/sts4/issues/761))
* _(Spring Boot)_ fixed: automatically add import for types when applying content-assist proposals for query methods ([#991](https://github.com/spring-projects/sts4/issues/991))
* _(Spring Boot)_ fixed: do not fetch metrics data right after connecting live data to an app ([#1003](https://github.com/spring-projects/sts4/issues/1003))
* _(Spring Boot)_ fixed: No hyperlink on a hover to a type from JAR ([#1004](https://github.com/spring-projects/sts4/issues/1004))
* _(Spring Boot)_ fixed: [spring-data-support] take type hierarchy of domain types into account ([#1013](https://github.com/spring-projects/sts4/issues/1013))
* _(Spring Boot)_ fixed: [spring-data-support] remove pure findBy method completion from proposal list ([#1014](https://github.com/spring-projects/sts4/issues/1014))
* _(Spring Boot)_ fixed: several live hovers are missing when running Spring Boot 3 applications ([#1019](https://github.com/spring-projects/sts4/issues/1019))
* _(Spring Boot)_ fixed: application.properties/yaml autocomplete for nested enums regression ([#1021](https://github.com/spring-projects/sts4/issues/1021))
* _(Spring Boot)_ fixed: [boot-upgrade] Upgrade petclinic to boot 3 results in compiler errors ([#1022](https://github.com/spring-projects/sts4/issues/1022))
* _(Spring Boot)_ fixed: Bump json from 20160810 to 20230227 in /headless-services/spring-boot-language-server ([#1027](https://github.com/spring-projects/sts4/issues/1027))
* _(Spring Boot)_ fixed: spring-configuration-metadata : must have primitive type property before F3 navigation works ([#1030](https://github.com/spring-projects/sts4/issues/1030))
* _(Spring Boot)_ fixed: spring-configuration-metadata : when two properties have the same Object type F3 fails on the later ones ([#1031](https://github.com/spring-projects/sts4/issues/1031))
* _(Spring Boot)_ fixed: support @ConditionalOnProperty in property navigation feature ([#1033](https://github.com/spring-projects/sts4/issues/1033))
* _(VSCode)_ fixed: In Codespaces, workspace symbols sometimes are empty. ([#1012](https://github.com/spring-projects/sts4/issues/1012))

## 2023-03-15 (4.18.0 RELEASE, incl. language servers version 1.45.0)

#### important highlights

* _(Spring Boot):_ new and vastly improved content-assist for Spring Data repository query methods (thanks to contributions from @danthe1st)
* _(Spring Boot):_ the additional reconciling of Spring Boot projects to show Spring specific validations, outdated versions and more, now reports progress, runs faster, and uses less memory - please consider using this and tell us about your experiences and if you hit issues while having this feature enabled
* _(VSCode):_ fixed an extremely annoying issue that caused regular Java content-assist in VSCode to stop working after a short while

#### all fixes and improvements in detail

* _(Spring Boot)_ fixed: language server seems to have issues refreshing live data ([#1002](https://github.com/spring-projects/sts4/issues/1002))
* _(Spring Boot)_ fixed: update default settings for generations validation ([#999](https://github.com/spring-projects/sts4/issues/999))
* _(Spring Boot)_ fixed: Switch to rewrite release versions for 4.18 ([#995](https://github.com/spring-projects/sts4/issues/995))
* _(Spring Boot)_ fixed: completions for predicate keywords in Spring Data repositories ([#988](https://github.com/spring-projects/sts4/issues/988)) - contributed by @danthe1st
* _(Spring Boot)_ fixed: False positive "Unnecessary @Autowired" warning when using abstract classes ([#985](https://github.com/spring-projects/sts4/issues/985))
* _(Spring Boot)_ fixed: [validation] report progress when reconciling projects ([#984](https://github.com/spring-projects/sts4/issues/984))
* _(Spring Boot)_ fixed: Factor out static methods for Spring Data repository completions ([#983](https://github.com/spring-projects/sts4/issues/983)) - contributed by @danthe1st
* _(Spring Boot)_ fixed: Spring Data JPA Content Assist ([#981](https://github.com/spring-projects/sts4/issues/981)) - contributed by @danthe1st
* _(Spring Boot)_ fixed: [boot-upgrade] [Spring-Security] Removed WebSecurityConfigurerAdapter ([#979](https://github.com/spring-projects/sts4/issues/979))
* _(Spring Boot)_ fixed: [validation] [Spring-Security] Authorize HttpServletRequests with AuthorizationFilter ([#978](https://github.com/spring-projects/sts4/issues/978))
* _(Spring Boot)_ fixed: [validation] [Spring Security] Lambda DSL ([#977](https://github.com/spring-projects/sts4/issues/977))
* _(Spring Boot)_ fixed: [validation] Parse sources with Rewrite slower than JDT ([#971](https://github.com/spring-projects/sts4/issues/971))
* _(Spring Boot)_ fixed: [validation] Version validation based on spring.io generations REST API ([#969](https://github.com/spring-projects/sts4/issues/969))
* _(Spring Boot)_ fixed: [refactoring] running Spring Boot 3 open-rewrite migration recipes takes very long ([#863](https://github.com/spring-projects/sts4/issues/863))
* _(Spring Boot)_ fixed: [validation] add navigation to release notes from version validation messages ([#923](https://github.com/spring-projects/sts4/issues/923))
* _(Spring Boot)_ fixed: [validation] investigate highly increased memory consumption with new Java source reconciling ([#922](https://github.com/spring-projects/sts4/issues/922))
* _(Spring Boot)_ fixed: VSCode cross project additional-spring-configuration-metadata.json ([#894](https://github.com/spring-projects/sts4/issues/894))
* _(Spring Boot)_ fixed: [refactoring] investigate memory usage when converting projects ([#877](https://github.com/spring-projects/sts4/issues/877))
* _(Spring Boot)_ fixed: add Spring Data JPA Content Assist to STS 4 ([#107](https://github.com/spring-projects/sts4/issues/107))
* _(VSCode)_ fixed: Some error on WSL and Spring Extension ([#982](https://github.com/spring-projects/sts4/issues/982))
* _(VSCode)_ fixed: vscode ext causes "Header must provide a Content-Length property" continuously ([#968](https://github.com/spring-projects/sts4/issues/968))
* _(VSCode)_ fixed: Unable to install extension 'pivotal.vscode-spring-boot' as it is not compatible with VS Code '1.68.1' ([#967](https://github.com/spring-projects/sts4/issues/967))
* _(VSCode)_ fixed: Expose command to establish live connection to remote apps in vscode ([#947](https://github.com/spring-projects/sts4/issues/947))

## 2023-02-01 (4.17.2 RELEASE, incl. language servers version 1.44.0)

* _(Spring Boot)_ fixed: [symbols] do not auto-limit the result of the workspace symbol request ([#915](https://github.com/spring-projects/sts4/issues/915))
* _(Spring Boot)_ fixed: Unit test MavenProjectParser ([#918](https://github.com/spring-projects/sts4/issues/918))
* _(Spring Boot)_ fixed: Give user more information about what is "java sources reconciling" ([#920](https://github.com/spring-projects/sts4/issues/920))
* _(Spring Boot)_ fixed: [Java-17] Enable jdt.ls.commons.test tests to execute in the maven build ([#928](https://github.com/spring-projects/sts4/issues/928))
* _(Spring Boot)_ fixed: update generated parser for Java properties with latest ANTLR runtime version ([#946](https://github.com/spring-projects/sts4/issues/946))
* _(Spring Boot)_ fixed: Bad Escape exception showing up in log ([#950](https://github.com/spring-projects/sts4/issues/950))
* _(Spring Boot)_ fixed: various exceptions while reconciling ([#951](https://github.com/spring-projects/sts4/issues/951))
* _(Spring Boot)_ fixed: NPE from OpenRewrite Java Parser - Cannot read field "info" because "env" is null ([#952](https://github.com/spring-projects/sts4/issues/952))
* _(Spring Boot)_ fixed: Unknown property error shown in application.yml when using java records ([#955](https://github.com/spring-projects/sts4/issues/955))
* _(Spring Boot)_ fixed: [upgrading] running upgrade recipe for Spring Boot 3 causes exception ([#958](https://github.com/spring-projects/sts4/issues/958))
* _(VSCode)_ fixed: Is it feasible to drop the activation event onLanguage:xml? ([#926](https://github.com/spring-projects/sts4/issues/926))
* _(VSCode)_ fixed: Error: command 'sts.vscode-spring-boot.enableClasspathListening' not found ([#939](https://github.com/spring-projects/

## 2023-01-03 (4.17.1 RELEASE, incl. language servers version 1.43.0)

* _(Spring Boot)_ fixed: Eclipse STS 4.17 takes 96 % processor time on Intel i9 ([#934](https://github.com/spring-projects/sts4/issues/934))
* _(Spring Boot)_ fixed: BootLanguageServerBootApp class is taking all available cpu continuously ([#932](https://github.com/spring-projects/sts4/issues/932))
* _(Spring Boot)_ fixed: OpenRewrite unchecked，but always Language Server Background Job(Loading Rewrite Recipes) ([#925](https://github.com/spring-projects/sts4/issues/925))

#### known issues

* _(Spring Boot)_: The newly introduced additional reconciling for Java source files which is used to show up additional validations and quick fixes can cause increased memory and CPU consumption. In case you stumble upon issues in this area, feel free to disable the reconciling via `Preferences -> Extensions -> Spring Boot Tools -> Open Rewrite` (in VSCode) or `Preferences -> Language Servers -> Spring Language Servers -> Spring Boot Language Server -> Open Rewrite` (in Eclipse).

## 2022-12-08 (1.42.0)

* _(Spring Boot)_ fixed: [vscode] Boot LS is broken if Java LS launch mode is 'Hybrid' ([#919](https://github.com/spring-projects/sts4/issues/919))
* _(Spring Boot)_ fixed: [validation] spring.factories EnableAutoConfiguration key for boot 3.0 ignores 'on' setting ([#917](https://github.com/spring-projects/sts4/issues/917))

## 2022-12-07 (4.17.0 RELEASE, incl. language servers version 1.41.0)

* _(Spring Boot)_ fixed: Refreshing Live Data over HTTP is not working on VS Code ([#872](https://github.com/spring-projects/sts4/issues/872))
* _(Spring Boot)_ fixed: gcPauses -> jvm.gc.pause, memory -> jvm.memory.used ([#875](https://github.com/spring-projects/sts4/issues/875))
* _(Spring Boot)_ fixed: live information hovers are broken when running Spring Boot 3 application ([#862](https://github.com/spring-projects/sts4/issues/862))
* _(Spring Boot)_ fixed: fetch heap & nonHeap memory metrics together ([#874](https://github.com/spring-projects/sts4/issues/874))
* _(Spring Boot)_ fixed: 404 when getting live metrics over HTTP ([#879](https://github.com/spring-projects/sts4/issues/879))
* _(Spring Boot)_ fixed: [validation] Spring Boot support range validation messages appear and disappear again ([#887](https://github.com/spring-projects/sts4/issues/887))
* _(Spring Boot)_ fixed: [validation] check for superfluous @Repository annotations on standard Spring Data repositories ([#898](https://github.com/spring-projects/sts4/issues/898))
* _(Spring Boot)_ fixed: [validation] prepare version validation for 4.17.0 release ([#904](https://github.com/spring-projects/sts4/issues/904))
* _(Spring Boot)_ fixed: [validation] changing the preferences while language server is not around doesn't have any effect ([#836](https://github.com/spring-projects/sts4/issues/836))
* _(Spring Boot)_ fixed: [validation] convert autowired field to constructor should not run on test classes ([#902](https://github.com/spring-projects/sts4/issues/902))
* _(Spring Boot)_ fixed: [validation] add preferences to boot version and generation validations ([#884](https://github.com/spring-projects/sts4/issues/884))
* _(Spring Boot)_ fixed: [validation] add validations for new Spring Boot versions in general ([#885](https://github.com/spring-projects/sts4/issues/885))
* _(Spring Boot)_ fixed: [refactoring] convert autowired field to constructor should make field final ([#896](https://github.com/spring-projects/sts4/issues/896))
* _(Spring Boot)_ fixed: [refactoring] [validation] final polishing work ([#911](https://github.com/spring-projects/sts4/issues/911))
* _(Spring Boot)_ fixed: [refactoring] convert project to Spring Boot 3 doesn't update Java to 17 anymore ([#909](https://github.com/spring-projects/sts4/issues/909))
* _(Spring Boot)_ fixed: [refactoring] when converting a project to Spring Boot 3, always use the latest 3.0.x version ([#869](https://github.com/spring-projects/sts4/issues/869))
* _(Spring Boot)_ fixed: [refactoring] OutOfMemoryError: Java Heap Space ([#899](https://github.com/spring-projects/sts4/issues/899))
* _(Spring Boot)_ fixed: [refactoring] Exception when trying to convert project ([#900](https://github.com/spring-projects/sts4/issues/900))
* _(Spring Boot)_ fixed: [refactoring] converting a simple rest service guide to Spring Boot 3 fails ([#867](https://github.com/spring-projects/sts4/issues/867))
* _(Spring Boot)_ fixed: [refactoring] open rewrite refactoring menu item is broken when no language server is running ([#865](https://github.com/spring-projects/sts4/issues/865))
* _(Spring Boot)_ fixed: [refactoring] enhance Spring Boot version validation with quick fix to update ([#886](https://github.com/spring-projects/sts4/issues/886))
* _(Spring Boot)_ fixed: [refactoring] converting petclinic to Spring Boot 3 results in broken project ([#864](https://github.com/spring-projects/sts4/issues/864))
* _(Spring Boot)_ fixed: [refactoring] Migrate Boot 2 auto-config to Boot ([#908](https://github.com/spring-projects/sts4/issues/908))
* _(VSCode)_ fixed: make sure VSCode shows an error message when running on a JDK <17 ([#903](https://github.com/spring-projects/sts4/issues/903))
* _(VSCode)_ enhancement: Add shortcut to dashboard ([#876](https://github.com/spring-projects/sts4/issues/876)) - contributed by [Eskibear](https://github.com/Eskibear))
* _(VSCode)_ enhancement: refine walkthrough steps ([#907](https://github.com/spring-projects/sts4/issues/907)) - contributed by [Eskibear](https://github.com/Eskibear))
* _(VSCode)_ enhancement: update walkthrough about creating projects ([#916](https://github.com/spring-projects/sts4/issues/916)) - contributed by [Eskibear](https://github.com/Eskibear))

## 2022-10-26 (4.16.1 RELEASE, incl. language servers version 1.40.0)

* _(Spring Boot)_ fixed: Spring XML Config support does not show symbols with scope "File" ([#860](https://github.com/spring-projects/sts4/issues/860))
* _(Spring Boot)_ fixed: [open-rewrite] exception when executing quick fix for project ([#853](https://github.com/spring-projects/sts4/issues/853))
* _(Spring Boot)_ fixed: [refactoring] quick fix to convert autowired field to constructor param shows up even if the constructor param already exists ([#815](https://github.com/spring-projects/sts4/issues/815))
* _(VSCode)_ fixed: Vscode Spring Boot Tools 1.39.0 prevents Java project from Running/Debugging ([#847](https://github.com/spring-projects/sts4/issues/847))

## 2022-09-14 (4.16.0 RELEASE, incl. language servers version 1.39.0)

* _(Spring Boot)_ fixed: Workspace symbol null on custom annotations ([#818](https://github.com/spring-projects/sts4/issues/818))
* _(Spring Boot)_ fixed: organize imports causes communication issues with the language server ([#806](https://github.com/spring-projects/sts4/issues/806))
* _(Spring Boot)_ enhancement: Quick fix action to refactor field injection to constructor injection ([#522](https://github.com/spring-projects/sts4/issues/522))
* _(Spring Boot)_ fixed: Workspace symbol null on custom annotations ([#818](https://github.com/spring-projects/sts4/issues/818))
* _(VSCode)_ fixed: Extension never activates, throwing error "Header must provide a Content-Length property" ([#811](https://github.com/spring-projects/sts4/issues/811))

## 2022-08-11 (4.15.3 RELEASE, incl. language servers version 1.38.0)

* _(Spring Boot)_ fixed: Spring Boot Tools slow down code complete ([#808](https://github.com/spring-projects/sts4/issues/808))
* _(Language Server)_ fixed: Copied files should retain copyright ownership ([#807](https://github.com/spring-projects/sts4/issues/807))

## 2022-08-03 (4.15.2 RELEASE, incl. language servers version 1.37.0)

* _(Spring Boot)_ fixed: Validation errors on @@ placeholders in YAML editor ([#711](https://github.com/spring-projects/sts4/issues/711))
* _(VSCode)_ fixed: Live process is not auto connected when app is launched in internalConsole ([#794](https://github.com/spring-projects/sts4/issues/794))

## 2022-06-17 (1.36.0)

* _(Spring Boot)_ fixed: VScode incorrectly suggests removing @Autowired annotation from methods ([#787](https://github.com/spring-projects/sts4/issues/787))
* _(Spring Boot)_ fixed: VScode quick fix should not suggest removing @Autowired annotation from JUnit tests ([#786](https://github.com/spring-projects/sts4/issues/786))

## 2022-06-15 (1.35.0)

* _(VSCode)_ fixed: Cannot update vscode plugin to 1.34.0 ([#784](https://github.com/spring-projects/sts4/issues/784))

## 2022-06-15 (4.15.0 RELEASE, incl. language servers version 1.34.0)

* _(Spring Boot)_ fixed: vscode-sts: an edge case of workspace symbol for @PutMapping ([#781](https://github.com/spring-projects/sts4/issues/781))
* _(VSCode)_ fixed: Failed to refresh live data from process 12704 - com.xxxx.xx.xxx.BillingServiceApp after retries: 10 ([#748](https://github.com/spring-projects/sts4/issues/748))

## 2022-04-27 (4.14.1 RELEASE, incl. language servers version 1.33.0)

#### import changes

* _(VSCode)_ enhancement: live hovers are now automatically show up when you launch a Spring Boot application in VSCode. Additional JVM args for the Spring Boot app to enable JMX are added to the launch automatically. More details can be found in the user guide section about [Live Application Information](https://github.com/spring-projects/sts4/wiki/Live-Application-Information).

#### fixes and improvements

* _(Spring Boot)_ fixed: use `startupSnapshot` instead of `startup` timer call to avoid wiping out the underlying data
* _(Spring Boot, VSCode)_ fixed: When vscode opens a Java project for about 2 hours, the suggestion function will fail ([#750](https://github.com/spring-projects/sts4/issues/750))
* _(VSCode)_ improvement: add extension APIs to get live data ([#751](https://github.com/spring-projects/sts4/pull/751)) - contributed by @Eskibear

## 2022-03-16 (4.14.0 RELEASE, incl. language servers version 1.32.0)

* _(VSCode)_ fixed: VSCode Spring boot tools 1.30.0 error trying to find JVM ([#726](https://github.com/spring-projects/sts4/issues/726))
* _(VSCode)_ fixed: vscode "Problems" diagnostic entries lack "source" field ([#725](https://github.com/spring-projects/sts4/issues/725))
* _(VSCode)_ fixed: orphan vscode extension processes left running ([#704](https://github.com/spring-projects/sts4/issues/704))
* _(VSCode)_ fixed: deadlock in language server process avoids process to be shutdown ([#741](https://github.com/spring-projects/sts4/issues/741))
* _(VSCode)_ fixed: Outline View is broken again on Windows ([#742](https://github.com/spring-projects/sts4/issues/742))
* _(VSCode)_ fixed: boot-java.live-information.automatic-tracking.on is not working on vscode ([#733](https://github.com/spring-projects/sts4/issues/733))
* _(VSCode)_ fixed: Java 17 is mis-identified as Java 8 ([#713](https://github.com/spring-projects/sts4/issues/713))

## 2022-02-02 (4.13.1 RELEASE, incl. language servers version 1.30.0)

* _(Spring Boot)_ improvement: Consider if content assist can be offered for spring.config.import property keys ([#536](https://github.com/spring-projects/sts4/issues/536))
* _(Spring Boot)_ fixed: YAML editor generates wrong EOL characters in Windows ([#709](https://github.com/spring-projects/sts4/issues/709))
* _(VSCode)_ fixed: Always pop up a prompt box: indexing spring boot properties ([#697](https://github.com/spring-projects/sts4/issues/697))
* _(VSCode)_ fixed: Spring Boot Tools for VS Code incorrectly misidentifies internal JDK for Language Support for Java as a JRE ([#715](https://github.com/spring-projects/sts4/issues/715))

## 2021-12-08 (4.13.0 RELEASE, incl. language servers version 1.29.0)

* _(Spring Boot)_ fixed: STS 4.12.0 (for eclipse) *.yml can't automatic prompt ([#690](https://github.com/spring-projects/sts4/issues/690))
* _(Spring Boot)_ fixed: A StackOverFlow error for serializable Kotlin data class for autocompletion in the Eclipse IDE ([#693](https://github.com/spring-projects/sts4/issues/693))

## 2021-09-15 (4.12.0 RELEASE, incl. language servers version 1.28.0)

* _(VS Code, Spring Boot)_ fixed: [codespaces] vscode extension crashes if specified log output location doesn't work ([#676](https://github.com/spring-projects/sts4/issues/676))

## 2021-08-18 (4.11.1 RELEASE, incl. language servers version 1.27.0)

* _(VS Code, Spring Boot)_ fixed: [codespaces] spring boot extension starting up multiple times in Codespaces setting ([#669](https://github.com/spring-projects/sts4/issues/669))
* _(VS Code, Spring Boot)_ fixed: [codespaces] spring boot extension doesn't find the right JDK when connecting to a codespace ([#670](https://github.com/spring-projects/sts4/issues/670))
* _(VS Code, Spring Boot)_ fixed: JAVA_Home should be right but still shows "Note Java 8 can still be used in your own projects" ([#664](https://github.com/spring-projects/sts4/issues/664))

## 2021-06-18 (4.11.0 RELEASE, incl. language servers version 1.26.0)

* _(Spring Boot)_ fixed: Live data highlights disapearing after closing and reopening editor ([#616](https://github.com/spring-projects/sts4/issues/616))
* _(Spring Boot)_ fixed: In application.yml deprecated 'spring.profiles' is not showing a warning ([#615](https://github.com/spring-projects/sts4/issues/615))
* _(Spring Boot)_ fixed: Rename BootLanguagServerBootApp to BootLanguageServerBootApp ([#631](https://github.com/spring-projects/sts4/issues/631))
* _(VS Code, Spring Boot)_ new: new walkthrough contribution to get started with Spring Boot in VS Code
* _(VS Code, Spring Boot)_ fixed: not work when change LightWeight mode to Standard ([#628](https://github.com/spring-projects/sts4/issues/628))
* _(VS Code, Spring Boot)_ fixed: spring boot language server causing long waits for Java outline view when running in lightweight mode ([#642](https://github.com/spring-projects/sts4/issues/642))
* _(VS Code, Spring Boot)_ fixed: language server processes doesn't get shutdown properly in VSCode ([#636](https://github.com/spring-projects/sts4/issues/636))
* _(VS Code, Spring Boot)_ fixed: nothing shown in VS Code Outline view ([#627](https://github.com/spring-projects/sts4/issues/627))
* _(VS Code, Spring Boot)_ fixed: vscode spring boot language server not starting up ([#635](https://github.com/spring-projects/sts4/issues/635))
* _(VS Code, Spring Boot)_ fixed: VSCode extension JDK version ([#612](https://github.com/spring-projects/sts4/issues/612))

## 2021-03-17 (4.10.0 RELEASE, incl. language servers version 1.25.0)

* _(Spring Boot)_ enhancement: `.sts4` dir location now configurable ([#601](https://github.com/spring-projects/sts4/issues/601))
* _(Spring Boot)_ fixed: sometimes live hovers do not disappear ([#609](https://github.com/spring-projects/sts4/issues/609))

## 2020-12-16 (4.9.0 RELEASE, incl. language servers version 1.24.0)

* _(Spring Boot)_ new: show bean startup performance metrics in live hovers and code lenses (details in the user guide)
* _(Spring Boot)_ new: show basic request mapping performance metrics in live hovers and code lensses (details in the user guide)
* _(Spring Boot)_ new: provide content-assist for constructor-arg name in Spring XML config files ([#562](https://github.com/spring-projects/sts4/issues/562))
* _(Spring Boot)_ fixed: language-server-internal exception happening when saving a file that has a space or other special characters in its name or path

## 2020-10-23 (4.8.1 RELEASE, incl. language servers version 1.23.0)

* _(Spring Boot)_ fixed: Spring Boot Language Server processing java.util.Properties type configuration logic error ([#534](https://github.com/spring-projects/sts4/issues/534))
* _(Spring Boot)_ fixed: Add support for Spring Boot multi-document properties files ([#533](https://github.com/spring-projects/sts4/issues/533))
* _(Spring Boot)_ fixed: Support escaping of map keys with '[]' in Spring Boot property yaml files
* _(Spring Boot)_ fixed: Tolerate '.[' for map navigation in .properties file

## 2020-09-17 (4.8.0 RELEASE, incl. language servers version 1.22.0)

* _(Spring Boot)_ fix: Eclips shows error on `@ConditionalOnExpression("${downlink.active:false}")` ([#529](https://github.com/spring-projects/sts4/issues/529))

## 2020-08-27 (4.7.2 RELEASE, incl. language servers version 1.21.0)

* no major changes

## 2020-07-30 (4.7.1 RELEASE, incl. language servers version 1.20.0)

* _(Spring Boot)_ enhancement: Syntax check for Annotations with Spring Expression Language ([#475](https://github.com/spring-projects/sts4/issues/475))
* _(Spring Boot)_ fixed: hard to reproduce BadLocationException inside of language server fixed now, occurred e.g. in ([#451](https://github.com/spring-projects/sts4/issues/451))

## 2020-06-18 (4.7.0 RELEASE, incl. language servers version 1.19.0)

* _(Spring Boot)_ bugfix: automatic live hover detection works again reliably
* _(VSCode)_ improvement: Flag to configure the "JAVA_HOME or PATH..." message display ([#478](https://github.com/spring-projects/sts4/issues/478))
* _(VSCode)_ bugfix: green live hovers markers are no longer gone after switching to a different editor

## 2020-05-28 (4.6.2 RELEASE, incl. language servers version 1.18.0)

* _(Spring Boot)_ improvement: additional check to auto-connect live hovers only when actuators are on the project classpath ([#450](https://github.com/spring-projects/sts4/issues/450))
* _(Spring Boot)_ improvement: added content assist for keys that exist in YAML and properties files ([#427](https://github.com/spring-projects/sts4/issues/427))
* _(Spring Boot)_ improvement: Yaml editor gives error when using @..@ placeholders ([#190](https://github.com/spring-projects/sts4/issues/190))
* _(Spring Boot)_ bugfix: super type hierarchy lookup might fail
* _(Spring Boot)_ bugfix: quickly restarting app caused error popup from live hover mechanism to show up
* _(VSCode/Theia)_ bugfix: Fix null reference when no JVM was found - contributed by [@tfriem](https://github.com/tfriem)

## 2020-04-23 (4.6.1 RELEASE)

* _(Spring Boot)_ improvement: Spring yaml validation doesn't understand the "<<:" syntax ([#440](https://github.com/spring-projects/sts4/issues/440))
* _(Spring Boot)_ improvement: YAML Property completion: Superclass properties not detected in case of List or Map ([#449](https://github.com/spring-projects/sts4/issues/449))
* _(Spring Boot)_ improvement: improved performance for content-assist for Spring XML config files
* _(Spring Boot)_ bugfix: Quick Fix for unknown properties broken ([#442](https://github.com/spring-projects/sts4/issues/442))

## 2020-03-19 (4.6.0 RELEASE)

* _(Spring Boot)_ performance: improved performance while scanning projects for symbols
* _(Spring Boot)_ performance: improved performance when multiple files change at once (e.g. after a switch to a different branch or a git pull + refresh)

## 2020-01-22 (4.5.1 RELEASE)

* _(Spring Boot)_ improvement: live hover mechanism now reports connection failures

## 2019-12-19 (4.5.0 RELEASE)

* _(Spring Boot)_ improvement: better and more consistent labels for running processes in live hover actions across the board

## 2019-11-21 (4.4.2 RELEASE)

* _(Spring Boot)_ bugfix: CTRL-click in yaml file inaccurate (for 'nested' properties)

## 2019-10-24 (4.4.1 RELEASE)

* _(Spring Boot)_ Provide UI to allow user to explicitly connect/disconnect to/from processes to collect Live Hover data from. See the [wiki](https://github.com/spring-projects/sts4/wiki/Live-Application-Information#managing-live-data-connections-to-local-processes) for details.
* _(Spring Boot)_ enhancement: Goto Symbol now also works from XML bean files.
* _(Spring Boot)_: improve performance of xml symbol scanning.

## 2019-09-19 (4.4.0 RELEASE)

* _(Spring Boot)_ fixed: Adding/removing bean in XML file doesn't update the symbol index
* _(VSCode)_ fixed: show error message when manually configured JDK is not there

## 2019-08-13 (4.3.2 RELEASE)

* _(Spring Boot)_ fixed: CTRL-click navigation does not handle properties on super class correctly ([#326](https://github.com/spring-projects/sts4/issues/326))
* _(Spring Boot)_ fixed: Configuration property analysis does not handle properties written in snake_case correctly ([#327](https://github.com/spring-projects/sts4/issues/327))
* _(VSCode)_ fixed: Spring boot tool vscode extension is causing system to run out of disk space ([#328](https://github.com/spring-projects/sts4/issues/328))

## 2019-07-12 (4.3.1 RELEASE)

* _(all language servers)_ performance: further improvements to the language server startup time
* _(Spring Boot)_ fixed: wrong error markers in properties files([#314](https://github.com/spring-projects/sts4/issues/314))
* _(VS Code)_ fixed: vscode goto definition error with lsp ([#309](https://github.com/spring-projects/sts4/issues/309))

## 2019-06-21 (4.3.0 RELEASE)

- _(Spring Boot)_ improvement: project classpath notifications now happen in batch on startup to further optimize performance and job load on the Eclipse side
- _(Spring Boot)_ improvement: symbols are now being re-created if dependent types change
- _(Spring Boot)_ fixed: Slow code completion takes more than a 1 sec. ([#293](https://github.com/spring-projects/sts4/issues/293))
- _(Spring Boot)_ fixed: content-assist for Spring XML config files now working again in VS Code and Theia
- _(Spring Boot)_ fixed: ClassCast Exception in Boot LS while application.yml file opened in the editor
- _(Spring Boot)_ fixed: Anonymous inner type beans don't have boot hints

## 2019-05-24 (4.2.2 RELEASE)

* _(all language servers)_ performance: additional improvements to language server startup time
* _(Spring Boot)_ new: additional fine-grained preferences for Spring XML config file support
* _(Spring Boot)_ new: navigation for bean identifiers, bean classes, and property names for Spring XML config files
* _(Spring Boot)_ new: content-assist rolled out for many more Spring XML config elements and attributes
* _(Spring Boot)_ new: live bean information now showing up in types from class files (when source code is shown) - _VSCode and Theia only at the moment_
* _(Spring Boot)_ improvement: hugely improved content-assist for bean class attribute in Spring XML config files (incl. package name proposals and vastly improved performance)
* _(Spring Boot)_ improvement: property name content-assist in Spring XML config files now shows proposals from properties defined in supertypes, too
* _(Spring Boot)_ improvement: symbol scanning skips output folders now
* _(Spring Boot)_ fixed: Detect @RequestMapping with path defined as constant instead of literal string ([#281](https://github.com/spring-projects/sts4/issues/281))
* _(Spring Boot)_ fixed: NPE when invoking property name content-assist in XML file without bean class being defined
* _(Spring Boot)_ fixed: tags in yaml files with dollar signs throw IllegalGroupReference in properties editor

## 2019-04-18 (4.2.1 RELEASE)

* _(Spring Boot)_ performance: additional performance and memory footprint improvements to symbol indexing, eats now less memory and is faster while doing the initial indexing run
* _(Spring Boot)_ new: content-assist for bean types in Spring XML config files
* _(Spring Boot)_ new: content-assist for property names in Spring XML config files
* _(Spring Boot)_ new: content-assist for bean references in property definitions in Spring XML config files (very rough early cut, needs a lot more work on proposal content, number of proposals, and sorting)
* _(Spring Boot)_ improvement: limit the number of XML files that are scanned for bean symbols, output folders now ignored
* _(Spring Boot)_ fixed: Ctrl-click in Java editor in Eclipse wasn't working due to issue with hyperlink detector
* _(VSCode, Atom, Theia)_ improvement: JVM args can now be configured for language server processes

## 2019-03-21 (4.2.0 RELEASE)

* _(Spring Boot)_ new: Allow configuration of VM arguments for LSP process "PropertiesLauncher" ([#211](https://github.com/spring-projects/sts4/issues/211))
* _(Spring Boot)_ performance: major performance improvements to symbol indexing infrastructure by caching created symbols across language server starts
* _(Spring Boot)_ performance: replaced internal type indexing with communication to JDT (language server) to save time and memory spend for keeping our own type index

## 2019-02-21 (4.1.2 RELEASE)

* _(Spring Boot)_ new: live hover information for bean wirings now supports war-packaged boot apps running in a local server installation
* _(Spring Boot)_ new: live hover information for `@Value` annotations ([#177](https://github.com/spring-projects/sts4/issues/177))
* _(Spring Boot)_ new: property completion now works for `.yaml` files as well ([#191](https://github.com/spring-projects/sts4/issues/191))
* _(Spring Boot)_ new: bean symbols from XML config files now include exact location information
* _(Spring Boot)_ new: bean symbols from XML config files now generated for beans without a bean ID, too
* _(Spring Boot)_ fixed: navigate to resource in live hovers for apps running on CF works again
* _(Spring Boot)_ fixed: ConcurrentModificationException while retrieving symbols from language server
* _(Spring Boot)_ fixed: race condition that sometimes caused initial project to be not indexed for symbols
* _(Spring Boot)_ fixed: search for symbols in project now happens on the server side to avoid no project-related symbols showing up on the client side before you start typing in a query
* _(Spring Boot)_ performance: improvement to further reduce the CPU load when checking processes for live hovers ([#140](https://github.com/spring-projects/sts4/issues/140))
* _(Spring Boot)_ performance: the language server doesn't trigger a full source and javadoc download for Maven projects anymore
* _(VSCode)_ fixed: "class" snippet is not available ([#192](https://github.com/spring-projects/sts4/issues/192))

## 2019-01-24 (4.1.1 RELEASE)

* (Spring Boot) quick navigation via symbols now available for non-Boot Spring projects
* (Spring Boot) live hover informations for bean wirings now available for non-Boot Spring projects - _for details how to enable this for your apps, take a look at the [user guide](https://github.com/spring-projects/sts4/wiki/Live-Application-Information) section for that_
* (Spring Boot) added support for deprecated properties (including corresponding quick-fix) 
* (Spring Boot) quick fix to generate default metadata for missing properties ([#101](https://github.com/spring-projects/sts4/issues/101))
* (Spring Boot) first steps towards generating symbols for Spring XML config files ([#108](https://github.com/spring-projects/sts4/issues/108#issuecomment-455135918))
* (Spring Boot) fixed: live hovers don't show up for classes with a name starting with multiple upper case charatecters
* (Spring Boot) fixed: type and resource navigation in bean live hovers don't work for types and resources from dependencies

## 2018-12-20 (4.1.0 RELEASE)

* (Spring Boot) first initial version of content-assist for Spring Data repository definitions
* (Spring Boot) live hover links to types now work for projects using JDK 9 and beyond, too
* (Spring Boot) fixed an issue with stopped apps on CF causing boot language server to get stuck when connected to JMX via SSH tunnel

## 2018-11-30 (4.0.2 RELEASE)

* (Spring Boot) Make CTRL-CLICK navigation from application.properties to Java work
* (Spring Boot) Make CTRL-CLICK navigation from application.yml to Java work
* (Spring Boot) Made content-assist for values in lists more consistent across .yml and .properties editors.
* (Spring Boot) Bugfix: Adding eureka client starter to classpath breaks requestmapping live hovers.
* (Spring Boot) More precise autowiring live hovers for @Bean method parameters.
* (Spring Boot) server.servlet.context-path now supported for Request Mapping live hover links.
* (Spring Boot) Improved 'Goto Symbol' support for functional style WebFlux requestmappings.
* (Spring Boot) Improved 'Live Hover' support for functional style WebFlux requestmappings.
* (Spring Boot) Bugfix: Insertion of mapping templates now takes into account leading @ in editor.
* (Spring Boot) Added support for Spring Boot log groups in properties and yaml editor.
* (General) Various bugfixes for bugs causing language servers to hang and become unresponsive.
* (Spring Boot) (Concourse) Bugfix: Quickfix not working (anymore?) in LSP editors.
* (Spring Boot) After adding Spring Boot configuration processor editor automatically becomes aware of new properties metadata.

## 2018-10-31 (4.0.1 RELEASE)

* _(Spring Boot)_ fixed NPE from SpringIndexer ([#105](https://github.com/spring-projects/sts4/issues/105))
* _(Spring Boot)_ filed: Spring Boot configuration property auto-completion does not offer properties on super classes ([#116](https://github.com/spring-projects/sts4/issues/116))
* _(Spring Boot)_ fixed: Lots of NPE noise in language server ([#90](https://github.com/spring-projects/sts4/issues/90))
* _(Spring Boot)_ fixed: Live Boot Hint Decorators not working when app ObjectMapper configured with NON_DEFAULT inclusion  ([#80](https://github.com/spring-projects/sts4/issues/80))
* _(Spring Boot)_ fixed: property support now understand nested project structure

## 2018-09-25 (4.0.0 RELEASE)

* _(Spring Boot)_ `Cmd-6` in Eclipse shows `Go To Symbols in Workspace` first, second `Cmd-6` switches to `Go To Symbols in File`
* _(Spring Boot)_ various bug fixes

## 2018-08-30 (M15)

* _(Spring Boot)_ improved the overall content of bean wiring live hovers
* _(Spring Boot)_ live hover information for bean wirings now show up more precisely on autowired fields and constructors as well as at `@Bean` definitions, including more complete information about the wirings
* _(Spring Boot)_ improved performance of live hovers for remote boot apps
* _(Spring Boot)_ added experimental option to show code lenses for live hover information, including bean wiring and request mapping URLs (use the preferences to switch that on)
* _(Spring Boot)_ added detailed information to the boot dashboard property view about JMX tunnels over SSH to boot apps running on CloudFoundry
* _(Spring Boot)_ added action to enable/disable JMX tunneling through SSH for already deployed and running apps
* _(Spring Boot)_ improved performance and reduced footprint of live hover update mechanism
* _(Spring Boot)_ bugfix: fixed missing line break in live hover for request mappings
* _(Spring Boot)_ bugfix: resource links in live hovers for remote boot apps now working
* _(Spring Boot)_ bugfix: make the overall classpath detection mechanism more reliable in case of project deletions ([#69](https://github.com/spring-projects/sts4/issues/69))

## 2018-08-09 (M14)

* _(Spring Boot)_ added support for showing live hovers for Spring Boot apps running remotely (on Cloud Foundry)
* _(Spring Boot)_ improved and simplified content for live hovers showing bean wiring information
* _(Spring Boot)_ improved look of live hover highlights
* (_Spring Boot)_ improved error handling when source code parsing goes wrong
* (_Spring Boot)_ added specific bean wiring live hovers for `@Autowired` fields and constructors
* (_Spring Boot)_ user-defined values in property files showing up as suggestions for `@Value` completions
* (_Spring Boot)_ bean symbols now directly contain additional annotations (like `@Conditional...` or `@Profile`)
* (_Spring Boot)_ added option to match running process directly to specific project in the workspace (for live hovers) via system property (set `-Dspring.boot.project.name=<project-name-in-workspace>` to show live hovers of that running process exclusively on the project with that name).
* (_Spring Boot)_ fixed bug that prevented property editing support to work on Windows ([#59](https://github.com/spring-projects/sts4/issues/59))

## 2018-07-23 (M13)

* _(Spring Boot)_ early prototype for detecting changed bean definitions in live-running (and restarted) boot applications
* _(Spring Boot)_ @Inject annotation now supported for live hovers
* _(Spring Boot)_ added option to match live running apps and workspace projects manually
* _(Spring Boot)_ improved JMX connector reuse (internal optimization)

## 2018-06-08 (M12)

* _(Spring Boot)_ live hovers now updated in all open editors, not just the active one
* _(Spring Boot)_ more detailed context shown in hover documentation when editing property files ([#265](https://github.com/spring-projects/spring-ide/issues/265))
* _(Spring Boot)_ performance improvement: project symbols now show up a lot faster for the open editors/projects (in a multi-root folder workspace)
* _(Spring Boot)_ bugfix: JDK9 and JDK10 projects supported now even if main editor and/or language server runs on JDK8
* _(Spring Boot)_ bugfix: various NPEs in Spring indexer fixed

## 2018-05-14 (M11)

* _(Spring Boot)_ major performance improvements and footprint reductions (due to a groundbreaking change to how projects are being resolved, this is now delegated to the surrounding Java tooling)
* _(Spring Boot)_ support for JDK10 added
* _(Spring Boot)_ bugfix - custom JMX domains now supported for live hovers ([#44](https://github.com/spring-projects/sts4/issues/44))
* _(Spring Boot)_ bugfix - live hovers now show up for all open editors (not limited to the one with focus anymore)
* _(all)_ JVM used to run the language servers can now be specified via custom settings ([#51](https://github.com/spring-projects/sts4/issues/51))

## 2018-03-15 (M10)

* _(Spring Boot)_ support added for request-mapping-like symbols for webflux router definitions
* _(Spring Boot)_ code lenses for webflux handler methods implemented that visualize the mapped route (VSCode only at the moment)
* _(Spring Boot)_ document highlight support added for webflux router definitions that highlight parts of the routes that belong together
* _(Spring Boot)_ request mapping symbols now include accept and content type defintions
* _(Spring Boot)_ support for direct navigation from live injection reports to source code added for Eclipse
* _(Spring Boot)_ abstract @Bean-annotated methods are now ignored when creating symbols for bean definitions
* _(Spring Boot)_ URLs from hovers (like request mapping URLs from running boot apps) now open in an internal browser that has a navigation and an address bar
* _(Spring Boot)_ bugfix for NPE that happened occasionally when creating a new Java file
* _(all)_ language server processes now show up with their real name when using `jps` instead of just `JarLauncher`

## 2018-02-23 (M9)

__Attention:__ We merged the two different extensions for Spring Boot (for Java code and for properties) into a single extension for the various platforms (Eclipse, VSCode, Atom). This might require that you manually uninstall the old extensions and install the new ones. Automatic updates don't work here. For the Eclipse case, you might want to start with a fresh STS4 M9 distribution and go from there to avoid manual uninstall/install steps.

* _(Spring Boot)_ support added for Spring Data repositories, they show up as bean symbols now
* _(Spring Boot)_ fixed a bug that caused an exception when using content-assist for a non-Spring-Boot java project
* _(Spring Boot, VSCode)_ support for navigation added to live injection reports, they allow you to directly navigate to the source code of the bean type and the resource where the bean got defined. Limitation: this works for VSCode only at the moment, support for Eclipse and Atom still in progress
* _(Spring Boot, Eclipse integration)_ fixed a bug that caused content-assist to be turned off in the java editor
* improved the way the JDK to run the language server is found together with an improved error message if no JDK can be found

## 2018-01-30 (M8)

* _(Spring Boot Java)_ function declarations are now being parsed into symbols for functions that directly inherit from `java.util.Function` ([#18](https://github.com/spring-projects/sts4/issues/18))
* _(Spring Boot Java)_ updated live hover mechanics to work with latest Spring Boot 2.0 snapshot versions 
* _(Spring Boot Java)_ improved the way the JDK (to run the language server) is found
* _(Spring Boot Java)_ improved warning message about missing tools.jar
* _(Spring Boot Java)_ live hovers now show up on class flies that are displayed as source
* _(Spring Boot Java)_ fixed a problem with outdated symbols showing up after file deletion/rename
* _(Spring Boot Java)_ fixed a deadlock issue
* _(Spring Boot Java)_ reduced number of threads used behind the scenes
* _(Spring Boot Java)_ reduced number of CPU cycles used by live hover mechanism
* _(Spring Boot Java, Spring Boot Properties)_ reduced memory footprint
* _(Spring Boot Properties)_ fixed an issue with wrong indentation after inserting property node

## 2017-12-15 (M7)

* _(all)_ issues solved when running on Windows ([#25](https://github.com/spring-projects/sts4/issues/25), [#26](https://github.com/spring-projects/sts4/issues/26), [#29](https://github.com/spring-projects/sts4/issues/29))
* _(Spring Boot Java)_ live hover information now works for inner classes
* _(Spring Boot Properties)_ boot property editing now activated for bootstrap*.yml files in VSCode automatically

## 2017-12-04

* initial public beta launch
