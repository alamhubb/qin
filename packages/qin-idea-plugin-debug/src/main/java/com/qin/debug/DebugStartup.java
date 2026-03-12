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

// 闂備礁鎲＄敮鈺呭磻閸曨垰绠栭柡鍥ュ灪閺咁剚鎱ㄥ鍡椾簻闁宠绻橀弻?qin-cli 闂備焦鐪归崝宀€鈧矮鍗抽崺鈧い鎺嶇劍閻ㄦ垿鏌℃担闈涒偓妤呭极椤曗偓楠炴牗鎷呴崨濠庝画
import static com.qin.constants.QinConstants.*;

/**
 * 濠碉紕鍋戦崐鏇烇耿閸楃伝鐑樺閺夋垼袝濡炪倖鐗楅懝鎹愵杽闂備胶鍎甸弲婊堝垂閻㈢绠氬鑸靛姇闂?
 * 闂備胶鍘ч〃搴㈢濠婂嫭鍙忛柍杞扮导濞戙垹鐒垫い鎺嗗亾闂?Qin 濠碉紕鍋戦崐鏇烇耿閸楃伝鐑樺鐎电绁﹂梺鍏兼倐濞佳囧煕閺冨牊鍋?sync
 * 闂備浇銆€閸嬫挻銇勯弽銊р槈闁?Monorepo闂備焦瀵х粙鎺撶┍濞差亜鍚规繝濠傜墕缁€澶愭煏婵犲繒鐣辨い銉ユ喘閺岀喓鎷犻懠顒傤唹婵犫拃鍐х€殿喖鐏氬鍕節閸曨剚鍟㈠┑锛勫亼閸婃洖锕㈤崡鐏?
 */
public class DebugStartup implements ProjectActivity {

