package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

// 闂佸憡甯╅崑鍕箖閺囥垺鏅慨姗嗗亜閳诲繘鏌?qin-cli 闂佹眹鍔岀€氫即鍩€椤掍焦鐨戦柡浣靛€楅弫顕€骞栨担鍛婎仭
import static com.qin.constants.QinConstants.*;

/**
 * 婵＄偑鍊曞﹢鍗灻烘导鏉戣Е妞ゆ牗鑹捐闂佺儵鏅滈崹鐢稿箚婢舵劕闂?
 * 闂佺厧顨庢禍婊勬叏閳轰絼娑㈠焵椤掆偓闇?Qin 婵＄偑鍊曞﹢鍗灻烘导瀵稿祦闁兼悂娼ч埛鏃堟偠?sync
 * 闂佽　鍋撴い鏍ㄧ☉閻?Monorepo闂佹寧绋掓穱娲吹濠婂牆绀夐柕濠忕畱椤ュ洭鏌熺拠鑼濠⒀冪Ч瀵灚寰勬繝鍕啢婵＄偑鍊曞﹢鍗灻?
 */
public class DebugStartup implements ProjectActivity {

    @Nullable
    @Override
    public Object execute(@NotNull Project project,
            @NotNull Continuation<? super Unit> cont) {

        String basePath = project.getBasePath();
        if (basePath == null)
            return Unit.INSTANCE;

        // 闂佸憡甯楃换鍌烇綖閹版澘绀岄柡宥庡幗妤楊垶鏌熼璺ㄥ妽婵☆偀鏅炵粻娑樜旈埀顒€鈻?
        QinLogger.init(basePath, project);
        QinLogger.info("[STARTUP] Qin plugin startup: " + project.getName());
        QinLogger.info("[STARTUP] Project base path: " + basePath);

        // 缂備焦鏌ㄩ鍛暤閸℃稒鐓€鐎广儱娲ㄩ弸?Project SDK闂佹寧绋戦悧鍡楋耿?EDT 缂備焦宕樺▔鏇㈠煝閸忚偐鈻旀い鎾卞妿缁€?
        ApplicationManager.getApplication().invokeLater(() -> {
            configureProjectSdk(project);
        });

        // 闂侀潻璐熼崝宀勫箖濡ゅ懎鐭楅柟娈垮枛濞堢姷绱掔€ｎ亶鍎庡┑鐐叉喘閹粙濡歌閸╃姴鈽夐幘顖氫壕闂?QinProjectSync 闂佸湱鐟抽崱鈺傛杸闂佸憡鑹鹃張顒勵敆閻愮儤鏅柛顐犲劜妤楊垰顫楀☉娅虫垼鍟悗娈垮枛妤犲繒妲愬┑鍠盯鍩€椤掑嫬钃熼柕澹懎顦╅柣搴㈢⊕閿曨偆妲?
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                QinLogger.info("[STARTUP] Background sync started");
                QinProjectSync projectSync = new QinProjectSync(project);
                // 婵帗绋掗…鍫ヮ敇婵犳碍顥堟繛鍡樻尵鐢稒淇婇妞诲亾瀹曞洨顢呴梺鎸庣☉閺堫剟藟閸涙潙钃熼柕澹懎顦╅柣搴㈢⊕钃辨繝鈧笟鈧顐﹀醇閻旀彃浜?
                projectSync.setSilentMode(true).syncAllProjects(true);
                QinLogger.info("[STARTUP] Background sync finished");
            } catch (Exception e) {
                QinLogger.error("[STARTUP] Background sync failed", e);
            }
        });

        // 婵炴垶鎸哥粔鎾疮閳ь剟鏌ゆ總澶夌盎濠殿喒鏅犻獮宥夊箻瀹曞洨锛?Qin 閻庤鎮堕崕閬嶅矗鐠恒劎鐜绘俊銈傚亾鐟滅増鐩弫宥呯暆閸曨亞绱氶梺绋跨箰缁夌懓螞椤栫偞鍊?
        // 闂佹椿娼块崝宥夊春濞戙垹鐭楁い鏍ㄧ懁缁ㄤ即鏌熼棃娑卞剰濠殿喒鏅犻幃娆撴偡閺夋寧鐦栭梺鐟扮仛閹稿摜妲?

        // 闂佸憡鍑归崹鐗堟叏閳哄懏鐓€鐎广儱娲ㄩ弸鍌炴煛閸屾碍鐭楁繛鍡愬灲閹嫰骞嬪┑鍥у壎闂侀潻缍嗛悡澶屾濞嗘挻鍎庨柟瀛樼箖閸?qin.config.json 闂佸憡鐟﹂敋閻庡灚鐓￠弫?
        QinConfigWatcher configWatcher = new QinConfigWatcher(project);
        configWatcher.startWatching();
        QinLogger.info("[STARTUP] Config watcher started");

        // 濡絽鍟弲?闂佸憡鍑归崹鐗堟叏?Java 闂佸搫鍊稿ú锝呪枎閵忋倖鍎庨柟瀛樼箖閸庢棃鏌涢敐搴ｅ帨缂佽鲸鐟╅幆鍕箣濠靛洤鍓?.java 闂佸搫鍊稿ú锝呪枎閵忋倕鐭楁俊顖氭惈椤曆囨煥濞戞鐒烽柛銈嗙矒瀹曟繈濡歌濡炰粙鎮归崶銊ョ厐缂?
        QinJavaFileWatcher javaWatcher = new QinJavaFileWatcher(project);
        javaWatcher.startWatching();
        QinLogger.info("[STARTUP] Java file watcher started (incremental compile + debounce)");

        return Unit.INSTANCE;
    }

    /**
     * 闂佸憡鐟﹂崹褰掔嵁閸ヮ剙绠ラ柍褜鍓熷?Qin 婵＄偑鍊曞﹢鍗灻?
     * 婵炶揪缍€濞夋洟寮?qin-cli 闂?LocalProjectResolver
     */
    public static List<Path> discoverQinProjects(Path ideaProjectDir) {
        return com.qin.core.LocalProjectResolver.scanAllProjects(ideaProjectDir.toString());
    }

    /**
     * 濠碘槅鍋€閸嬫捇鏌＄仦璇插姎闁烩剝鐟╅幆鍕敊閼测晝协婵炴垶鎼╅崢钘壩ｉ幖浣歌Е闁挎棁濮ょ粻?Qin 婵＄偑鍊曞﹢鍗灻烘导瀛樻櫖闁割偅绻冭ぐ銉╂⒑椤愩倕鏋戞い銉ユ嚇瀵濡烽婊咁槷闂佸憡鐟禍鐐诧耿閸涱喚鈻旈柍褜鍓涙禒锕傚磼閿斿墽顦?
     */
    private boolean hasQinProjectInSubdirs(Path dir) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, Files::isDirectory)) {
            for (Path subDir : stream) {
                String dirName = subDir.getFileName().toString();
                if (!EXCLUDED_DIRS.contains(dirName) && !dirName.startsWith(HIDDEN_PREFIX)) {
                    if (Files.exists(subDir.resolve(CONFIG_FILE))) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            // 闂婎偄娲ㄩ弲顐﹀汲?
        }
        return false;
    }

    /**
     * 闂佸湱鐟抽崱鈺傛杸 qin sync 闂佸憡绋掗崹婵嬪箮?
     */
    private void runQinSync(String projectPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(CMD_PREFIX, CMD_FLAG, QIN_CMD,
                "sync");
        pb.directory(new File(projectPath));
        pb.redirectErrorStream(true);

        Process process = pb.start();

        // 闁荤姴娲╅褑銇愰崶銊︾秶闁规儳鍟垮В?
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                QinLogger.info("[sync] " + line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            QinLogger.error("qin sync failed with exit code: " + exitCode);
        }
    }

    /**
     * 闂佺厧顨庢禍婊勬叏閳哄懏鐓€鐎广儱娲ㄩ弸?Project SDK
     * 濠碘槅鍋€閸嬫挻绻涢弶鎴剳闂侇喖顕槐?JDK 濡ょ姷鍋為崕濂割敊閺囩姷纾炬い鏃€妲掔粈瀣渻閵堝懏璐℃繛?SDK
     */
    private static void configureProjectSdk(Project project) {
        try {
            QinLogger.info("[SDK] ========== Configuring Project SDK ==========");

            // 闂佸吋鍎抽崲鑼躲亹閸ヮ亗浜归柟鎯у暱椤?Project SDK
            com.intellij.openapi.projectRoots.ProjectJdkTable jdkTable = com.intellij.openapi.projectRoots.ProjectJdkTable
                    .getInstance();
            com.intellij.openapi.projectRoots.Sdk[] allJdks = jdkTable.getAllJdks();
            QinLogger.info("[SDK] Detected " + allJdks.length + " configured JDK(s)");
            for (com.intellij.openapi.projectRoots.Sdk sdk : allJdks) {
                QinLogger.info("[SDK]   - " + sdk.getName() + " (" + sdk.getHomePath() + ")");
            }

            // 闂佸吋鍎抽崲鑼躲亹閸ャ劊浜滈柛锔诲幗缁愭鏌?SDK 闂備焦婢樼粔鍫曟偪?
            com.intellij.openapi.roots.ProjectRootManager rootManager = com.intellij.openapi.roots.ProjectRootManager
                    .getInstance(project);
            com.intellij.openapi.projectRoots.Sdk currentSdk = rootManager.getProjectSdk();
            QinLogger.info("[SDK] Current Project SDK = " + (currentSdk != null ? currentSdk.getName() : "null"));

            if (currentSdk != null) {
                QinLogger.info("[SDK] Existing Project SDK detected: " + currentSdk.getName() + ", no reconfiguration needed");
                return;
            }

            // 濠电偛澶囬崜婵嗭耿?SDK闂佹寧绋戦懟顖炴儍閸撗勫珰闁哄洨鍠撻崣鈧梺鐟扮仛閸庢娊骞冩惔銊︾劵闁稿本绮嶉悾?JDK
            QinLogger.info("[SDK] No Project SDK configured, selecting the best available JDK...");

            // 婵炴潙鍚嬮敋闁告ɑ鐩濠氬Ψ閿曗偓椤ユ繄鈧湱顭堝鍫曞极閻愬搫绀冪€光偓閳ь剙鈻?JDK
            com.intellij.openapi.projectRoots.Sdk bestSdk = null;
            int bestVersion = 0;

            for (com.intellij.openapi.projectRoots.Sdk sdk : allJdks) {
                if (sdk.getSdkType() instanceof com.intellij.openapi.projectRoots.JavaSdk) {
                    String versionStr = com.intellij.openapi.projectRoots.JavaSdk.getInstance()
                            .getVersionString(sdk);
                    if (versionStr != null) {
                        // 缂備胶濮崑鎾绘煕濡や焦绀夌悮娆撴煛鐎ｎ偄濮夊褎顨婂鐢割敆閳ь剝銇?
                        int version = parseJavaVersion(versionStr);
                        QinLogger.info("[SDK]   Candidate JDK: " + sdk.getName() + " (version: " + version + ")");
                        if (version > bestVersion) {
                            bestVersion = version;
                            bestSdk = sdk;
                        }
                    }
                }
            }

            if (bestSdk != null) {
                final com.intellij.openapi.projectRoots.Sdk sdkToSet = bestSdk;
                final String sdkName = bestSdk.getName();
                QinLogger.info("[SDK] Selected JDK: " + sdkName + " (version: " + bestVersion + ")");

                // 闁荤姳绀佹晶浠嬫偪?Project SDK闂佹寧绋戦悧鍛箾閸ヮ剚鍋ㄩ柕濞垮劚瑜板棝鏌ｉ～顒€濡介柡宀€鍠庨埢鏃堝即椤忓棛顦?
                QinLogger.info("[SDK] Applying selected Project SDK...");
                applyAndPersistSdk(project, rootManager, sdkToSet);
            } else {
                // 濠电偛澶囬崜婵嗭耿娓氣偓楠炲秹骞嗚閻撳倻鈧湱顭堝鍫曞极閻愬搫绀冪€光偓閳ь剙鈻?JDK闂佹寧绋戦懟顖炴儍閸撗勫珰闁哄洦姘ㄩ惌?JAVA_HOME 闂佺厧顨庢禍婊勬叏閳轰脊搴ｆ嫚閹绘帩娼?
                String javaHome = System.getenv("JAVA_HOME");
                if (javaHome != null && !javaHome.isEmpty() && Files.exists(Paths.get(javaHome))) {
                    QinLogger.info("[SDK] No registered JDK found, trying JAVA_HOME: " + javaHome);

                    // 闂佸憡甯楃粙鎴犵磽閹捐妫橀柟娈垮枟閻?JDK
                    com.intellij.openapi.projectRoots.JavaSdk javaSdkType = com.intellij.openapi.projectRoots.JavaSdk
                            .getInstance();

                    // 闂佹眹鍨婚崰鎰板垂?SDK 闂佸憡鑹剧粔鎯扳叿
                    String sdkName = "JDK-" + System.getProperty("java.version", "auto");

                    // 闂佸憡甯楃粙鎴犵磽?SDK
                    com.intellij.openapi.projectRoots.Sdk newSdk = javaSdkType.createJdk(sdkName, javaHome, false);

                    if (newSdk != null) {
                        // 闂佺绻愰悧濠囧锤婵犲洤绀夐柣妯诲絻閻?JDK 闁?
                        ApplicationManager.getApplication().runWriteAction(() -> {
                            jdkTable.addJdk(newSdk);
                        });
                        QinLogger.info("[SDK]   Registered new JDK in IDE: " + sdkName);

                        // 闁荤姳绀佹晶浠嬫偪?Project SDK闂佹寧绋戦悧鍛箾閸ヮ剚鍋ㄩ柕濞垮劚瑜板棝鏌ｉ～顒€濡介柡宀€鍠庨埢鏃堝即椤忓棛顦?
                        applyAndPersistSdk(project, rootManager, newSdk);
                    } else {
                        QinLogger.error("[SDK] Unable to create JDK automatically, please configure it manually");
                    }
                } else {
                    QinLogger.info("[SDK] JAVA_HOME is unavailable, Project SDK remains unset until configured manually");
                    QinLogger.info("[SDK]   JAVA_HOME = " + (javaHome != null ? javaHome : "null"));
                }
            }

            // 闂佸憡甯￠弨閬嶅蓟婵犲啨浜滈柛锔诲幗缁愭绱撴担瑙勫鞍闁诲寒鍨堕弫宥呯暆閸愭儳鏁?IDEA UI 闂佸搫娲ら悺銊╁蓟?
            QinLogger.info("[SDK] Refreshing IDEA project structure after SDK update...");
            refreshProjectStructure(project);

            QinLogger.info("[SDK] ========== Project SDK configuration complete ==========");
        } catch (Exception e) {
            QinLogger.error("[SDK] Failed to configure Project SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 闂佸憡甯￠弨閬嶅蓟婵犲啨浜滈柛锔诲幗缁愭绱撴担瑙勫鞍闁?
     * 闁?IDEA 闂備焦褰冪粔鐢稿蓟婵犲洤绀夐柣妯煎劋缁佹澘顪冮妶鍛础婵炶弓鍗抽弻濠傤吋閸モ晜鐎?
     */
    private static void refreshProjectStructure(Project project) {
        try {
            String basePath = project.getBasePath();

            // 1. 闂佺绻愰悧鍡涘春濞戙垹妫?misc.xml 闂佸搫鍊稿ú锝呪枎閵忋倖鏅€光偓閳ь剟鍨惧Ο鑽も攳?IDEA 闂佺厧纾弲顐耿閸涙潙绀嗛柣妤€鐗嗛悘澶娒归悪鈧崜娑樷枔閹寸偟鈹嶆い鏃囧Г閺?
            if (basePath != null) {
                Path miscXmlPath = Paths.get(basePath, ".idea", "misc.xml");
                com.intellij.openapi.vfs.VirtualFile miscVf = com.intellij.openapi.vfs.LocalFileSystem.getInstance()
                        .refreshAndFindFileByPath(miscXmlPath.toString().replace('\\', '/'));
                if (miscVf != null) {
                    miscVf.refresh(false, false);
                    QinLogger.info("[SDK]   Refreshed misc.xml VirtualFile");
                }
            }

            // 2. 闂佸憡甯￠弨閬嶅蓟婵犲洤鏋佺紓鍫㈠█閸ゅ鏌ц箛鏃傤暡閻庢碍鐟╁顒勫炊閿旂瓔鍋ㄧ紓渚囧灥瀹曠數鍒?
            com.intellij.openapi.vfs.VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
            QinLogger.info("[SDK]   VirtualFileManager refresh complete");

            // 3. 闂佸憡甯￠弨閬嶅蓟婵犲啨浜滈柛锔诲幗缁愭淇婇妞诲亾瀹曞洠鍋?
            ApplicationManager.getApplication().invokeLater(() -> {
                try {
                    // 闁荤喐鐟辩粻鎴ｃ亹閸屾稏浜滈柛锔诲幗缁愭姊洪幓鎺斝ｉ柡灞斤躬瀹曘儳浠︾紒銏犲Ρ
                    com.intellij.openapi.project.DumbService dumbService = com.intellij.openapi.project.DumbService
                            .getInstance(project);

                    dumbService.runWhenSmart(() -> {
                        QinLogger.info("[SDK]   Project index rebuild complete");

                        // 闂佸憡鍔曠粔鐢割敃閸忕⒈娈界€光偓閸愵亝顫?SDK 闁荤姳绀佹晶浠嬫偪?
                        com.intellij.openapi.roots.ProjectRootManager rootManager = com.intellij.openapi.roots.ProjectRootManager
                                .getInstance(project);
                        com.intellij.openapi.projectRoots.Sdk sdk = rootManager.getProjectSdk();
                        QinLogger.info("[SDK]   Project SDK after refresh = " + (sdk != null ? sdk.getName() : "null"));
                    });
                } catch (Exception e) {
                    QinLogger.error("[SDK]   Failed during deferred project refresh: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            QinLogger.error("[SDK]   Failed to refresh project structure: " + e.getMessage());
        }
    }

    /**
     * 婵烇絽娲︾换鍌炴偤閵婏负浜滈柛锔诲幗缁愭鎮规担绋库挃闁汇倕妫濆畷姘跺箥椤曞懏鈷栭梺?
     * 闂婎偄娲ら幊姗€濡磋箛娑樻嵍?write action 婵犮垼鍩栭悧鐘诲磿鐎靛憡瀚柛鎰典簼閺?
     */
    private static void saveProjectToDisk(Project project) {
        ApplicationManager.getApplication().invokeLater(() -> {
            try {
                project.save();
                QinLogger.info("[SDK]   Project save requested");
            } catch (Exception e) {
                QinLogger.error("[SDK]   Failed to save project: " + e.getMessage());
            }
        });
    }

    /**
     * 闁圭厧鐡ㄥ濠氬极閵堝宓侀柤鎼佹涧閻︻喖鈽夐弬鎸庡櫣閻?SDK 闁荤姳绀佹晶浠嬫偪?
     * 闁哄鏅滈悷锕€危閸濄儲濯奸柛鎾楀懏鐎?Project SDK 闂佹眹鍔岀€氼喚鍒掗悜妯尖枖闁逞屽墴瀵剛鎲撮崟顓溾偓?
     * 
     * @param project     婵＄偑鍊曞﹢鍗灻?
     * @param rootManager 婵＄偑鍊曞﹢鍗灻烘导鏉戝唨闁革富鍙庨崥鈧梺鑽ゅ仜濡鈻?
     * @param sdk         闁荤喐娲戝ù鍥敊閺囩姷纾炬い鏃傚亾閻?SDK
     */
    private static void applyAndPersistSdk(Project project,
            com.intellij.openapi.roots.ProjectRootManager rootManager,
            com.intellij.openapi.projectRoots.Sdk sdk) {
        String sdkName = sdk.getName();
        QinLogger.info("[SDK]   Applying SDK: " + sdkName);

        // 1. 婵炶揪缍€濞夋洟寮?IDEA API 闁荤姳绀佹晶浠嬫偪閸℃稑绀冮柛娑卞幘閹界姴鈽夐幙鍐ㄥ箻婵炲牊鍨垮畷?
        ApplicationManager.getApplication().runWriteAction(() -> {
            rootManager.setProjectSdk(sdk);
        });
        QinLogger.info("[SDK]   Writing Project SDK to misc.xml");

        // 2. 闂佺儵鏅涢悺銊ф暜鐎涙鈹嶆い鏃囧Г閺?misc.xml 缂佺虎鍙庨崰鏇犳崲濮樿泛绠板ù锝夘棑閻ｄ粙鏌?
        String basePath = project.getBasePath();
        if (basePath != null) {
            Path miscXml = Paths.get(basePath, ".idea", "misc.xml");
            updateMiscXmlWithSdk(miscXml, sdkName);
        }

        // 3. 闂佸憡甯￠弨閬嶅蓟?IDEA
        refreshProjectStructure(project);

        // 4. 婵°倗濮撮惌渚€鎯?
        com.intellij.openapi.projectRoots.Sdk afterSdk = rootManager.getProjectSdk();
        if (afterSdk != null && afterSdk.getName().equals(sdkName)) {
            QinLogger.info("[SDK] Project SDK persisted to misc.xml: " + sdkName);
        } else {
            QinLogger.info("[SDK]   misc.xml updated, you may need to reopen the project for changes to fully apply");
        }
    }

    /**
     * 闂佺儵鏅涢悺銊ф暜鐎涙鈹嶆い鏃囧Г閺?misc.xml 闂佸搫鍊稿ú锝呪枎閵忋垺濯奸柛鎾楀懏鐎?Project SDK
     * 闁哄鏅滈悷锕€危閹间礁瀚夐柍褜鍓熷畷锝夘敍閻愬瓨绁梺姹囧妼鐎氼參寮婚悢濂夊殨闊洢鍎崇粈澶岀棯椤撗冩灆缂?SDK 闁荤姳绀佹晶浠嬫偪閸℃瑦鍋栨い鎰剁到閻︻喖鈽夐弬鎸庡櫣閻?
     */
    private static void updateMiscXmlWithSdk(Path miscXml, String sdkName) {
        try {
            QinLogger.info("[SDK]   Updating misc.xml: " + miscXml);

            String content;
            if (Files.exists(miscXml)) {
                content = Files.readString(miscXml);
            } else {
                // 闂佸憡甯楃粙鎴犵磽閹捐妫橀柟娈垮枟閻?misc.xml
                content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<project version=\"4\">\n" +
                        "</project>";
                Files.createDirectories(miscXml.getParent());
            }

            // 濠碘槅鍋€閸嬫捇鏌＄仦璇插姕婵″弶鎮傚畷銉╂晝閳ь剟宕欓敓鐘插珘?ProjectRootManager 缂傚倷绀佺€氼亜鈻?
            if (content.contains("<component name=\"ProjectRootManager\"")) {
                // 闂佸搫娲ら悺銊╁蓟婵犲洦鍋濋柣妤€鐗婄粻鎺旂磽娴ｇ顏ф繛?
                if (content.contains("project-jdk-name=")) {
                    // 闂佸搫娲︾€笛冪暦閺屻儲鍋濋柣妤€鐗婄粻鎺楁煟?project-jdk-name
                    content = content.replaceAll("project-jdk-name=\"[^\"]*\"",
                            "project-jdk-name=\"" + sdkName + "\"");
                    QinLogger.info("[SDK]   Updated project-jdk-name attribute");
                } else {
                    // 濠电儑缍€椤曆勬叏?project-jdk-name 闁诲繒鍋熼崑鐐哄焵?
                    content = content.replace("<component name=\"ProjectRootManager\"",
                            "<component name=\"ProjectRootManager\" project-jdk-name=\"" + sdkName
                                    + "\" project-jdk-type=\"JavaSDK\"");
                    QinLogger.info("[SDK]   Added project-jdk-name attribute");
                }

                // 缂佺虎鍙庨崰鏇犳崲?project-jdk-type 闁诲孩绋掗敋婵犫偓?
                if (!content.contains("project-jdk-type=")) {
                    content = content.replace("project-jdk-name=\"" + sdkName + "\"",
                            "project-jdk-name=\"" + sdkName + "\" project-jdk-type=\"JavaSDK\"");
                }
            } else {
                // 濠电儑缍€椤曆勬叏閻愬搫妫橀柟娈垮枟閻?ProjectRootManager 缂傚倷绀佺€氼亜鈻?
                String component = "  <component name=\"ProjectRootManager\" version=\"2\" " +
                        "project-jdk-name=\"" + sdkName + "\" project-jdk-type=\"JavaSDK\">\n" +
                        "    <output url=\"file://$PROJECT_DIR$/out\" />\n" +
                        "  </component>\n";
                content = content.replace("</project>", component + "</project>");
                QinLogger.info("[SDK]   Added ProjectRootManager component");
            }

            // 闂佸憡鍔栭悷銉ッ洪弽顓炴闁搞儻闄勯?
            Files.writeString(miscXml, content);
            QinLogger.info("[SDK]   misc.xml now contains project-jdk-name=\"" + sdkName + "\"");

            // 婵°倗濮撮惌渚€鎯佹径鎰婵炲棗绻愬?
            String verify = Files.readString(miscXml);
            if (verify.contains("project-jdk-name=\"" + sdkName + "\"")) {
                QinLogger.info("[SDK] misc.xml write verification succeeded");
            } else {
                QinLogger.error("[SDK] misc.xml write verification failed");
            }
            // 濠电偛顦崝宥夊礈娴煎瓨鏅慨姗嗗墮閻撴洟鏌?IDEA 闂傚倸娲犻崑鎾绘偡閺囨氨顦︽繝鈧鍡樺闁告劦浜濋弳蹇擃熆鐠哄搫顏у┑鐐叉喘閹?refreshProjectStructure(project)
        } catch (Exception e) {
            QinLogger.error("[SDK]   Failed to update misc.xml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 闁荤喐鐟辩徊楣冩倵?Java 闂佺粯顨呴悧濠傦耿娴兼潙鐭?
     */
    private static int parseJavaVersion(String versionStr) {
        try {
            // 闂佸憡鐗曠紞濠囧储閵堝鍋嬮柛顐ゅ枑閹烽亶鏌涘▎蹇撴缂佽鲸绻冩穱?"21", "17", "1.8"
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("(\\d+)");
            java.util.regex.Matcher matcher = pattern.matcher(versionStr);
            if (matcher.find()) {
                int version = Integer.parseInt(matcher.group(1));
                // 1.8 -> 8
                if (version == 1 && matcher.find()) {
                    version = Integer.parseInt(matcher.group(1));
                }
                return version;
            }
        } catch (Exception e) {
            // 闂婎偄娲ㄩ弲顐﹀汲?
        }
        return 0;
    }

    /**
     * 闂佸搫琚崕鍙夌珶濡皷鍋撻悽鍨殌缂併劍鐓￠幆?sources jar 闂佸搫鍊稿ú锝呪枎?
     * 婵炴挻鑹鹃鍛淬€? xxx.jar -> xxx-sources.jar
     */
    private static String findSourcesJar(String jarPath) {
        if (jarPath == null || !jarPath.endsWith(".jar")) {
            return null;
        }

        // 闁诲繐绻戠换鍡涙儊椤栫偛鎹堕柕濞垮劜閸婂崬鈽夐幘顖氫壕闂佺儵鏅╅崰鏍礊瀹ュ棛鈻旈悗锝庡亞閸欌偓闂?-sources.jar
        String basePath = jarPath.substring(0, jarPath.length() - 4); // 缂備礁顦…宄扳枍?.jar
        String sourcesPath = basePath + "-sources.jar";

        if (java.nio.file.Files.exists(java.nio.file.Paths.get(sourcesPath))) {
            return sourcesPath.replace("\\", "/");
        }

        return null;
    }

    /**
     * 闂佸搫琚崕鍙夌珶濡皷鍋撻悽鍨殌缂併劍鐓￠幆?javadoc jar 闂佸搫鍊稿ú锝呪枎?
     * 婵炴挻鑹鹃鍛淬€? xxx.jar -> xxx-javadoc.jar
     */
    private static String findJavadocJar(String jarPath) {
        if (jarPath == null || !jarPath.endsWith(".jar")) {
            return null;
        }

        // 闁诲繐绻戠换鍡涙儊椤栫偛鎹堕柕濞垮劜閸婂崬鈽夐幘顖氫壕闂佺儵鏅╅崰鏍礊瀹ュ棛鈻旈悗锝庡亞閸欌偓闂?-javadoc.jar
        String basePath = jarPath.substring(0, jarPath.length() - 4); // 缂備礁顦…宄扳枍?.jar
        String javadocPath = basePath + "-javadoc.jar";

        if (java.nio.file.Files.exists(java.nio.file.Paths.get(javadocPath))) {
            return javadocPath.replace("\\", "/");
        }

        return null;
    }

    /**
     * 婵?Qin 婵＄偑鍊曞﹢鍗灻烘导瀛樺仺闁绘梻顭堥悘?.iml 闂佸搫鍊稿ú锝呪枎?
     * 闁?IDEA 闁荤姴娲ゅΛ妤呭春閸℃せ鏀﹂柟閭︿簻閺佲晠鏌ｉ鏄忓厡婵炲弶濯介妵?
     * 
     * @param forceOverwrite true=閻庢鍠栭幖顐﹀春濡ゅ啯鍟洪柛鈩冪懄绾句即鏌ㄥ☉妯煎濠⒀勭矒瀹?sync闂佹寧绋戦¨鈧紒杈ㄧ吹alse=閻庣懓鎲¤ぐ鍐偤閵娾晛鎹堕柕濞垮妿閸庢煡寮堕埡浣圭カ缂佽鲸鐟╅幊娑㈩敂閸曨倣妤呮煕濮樼厧鐏犲┑顔规櫊閺?
     */
    public static void generateImlFile(Path projectPath, boolean forceOverwrite) {
        generateImlFile(projectPath, forceOverwrite, null);
    }

    /**
     * 婵?Qin 婵＄偑鍊曞﹢鍗灻烘导瀛樺仺闁绘梻顭堥悘?.iml 闂佸搫鍊稿ú锝呪枎?
     * 闁?IDEA 闁荤姴娲ゅΛ妤呭春閸℃せ鏀﹂柟閭︿簻閺佲晠鏌ｉ鏄忓厡婵炲弶濯介妵?
     * 
     * @param forceOverwrite true=閻庢鍠栭幖顐﹀春濡ゅ啯鍟洪柛鈩冪懄绾句即鏌ㄥ☉妯煎濠⒀勭矒瀹?sync闂佹寧绋戦¨鈧紒杈ㄧ吹alse=閻庣懓鎲¤ぐ鍐偤閵娾晛鎹堕柕濞垮妿閸庢煡寮堕埡浣圭カ缂佽鲸鐟╅幊娑㈩敂閸曨倣妤呮煕濮樼厧鐏犲┑顔规櫊閺?
     * @param ideaDir        IDEA 婵＄偑鍊曞﹢鍗灻烘导瀛樺剭?.idea 闂佺儵鏅╅崰鏍礊瀹ュ洦宕夋い鏍ㄦ皑缁愮偤鏌ㄥ☉妯煎ⅱ闁轰降鍊栫粋宥嗘償閵忊剝娈橀梺鍛婂姇閺堫剝鍟梺绉嗗嫷娈ｇ紒?
     */
    public static void generateImlFile(Path projectPath, boolean forceOverwrite, Path ideaDir) {
        try {
            // 闂佸吋鍎抽崲鑼躲亹閸ャ劊浜滈柛锔诲幗缁愭鏌涘顒傂ょ悮?
            String projectName = projectPath.getFileName().toString();
            Path imlPath = projectPath.resolve(projectName + ".iml");

            QinLogger.info("[iml] Processing project: " + projectPath);
            QinLogger.info("[iml]   iml path: " + imlPath);
            QinLogger.info("[iml]   forceOverwrite: " + forceOverwrite);

            // 婵犵鈧啿鈧綊鎮樻径濠庡晠闁圭粯甯為幗鐘绘煕閿斿搫濡虹紒妤佺墬缁嬪顓奸崨顓犵畳闂佸憡甯楅崕濂搞€呴锔藉剮闁哄秶鏁哥粈澶嬩繆椤愮喎浜鹃梺鍝勮閸庢彃危閹间礁瑙﹂柨鏇炲€规禒姗€鎮烽弴姘樂闁逛究鍔嶅?sourceFolder
            boolean needGenerate = !Files.exists(imlPath) || forceOverwrite;

            if (!needGenerate) {
                QinLogger.info("[iml]   Existing .iml found, checking whether repair is needed...");
                // 濠碘槅鍋€閸嬫捇鏌＄仦璇插姢妤犵偛娲?.iml 闂佸搫瀚烽崹浼村箚娴ｈ櫣纾介柛婵嗗濮?sourceFolder
                String existingContent = Files.readString(imlPath);
                if (!existingContent.contains("<sourceFolder")) {
                    QinLogger.info("[iml]   Missing sourceFolder configuration, attempting repair...");
                    String fixedContent = fixMissingSourceFolder(existingContent, projectPath);
                    if (fixedContent != null && !fixedContent.equals(existingContent)) {
                        Files.writeString(imlPath, fixedContent);
                        QinLogger.info("[iml]   sourceFolder configuration repaired");
                    }
                } else {
                    QinLogger.info("[iml]   sourceFolder configuration already present");
                }
            } else {
                // 婵炶揪缍€濞夋洟寮?BSP 婵犮垼娉涚€氼噣骞冩繝鍥ч棷闁靛鍔岀粻顖炴煕濞嗘劗澧甸柕鍡楃Ч閹嫰顢欑拋宕囩畾闂?
                com.qin.bsp.BspHandler bspHandler = new com.qin.bsp.BspHandler(projectPath.toString());

                // 闂佸吋鍎抽崲鑼躲亹閸パ€鏀﹂柟閭︿簻閺佲晠鏌ｉ鏄忓厡婵炲弶濯介妵鎰板即椤忓棛顦╂繛鏉戝悑閿氶柛妯荤⊕缁?qin.config.json闂?
                String sourceDir = bspHandler.getSourceDir();
                String testDir = bspHandler.getTestDir();

                // 濠碘槅鍋€閸嬫捇鏌＄仦璇插姢婵炲弶濯介妵鎰板即閻旀劅锕傛煕濮樺墽鐣遍柣銈呭濮婁粙宕ㄩ鐘虫喖闂?
                if (!Files.exists(projectPath.resolve(sourceDir))) {
                    // 闂佹悶鍎抽崑銈夊焵椤戣棄浜鹃梺鍛婂笚婵粙宕靛鍫濈闁靛瀵屽鐐箾?
                    sourceDir = detectSourceDir(projectPath);
                }
                QinLogger.info("[iml]   sourceDir: " + sourceDir);
                QinLogger.info("[iml]   testDir: " + testDir);

                if (sourceDir == null) {
                    QinLogger.info("[iml]   Source directory not found");
                    return;
                }

                // 闂佸吋鍎抽崲鑼躲亹閸ャ劍缍囬柟鎯у暱濮ｅ鏌ｉ埡濠傛灈缂?
                String outputDir = bspHandler.getOutputDir();
                QinLogger.info("[iml]   outputDir: " + outputDir);

                // 闂佹眹鍨婚崰鎰板垂濮樿泛绠抽柟鐑橆殕閻濈喖鏌ｉ埡濠傛灈缂?XML
                StringBuilder excludeFolders = new StringBuilder();
                for (String excludeDir : com.qin.debug.QinConstants.IML_EXCLUDED_DIRS) {
                    excludeFolders.append("          <excludeFolder url=\"file://$MODULE_DIR$/")
                            .append(excludeDir)
                            .append("\" />\n");
                }

                // 闂佹眹鍨婚崰鎰板垂濮橆兘鏀﹂柟閭﹀幗閻庮喖霉閻樼儤纭鹃柕?XML
                StringBuilder sourceFolders = new StringBuilder();
                sourceFolders.append("      <sourceFolder url=\"file://$MODULE_DIR$/").append(sourceDir)
                        .append("\" isTestSource=\"false\" />\n");
                if (testDir != null && Files.exists(projectPath.resolve(testDir))) {
                    sourceFolders.append("      <sourceFolder url=\"file://$MODULE_DIR$/").append(testDir)
                            .append("\" isTestSource=\"true\" />\n");
                }

                // 闂備緡鍋呮穱铏规崲?BSP 闂佸吋鍎抽崲鑼躲亹閸ャ劎鐟规繝闈涳功椤╊偊鏌ㄥ☉妯荤秶lasspath闂?
                List<String> classpath = bspHandler.getClasspath();
                StringBuilder dependencyEntries = new StringBuilder();

                for (String path : classpath) {
                    String entryPath = path.replace("\\", "/");

                    if (entryPath.endsWith(".jar")) {
                        // JAR 闂佸搫鍊稿ú锝呪枎閵忥紕鐟规繝闈涳功椤?- 濠碘槅鍋€閸嬫捇鏌＄仦璇插姕婵″弶鎮傚畷銉╂晜閼恒儳鐣抽柣搴ｆ暩閹虫挾鑺遍弻銉﹀剭?sources 闂?javadoc
                        String sourcesPath = findSourcesJar(entryPath);
                        String javadocPath = findJavadocJar(entryPath);

                        dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                                .append("      <library>\n")
                                .append("        <CLASSES>\n")
                                .append("          <root url=\"jar://").append(entryPath).append("!/\" />\n")
                                .append("        </CLASSES>\n");

                        // 濠电儑缍€椤曆勬叏?JAVADOC 闂佺厧鎼崐濠氬磻?
                        if (javadocPath != null) {
                            dependencyEntries.append("        <JAVADOC>\n")
                                    .append("          <root url=\"jar://").append(javadocPath).append("!/\" />\n")
                                    .append("        </JAVADOC>\n");
                        } else {
                            dependencyEntries.append("        <JAVADOC />\n");
                        }

                        // 濠电儑缍€椤曆勬叏?SOURCES 闂佺厧鎼崐濠氬磻?
                        if (sourcesPath != null) {
                            dependencyEntries.append("        <SOURCES>\n")
                                    .append("          <root url=\"jar://").append(sourcesPath).append("!/\" />\n")
                                    .append("        </SOURCES>\n");
                        } else {
                            dependencyEntries.append("        <SOURCES />\n");
                        }

                        dependencyEntries.append("      </library>\n")
                                .append("    </orderEntry>\n");
                        QinLogger.info("[iml]   Added JAR dependency: " + entryPath +
                                (sourcesPath != null ? " (+sources)" : "") +
                                (javadocPath != null ? " (+javadoc)" : ""));
                    } else {
                        // 闂佸搫鐗滈崜娆忥耿鐎靛摜灏甸柤濮愬€栫粣妤冩喐?- 闁荤姳绶ょ槐鏇㈡偩閼姐倐鍋撻悽鍨殌缂併劍鐓￠幆鍐礋椤掑倻鍩嶉梺娲诲枙閼宠泛煤閹峰被浜?
                        String sourcePath = computeSourcePath(entryPath);

                        dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                                .append("      <library>\n")
                                .append("        <CLASSES>\n")
                                .append("          <root url=\"file://").append(entryPath).append("\" />\n")
                                .append("        </CLASSES>\n");

                        // 婵犵鈧啿鈧綊鎮樻径鎰闁诡垎鍐帓濠电姍鍕闁绘牗绮撻幆鍕敊閼测晝协闂佹寧绋戦張顒勫锤婵犲洤绀?SOURCES 闂備焦婢樼粔鍫曟偪?
                        if (sourcePath != null) {
                            dependencyEntries.append("        <SOURCES>\n")
                                    .append("          <root url=\"file://").append(sourcePath).append("\" />\n")
                                    .append("        </SOURCES>\n");
                            QinLogger.info("[iml]   Added local classpath entry: " + entryPath + " (sources: " + sourcePath + ")");
                        } else {
                            QinLogger.info("[iml]   Added local classpath entry: " + entryPath + " (sources not found)");
                        }

                        dependencyEntries.append("      </library>\n")
                                .append("    </orderEntry>\n");
                    }
                }

                // 闂佹眹鍨婚崰鎰板垂?.iml 闂佸憡鍔曢幊搴敊?
                String imlContent = """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <module type="JAVA_MODULE" version="4">
                          <component name="NewModuleRootManager" inherit-compiler-output="false">
                            <exclude-output />
                            <output url="file://$MODULE_DIR$/%s" />
                            <output-test url="file://$MODULE_DIR$/%s" />
                            <content url="file://$MODULE_DIR$">
                        %s%s    </content>
                            <orderEntry type="inheritedJdk" />
                            <orderEntry type="sourceFolder" forTests="false" />
                        %s  </component>
                        </module>
                        """.formatted(outputDir, outputDir.replace("classes", "test-classes"),
                        sourceFolders.toString(), excludeFolders.toString(), dependencyEntries.toString());

                Files.writeString(imlPath, imlContent);
                QinLogger.info("Generated .iml file via BSP: " + projectName + ".iml");
            }

            // 濠电偛顦崝宀勫船閽樺）鐔煎灳閾忣偄浠撮梺?modules.xml
            if (ideaDir != null) {
                registerModuleToIdeaProject(imlPath, ideaDir);
            }

        } catch (Exception e) {
            QinLogger.error("Failed to generate .iml file: " + e.getMessage());
        }
    }

    /**
     * 濠电偛顦崝宀勫船閽樺）鐔煎灳閾忣偄浠撮梺?IDEA 闂?modules.xml
     */
    private static void registerModuleToIdeaProject(Path imlPath, Path ideaDir) {
        try {
            Path modulesXml = ideaDir.resolve("modules.xml");

            // 闁荤姳绶ょ槐鏇㈡偩婵犳碍鍎庣紒瀣儥閸ょ娀鎮规笟顖氱仩缂?
            Path ideaParent = ideaDir.getParent(); // 婵＄偑鍊曞﹢鍗灻烘导鏉戝唨闁革富鍘界粣妤冩喐?
            Path relativePath = ideaParent.relativize(imlPath);
            String moduleEntry = relativePath.toString().replace("\\", "/");

            String content;
            if (!Files.exists(modulesXml)) {
                // modules.xml 婵炴垶鎸哥粔鎾偤閵娾晛鎹舵い顓熷笧缁€澶愭煕閹烘挾鈽夌紓鍌涙崌瀵剟骞嶉鐣屾殸
                QinLogger.info("[iml]   modules.xml not found, creating a new file");
                content = """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project version="4">
                          <component name="ProjectModuleManager">
                            <modules>
                            </modules>
                          </component>
                        </project>
                        """;
            } else {
                content = Files.readString(modulesXml);
            }

            // 濠碘槅鍋€閸嬫捇鏌＄仦璇插姕婵″弶鎮傚畷銉╂晝閳ь剟宕欓敍鍕＜闊洦姊归弳鍫ユ煕?
            if (content.contains(moduleEntry)) {
                QinLogger.info("[iml]   Module already registered in modules.xml");
                return;
            }

            // 闂佸搫顑呯€氼剛绱撻幘璇叉闁规鍠楅悾?module 闂佸搫顧€缁辨洖煤?
            String newModule = String.format(
                    "      <module fileurl=\"file://$PROJECT_DIR$/%s\" filepath=\"$PROJECT_DIR$/%s\" />",
                    moduleEntry, moduleEntry);

            // 闂?</modules> 婵炴垶鏌ㄩ鍛櫠閻樿绠甸柟鐑樺灥瀵?
            String newContent = content.replace("    </modules>", newModule + "\n    </modules>");

            Files.writeString(modulesXml, newContent);
            QinLogger.info("[iml]   Registered module in modules.xml: " + moduleEntry);

        } catch (Exception e) {
            QinLogger.error("[iml]   Failed to update modules.xml: " + e.getMessage());
        }
    }

    /**
     * 濠碘槅鍋€閸嬫挻绻涢弶鎴剱缂侇煈鍠楃粋鎺楁晲閸モ晛鐏ｉ梺鐑╂櫓閸犳牜绱?
     */
    private static String detectSourceDir(Path projectPath) {
        // 婵炴潙鍚嬮敋闁告ɑ绋戣灋闁逞屽墮闇夐悗锝庡亞閸ㄥジ鏌?Maven 缂傚倷鐒﹂幐濠氭倵?
        Path mavenSrc = projectPath.resolve(DEFAULT_SOURCE_DIR);
        if (Files.exists(mavenSrc)) {
            return DEFAULT_SOURCE_DIR;
        }
        // 闂佺绻楅崺鏍敃閻撳宫娑㈠焵椤掆偓闇夐悗锝庡墰閺嗗棝鏌涘Δ浣圭缂侇喓鍔戝?
        Path simpleSrc = projectPath.resolve("src");
        if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
            return "src";
        }
        // 濠电偛澶囬崜婵囩珶濮椻偓瀹曟岸鎮ч崼銏㈠煃婵炲濯寸徊鍧楁偉濠婂牊鍎庢い鏃囧亹缁?
        return null;
    }

    /**
     * 闂佸搫绉烽～澶婄暤娴ｈ櫣灏甸柡鍕箳缂堝鏌涢幋锝嗩仩婵炲弶濯介妵鎰板即閳藉棗鎮佺紓浣哄У椤ㄥ棛鑺遍鈧幆宥嗘媴閻戞鐛ラ悷?
     * 婵炴挻鑹鹃鍛淬€? D:/project/subhuti-java/build/classes ->
     * D:/project/subhuti-java/src/main/java
     */
    private static String computeSourcePath(String classPath) {
        try {
            // 闁?build/classes 闂佸搫娲︾€笛冪暦閸欏鈻旈柣鎴烆焽閻栭亶鏌ｉ鏄忓厡婵炲弶濯介妵?
            Path classDir = Paths.get(classPath);

            // 闂佸憡纰嶉崹宕囩箔閸岀偛绠ラ柟顖嗗啰鍘掓俊鐐€曞﹢鍗灻烘导鏉戝唨闁革富鍘界粣妤冩喐閻楀牊绌跨紒杈ㄧ懇瀹曠娀宕ㄩ鐔峰壍 build 闂佹眹鍔岀€氼噣宕哄Δ鍛剮妞ゆ棁鍋愮粔鍧楁煥?
            Path current = classDir;
            while (current != null && !current.getFileName().toString().equals("build")) {
                current = current.getParent();
            }

            if (current != null && current.getParent() != null) {
                Path projectRoot = current.getParent();

                // 濠碘槅鍋€閸嬫捇鏌?src/main/java
                Path mavenSrc = projectRoot.resolve(DEFAULT_SOURCE_DIR);
                if (Files.exists(mavenSrc)) {
                    return mavenSrc.toString().replace("\\", "/");
                }

                // 濠碘槅鍋€閸嬫捇鏌?src
                Path simpleSrc = projectRoot.resolve("src");
                if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
                    return simpleSrc.toString().replace("\\", "/");
                }
            }
        } catch (Exception e) {
            // 闂婎偄娲ㄩ弲顐﹀汲?
        }
        return null;
    }

    /**
     * 婵烇絽娴傞崰鏍囬懠顒傜＝闁告繂瀚В?sourceFolder 闂?.iml 闂佸搫鍊稿ú锝呪枎閵忋倕绀冮柛娑卞弾閸?
     * 闁诲繐绻愬Λ婊堝吹濠婂牊鈷掓い鎾跺仦閸娿倝鏌?<content url="..." /> 闁哄鍎愰崜姘暦閸欏鈻旈柛婵嗗閻﹀爼鏌?sourceFolder 闂佹眹鍔岀€氼剟鎮鹃鍕瀬闁哄鍨奸崺宀€鈧?
     */
    private static String fixMissingSourceFolder(String imlContent, Path projectPath) {
        try {
            // 濠碘槅鍋€閸嬫挻绻涢弶鎴剱缂侇煈鍠楃粋鎺楁晲閸モ晛鐏ｉ梺鐑╂櫓閸犳牜绱?
            String sourceDir = detectSourceDir(projectPath);
            if (sourceDir == null) {
                QinLogger.info("[iml]   Could not detect source directory, skipping sourceFolder repair");
                return imlContent;
            }

            // 濠碘槅鍋€閸嬫捇鏌＄仦璇插姕婵″弶鎮傚畷銉╂晜閼恒儛锕傛煠婵傚閭俊顐ｆ尦瀹曘儵宕奸姀锛勬殸 content 闂佸搫绉村ú銊╊敆?
            java.util.regex.Pattern selfClosingPattern = java.util.regex.Pattern.compile(
                    "<content\\s+url=\"[^\"]*\"\\s*/>");
            java.util.regex.Matcher matcher = selfClosingPattern.matcher(imlContent);

            if (matcher.find()) {
                // 闂佺懓鐏氶崕鎶藉春瀹€鍕殜妞ゅ繐鐗婇敍鏃堟煕濮橆剛澧㈡繛?content 闂佸搫绉村ú銊╊敆閻戣姤鏅€光偓閸曨兛绮柣鐔告磻閻掞箑霉濞戙垹绠叉い顐弨缁€瀣倵閻熺増婀伴柡鍡秬閵囨劙濮€閻樼數顢?
                String originalTag = matcher.group();
                int urlStart = originalTag.indexOf("url=\"") + 5;
                int urlEnd = originalTag.indexOf("\"", urlStart);
                String url = originalTag.substring(urlStart, urlEnd);

                // 闂佸搫顑呯€氼剛绱撻幘鍨涘亾閻熺増婀伴柡鍡秮閹?content 闂佸搫绉村ú銊╊敆?
                StringBuilder newContent = new StringBuilder();
                newContent.append("<content url=\"").append(url).append("\">\n");
                newContent.append("      <sourceFolder url=\"file://$MODULE_DIR$/")
                        .append(sourceDir).append("\" isTestSource=\"false\" />\n");

                // 濠碘槅鍋€閸嬫捇鏌＄仦璇插姕婵″弶鎮傚畷銉╂晜閼恒儳鐣冲┑鐐存綑椤戝牓鎯侀鐐村剮妞ゆ棁鍋愮粔?
                Path testDir = projectPath.resolve("src/test/java");
                if (Files.exists(testDir)) {
                    newContent.append(
                            "      <sourceFolder url=\"file://$MODULE_DIR$/src/test/java\" isTestSource=\"true\" />\n");
                }

                // 濠电儑缍€椤曆勬叏閻愬搫绠抽柟鐑橆殕閻濈喖鏌ｉ埡濠傛灈缂?
                for (String excludeDir : QinConstants.IML_EXCLUDED_DIRS) {
                    newContent.append("          <excludeFolder url=\"file://$MODULE_DIR$/")
                            .append(excludeDir).append("\" />\n");
                }

                newContent.append("    </content>");

                return imlContent.replace(originalTag, newContent.toString());
            }

            return imlContent;
        } catch (Exception e) {
            QinLogger.error("[iml]   Failed to repair sourceFolder configuration: " + e.getMessage());
            return imlContent;
        }
    }
}