    @Nullable
    @Override
    public Object execute(@NotNull Project project,
            @NotNull Continuation<? super Unit> cont) {

        String basePath = project.getBasePath();
        if (basePath == null)
            return Unit.INSTANCE;

        // 闂備礁鎲＄敮妤冩崲閸岀儑缍栭柟鐗堟緲缁€宀勬煛瀹ュ骸骞楀Δ妤婂灦閺岀喖顢栫捄銊ュ濠碘槅鍋€閺呯偟绮诲☉妯滄棃鍩€椤掆偓閳?
        QinLogger.init(basePath, project);
        QinLogger.info("[STARTUP] Qin plugin startup: " + project.getName());
        QinLogger.info("[STARTUP] Project base path: " + basePath);

        // 根据 Qin 配置链自动补齐 Project SDK。已有用户配置时不覆盖。
        if (hasQinSdkContext(Paths.get(basePath))) {
            ApplicationManager.getApplication().invokeLater(() -> configureProjectSdk(project));
        } else {
            QinLogger.info("[SDK] Skipping project SDK auto-configuration because no Qin config context was found");
        }

        // 闂備線娼荤拹鐔煎礉瀹€鍕畺婵°倕鎳庨惌妤呮煙濞堝灝鏋涙繛鍫㈠Х缁辨帞鈧綆浜堕崕搴♀攽閻愬弶鍠橀柟顔荤矙婵℃瓕顦撮柛鈺冨Т閳藉骞橀姘闂?QinProjectSync 闂備礁婀遍悷鎶藉幢閳哄倹鏉搁梻浣告啞閼归箖寮甸鍕垫晢闁绘劗鍎ら弲顒勬煕椤愮姴鍔滃Δ妤婂灠椤鈽夊▍铏灱閸燁垶鎮楀▓鍨灈濡ょ姴绻掑Σ鎰攽閸狀喗鐩崺鈧い鎺戝閽冪喖鏌曟竟顖氭噹椤︹晠鏌ｆ惔銏⑩姇闁挎洦鍋嗗Σ?
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                QinLogger.info("[STARTUP] Background sync started");
                QinProjectSync projectSync = new QinProjectSync(project);
                // 濠殿喗甯楃粙鎺椻€﹂崼銉晣濠电姵纰嶉ˉ鍫熺箾閸℃ɑ灏甸悽顖涚⊕娣囧﹪顢涘璇蹭壕鐎规洖娲ㄩ、鍛存⒑閹稿海鈽夐柡鍫墴钘熼柛娑欐綑閽冪喖鏌曟竟顖氭噹椤︹晠鏌ｆ惔銏⑩姇閽冭鲸绻濋埀顒佺瑹閳ь剙顕ｉ锕€閱囬柣鏃€褰冩禍?
                projectSync.setSilentMode(true).syncAllProjects(true);
                QinLogger.info("[STARTUP] Background sync finished");
            } catch (Exception e) {
                QinLogger.error("[STARTUP] Background sync failed", e);
            }
        });

        // 濠电偞鍨堕幐鍝ョ矓閹绢喖鐤柍褜鍓熼弻銈嗙附婢跺鐩庢繝娈垮枓閺呯娀鐛澶婄鐎规洖娲ㄩ敍?Qin 闁诲氦顫夐幃鍫曞磿闁秴鐭楅悹鎭掑妿閻滅粯淇婇妶鍌氫壕閻熸粎澧楅惄顖炲极瀹ュ懐鏆嗛柛鏇ㄤ簽缁辨岸姊虹粙璺ㄧ缂佸鎳撹灋妞ゆ牜鍋為崐?
        // 闂備焦妞垮鍧楀礉瀹ュ鏄ユ繛鎴欏灩閻銇勯弽銊ф噥缂併劋鍗抽弻鐔兼濞戝崬鍓版繝娈垮枓閺呯娀骞冨▎鎾村仭闁哄瀵ч惁鏍⒑閻熸壆浠涢柟绋挎憸濡?

        // 闂備礁鎲￠崙褰掑垂閻楀牊鍙忛柍鍝勬噺閻撯偓閻庡箍鍎卞ú銊╁几閸岀偞鐓涢柛灞剧閻绻涢崱鎰伈闁诡垰瀚伴獮瀣攽閸パ冨闂備線娼荤紞鍡涙偂婢跺本顫曟繛鍡樻尰閸庡酣鏌熺€涙绠栭柛?qin.config.json 闂備礁鎲￠悷锕傛晪闁诲骸鐏氶悡锟犲极?
        QinConfigWatcher configWatcher = new QinConfigWatcher(project);
        configWatcher.startWatching();
        QinLogger.info("[STARTUP] Config watcher started");

        // 婵☆偓绲介崯顐﹀疾?闂備礁鎲￠崙褰掑垂閻楀牊鍙?Java 闂備礁鎼崐绋棵洪敐鍛瀻闁靛繈鍊栭崕搴ㄦ煙鐎涙绠栭柛搴㈡閺屾盯鏁愭惔锝呭辅缂備浇椴搁悷鈺呭箚閸曨垰绠ｆ繝闈涙搐閸?.java 闂備礁鎼崐绋棵洪敐鍛瀻闁靛繈鍊曢惌妤佷繆椤栨碍鎯堟い鏇嗗洦鐓ユ繛鎴烆焽閻掔兘鏌涢妶鍡欑煉鐎规洘绻堟俊姝岊槾婵＄偘绮欓幃褰掑炊閵娿儳鍘愮紓?
        QinJavaFileWatcher javaWatcher = new QinJavaFileWatcher(project);
        javaWatcher.startWatching();
        QinLogger.info("[STARTUP] Java file watcher started (incremental compile + debounce)");

        return Unit.INSTANCE;
    }

    /**
     * 闂備礁鎲￠悷锕傚垂瑜版帞宓侀柛銉墮缁犮儵鏌嶈閸撶喎顕?Qin 濠碉紕鍋戦崐鏇烇耿閸楃伝?
     * 濠电偠鎻紞鈧繛澶嬫礋瀵?qin-cli 闂?LocalProjectResolver
     */
    public static List<Path> discoverQinProjects(Path ideaProjectDir) {
        return com.qin.core.LocalProjectResolver.scanAllProjects(ideaProjectDir.toString());
    }

    /**
     * 婵犵妲呴崑鈧柛瀣崌閺岋紕浠︾拠鎻掑闂佺儵鍓濋悷鈺呭箚閸曨垼鏁婇柤娴嬫櫇鍗忓┑鐐村灦閹尖晠宕㈤挊澹╋綁骞栨担姝屝曢梺鎸庢婵倗绮?Qin 濠碉紕鍋戦崐鏇烇耿閸楃伝鐑樺鐎涙ɑ娅栭梺鍓插亝缁诲啳銇愰妷鈺傗拺妞ゆ劑鍊曢弸鎴炪亜閵夈儲鍤囩€殿喖顭锋俊鐑筋敍濠婂拋妲烽梻浣告啞閻燁垱绂嶉悙璇ц€块柛娑卞枤閳绘棃鏌嶈閸撴稒绂掗敃鍌氱＜闁挎柨澧介ˇ?
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
            // 闂傚鍋勫ú銊╁疾椤愶箑姹?
        }
        return false;
    }

    /**
     * 闂備礁婀遍悷鎶藉幢閳哄倹鏉?qin sync 闂備礁鎲＄粙鎺楀垂濠靛绠?
     */
    private void runQinSync(String projectPath) throws IOException, InterruptedException {
        ProcessBuilder pb = QinCommandResolver.createProcessBuilder(projectPath, "sync");

        Process process = pb.start();

        // 闂佽崵濮村ú鈺咁敋瑜戦妵鎰板炊閵婏妇绉堕梺瑙勫劤閸熷灝袙?
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
     * 闂備胶鍘ч〃搴㈢濠婂嫭鍙忛柍鍝勬噺閻撯偓閻庡箍鍎卞ú銊╁几?Project SDK
     * 婵犵妲呴崑鈧柛瀣尰缁绘盯寮堕幋顓炲壋闂備緡鍠栭顓犳?JDK 婵°倗濮烽崑鐐哄磿婵傚壊鏁婇柡鍥╁Х绾剧偓銇勯弮鈧Σ鎺旂矆鐎ｎ亶娓婚柕鍫濇噺鐠愨剝绻?SDK
     */
    private static void configureProjectSdk(Project project) {
        try {
            QinLogger.info("[SDK] ========== Configuring Project SDK ==========");
            String basePath = project.getBasePath();
            if (basePath == null) {
                QinLogger.info("[SDK] Project base path is unavailable, skipping");
                return;
            }

            // 闂備礁鍚嬮崕鎶藉床閼艰翰浜归柛銉簵娴滃綊鏌熼幆褍鏆辨い?Project SDK
            com.intellij.openapi.projectRoots.ProjectJdkTable jdkTable = com.intellij.openapi.projectRoots.ProjectJdkTable
                    .getInstance();
            com.intellij.openapi.projectRoots.Sdk[] allJdks = jdkTable.getAllJdks();
            QinLogger.info("[SDK] Detected " + allJdks.length + " configured JDK(s)");
            for (com.intellij.openapi.projectRoots.Sdk sdk : allJdks) {
                QinLogger.info("[SDK]   - " + sdk.getName() + " (" + sdk.getHomePath() + ")");
            }

            // 闂備礁鍚嬮崕鎶藉床閼艰翰浜归柛銉ｅ妸娴滄粓鏌涢敂璇插箺缂佹劖顨婇弻?SDK 闂傚倷鐒﹀妯肩矓閸洘鍋?
            com.intellij.openapi.roots.ProjectRootManager rootManager = com.intellij.openapi.roots.ProjectRootManager
                    .getInstance(project);
            com.intellij.openapi.projectRoots.Sdk currentSdk = rootManager.getProjectSdk();
            QinLogger.info("[SDK] Current Project SDK = " + (currentSdk != null ? currentSdk.getName() : "null"));

            if (currentSdk != null) {
                QinLogger.info("[SDK] Existing Project SDK detected: " + currentSdk.getName() + ", no reconfiguration needed");
                return;
            }

            String desiredJavaVersion = resolvePreferredJavaVersion(Paths.get(basePath));
            int desiredVersion = parseJavaVersion(desiredJavaVersion);
            QinLogger.info("[SDK] No Project SDK configured, resolving preferred JDK from Qin config context...");
            QinLogger.info("[SDK] Desired Java version = " + desiredJavaVersion);

            com.intellij.openapi.projectRoots.Sdk bestSdk = selectBestMatchingJdk(allJdks, desiredVersion);

            if (bestSdk != null) {
                final com.intellij.openapi.projectRoots.Sdk sdkToSet = bestSdk;
                final String sdkName = bestSdk.getName();
                int selectedVersion = parseJavaVersion(
                        com.intellij.openapi.projectRoots.JavaSdk.getInstance().getVersionString(bestSdk));
                QinLogger.info("[SDK] Selected JDK: " + sdkName + " (version: " + selectedVersion + ", desired: " + desiredVersion + ")");

                // 闂佽崵濮崇粈浣规櫠娴犲鍋?Project SDK闂備焦瀵х粙鎴︽偋閸涱喚绠鹃柛銉墯閸嬨劑鏌曟繛鍨姎鐟滄澘妫濋弻锝夛綖椤掆偓婵′粙鏌″畝鈧崰搴ㄥ煝閺冨牆鍗虫い蹇撴椤?
                QinLogger.info("[SDK] Applying selected Project SDK...");
                applyAndPersistSdk(project, rootManager, sdkToSet);
            } else {
                // 婵犵數鍋涙径鍥礈濠靛棴鑰垮〒姘ｅ亾妤犵偛绉归獮鍡氼槻闁绘挸鍊婚埀顒€婀遍…鍫濐嚕閸洖鏋侀柣鎰惈缁€鍐偓鍏夊亾闁逞屽墮閳?JDK闂備焦瀵х粙鎴︽嚐椤栫偞鍎嶉柛鎾楀嫬鐝伴梺鍝勬处濮樸劑鎯?JAVA_HOME 闂備胶鍘ч〃搴㈢濠婂嫭鍙忛柍杞拌剨鎼达絾瀚氶柟缁樺俯濞?
                String javaHome = System.getenv("JAVA_HOME");
                if (javaHome != null && !javaHome.isEmpty() && Files.exists(Paths.get(javaHome))) {
                    QinLogger.info("[SDK] No registered JDK found, trying JAVA_HOME: " + javaHome);

                    // 闂備礁鎲＄敮妤冪矙閹寸姷纾介柟鎹愵嚙濡﹢鏌熷▓鍨灍闁?JDK
                    com.intellij.openapi.projectRoots.JavaSdk javaSdkType = com.intellij.openapi.projectRoots.JavaSdk
                            .getInstance();

                    // 闂備焦鐪归崹濠氬窗閹版澘鍨?SDK 闂備礁鎲￠懝鍓х矓閹壋鍙?
                    String sdkName = "JDK-" + System.getProperty("java.version", "auto");

                    // 闂備礁鎲＄敮妤冪矙閹寸姷纾?SDK
                    com.intellij.openapi.projectRoots.Sdk newSdk = javaSdkType.createJdk(sdkName, javaHome, false);

                    if (newSdk != null) {
                        // 闂備胶顭堢换鎰版偋婵犲洤閿ゅ┑鐘叉搐缁€澶愭煟濡绲婚柣?JDK 闂?
                        ApplicationManager.getApplication().runWriteAction(() -> {
                            jdkTable.addJdk(newSdk);
                        });
                        QinLogger.info("[SDK]   Registered new JDK in IDE: " + sdkName);

                        // 闂佽崵濮崇粈浣规櫠娴犲鍋?Project SDK闂備焦瀵х粙鎴︽偋閸涱喚绠鹃柛銉墯閸嬨劑鏌曟繛鍨姎鐟滄澘妫濋弻锝夛綖椤掆偓婵′粙鏌″畝鈧崰搴ㄥ煝閺冨牆鍗虫い蹇撴椤?
                        applyAndPersistSdk(project, rootManager, newSdk);
                    } else {
                        QinLogger.error("[SDK] Unable to create JDK automatically, please configure it manually");
                    }
                } else {
                    QinLogger.info("[SDK] JAVA_HOME is unavailable, Project SDK remains unset until configured manually");
                    QinLogger.info("[SDK]   JAVA_HOME = " + (javaHome != null ? javaHome : "null"));
                }
            }

            // 闂備礁鎲＄敮锟犲绩闁秴钃熷┑鐘插暔娴滄粓鏌涢敂璇插箺缂佹劖顨堢槐鎾存媴鐟欏嫬闉嶉梺璇插瘨閸ㄥ爼寮鍛殕闁告劖鍎抽弫?IDEA UI 闂備礁鎼ú銈夋偤閵娾晛钃?
            QinLogger.info("[SDK] Refreshing IDEA project structure after SDK update...");
            refreshProjectStructure(project);

            QinLogger.info("[SDK] ========== Project SDK configuration complete ==========");
        } catch (Exception e) {
            QinLogger.error("[SDK] Failed to configure Project SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 闂備礁鎲＄敮锟犲绩闁秴钃熷┑鐘插暔娴滄粓鏌涢敂璇插箺缂佹劖顨堢槐鎾存媴鐟欏嫬闉嶉梺?
     * 闂?IDEA 闂傚倷鐒﹁ぐ鍐矓閻㈢钃熷┑鐘叉搐缁€澶愭煟濡厧鍔嬬紒浣规緲椤啴濡堕崨顔跨濠电偠寮撻崡鎶藉蓟婵犲偆鍚嬮柛銉㈡櫆閻?
     */
    private static void refreshProjectStructure(Project project) {
        try {
            String basePath = project.getBasePath();

            // 1. 闂備胶顭堢换鎰版偋閸℃稑鏄ユ繛鎴欏灩濡?misc.xml 闂備礁鎼崐绋棵洪敐鍛瀻闁靛繈鍊栭弲顒傗偓鍏夊亾闁逞屽墴閸ㄦ儳螣閼姐倐鏀?IDEA 闂備胶鍘х壕顓㈠疾椤愵澁鑰块柛娑欐綑缁€鍡涙煟濡も偓閻楀棝鎮樻径濞掑綊鎮埀顒勫礈濞戞ǚ鏋旈柟瀵稿仧閳瑰秵銇勯弮鍥撻柡?
            if (basePath != null) {
                Path miscXmlPath = Paths.get(basePath, ".idea", "misc.xml");
                com.intellij.openapi.vfs.VirtualFile miscVf = com.intellij.openapi.vfs.LocalFileSystem.getInstance()
                        .refreshAndFindFileByPath(miscXmlPath.toString().replace('\\', '/'));
                if (miscVf != null) {
                    miscVf.refresh(false, false);
                    QinLogger.info("[SDK]   Refreshed misc.xml VirtualFile");
                }
            }

            // 2. 闂備礁鎲＄敮锟犲绩闁秴钃熷┑鐘叉搐閺嬩胶绱撻崼銏犫枅闁搞倕顑夐弻褑绠涢弮鍌ゆ殹闁诲孩纰嶉悷鈺侇嚕椤掑嫬鐐婇柨鏃傜摂閸嬨劎绱撴笟鍥х仴鐎规洜鏁搁崚?
            com.intellij.openapi.vfs.VirtualFileManager.getInstance().refreshWithoutFileWatcher(true);
            QinLogger.info("[SDK]   VirtualFileManager refresh complete");

            // 3. 闂備礁鎲＄敮锟犲绩闁秴钃熷┑鐘插暔娴滄粓鏌涢敂璇插箺缂佹劖顨嗘穱濠囶敍濡炶浜剧€规洖娲犻崑?
            ApplicationManager.getApplication().invokeLater(() -> {
                try {
                    // 闂佽崵鍠愰悷杈╃不閹达絻浜归柛灞剧◤娴滄粓鏌涢敂璇插箺缂佹劖顨婂娲箵閹烘枬锝夋煛鐏炴枻韬€规洏鍎虫禒锔剧磼閵忕姴巍
                    com.intellij.openapi.project.DumbService dumbService = com.intellij.openapi.project.DumbService
                            .getInstance(project);

                    dumbService.runWhenSmart(() -> {
                        QinLogger.info("[SDK]   Project index rebuild complete");

                        // 闂備礁鎲￠崝鏇犵矓閻㈠壊鏁冮柛蹇曗拡濞堢晫鈧厜鍋撻柛鎰典簼椤?SDK 闂佽崵濮崇粈浣规櫠娴犲鍋?
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
     * 濠电儑绲藉ú锔炬崲閸岀偞鍋ら柕濠忚礋娴滄粓鏌涢敂璇插箺缂佹劖顨婇幃瑙勬媴缁嬪簱鎸冮梺姹囧€曞Λ婵嗙暦濮樿泛绠ユい鏇炴噺閳锋牠姊?
     * 闂傚鍋勫ú銈夊箠濮椻偓婵＄绠涘☉妯诲祶?write action 濠电姰鍨奸崺鏍偋閻樿纾块悗闈涙啞鐎氼剟鏌涢幇鍏哥凹闁?
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
     * 闂佸湱鍘ч悺銊ヮ潖婵犳艾鏋侀柕鍫濐槺瀹撲線鏌ら幖浣规锭闁伙富鍠栭埥澶愬棘閹稿骸娅ｉ柣?SDK 闂佽崵濮崇粈浣规櫠娴犲鍋?
     * 闂佸搫顦弲婊堟偡閿曗偓鍗遍柛婵勫劜婵ジ鏌涢幘妤€鎳忛悗?Project SDK 闂備焦鐪归崝宀€鈧凹鍠氶崚鎺楁倻濡皷鏋栭梺閫炲苯澧寸€殿噮鍓涢幉鎾礋椤撴壕鍋?
     * 
     * @param project     濠碉紕鍋戦崐鏇烇耿閸楃伝?
     * @param rootManager 濠碉紕鍋戦崐鏇烇耿閸楃伝鐑樺閺夋垵鍞ㄩ梺闈╁瘜閸欏酣宕ラ埀顒勬⒑閼姐倕浠滄俊顐ｎ殔閳?
     * @param sdk         闂佽崵鍠愬ú鎴澝归崶顒夋晩闁哄洨濮风壕鐐亜閺冨倸浜鹃柣?SDK
     */
    private static void applyAndPersistSdk(Project project,
            com.intellij.openapi.roots.ProjectRootManager rootManager,
            com.intellij.openapi.projectRoots.Sdk sdk) {
        String sdkName = sdk.getName();
        QinLogger.info("[SDK]   Applying SDK: " + sdkName);

        // 1. 濠电偠鎻紞鈧繛澶嬫礋瀵?IDEA API 闂佽崵濮崇粈浣规櫠娴犲鍋柛鈩冪☉缁€鍐煕濞戝崬骞橀柟鐣屽Т閳藉骞欓崘銊ョ濠电偛鐗婇崹鍨暦?
        ApplicationManager.getApplication().runWriteAction(() -> {
            rootManager.setProjectSdk(sdk);
        });
        QinLogger.info("[SDK]   Writing Project SDK to misc.xml");

        // 2. 闂備胶鍎甸弲娑㈡偤閵娧勬殰閻庢稒顭囬埞宥嗐亜閺冨洤袚闁?misc.xml 缂備胶铏庨崣搴ㄥ窗閺囩姵宕叉慨妯挎硾缁犳澘霉閿濆妫戦柣锝勭矙閺?
        String basePath = project.getBasePath();
        if (basePath != null) {
            Path miscXml = Paths.get(basePath, ".idea", "misc.xml");
            updateMiscXmlWithSdk(miscXml, sdkName);
        }

        // 3. 闂備礁鎲＄敮锟犲绩闁秴钃?IDEA
        refreshProjectStructure(project);

        // 4. 濠德板€楁慨鎾儗娓氣偓閹?
        com.intellij.openapi.projectRoots.Sdk afterSdk = rootManager.getProjectSdk();
        if (afterSdk != null && afterSdk.getName().equals(sdkName)) {
            QinLogger.info("[SDK] Project SDK persisted to misc.xml: " + sdkName);
        } else {
            QinLogger.info("[SDK]   misc.xml updated, you may need to reopen the project for changes to fully apply");
        }
    }

    /**
     * 闂備胶鍎甸弲娑㈡偤閵娧勬殰閻庢稒顭囬埞宥嗐亜閺冨洤袚闁?misc.xml 闂備礁鎼崐绋棵洪敐鍛瀻闁靛繈鍨烘刊濂告煕閹炬鎳忛悗?Project SDK
     * 闂佸搫顦弲婊堟偡閿曗偓鍗遍柟闂寸鐎氬鏌嶈閸撶喎鐣烽敐澶樻晬闁绘劕鐡ㄧ粊顕€姊哄Ч鍥у閻庢凹鍙冨濠氭偄婵傚娈ㄩ棅顐㈡储閸庡磭绮堟径宀€妫い鎾楀啯鐏嗙紓?SDK 闂佽崵濮崇粈浣规櫠娴犲鍋柛鈩冪懄閸嬫牗銇勯幇鍓佸埌闁伙富鍠栭埥澶愬棘閹稿骸娅ｉ柣?
     */
    private static void updateMiscXmlWithSdk(Path miscXml, String sdkName) {
        try {
            QinLogger.info("[SDK]   Updating misc.xml: " + miscXml);
            IdeaMiscXmlSupport.updateProjectSdk(miscXml, sdkName);
            String verify = Files.readString(miscXml, StandardCharsets.UTF_8);
            if (verify.contains("project-jdk-name=\"" + sdkName + "\"")) {
                QinLogger.info("[SDK] misc.xml write verification succeeded");
            } else {
                QinLogger.error("[SDK] misc.xml write verification failed");
            }
        } catch (Exception e) {
            QinLogger.error("[SDK]   Failed to update misc.xml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 闂佽崵鍠愰悷杈╁緤妤ｅ啯鍊?Java 闂備胶绮〃鍛存偋婵犲偊鑰垮ù鍏兼綑閻?
     */
    private static int parseJavaVersion(String versionStr) {
        try {
            // 闂備礁鎲￠悧鏇犵礊婵犲洤鍌ㄩ柕鍫濐槹閸嬪鏌涢銈呮瀾闁圭兘浜堕弻娑樷枎韫囨挻顔€缂備浇椴哥换鍐╃┍?"21", "17", "1.8"
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
            // 闂傚鍋勫ú銊╁疾椤愶箑姹?
        }
        return 0;
    }

    /**
     * 闂備礁鎼悮顐﹀磿閸欏鐝舵俊顖氱毞閸嬫捇鎮介崹顐㈡畬缂備降鍔嶉悡锟犲箚?sources jar 闂備礁鎼崐绋棵洪敐鍛瀻?
     * 濠电偞鎸婚懝楣冾敄閸涙番鈧? xxx.jar -> xxx-sources.jar
     */
    private static String findSourcesJar(String jarPath) {
        if (jarPath == null || !jarPath.endsWith(".jar")) {
            return null;
        }

        // 闂佽绻愮换鎴犳崲閸℃稒鍎婃い鏍仜閹瑰爼鏌曟繛鍨姕闁稿﹤宕埥澶愬箻椤栨矮澹曢梻浣哄劦閺呪晠宕伴弽顐ょ鐎广儱妫涢埢鏃堟倵閿濆骸浜為柛娆屽亾闂?-sources.jar
        String basePath = jarPath.substring(0, jarPath.length() - 4); // 缂傚倷绀侀ˇ顖炩€﹀畡鎵虫瀺?.jar
        String sourcesPath = basePath + "-sources.jar";

        if (java.nio.file.Files.exists(java.nio.file.Paths.get(sourcesPath))) {
            return sourcesPath.replace("\\", "/");
        }

        return null;
    }

    /**
     * 闂備礁鎼悮顐﹀磿閸欏鐝舵俊顖氱毞閸嬫捇鎮介崹顐㈡畬缂備降鍔嶉悡锟犲箚?javadoc jar 闂備礁鎼崐绋棵洪敐鍛瀻?
     * 濠电偞鎸婚懝楣冾敄閸涙番鈧? xxx.jar -> xxx-javadoc.jar
     */
    private static String findJavadocJar(String jarPath) {
        if (jarPath == null || !jarPath.endsWith(".jar")) {
            return null;
        }

        // 闂佽绻愮换鎴犳崲閸℃稒鍎婃い鏍仜閹瑰爼鏌曟繛鍨姕闁稿﹤宕埥澶愬箻椤栨矮澹曢梻浣哄劦閺呪晠宕伴弽顐ょ鐎广儱妫涢埢鏃堟倵閿濆骸浜為柛娆屽亾闂?-javadoc.jar
        String basePath = jarPath.substring(0, jarPath.length() - 4); // 缂傚倷绀侀ˇ顖炩€﹀畡鎵虫瀺?.jar
        String javadocPath = basePath + "-javadoc.jar";

        if (java.nio.file.Files.exists(java.nio.file.Paths.get(javadocPath))) {
            return javadocPath.replace("\\", "/");
        }

        return null;
    }

    /**
     * 濠?Qin 濠碉紕鍋戦崐鏇烇耿閸楃伝鐑樺鐎涙ê浠洪梺缁樻⒒椤牓鎮?.iml 闂備礁鎼崐绋棵洪敐鍛瀻?
     * 闂?IDEA 闂佽崵濮村ú銈呂涘Δ鍛槬闁糕剝銇涢弨锕傛煙闁缚绨婚柡浣叉櫊閺岋綁顢欓弰蹇撳帯濠电偛寮舵刊浠嬪Φ?
     * 
     * @param forceOverwrite true=闁诲孩顔栭崰鏍箹椤愶箑鏄ユ俊銈呭暞閸熸椽鏌涢埄鍐噭缁惧彞鍗抽弻銊モ槈濡厧顤€婵犫拃鍕煉鐎?sync闂備焦瀵х粙鎴βㄩ埀顒傜磼鏉堛劎鍚筧lse=闁诲海鎳撻幉陇銇愰崘顔藉仱闁靛ň鏅涢幑鍫曟煏婵炲灝濡块柛搴㈢叀瀵爼鍩℃担鍦偒缂備浇椴搁悷鈺呭箠濞戙埄鏁傞柛鏇ㄥ€ｅΔ鍛厱婵鍘ч悘鐘测攽椤旇娅婇柡?
     */
    public static void generateImlFile(Path projectPath, boolean forceOverwrite) {
        generateImlFile(projectPath, forceOverwrite, null);
    }

    /**
     * 濠?Qin 濠碉紕鍋戦崐鏇烇耿閸楃伝鐑樺鐎涙ê浠洪梺缁樻⒒椤牓鎮?.iml 闂備礁鎼崐绋棵洪敐鍛瀻?
     * 闂?IDEA 闂佽崵濮村ú銈呂涘Δ鍛槬闁糕剝銇涢弨锕傛煙闁缚绨婚柡浣叉櫊閺岋綁顢欓弰蹇撳帯濠电偛寮舵刊浠嬪Φ?
     * 
     * @param forceOverwrite true=闁诲孩顔栭崰鏍箹椤愶箑鏄ユ俊銈呭暞閸熸椽鏌涢埄鍐噭缁惧彞鍗抽弻銊モ槈濡厧顤€婵犫拃鍕煉鐎?sync闂備焦瀵х粙鎴βㄩ埀顒傜磼鏉堛劎鍚筧lse=闁诲海鎳撻幉陇銇愰崘顔藉仱闁靛ň鏅涢幑鍫曟煏婵炲灝濡块柛搴㈢叀瀵爼鍩℃担鍦偒缂備浇椴搁悷鈺呭箠濞戙埄鏁傞柛鏇ㄥ€ｅΔ鍛厱婵鍘ч悘鐘测攽椤旇娅婇柡?
     * @param ideaDir        IDEA 濠碉紕鍋戦崐鏇烇耿閸楃伝鐑樺鐎涙ê鍓?.idea 闂備胶鍎甸弲鈺呭窗閺嶎偆绀婄€广儱娲﹀畷澶嬨亜閺嶃劍鐨戠紒鎰仱閺屻劌鈽夊Ο鐓庘叡闂佽桨闄嶉崐鏍矉瀹ュ棙鍎熼柕蹇婂墲濞堟﹢姊洪崨濠傚闁哄牜鍓濋崯顖炴⒑缁夊棗瀚峰▓锝囩磼?
     */
    public static void generateImlFile(Path projectPath, boolean forceOverwrite, Path ideaDir) {
        try {
            if (!hasSourceDirectory(projectPath)) {
                QinLogger.info("[iml] Skipping .iml generation for source-less aggregate project: " + projectPath);
                return;
            }

            // 闂備礁鍚嬮崕鎶藉床閼艰翰浜归柛銉ｅ妸娴滄粓鏌涢敂璇插箺缂佹劖顨婇弻娑橆潩椤掑倐銈囨偖?
            String projectName = projectPath.getFileName().toString();
            Path imlPath = projectPath.resolve(projectName + ".iml");

            QinLogger.info("[iml] Processing project: " + projectPath);
            QinLogger.info("[iml]   iml path: " + imlPath);
            QinLogger.info("[iml]   forceOverwrite: " + forceOverwrite);

            // 濠电姷顣介埀顒€鍟块埀顒€缍婇幃妯诲緞婵犲骸鏅犻梺鍦帛鐢偤骞楅悩缁樼厱闁挎柨鎼俊铏圭磼濡や胶澧紒瀣槸椤撳ジ宕ㄩ鐘电暢闂備礁鎲＄敮妤呭磿婵傛悶鈧懘顢曢敂钘夊壆闂佸搫绉堕弫鍝ョ矆婢跺绻嗘い鎰枎娴滈箖姊洪崫鍕潶闁稿孩褰冨嵄闁归棿绀佺憴锕傛煥閺囩偛鈧绂掑鈧幃鐑藉即濮橆収妯傞梺閫涚┒閸斿秴顕?sourceFolder
            boolean needGenerate = !Files.exists(imlPath) || forceOverwrite;

            if (!needGenerate) {
                QinLogger.info("[iml]   Existing .iml found, checking whether repair is needed...");
                // 婵犵妲呴崑鈧柛瀣崌閺岋紕浠︾拠鎻掑Б濡ょ姷鍋涘ú顓烆嚕?.iml 闂備礁鎼€氱兘宕规导鏉戠畾濞达綀娅ｇ壕浠嬫煕濠靛棗顏慨?sourceFolder
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
                // 濠电偠鎻紞鈧繛澶嬫礋瀵?BSP 濠电姰鍨煎▔娑氣偓姘煎櫍楠炲啯绻濋崶褔妫烽梺闈涱焾閸斿瞼绮婚鐐寸厱婵炲棙鍔楁晶鐢告煏閸℃效闁诡垰瀚伴、娆戞媼瀹曞洨鐣鹃梻?
                com.qin.bsp.BspHandler bspHandler = new com.qin.bsp.BspHandler(projectPath.toString());

                // 闂備礁鍚嬮崕鎶藉床閼艰翰浜归柛銉戔偓閺€锕傛煙闁缚绨婚柡浣叉櫊閺岋綁顢欓弰蹇撳帯濠电偛寮舵刊浠嬪Φ閹版澘鍗虫い蹇撴椤︹晜绻涢弶鎴濇倯闁挎岸鏌涘Ο鑽も姇缂?qin.config.json闂?
                String sourceDir = bspHandler.getSourceDir();
                String testDir = bspHandler.getTestDir();

                // 婵犵妲呴崑鈧柛瀣崌閺岋紕浠︾拠鎻掑Б濠电偛寮舵刊浠嬪Φ閹版澘鍗抽柣鏃€鍔呴敃鍌涚厱婵ê澧介悾閬嶆煟閵堝懎顏慨濠佺矙瀹曘劑顢橀悩铏枛闂?
                if (!Files.exists(projectPath.resolve(sourceDir))) {
                    // 闂備焦鎮堕崕鎶藉磻閵堝鐒垫い鎴ｆ娴滈箖姊洪崨濠傜瑲濠殿垯绮欏畷闈涱煥閸繄顦梺闈涱煭鐎靛苯顫忛悙顒傜?
                    sourceDir = detectSourceDir(projectPath);
                }
                QinLogger.info("[iml]   sourceDir: " + sourceDir);
                QinLogger.info("[iml]   testDir: " + testDir);

                if (sourceDir == null) {
                    QinLogger.info("[iml]   Source directory not found");
                    return;
                }

                // 闂備礁鍚嬮崕鎶藉床閼艰翰浜归柛銉ｅ妽缂嶅洭鏌熼幆褍鏆辨慨锝咁樀閺岋綁鍩℃繝鍌涚亪缂?
                String outputDir = bspHandler.getOutputDir();
                QinLogger.info("[iml]   outputDir: " + outputDir);

                // 闂備焦鐪归崹濠氬窗閹版澘鍨傛慨妯挎硾缁犳娊鏌熼悜姗嗘畷闁绘繄鍠栭弻锝夊煛婵犲倹鐏堢紓?XML
                StringBuilder excludeFolders = new StringBuilder();
                for (String excludeDir : IML_EXCLUDED_DIRS) {
                    excludeFolders.append("          <excludeFolder url=\"file://$MODULE_DIR$/")
                            .append(excludeDir)
                            .append("\" />\n");
                }

                // 闂備焦鐪归崹濠氬窗閹版澘鍨傛慨姗嗗厴閺€锕傛煙闁箑骞楅柣搴枛闇夐柣妯煎劋绾箖鏌?XML
                StringBuilder sourceFolders = new StringBuilder();
                sourceFolders.append("      <sourceFolder url=\"file://$MODULE_DIR$/").append(sourceDir)
                        .append("\" isTestSource=\"false\" />\n");
                if (testDir != null && Files.exists(projectPath.resolve(testDir))) {
                    sourceFolders.append("      <sourceFolder url=\"file://$MODULE_DIR$/").append(testDir)
                            .append("\" isTestSource=\"true\" />\n");
                }

                // 闂傚倷绶￠崑鍛┍閾忚宕?BSP 闂備礁鍚嬮崕鎶藉床閼艰翰浜归柛銉ｅ妿閻熻绻濋棃娑冲姛妞も晩鍋婇弻銊モ槈濡崵绉秎asspath闂?
                List<String> classpath = bspHandler.getClasspath();
                StringBuilder dependencyEntries = new StringBuilder();

                for (String path : classpath) {
                    String entryPath = path.replace("\\", "/");

                    if (entryPath.endsWith(".jar")) {
                        // JAR 闂備礁鎼崐绋棵洪敐鍛瀻闁靛骏绱曢悷瑙勭節闂堟冻鍔熸い?- 婵犵妲呴崑鈧柛瀣崌閺岋紕浠︾拠鎻掑濠碘€冲级閹倸鐣烽妷鈺傛櫆闁兼亽鍎抽悾鎶芥煟鎼达絾鏆╅柟铏尵閼洪亶寮婚妷锕€鍓?sources 闂?javadoc
                        String sourcesPath = findSourcesJar(entryPath);
                        String javadocPath = findJavadocJar(entryPath);

                        dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                                .append("      <library>\n")
                                .append("        <CLASSES>\n")
                                .append("          <root url=\"jar://").append(entryPath).append("!/\" />\n")
                                .append("        </CLASSES>\n");

                        // 婵犵數鍎戠紞鈧い鏇嗗嫭鍙?JAVADOC 闂備胶鍘ч幖顐﹀磹婵犳艾纾?
                        if (javadocPath != null) {
                            dependencyEntries.append("        <JAVADOC>\n")
                                    .append("          <root url=\"jar://").append(javadocPath).append("!/\" />\n")
                                    .append("        </JAVADOC>\n");
                        } else {
                            dependencyEntries.append("        <JAVADOC />\n");
                        }

                        // 婵犵數鍎戠紞鈧い鏇嗗嫭鍙?SOURCES 闂備胶鍘ч幖顐﹀磹婵犳艾纾?
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
                        // 闂備礁鎼悧婊堝礈濞嗗骏鑰块悗闈涙憸鐏忕敻鏌ゆ慨鎰偓鏍玻濡ゅ啯鍠?- 闂佽崵濮崇欢銈囨閺囥垺鍋╅柤濮愬€愰崑鎾绘偨閸偄娈岀紓浣靛妽閻擄繝骞嗛崘顔肩妞ゆ帒鍊婚崺宥夋⒑濞茶鏋欓柤瀹犳硾鐓ら柟宄拌娴?
                        String sourcePath = computeSourcePath(entryPath);

                        dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                                .append("      <library>\n")
                                .append("        <CLASSES>\n")
                                .append("          <root url=\"file://").append(entryPath).append("\" />\n")
                                .append("        </CLASSES>\n");

                        // 濠电姷顣介埀顒€鍟块埀顒€缍婇幃妯诲緞閹邦剛顓奸梺璇″瀻閸愵亜甯撴繝鐢靛閸曨偄顫庨梺缁樼墬缁捇骞嗛崟顖ｆ晩闁兼祴鏅濆崗闂備焦瀵х粙鎴﹀嫉椤掑嫬閿ゅ┑鐘叉搐缁€?SOURCES 闂傚倷鐒﹀妯肩矓閸洘鍋?
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

                // 闂備焦鐪归崹濠氬窗閹版澘鍨?.iml 闂備礁鎲￠崝鏇㈠箠鎼搭煈鏁?
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

            // 婵犵數鍋涢ˇ顓㈠礉瀹€鍕埞闁芥ê锛夐悢鐓庣伋闁惧浚鍋勬禒鎾⒑?modules.xml
            if (ideaDir != null) {
                registerModuleToIdeaProject(imlPath, ideaDir);
            }

        } catch (Exception e) {
            QinLogger.error("Failed to generate .iml file: " + e.getMessage());
        }
    }

    private static boolean hasQinSdkContext(Path basePath) {
        if (basePath == null) {
            return false;
        }
        if (QinConfig.loadNearest(basePath) != null) {
            return true;
        }
        try {
            return !discoverQinProjects(basePath).isEmpty();
        } catch (Exception e) {
            QinLogger.info("[SDK] Failed to detect Qin project context: " + e.getMessage());
            return false;
        }
    }

    private static String resolvePreferredJavaVersion(Path basePath) {
        QinConfig nearestConfig = QinConfig.loadNearest(basePath);
        if (nearestConfig != null) {
            String version = nearestConfig.getJavaVersion();
            QinLogger.info("[SDK] Resolved Java version from nearest Qin config: " + version);
            return version;
        }

        List<Path> qinProjects = discoverQinProjects(basePath);
        if (qinProjects.isEmpty()) {
            QinLogger.info("[SDK] No Qin project config found, using default Java version: " + DEFAULT_JAVA_VERSION);
            return DEFAULT_JAVA_VERSION;
        }

        qinProjects.sort(Comparator
                .comparingInt((Path path) -> depthFrom(basePath, path))
                .thenComparing(Path::toString));

        Map<String, Integer> versionCounts = new LinkedHashMap<>();
        int bestDepth = Integer.MAX_VALUE;
        String bestVersion = null;

        for (Path projectPath : qinProjects) {
            QinConfig config = QinConfig.load(projectPath);
            if (config == null) {
                continue;
            }

            String version = config.getJavaVersion();
            versionCounts.merge(version, 1, Integer::sum);

            int depth = depthFrom(basePath, projectPath);
            if (depth < bestDepth) {
                bestDepth = depth;
                bestVersion = version;
            }
        }

        if (bestVersion != null) {
            if (versionCounts.size() > 1) {
                QinLogger.info("[SDK] Multiple Java versions detected in workspace: " + versionCounts
                        + ". Using nearest project version: " + bestVersion);
            } else {
                QinLogger.info("[SDK] Resolved Java version from workspace Qin projects: " + bestVersion);
            }
            return bestVersion;
        }

        QinLogger.info("[SDK] Workspace Qin configs did not provide a Java version, using default: " + DEFAULT_JAVA_VERSION);
        return DEFAULT_JAVA_VERSION;
    }

    private static int depthFrom(Path basePath, Path childPath) {
        try {
            return basePath.relativize(childPath).getNameCount();
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    private static com.intellij.openapi.projectRoots.Sdk selectBestMatchingJdk(
            com.intellij.openapi.projectRoots.Sdk[] allJdks,
            int desiredVersion) {
        com.intellij.openapi.projectRoots.Sdk exactMatch = null;
        com.intellij.openapi.projectRoots.Sdk nearestHigher = null;
        int nearestHigherVersion = Integer.MAX_VALUE;
        com.intellij.openapi.projectRoots.Sdk nearestLower = null;
        int nearestLowerVersion = Integer.MIN_VALUE;

        for (com.intellij.openapi.projectRoots.Sdk sdk : allJdks) {
            if (!(sdk.getSdkType() instanceof com.intellij.openapi.projectRoots.JavaSdk)) {
                continue;
            }

            String versionStr = com.intellij.openapi.projectRoots.JavaSdk.getInstance().getVersionString(sdk);
            if (versionStr == null) {
                continue;
            }

            int version = parseJavaVersion(versionStr);
            QinLogger.info("[SDK]   Candidate JDK: " + sdk.getName() + " (version: " + version + ")");

            if (version == desiredVersion) {
                exactMatch = sdk;
                break;
            }
            if (version > desiredVersion && version < nearestHigherVersion) {
                nearestHigherVersion = version;
                nearestHigher = sdk;
            }
            if (version < desiredVersion && version > nearestLowerVersion) {
                nearestLowerVersion = version;
                nearestLower = sdk;
            }
        }

        if (exactMatch != null) {
            return exactMatch;
        }
        if (nearestHigher != null) {
            return nearestHigher;
        }
        return nearestLower;
    }

    public static boolean hasSourceDirectory(Path projectPath) {
        try {
            com.qin.bsp.BspHandler bspHandler = new com.qin.bsp.BspHandler(projectPath.toString());
            String configuredSourceDir = bspHandler.getSourceDir();
            if (configuredSourceDir != null && !configuredSourceDir.isBlank()) {
                Path configuredPath = projectPath.resolve(configuredSourceDir);
                if (Files.exists(configuredPath) && Files.isDirectory(configuredPath)) {
                    return true;
                }
            }
        } catch (Exception e) {
            QinLogger.info("[iml] Failed to inspect configured sourceDir, falling back to directory detection: " + e.getMessage());
        }

        return detectSourceDir(projectPath) != null;
    }

    /**
     * 婵犵數鍋涢ˇ顓㈠礉瀹€鍕埞闁芥ê锛夐悢鐓庣伋闁惧浚鍋勬禒鎾⒑?IDEA 闂?modules.xml
     */
    private static void registerModuleToIdeaProject(Path imlPath, Path ideaDir) {
        try {
            Path modulesXml = ideaDir.resolve("modules.xml");

            // 闂佽崵濮崇欢銈囨閺囥垺鍋╁┑鐘崇閸庡海绱掔€ｎ偒鍎ラ柛銈囧█閹绗熼姘变哗缂?
            Path ideaParent = ideaDir.getParent(); // 濠碉紕鍋戦崐鏇烇耿閸楃伝鐑樺閺夋垵鍞ㄩ梺闈╁瘜閸樼晫绮ｅΔ鍐╁枑?
            Path relativePath = ideaParent.relativize(imlPath);
            String moduleEntry = relativePath.toString().replace("\\", "/");

            String content;
            if (!Files.exists(modulesXml)) {
                // modules.xml 濠电偞鍨堕幐鍝ョ矓閹绢喗鍋ら柕濞炬櫅閹硅埖銇勯鐔风缂佲偓婢舵劖鐓曢柟鐑樻尵閳藉绱撻崒娑欏磳鐎殿噮鍓熼獮宥夘敊閻ｅ本娈?
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

            // 婵犵妲呴崑鈧柛瀣崌閺岋紕浠︾拠鎻掑濠碘€冲级閹倸鐣烽妷鈺傛櫇闁逞屽墴瀹曟瑩鏁嶉崟顓狅紲闂婎偄娲﹀褰掑汲閸儲鐓?
            if (content.contains(moduleEntry)) {
                QinLogger.info("[iml]   Module already registered in modules.xml");
                return;
            }

            // 闂備礁鎼鍛偓姘煎墰缁辨捇骞樼拠鍙夘棟闂佽顔栭崰妤呮偩?module 闂備礁鎼¨鈧紒杈ㄦ礀鐓?
            String newModule = String.format(
                    "      <module fileurl=\"file://$PROJECT_DIR$/%s\" filepath=\"$PROJECT_DIR$/%s\" />",
                    moduleEntry, moduleEntry);

            // 闂?</modules> 濠电偞鍨堕弻銊╊敄閸涱喗娅犻柣妯款嚙缁犵敻鏌熼悜妯虹仴鐎?
            String newContent = content.replace("    </modules>", newModule + "\n    </modules>");

            Files.writeString(modulesXml, newContent);
            QinLogger.info("[iml]   Registered module in modules.xml: " + moduleEntry);

        } catch (Exception e) {
            QinLogger.error("[iml]   Failed to update modules.xml: " + e.getMessage());
        }
    }

    /**
     * 婵犵妲呴崑鈧柛瀣尰缁绘盯寮堕幋顓炲壉缂備緡鐓堥崰妤冪矉閹烘鏅查柛銉㈡櫅閻忥綁姊洪悜鈺傛珦闁哥姵鐗滅槐?
     */
    private static String detectSourceDir(Path projectPath) {
        // 濠电偞娼欓崥瀣晪闂佸憡蓱缁嬫垼鐏嬮梺閫炲苯澧棁澶愭倵閿濆骸浜為柛銊ャ偢閺?Maven 缂傚倸鍊烽悞锕傚箰婵犳碍鍊?
        Path mavenSrc = projectPath.resolve(DEFAULT_SOURCE_DIR);
        if (Files.exists(mavenSrc)) {
            return DEFAULT_SOURCE_DIR;
        }
        // 闂備胶顭堢换妤呭春閺嶎収鏁冮柣鎾冲濞戙垹鐒垫い鎺嗗亾闂囧鎮楅敐搴″闁哄棗妫濋弻娑樜旀担鍦槶缂備緡鍠撻崝鎴濐嚕?
        Path simpleSrc = projectPath.resolve("src");
        if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
            return "src";
        }
        // 婵犵數鍋涙径鍥礈濠靛洨鐝舵慨妞诲亾鐎规洘宀搁幃褔宕奸姀銏犵厓濠电偛顕刊瀵稿緤閸ф鍋夋繝濠傜墛閸庡孩銇勯弮鍥т汗缂?
        return null;
    }

    /**
     * 闂備礁鎼粔鐑斤綖婢跺﹦鏆ゅù锝堟鐏忕敻鏌￠崟顐ょ缂傚牆顭烽弻娑㈠箣閿濆棭浠╁┑鐐插级婵粙濡甸幇鏉垮嵆闁宠棄妫楅幃浣虹磽娴ｅ搫校妞ゃ劌妫涢懞閬嶎敋閳ь剟骞嗗鍡樺闁绘垶顭囬悰銉╂偡?
     * 濠电偞鎸婚懝楣冾敄閸涙番鈧? D:/project/subhuti-java/build/classes ->
     * D:/project/subhuti-java/src/main/java
     */
    private static String computeSourcePath(String classPath) {
        try {
            // 闂?build/classes 闂備礁鎼ú锔锯偓绗涘啰鏆﹂柛娆忣槺閳绘棃鏌ｉ幋鐑嗙劷闁绘牠浜堕弻锝夘敊閺勫繐鍘″┑鐐插级婵粙濡?
            Path classDir = Paths.get(classPath);

            // 闂備礁鎲＄喊宥夊垂瀹曞洨绠旈柛宀€鍋涚粻銉╂煙椤栧棗鍟伴崢鎺撲繆閻愵亜鈧洖锕㈤崡鐏荤儤瀵奸弶鎴濆敤闂侀潻瀵岄崢鐣岀玻濡ゅ啯鍠愰柣妤€鐗婄粚璺ㄧ磼鏉堛劎鎳囩€规洜濞€瀹曘劑顢橀悢宄板 build 闂備焦鐪归崝宀€鈧凹鍣ｅ畷鍝勎旈崨顔煎壆濡炪倖妫侀崑鎰矓閸ф鐓?
            Path current = classDir;
            while (current != null && !current.getFileName().toString().equals("build")) {
                current = current.getParent();
            }

            if (current != null && current.getParent() != null) {
                Path projectRoot = current.getParent();

                // 婵犵妲呴崑鈧柛瀣崌閺?src/main/java
                Path mavenSrc = projectRoot.resolve(DEFAULT_SOURCE_DIR);
                if (Files.exists(mavenSrc)) {
                    return mavenSrc.toString().replace("\\", "/");
                }

                // 婵犵妲呴崑鈧柛瀣崌閺?src
                Path simpleSrc = projectRoot.resolve("src");
                if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
                    return simpleSrc.toString().replace("\\", "/");
                }
            }
        } catch (Exception e) {
            // 闂傚鍋勫ú銊╁疾椤愶箑姹?
        }
        return null;
    }

    /**
     * 濠电儑绲藉ù鍌炲窗閺嶎厔鍥嚑椤掑倻锛濋梺鍛婄箓鐎氼喖袙?sourceFolder 闂?.iml 闂備礁鎼崐绋棵洪敐鍛瀻闁靛繈鍊曠粈鍐煕濞戝崬寮鹃柛?
     * 闂佽绻愮换鎰涘鍫濆惞婵犲﹤鐗婇埛鎺撱亜閹捐泛浠﹂柛濞垮€濋弻?<content url="..." /> 闂佸搫顦遍崕鎰板礈濮橆剛鏆﹂柛娆忣槺閳绘棃鏌涘┑鍡楊仼闁伙箑鐖奸弻?sourceFolder 闂備焦鐪归崝宀€鈧凹鍓熼幃楣冾敆閸曨偅鐎梺鍝勵槹閸ㄥジ宕哄畝鈧埀?
     */
    private static String fixMissingSourceFolder(String imlContent, Path projectPath) {
        try {
            // 婵犵妲呴崑鈧柛瀣尰缁绘盯寮堕幋顓炲壉缂備緡鐓堥崰妤冪矉閹烘鏅查柛銉㈡櫅閻忥綁姊洪悜鈺傛珦闁哥姵鐗滅槐?
            String sourceDir = detectSourceDir(projectPath);
            if (sourceDir == null) {
                QinLogger.info("[iml]   Could not detect source directory, skipping sourceFolder repair");
                return imlContent;
            }

            // 婵犵妲呴崑鈧柛瀣崌閺岋紕浠︾拠鎻掑濠碘€冲级閹倸鐣烽妷鈺傛櫆闁兼亽鍎涢敃鍌涚厾濠靛倸顦柇顖涗繆椤愶絾灏︾€规洏鍎靛畷濂稿閿涘嫭娈?content 闂備礁鎼粔鏉懨洪妸鈺婃晢?
            java.util.regex.Pattern selfClosingPattern = java.util.regex.Pattern.compile(
                    "<content\\s+url=\"[^\"]*\"\\s*/>");
            java.util.regex.Matcher matcher = selfClosingPattern.matcher(imlContent);

            if (matcher.find()) {
                // 闂備胶鎳撻悘姘跺磿閹惰棄鏄ョ€光偓閸曨剙娈滃銈呯箰閻楀﹪鏁嶉弮鍫熺厱婵﹩鍓涙晶銏＄箾?content 闂備礁鎼粔鏉懨洪妸鈺婃晢闁绘垼濮ら弲顒傗偓鍏夊亾闁告洦鍏涚划顖炴煟閻斿憡纾婚柣鎺炵畱闇夋繛鎴欏灩缁犲弶銇勯顐㈠绩缂佲偓鐎ｎ喗鍊甸柣鐔哄濠€浼存煛閸☆厾绉柕鍥ㄥ姍婵偓闁绘鏁搁、?
                String originalTag = matcher.group();
                int urlStart = originalTag.indexOf("url=\"") + 5;
                int urlEnd = originalTag.indexOf("\"", urlStart);
                String url = originalTag.substring(urlStart, urlEnd);

                // 闂備礁鎼鍛偓姘煎墰缁辨捇骞橀崹娑樹壕闁荤喓澧楀﹢浼存煛閸☆厾绉柟?content 闂備礁鎼粔鏉懨洪妸鈺婃晢?
                StringBuilder newContent = new StringBuilder();
                newContent.append("<content url=\"").append(url).append("\">\n");
                newContent.append("      <sourceFolder url=\"file://$MODULE_DIR$/")
                        .append(sourceDir).append("\" isTestSource=\"false\" />\n");

                // 婵犵妲呴崑鈧柛瀣崌閺岋紕浠︾拠鎻掑濠碘€冲级閹倸鐣烽妷鈺傛櫆闁兼亽鍎抽悾鍐测攽閻愬瓨缍戞い鎴濈墦閹線顢涢悙鏉戝壆濡炪倖妫侀崑鎰矓?
                Path testDir = projectPath.resolve("src/test/java");
                if (Files.exists(testDir)) {
                    newContent.append(
                            "      <sourceFolder url=\"file://$MODULE_DIR$/src/test/java\" isTestSource=\"true\" />\n");
                }

                // 婵犵數鍎戠紞鈧い鏇嗗嫭鍙忛柣鎰惈缁犳娊鏌熼悜姗嗘畷闁绘繄鍠栭弻锝夊煛婵犲倹鐏堢紓?
                for (String excludeDir : IML_EXCLUDED_DIRS) {
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
