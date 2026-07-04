package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.qin.debug.lsp.QinLspStartupProbe;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import static com.qin.constants.QinConstants.*;

// 闂傚倷绀侀幉锛勬暜閳哄懎纾婚柛鏇ㄥ灠缁犳牠鏌￠崶銉ョ仾闁哄拋鍓氶幈銊ヮ潨閸℃ぞ绨婚梺瀹狀嚙缁绘﹢寮?qin-cli 闂傚倷鐒﹂惇褰掑礉瀹€鈧埀顒佺煯閸楁娊宕洪埀顒併亜閹哄秶鍔嶉柣銊﹀灴閺屸剝鎷呴棃娑掑亾濡ゅ懎鏋佹い鏇楀亾妤犵偞鐗楅幏鍛村川婵犲簼鐢?import static com.qin.constants.QinConstants.*;

/**
 * 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳閻戞ê顕遍柡澶嬪灱琚濇俊鐐€栭悧妤呮嚌閹规劦鏉介梻鍌欒兌閸庣敻寮插鍫濆瀭闁汇垻顭堢粻姘熆閼搁潧濮囬梻?
 * 闂傚倷鑳堕崢褔銆冩惔銏㈩洸婵犲﹤瀚崣蹇涙煃鏉炴壆瀵兼繛鎴欏灩閻掑灚銇勯幒鍡椾壕闂?Qin 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳閻戞ê顕遍悗鐢殿焾缁侊箓姊洪崗鍏煎€愭繛浣冲洤鐓曢柡鍐ㄧ墛閸?sync
 * 闂傚倷娴囬妴鈧柛瀣尰閵囧嫰寮介妸褉妲堥梺?Monorepo闂傚倷鐒︾€笛呯矙閹烘挾鈹嶆繛宸簻閸氳绻濇繝鍌滃缂佲偓婢舵劖鐓忓┑鐘茬箳閻ｈ鲸銇勯妷銉﹀枠闁哄瞼鍠撻幏鐘绘嚑椤掑偆鍞瑰┑鐘媰閸愵€呪偓娈垮枛閻忔艾顕ラ崟顒傜瘈闁告洦鍓氶崯銏犫攽閿涘嫬浜奸柛濠冩礀閿曘垽宕￠悘?
 */
public class DebugStartup implements ProjectActivity {
    private static final String LEGACY_QIN_RUN_CONFIG_ID = "QinRunConfiguration";
    private static final String LEGACY_QIN_TEST_CONFIG_ID = "QinTestConfiguration";

    @Nullable
    @Override
    public Object execute(@NotNull Project project,
            @NotNull Continuation<? super Unit> cont) {

        String basePath = project.getBasePath();
        if (basePath == null)
            return Unit.INSTANCE;

        // 闂傚倷绀侀幉锛勬暜濡ゅ啯宕查柛宀€鍎戠紞鏍煙閻楀牊绶茬紒鈧畝鍕厸鐎广儱楠搁獮妤€螖濡ゅ﹤鐏﹂柡宀€鍠栭、鏍崉閵娿儱顬夋繝纰樻閸嬧偓闁哄懐鍋熺划璇测槈濡粍妫冮崺鈧い鎺嗗亾闁?
        QinLogger.init(basePath, project);
        QinLogger.info("[STARTUP] Qin plugin startup: " + project.getName());
        QinLogger.info("[STARTUP] Project base path: " + basePath);
        removeLegacyQinRunConfigurations(project);
        ApplicationManager.getApplication().invokeLater(() -> QinLspStartupProbe.log(project, Paths.get(basePath)));

        if (QinWorkspaceSdkDefaults.hasQinSdkContext(Paths.get(basePath))) {
            ApplicationManager.getApplication().invokeLater(() -> configureProjectSdk(project));
        } else {
            QinLogger.info("[SDK] Skipping project SDK auto-configuration because no Qin config context was found");
        }

        // 闂傚倷绶氬鑽ゆ嫻閻旂厧绀夌€光偓閸曨偆鐣哄┑掳鍊曢幊搴ㄦ儗濡ゅ懏鐓欐繛鍫濈仢閺嬫稒绻涢崼銏犘ョ紒杈ㄥ笧閳ь剨缍嗘禍鍫曞磿鎼粹檧鏀介柣鎰级閸犳﹢鏌熼鑽ょ煓濠碘剝鐡曢ˇ鎾煕閳哄啫孝闁宠棄顦甸獮姗€顢涘顐㈩棜闂?QinProjectSync 闂傚倷绀佸﹢閬嶆偡閹惰棄骞㈤柍鍝勫€归弶鎼佹⒒娴ｅ憡鍟為柤褰掔畺瀵敻顢楅崟鍨櫌闂佺粯鍔楅崕銈夊疾椤掑嫭鐓曟い鎰Т閸旀粌螖濡ゅ﹤鐏犳い顐ｎ殔閳藉鈻嶉搹顐㈢伇闁哥噥鍨堕幃妤€鈻撻崹顔界亪婵°倗濮寸换鎺懳ｉ幇顑芥斀闁哥媭鍠楅惄顖炲春閳ь剚銇勯幒鎴濐仼闁藉啰鍠栭弻鏇熺珶椤栨碍鍣规い锔规櫊閺岋絾鎯旈姀鈶╁闂佹寧娲﹂崑鍡椢?
        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            try {
                QinLogger.info("[STARTUP] Background sync started");
                QinProjectSync projectSync = new QinProjectSync(project);
                // 婵犳鍠楃敮妤冪矙閹烘せ鈧箓宕奸妷顔芥櫍婵犵數濮电喊宥壦夐崼鐔虹闁糕剝蓱鐏忕敻鎮介娑氣姇濞ｅ洤锕、娑橆渻鐠囪弓澹曢悗瑙勬礀濞层劑銆侀崨瀛樷拺闁圭娴烽埥澶愭煛閸偄澧撮挊鐔兼煕濞戞瑦缍戦柦鍐枛閺屾洘绔熼姘櫣妞わ腹鏅犻弻锝嗘償閵忊懇濮囬柦鍐哺缁绘繈鍩€椤掍胶鐟归柍褜鍓欓锝夘敃閿曗偓闁卞洭鏌ｉ弮鈧ぐ鍐╃?
                projectSync.setSilentMode(true).syncAllProjects(true);
                QinLogger.info("[STARTUP] Background sync finished");
            } catch (Exception e) {
                QinLogger.error("[STARTUP] Background sync failed", e);
            }
        });

        // 婵犵數鍋為崹鍫曞箰閸濄儳鐭撻柟缁㈠枛閻ゎ噣鏌嶈閸撶喖寮婚妶鍡欓檮濠㈣泛顦遍惄搴㈢節濞堝灝鏋撻柡鍛█閻涱喖顓兼径濠勵啋閻庤娲栧ú銊╂晬?Qin 闂佽姘﹂～澶愬箖閸洖纾块梺顒€绉撮惌妤呮偣閹帒濡块柣婊呯帛娣囧﹪濡堕崒姘闁荤喐绮庢晶妤呮儎椤栫偛鏋佺€广儱鎳愰弳鍡涙煕閺囥劋绨界紒杈ㄥ哺濮婅櫣绮欑捄銊ь唶缂備礁顦遍幊鎾圭亱濡炪倖鐗滈崑鐐哄磹?
        // 闂傚倷鐒﹀鍨焽閸ф绀夌€广儱顦弰銉︾箾閹存瑥鐏╅柣顓燁殕閵囧嫰寮介妸褎鍣ョ紓浣靛妺閸楁娊寮婚悢鍏碱棃婵炴垵宕崜鐗堢節濞堝灝鏋撻柡鍛█楠炲啫鈻庨幘鏉戜画闂佸搫顦扮€笛囨儊閺嶎厽鈷戦柣鐔稿娴犳盯鏌熺粙鎸庢喐婵?

        // 闂傚倷绀侀幉锟犲礄瑜版帒鍨傞柣妤€鐗婇崣蹇涙煃閸濆嫭鍣洪柣鎾亾闁诲骸绠嶉崕鍗灻洪妸鈺佸嚑闁稿瞼鍋為悡娑㈡煕鐏炲墽顣查柣顓燁殕缁绘盯宕遍幇顒備紙闂佽鍨扮€氫即鐛€ｎ亖鏀介柛銉戝啫顥撻梻鍌欑窔濞艰崵绱為崱娑欏亗濠㈣泛鏈～鏇熺箾閸℃ɑ灏伴柛搴￠叄閺岀喓鈧稒顭囩粻鏍煕?qin.config.js 闂傚倷绀侀幉锟犳偡閿曞倹鏅梺璇查閻忔岸鎮￠敓鐘叉瀬?
        QinConfigWatcher configWatcher = new QinConfigWatcher(project);
        configWatcher.startWatching();
        QinLogger.info("[STARTUP] Config watcher started");

        // 濠碘槅鍋撶徊浠嬪疮椤愶箑鐤?闂傚倷绀侀幉锟犲礄瑜版帒鍨傞柣妤€鐗婇崣?Java 闂傚倷绀侀幖顐﹀磹缁嬫５娲晲閸涱亝鐎婚梺闈涚箞閸婃牠宕曟惔銊︾厵閻庢稒顭囩粻鏍煕鎼淬垺顥堥柡灞剧洴閺佹劖鎯旈敐鍛緟缂傚倷娴囨ご鎼佹偡閳哄懎绠氶柛鏇ㄥ灠缁狅絾绻濋棃娑欐悙闁?.java 闂傚倷绀侀幖顐﹀磹缁嬫５娲晲閸涱亝鐎婚梺闈涚箞閸婃洟鎯屽Δ浣风箚妞ゆ牗纰嶉幆鍫熴亜閺囧棗娲﹂悡銉︾箾閹寸儐鐒介柣鎺斿厴閺屾盯濡堕崱娆戠厜閻庤娲樼换鍫熶繆濮濆矈妲惧┑锛勫仒缁瑩骞冭ぐ鎺戠倞闁靛鍎抽崢鎰磽?
        QinJavaFileWatcher javaWatcher = new QinJavaFileWatcher(project);
        javaWatcher.startWatching();
        QinLogger.info("[STARTUP] Java file watcher started (incremental compile + debounce)");

        return Unit.INSTANCE;
    }

    private static void removeLegacyQinRunConfigurations(Project project) {
        ApplicationManager.getApplication().invokeLater(() -> ApplicationManager.getApplication().runWriteAction(() -> {
            RunManager runManager = RunManager.getInstance(project);
            List<RunnerAndConfigurationSettings> allSettings = new ArrayList<>(runManager.getAllSettings());
            int removed = 0;

            for (RunnerAndConfigurationSettings settings : allSettings) {
                String typeId = settings.getType().getId();
                if (LEGACY_QIN_RUN_CONFIG_ID.equals(typeId) || LEGACY_QIN_TEST_CONFIG_ID.equals(typeId)) {
                    runManager.removeConfiguration(settings);
                    removed++;
                }
            }

            if (removed > 0) {
                QinLogger.info("[RUN] Removed legacy Qin run configurations: " + removed);
            } else {
                QinLogger.info("[RUN] No legacy Qin run configurations found.");
            }
        }));
    }

    /**
     * 闂傚倷绀侀幉锟犳偡閿曞倸鍨傜憸鐗堝笧瀹撲線鏌涢妷顔煎缂佺姰鍎甸弻宥堫檨闁告挾鍠庨?Qin 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳?
     * 婵犵數鍋犻幓顏嗙礊閳ь剚绻涙径瀣鐎?qin-cli 闂?LocalProjectResolver
     */
    public static List<Path> discoverQinProjects(Path ideaProjectDir) {
        return com.qin.core.LocalProjectResolver.scanAllProjects(ideaProjectDir.toString());
    }

    /**
     * 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅宕岄柡宀嬬磿娴狅妇鎷犻幓鎺戭潛闂備胶鍎甸崜婵嬫偡閳哄懎绠氶柛鏇ㄥ灱閺佸﹪鏌ゅù瀣珖閸楀繐鈹戦悙鏉戠仸闁瑰皷鏅犲畷銏ゆ寠婢光晪缍侀獮鏍ㄦ媴濮濆睗鏇㈡⒑閹稿孩顥嗗┑顔哄€楃划?Qin 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳閻戞ê顕遍悗娑櫳戝▍鏍⒑閸撴彃浜濈紒璇插暢閵囨劙濡烽埡鍌楁嫼濡炪倖鍔戦崐鏇㈠几閹寸偑浜滈柕澶堝劜閸ゅ洨鈧鍠栭…閿嬩繆閻戠瓔鏁嶆繝濠傛媼濡茬兘姊绘担鍛婂暈闁荤噥鍨辩粋宥夋倷鐠囇嗏偓鍧楁煕濞戝崬鏋ら柍缁樻閺屽秷顧侀柛鎾寸⊕缁傛帡鏁冮崒姘憋紲闂佹寧鏌ㄦ晶浠嬎?
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
            // 闂傚倸顭崑鍕洪妸鈺佺柧妞ゆ劧绠戝Ч?
        }
        return false;
    }

    /**
     * 闂傚倷绀佸﹢閬嶆偡閹惰棄骞㈤柍鍝勫€归弶?qin sync 闂傚倷绀侀幉锛勭矙閹烘鍨傛繝闈涱儏缁?
     */
    private void runQinSync(String projectPath) throws IOException, InterruptedException {
        ProcessBuilder pb = QinCliProcessBuilders.syncDependencies(projectPath);

        Process process = pb.start();

        // 闂備浇宕垫慨鏉懨洪埡鍜佹晪鐟滄垿濡甸幇鏉跨倞闁靛濡囩粔鍫曟⒑鐟欏嫬鍔ら柛鐔风仢琚?
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
     * 闂傚倷鑳堕崢褔銆冩惔銏㈩洸婵犲﹤瀚崣蹇涙煃閸濆嫭鍣洪柣鎾亾闁诲骸绠嶉崕鍗灻洪妸鈺佸嚑?Project SDK
     * 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅灏扮紒缁樼洴瀵爼骞嬮鐐插闂傚倷绶￠崰鏍敋椤撶姵顫?JDK 濠德板€楁慨鐑藉磻閻愬搫纾垮┑鍌氬閺佸﹪鏌￠崶鈺佇ョ痪鍓у亾閵囧嫰寮埀顒€危閹烘梻鐭嗛悗锝庝憾濞撳鏌曢崼婵囧櫤閻犳劏鍓濈换?SDK
     */
    private static void configureProjectSdk(Project project) {
        try {
            QinLogger.info("[SDK] ========== Configuring Project SDK ==========");
            String basePath = project.getBasePath();
            if (basePath == null) {
                QinLogger.info("[SDK] Project base path is unavailable, skipping");
                return;
            }

            // 闂傚倷绀侀崥瀣磿閹惰棄搴婇柤鑹扮堪娴滃綊鏌涢妷顔荤暗濞存粌缍婇弻鐔煎箚瑜嶉弳杈ㄣ亜?Project SDK
            com.intellij.openapi.projectRoots.ProjectJdkTable jdkTable = com.intellij.openapi.projectRoots.ProjectJdkTable
                    .getInstance();
            com.intellij.openapi.projectRoots.Sdk[] allJdks = jdkTable.getAllJdks();
            QinLogger.info("[SDK] Detected " + allJdks.length + " configured JDK(s)");
            for (com.intellij.openapi.projectRoots.Sdk sdk : allJdks) {
                QinLogger.info("[SDK]   - " + sdk.getName() + " (" + sdk.getHomePath() + ")");
            }

            // 闂傚倷绀侀崥瀣磿閹惰棄搴婇柤鑹扮堪娴滃綊鏌涢妷锝呭Ω濞存粍绮撻弻娑㈡晜鐠囨彃绠虹紓浣瑰姈椤ㄥ﹪寮?SDK 闂傚倸鍊烽悞锕€顭垮Ο鑲╃煋闁割偅娲橀崑?
            com.intellij.openapi.roots.ProjectRootManager rootManager = com.intellij.openapi.roots.ProjectRootManager
                    .getInstance(project);
            com.intellij.openapi.projectRoots.Sdk currentSdk = rootManager.getProjectSdk();
            QinLogger.info("[SDK] Current Project SDK = " + (currentSdk != null ? currentSdk.getName() : "null"));

            String desiredJavaVersion = QinWorkspaceSdkDefaults.preferredJavaVersion(Paths.get(basePath));
            int desiredVersion = QinWorkspaceSdkDefaults.parseJavaVersion(desiredJavaVersion);
            QinLogger.info("[SDK] Required Java version from Qin workspace = " + desiredJavaVersion);

            int currentVersion = 0;
            if (currentSdk != null) {
                String currentVersionStr = com.intellij.openapi.projectRoots.JavaSdk.getInstance()
                        .getVersionString(currentSdk);
                currentVersion = QinWorkspaceSdkDefaults.parseJavaVersion(currentVersionStr);
                if (currentVersion >= desiredVersion) {
                    QinLogger.info("[SDK] Existing Project SDK is compatible (current: "
                            + currentVersion + ", required: " + desiredVersion + "), keeping as-is");
                    return;
                }
                QinLogger.warn("[SDK] Existing Project SDK is lower than required (current: "
                        + currentVersion + ", required: " + desiredVersion + "), attempting auto-upgrade");
            } else {
                QinLogger.info("[SDK] No Project SDK configured, selecting one automatically...");
            }

            com.intellij.openapi.projectRoots.Sdk bestSdk = selectBestMatchingJdk(allJdks, desiredVersion);

            if (bestSdk != null) {
                final com.intellij.openapi.projectRoots.Sdk sdkToSet = bestSdk;
                final String sdkName = bestSdk.getName();
                int selectedVersion = QinWorkspaceSdkDefaults.parseJavaVersion(
                        com.intellij.openapi.projectRoots.JavaSdk.getInstance().getVersionString(bestSdk));
                QinLogger.info("[SDK] Selected JDK: " + sdkName + " (version: " + selectedVersion + ", desired: " + desiredVersion + ")");

                if (currentSdk != null && sdkName.equals(currentSdk.getName())) {
                    QinLogger.info("[SDK] Selected JDK is the same as current SDK, no update needed");
                    return;
                }

                if (selectedVersion < desiredVersion) {
                    QinLogger.warn("[SDK] Best available JDK is still lower than required (selected: "
                            + selectedVersion + ", required: " + desiredVersion + ")");
                }

                // 闂備浇宕垫慨宕囩矆娴ｈ娅犲ù鐘差儐閸?Project SDK闂傚倷鐒︾€笛呯矙閹达附鍋嬮柛娑卞枤缁犻箖鏌涢妷顔煎闁稿鍔戦弻鏇熺箾閸喖濮庨悷婊勬緲濡繈寮婚敐澶涚稏妞ゆ巻鍋撳┑鈥茬矙閺屸€崇暆閳ь剟宕版惔銊ョ厺闁哄啫鐗嗛崡铏亜韫囨挻顥犳い?
                QinLogger.info("[SDK] Applying selected Project SDK...");
                QinProjectSdkPersistence.applyAndPersist(project, rootManager, sdkToSet);
            } else {
                // 濠电姷鏁搁崑娑欏緞閸ヮ剙绀堟繝闈涙４閼板灝銆掑锝呬壕濡ょ姷鍋涚粔褰掔嵁閸℃凹妲婚梺缁樻尭閸婂鍩€椤掆偓濠€閬嶁€﹂崼婵愬殨闁割偅娲栭弸渚€鏌ｉ幇顒佹儓缂佲偓閸愵亖鍋撻崗澶婁壕闂侀€炲苯澧柍?JDK闂傚倷鐒︾€笛呯矙閹达附鍤愭い鏍仦閸庡秹鏌涢幘妤€瀚悵浼存⒑閸濆嫭澶勬慨妯稿姂閹?JAVA_HOME 闂傚倷鑳堕崢褔銆冩惔銏㈩洸婵犲﹤瀚崣蹇涙煃鏉炴媽鍓ㄩ幖杈剧稻鐎氭岸鏌熺紒妯轰刊婵?
                String javaHome = System.getenv("JAVA_HOME");
                if (javaHome != null && !javaHome.isEmpty() && Files.exists(Paths.get(javaHome))) {
                    QinLogger.info("[SDK] No registered JDK found, trying JAVA_HOME: " + javaHome);

                    // 闂傚倷绀侀幉锛勬暜濡ゅ啰鐭欓柟瀵稿Х绾句粙鏌熼幑鎰靛殭婵☆偅锕㈤弻鐔封枔閸喗鐏嶉梺?JDK
                    com.intellij.openapi.projectRoots.JavaSdk javaSdkType = com.intellij.openapi.projectRoots.JavaSdk
                            .getInstance();

                    // 闂傚倷鐒﹂惇褰掑垂婵犳艾绐楅柟鐗堟緲閸?SDK 闂傚倷绀侀幉锟犳嚌閸撗呯煋闁诡垱澹嬮崣?
                    String sdkName = "JDK-" + System.getProperty("java.version", "auto");

                    // 闂傚倷绀侀幉锛勬暜濡ゅ啰鐭欓柟瀵稿Х绾?SDK
                    com.intellij.openapi.projectRoots.Sdk newSdk = javaSdkType.createJdk(sdkName, javaHome, false);

                    if (newSdk != null) {
                        // 闂傚倷鑳堕…鍫㈡崲閹扮増鍋嬪┑鐘叉搐闁裤倕鈹戦悩鍙夋悙缂佲偓婢舵劖鐓熸俊顖濐嚙缁插鏌?JDK 闂?
                        ApplicationManager.getApplication().runWriteAction(() -> {
                            jdkTable.addJdk(newSdk);
                        });
                        QinLogger.info("[SDK]   Registered new JDK in IDE: " + sdkName);

                        // 闂備浇宕垫慨宕囩矆娴ｈ娅犲ù鐘差儐閸?Project SDK闂傚倷鐒︾€笛呯矙閹达附鍋嬮柛娑卞枤缁犻箖鏌涢妷顔煎闁稿鍔戦弻鏇熺箾閸喖濮庨悷婊勬緲濡繈寮婚敐澶涚稏妞ゆ巻鍋撳┑鈥茬矙閺屸€崇暆閳ь剟宕版惔銊ョ厺闁哄啫鐗嗛崡铏亜韫囨挻顥犳い?
                        QinProjectSdkPersistence.applyAndPersist(project, rootManager, newSdk);
                    } else {
                        QinLogger.error("[SDK] Unable to create JDK automatically, please configure it manually");
                    }
                } else {
                    QinLogger.info("[SDK] JAVA_HOME is unavailable, Project SDK remains unset until configured manually");
                    QinLogger.info("[SDK]   JAVA_HOME = " + (javaHome != null ? javaHome : "null"));
                }
            }

            // 闂傚倷绀侀幉锛勬暜閿熺姴缁╅梺顒€绉撮拑鐔封攽閻樻彃鏆斿ù婊勭矒閺屾盯鏁傜拠鎻掔缂備焦鍔栭〃鍫㈡閹惧瓨濯撮悷娆忓闂夊秹姊虹拠鎻掔槰闁搞劌鐖煎顐㈩吋閸涱垱娈曢梺鍛婂姈閸庢娊寮?IDEA UI 闂傚倷绀侀幖顐⒚洪妶澶嬪仱闁靛ň鏅涢拑?
            QinLogger.info("[SDK] Refreshing IDEA project structure after SDK update...");
            QinProjectSdkPersistence.refreshProjectStructure(project);

            QinLogger.info("[SDK] ========== Project SDK configuration complete ==========");
        } catch (Exception e) {
            QinLogger.error("[SDK] Failed to configure Project SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 闂傚倷绀侀幖顐ゆ偖椤愶箑纾块柛娆忣槺閻濊埖淇婇姘辨癁闁稿鎹囬幃浠嬪垂椤愩垺鐣紓鍌欓檷閸斿秹鎮￠敓鐘茬畾?sources jar 闂傚倷绀侀幖顐﹀磹缁嬫５娲晲閸涱亝鐎?
     * 婵犵數鍋為幐濠氭嚌妤ｅ喚鏁勯柛娑欑暘閳? xxx.jar -> xxx-sources.jar
     */
    private static String findSourcesJar(String jarPath) {
        if (jarPath == null || !jarPath.endsWith(".jar")) {
            return null;
        }

        // 闂備浇顕х换鎰崲閹寸姵宕查柛鈩冪⊕閸庡﹥銇勯弽顐粶闁圭懓鐖奸弻鏇熺箾閸喖濮曢梺绋匡工瀹曨剟鍩ユ径鎰妞ゆ牗鐭竟鏇㈡⒒娴ｅ搫鍔﹂柡鍛櫊瀹曚即寮介銈囶槸閻庡箍鍎卞Λ娑㈠煝閺冨牊鍊甸柨婵嗛娴滅偤鏌涘▎灞戒壕闂?-sources.jar
        String basePath = jarPath.substring(0, jarPath.length() - 4); // 缂傚倸鍊风粈渚€藝椤栫偐鈧箑鐣￠幍铏€?.jar
        String sourcesPath = basePath + "-sources.jar";

        if (java.nio.file.Files.exists(java.nio.file.Paths.get(sourcesPath))) {
            return sourcesPath.replace("\\", "/");
        }

        return null;
    }

    /**
     * 闂傚倷绀侀幖顐ゆ偖椤愶箑纾块柛娆忣槺閻濊埖淇婇姘辨癁闁稿鎹囬幃浠嬪垂椤愩垺鐣紓鍌欓檷閸斿秹鎮￠敓鐘茬畾?javadoc jar 闂傚倷绀侀幖顐﹀磹缁嬫５娲晲閸涱亝鐎?
     * 婵犵數鍋為幐濠氭嚌妤ｅ喚鏁勯柛娑欑暘閳? xxx.jar -> xxx-javadoc.jar
     */
    private static String findJavadocJar(String jarPath) {
        if (jarPath == null || !jarPath.endsWith(".jar")) {
            return null;
        }

        // 闂備浇顕х换鎰崲閹寸姵宕查柛鈩冪⊕閸庡﹥銇勯弽顐粶闁圭懓鐖奸弻鏇熺箾閸喖濮曢梺绋匡工瀹曨剟鍩ユ径鎰妞ゆ牗鐭竟鏇㈡⒒娴ｅ搫鍔﹂柡鍛櫊瀹曚即寮介銈囶槸閻庡箍鍎卞Λ娑㈠煝閺冨牊鍊甸柨婵嗛娴滅偤鏌涘▎灞戒壕闂?-javadoc.jar
        String basePath = jarPath.substring(0, jarPath.length() - 4); // 缂傚倸鍊风粈渚€藝椤栫偐鈧箑鐣￠幍铏€?.jar
        String javadocPath = basePath + "-javadoc.jar";

        if (java.nio.file.Files.exists(java.nio.file.Paths.get(javadocPath))) {
            return javadocPath.replace("\\", "/");
        }

        return null;
    }

    /**
     * 婵?Qin 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳閻戞ê顕遍悗娑櫭禒娲⒑缂佹ɑ鈷掓い顓炵墦閹?.iml 闂傚倷绀侀幖顐﹀磹缁嬫５娲晲閸涱亝鐎?
     * 闂?IDEA 闂備浇宕垫慨鏉懨洪妶鍛傛稑螖閸涱厽妲梺绯曞墲閵囨盯寮ㄩ敃鍌涚厵闂侇叏缂氱花濠氭煛娴ｅ弶娅婇柡宀嬬秮椤㈡瑩寮拌箛鎾冲腐婵犵數鍋涘鑸靛垔娴犲桅?
     * 
     * @param forceOverwrite true=闂佽瀛╅鏍窗閺嶎厼绠规い鎰剁畱閺勩儲淇婇妶鍛殲闁哥喐妞介弻娑㈠焺閸愵亝鍣紒鎯у綖閸楁娊寮婚妸銉㈡婵☆垳鍘ч·鈧┑鐘媰閸曨厾鐓夐悗?sync闂傚倷鐒︾€笛呯矙閹次层劑鍩€椤掑倻纾奸弶鍫涘妿閸氱lse=闂佽娴烽幊鎾诲箟闄囬妵鎰板礃椤旇棄浠遍梺闈浥堥弲娑㈠箲閸洘鐓忓┑鐐茬仢婵″潡鏌涙惔銏㈠弨鐎殿喖鐖奸崺鈩冩媴閸︻厹鍋掔紓鍌欐祰妞存悂鎮烽埡鍛疇婵炴垯鍩勯弫鍌炴煕閺囥劌鈧絽螖閸涱喚鍘卞┑顔筋焽閸樠囨倶閻樻祴鏀芥い鏃囶潐濞呭﹪鏌?
     */
    public static void generateImlFile(Path projectPath, boolean forceOverwrite) {
        generateImlFile(projectPath, forceOverwrite, null);
    }

    /**
     * 婵?Qin 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳閻戞ê顕遍悗娑櫭禒娲⒑缂佹ɑ鈷掓い顓炵墦閹?.iml 闂傚倷绀侀幖顐﹀磹缁嬫５娲晲閸涱亝鐎?
     * 闂?IDEA 闂備浇宕垫慨鏉懨洪妶鍛傛稑螖閸涱厽妲梺绯曞墲閵囨盯寮ㄩ敃鍌涚厵闂侇叏缂氱花濠氭煛娴ｅ弶娅婇柡宀嬬秮椤㈡瑩寮拌箛鎾冲腐婵犵數鍋涘鑸靛垔娴犲桅?
     * 
     * @param forceOverwrite true=闂佽瀛╅鏍窗閺嶎厼绠规い鎰剁畱閺勩儲淇婇妶鍛殲闁哥喐妞介弻娑㈠焺閸愵亝鍣紒鎯у綖閸楁娊寮婚妸銉㈡婵☆垳鍘ч·鈧┑鐘媰閸曨厾鐓夐悗?sync闂傚倷鐒︾€笛呯矙閹次层劑鍩€椤掑倻纾奸弶鍫涘妿閸氱lse=闂佽娴烽幊鎾诲箟闄囬妵鎰板礃椤旇棄浠遍梺闈浥堥弲娑㈠箲閸洘鐓忓┑鐐茬仢婵″潡鏌涙惔銏㈠弨鐎殿喖鐖奸崺鈩冩媴閸︻厹鍋掔紓鍌欐祰妞存悂鎮烽埡鍛疇婵炴垯鍩勯弫鍌炴煕閺囥劌鈧絽螖閸涱喚鍘卞┑顔筋焽閸樠囨倶閻樻祴鏀芥い鏃囶潐濞呭﹪鏌?
     * @param ideaDir        IDEA 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳閻戞ê顕遍悗娑櫭崜?.idea 闂傚倷鑳堕崕鐢稿疾閳哄懎绐楅柡宥庡亞缁€濠勨偓骞垮劚濞诧箑鐣锋径瀣ㄤ簻闁哄秲鍔嶉惃鎴犵磼閹邦喖浠遍柡灞诲妼閳藉螣閻撳簶鍙￠梻浣芥〃闂勫秹宕愰弽顐ょ焿鐎广儱妫欓崕鐔兼煏韫囧﹤澧叉繛鍫燂耿濮婃椽宕ㄦ繝鍌氼潓闂佸搫鐗滈崜婵嬪疮椤栫偞鈷戠紒澶婃鐎氬嘲鈻撻敐鍥╃＜?
     */
    public static void generateImlFile(Path projectPath, boolean forceOverwrite, Path ideaDir) {
        try {
            if (!hasSourceDirectory(projectPath)) {
                QinLogger.info("[iml] Skipping .iml generation for source-less aggregate project: " + projectPath);
                return;
            }

            // 闂傚倷绀侀崥瀣磿閹惰棄搴婇柤鑹扮堪娴滃綊鏌涢妷锝呭Ω濞存粍绮撻弻娑㈡晜鐠囨彃绠虹紓浣瑰姈椤ㄥ﹪寮诲☉姗嗘僵妞ゆ帒鍊愰妶鍥ㄥ仏?
            String projectName = projectPath.getFileName().toString();
            Path imlPath = projectPath.resolve(projectName + ".iml");

            QinLogger.info("[iml] Processing project: " + projectPath);
            QinLogger.info("[iml]   iml path: " + imlPath);
            QinLogger.info("[iml]   forceOverwrite: " + forceOverwrite);

            // 婵犵數濮烽。浠嬪焵椤掆偓閸熷潡鍩€椤掆偓缂嶅﹪骞冨Ο璇茬窞濠电姴楠搁弲鐘绘⒑閸︻厾甯涢悽顖滃仱楠炴鎮╃紒妯煎幈闂佹寧鏌ㄩ幖顐ｄ繆閾忓湱纾兼俊銈勮兌婢ь剛绱掔€ｎ亶妲告い鎾炽偢瀹曘劑顢橀悩鐢垫殺闂傚倷绀侀幉锛勬暜濡ゅ懎纾垮┑鍌涙偠閳ь剙鎳橀、鏇㈡晜閽樺澹嗛梻浣告惈缁夊爼寮崫銉х焼濠㈣泛顑勭换鍡樸亜閹邦喖鏋庡ù婊堢畺濮婃椽宕崟顕呮蕉闂佺瀛╄ぐ鍐ㄥ祫闂佸綊妫跨粈浣烘喆閿曞倹鐓ラ柡鍥╁仜閳ь剝顫夌粋鎺戭潨閳ь剟骞冮悜钘夊嵆婵﹩鍙庡Ο鍌炴⒑闁稓鈹掗柛鏂跨Т椤?sourceFolder
            boolean needGenerate = !Files.exists(imlPath) || forceOverwrite;

            if (!needGenerate) {
                QinLogger.info("[iml]   Existing .iml found, checking whether repair is needed...");
                // 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅宕岄柡宀嬬磿娴狅妇鎷犻幓鎺懶戞俊銈囧Х閸嬫稑煤椤撶儐鍤?.iml 闂傚倷绀侀幖顐も偓姘卞厴瀹曡瀵奸弶鎴犵暰婵炶揪缍€濞咃絿澹曟禒瀣厱婵犻潧妫楅顐ｆ叏?sourceFolder
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
                // 婵犵數鍋犻幓顏嗙礊閳ь剚绻涙径瀣鐎?BSP 婵犵數濮伴崹鐓庘枖濞戞埃鍋撳鐓庢珝妤犵偛鍟换婵嬪炊瑜斿Λ鐑芥⒑闂堟侗鐒鹃柛鏂跨灱缁顢涢悙瀵稿幈濠电偛妫欓崝妤佹櫠閻㈠憡鐓忛柛鈩冾殘鏁堥梺璇″灠鐎氫即銆佸▎鎴炲鐎规洖娲ㄩ悾楣冩⒒?
                com.qin.bsp.BspHandler bspHandler = new com.qin.bsp.BspHandler(projectPath.toString());

                // 闂傚倷绀侀崥瀣磿閹惰棄搴婇柤鑹扮堪娴滃綊鏌涢妷鎴斿亾闁衡偓閿曞倹鐓欓梺顓ㄧ細缁ㄥ鏌℃担鍙夋珚闁哄矉缍侀、娆撳及韫囨挸甯繝鐢靛仜瀵埖鍒婃禒瀣﹂柟鐗堟緲閸楄櫕銇勮箛鎾搭棤妞わ腹鏅滅换娑㈠级閹存繃鍊梺鎸庡哺閺屾稑螣閼姐倐濮囩紓?qin.config.js闂?
                String sourceDir = bspHandler.getSourceDir();
                String testDir = bspHandler.getTestDir();

                // 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅宕岄柡宀嬬磿娴狅妇鎷犻幓鎺懶戞繝鐢靛仜瀵埖鍒婃禒瀣﹂柟鐗堟緲閸楁娊鏌ｉ弮鈧崝鍛存晝閸屾稓鍘卞┑顔矫晶浠嬫偩闁秵鐓熼柕鍫濇噹椤忣厽鎱ㄦ繝浣虹煓鐎规洏鍔戦、姗€鎮╅搹顐㈡灈闂?
                if (!Files.exists(projectPath.resolve(sourceDir))) {
                    // 闂傚倷鐒﹂幃鍫曞磿閹惰棄纾婚柕鍫濐槸閻掑灚銇勯幋锝嗩棄濞存粓绠栧娲川婵犲倻鐟叉繝娈垮灟缁瑥鐣烽棃娑辩叆闁割偅绻勯ˇ顕€姊洪棃娑辩叚閻庨潧鑻～蹇涙倷椤掑倻顔?
                    sourceDir = detectSourceDir(projectPath);
                }
                QinLogger.info("[iml]   sourceDir: " + sourceDir);
                QinLogger.info("[iml]   testDir: " + testDir);

                if (sourceDir == null) {
                    QinLogger.info("[iml]   Source directory not found");
                    return;
                }

                // 闂傚倷绀侀崥瀣磿閹惰棄搴婇柤鑹扮堪娴滃綊鏌涢妷锝呭缂傚秴娲弻鐔煎箚瑜嶉弳杈ㄦ叏閿濆拋妯€闁哄矉缍侀崺鈩冪節閸屾稓浜紓?
                String outputDir = bspHandler.getOutputDir();
                QinLogger.info("[iml]   outputDir: " + outputDir);

                // 闂傚倷鐒﹂惇褰掑垂婵犳艾绐楅柟鐗堟緲閸ㄥ倹鎱ㄥΟ鎸庣【缂佺姵濞婇弻鐔兼倻濮楀棙鐣烽梺缁樼箘閸犳牠寮婚敐澶婄厸濠电姴鍊归悘鍫㈢磽?XML
                StringBuilder excludeFolders = new StringBuilder();
                for (String excludeDir : IML_EXCLUDED_DIRS) {
                    excludeFolders.append("          <excludeFolder url=\"file://$MODULE_DIR$/")
                            .append(excludeDir)
                            .append("\" />\n");
                }

                // 闂傚倷鐒﹂惇褰掑垂婵犳艾绐楅柟鐗堟緲閸ㄥ倹鎱ㄥ鍡楀幋闁衡偓閿曞倹鐓欓梺顓ㄧ畱楠炴鏌ｆ惔顔兼灈闂囧鏌ｅΟ鐓庡妺缁绢參绠栭弻?XML
                StringBuilder sourceFolders = new StringBuilder();
                sourceFolders.append("      <sourceFolder url=\"file://$MODULE_DIR$/").append(sourceDir)
                        .append("\" isTestSource=\"false\" />\n");
                if (testDir != null && Files.exists(projectPath.resolve(testDir))) {
                    sourceFolders.append("      <sourceFolder url=\"file://$MODULE_DIR$/").append(testDir)
                            .append("\" isTestSource=\"true\" />\n");
                }

                // 闂傚倸鍊风欢锟犲磻閸涱喚鈹嶉柧蹇氼潐瀹?BSP 闂傚倷绀侀崥瀣磿閹惰棄搴婇柤鑹扮堪娴滃綊鏌涢妷锝呭闁荤喕顫夌换婵嬫濞戝啿濮涘銈傛櫓閸嬪﹪寮婚妸銉㈡婵☆垵宕电粔绉巃sspath闂?
                List<String> classpath = bspHandler.getClasspath();
                StringBuilder dependencyEntries = new StringBuilder();

                for (String path : classpath) {
                    String entryPath = path.replace("\\", "/");

                    if (entryPath.endsWith(".jar")) {
                        // JAR 闂傚倷绀侀幖顐﹀磹缁嬫５娲晲閸涱亝鐎婚梺闈涢獜缁辨洟鎮风憴鍕瘈闂傚牊鍐婚崝鐔搞亜?- 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅宕岄柡宀嬬磿娴狅妇鎷犻幓鎺戭潥婵犵鈧啿绾ч柟顔煎€搁悾鐑藉Ψ閳哄倹娅嗛梺鍏间航閸庢娊鎮鹃幎鑺ョ厽閹艰揪绲鹃弳鈺呮煙閾忣偅灏甸柤娲憾瀵濡烽敃鈧崜?sources 闂?javadoc
                        String sourcesPath = findSourcesJar(entryPath);
                        String javadocPath = findJavadocJar(entryPath);

                        dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                                .append("      <library>\n")
                                .append("        <CLASSES>\n")
                                .append("          <root url=\"jar://").append(entryPath).append("!/\" />\n")
                                .append("        </CLASSES>\n");

                        // 濠电姷鏁搁崕鎴犵礊閳ь剚銇勯弴鍡楀閸?JAVADOC 闂傚倷鑳堕崢褔骞栭锕€纾瑰┑鐘宠壘绾?
                        if (javadocPath != null) {
                            dependencyEntries.append("        <JAVADOC>\n")
                                    .append("          <root url=\"jar://").append(javadocPath).append("!/\" />\n")
                                    .append("        </JAVADOC>\n");
                        } else {
                            dependencyEntries.append("        <JAVADOC />\n");
                        }

                        // 濠电姷鏁搁崕鎴犵礊閳ь剚銇勯弴鍡楀閸?SOURCES 闂傚倷鑳堕崢褔骞栭锕€纾瑰┑鐘宠壘绾?
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
                        // 闂傚倷绀侀幖顐︽偋濠婂牆绀堟繛鍡楅獜閼板潡鎮楅棃娑欐喐閻忓繒鏁婚弻銈嗘叏閹邦兘鍋撻弽顐ょ幓婵°倕鍟崰?- 闂備浇宕垫慨宕囨閵堝洦顫曢柡鍥ュ灪閸嬧晠鏌ゆ慨鎰偓鎰板磻閹剧粯鍋ㄩ柛顭戝亜濞堝瞼绱撴担闈涘闁绘搫绻濋獮鍡涘礃椤旇偐顦板銈嗗笒閸婂宕哄澶嬧拺婵炶尪顕ч弸娆撴煠鐎圭姵纭鹃悡銈夋煙瀹勬媽顫﹀ù?
                        String sourcePath = computeSourcePath(entryPath);

                        dependencyEntries.append("    <orderEntry type=\"module-library\">\n")
                                .append("      <library>\n")
                                .append("        <CLASSES>\n")
                                .append("          <root url=\"file://").append(entryPath).append("\" />\n")
                                .append("        </CLASSES>\n");

                        // 婵犵數濮烽。浠嬪焵椤掆偓閸熷潡鍩€椤掆偓缂嶅﹪骞冨Ο璇茬窞闁归偊鍓涢濂告⒑鐠団€崇€婚柛鎰典簻鐢挻绻濋悽闈涱潚闁告洦鍋勯～搴ㄦ⒑缂佹澧紒顔芥崌楠炲棝宕熼锝嗘櫓闂佸吋绁撮弲婵嗗礂闂傚倷鐒︾€笛呯矙閹达箑瀚夋い鎺戝闁裤倕鈹戦悩鍙夋悙缂佲偓?SOURCES 闂傚倸鍊烽悞锕€顭垮Ο鑲╃煋闁割偅娲橀崑?
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

                // 闂傚倷鐒﹂惇褰掑垂婵犳艾绐楅柟鐗堟緲閸?.iml 闂傚倷绀侀幉锟犲礉閺囥垹绠犻幖鎼厛閺?
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

            // 濠电姷鏁搁崑娑⑺囬銏犵鐎光偓閸曨偉鍩為梺鑺ッ敍澶愭偄閻撳海浼嬮梺鎯ф禋閸嬪嫭绂掗幘顔解拺?modules.xml
            if (ideaDir != null) {
                registerModuleToIdeaProject(imlPath, ideaDir);
            }

        } catch (Exception e) {
            QinLogger.error("Failed to generate .iml file: " + e.getMessage());
        }
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

            int version = QinWorkspaceSdkDefaults.parseJavaVersion(versionStr);
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
     * 濠电姷鏁搁崑娑⑺囬銏犵鐎光偓閸曨偉鍩為梺鑺ッ敍澶愭偄閻撳海浼嬮梺鎯ф禋閸嬪嫭绂掗幘顔解拺?IDEA 闂?modules.xml
     */
    private static void registerModuleToIdeaProject(Path imlPath, Path ideaDir) {
        try {
            Path modulesXml = ideaDir.resolve("modules.xml");

            // 闂備浇宕垫慨宕囨閵堝洦顫曢柡鍥ュ灪閸嬧晛鈹戦悩宕囶暡闁稿骸娴风槐鎺斺偓锝庡亽閸庛儵鏌涢妶鍥р枅闁诡喛顫夌粭鐔碱敍濮樺彉鍝楃紓?
            Path ideaParent = ideaDir.getParent(); // 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳閻戞ê顕遍柡澶嬪灥閸炪劑姊洪棃鈺佺槣闁告鏅划锝呂旈崘鈺佹瀾?
            Path relativePath = ideaParent.relativize(imlPath);
            String moduleEntry = relativePath.toString().replace("\\", "/");

            String content;
            if (!Files.exists(modulesXml)) {
                // modules.xml 婵犵數鍋為崹鍫曞箰閸濄儳鐭撻柟缁㈠枟閸嬨倝鏌曟繛鐐珔闁圭鍩栭妵鍕敇閻旈顑傜紓浣插亾濠㈣埖鍔栭悡鏇㈡煙閻戞ɑ灏甸柍钘夘槺缁辨捇宕掑☉娆忕３閻庢鍣崜鐔肩嵁瀹ュ鏁婇柣锝呮湰濞?
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

            // 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅宕岄柡宀嬬磿娴狅妇鎷犻幓鎺戭潥婵犵鈧啿绾ч柟顔煎€搁悾鐑藉Ψ閳哄倹娅囬梺閫炲苯澧寸€规洘鐟╅弫宥夊礋椤撶媴绱查梻濠庡亜濞诧箑顫忚ぐ鎺戞辈闁割偁鍎查悡?
            if (content.contains(moduleEntry)) {
                QinLogger.info("[iml]   Module already registered in modules.xml");
                return;
            }

            // 闂傚倷绀侀幖顐︻敄閸涱垪鍋撳鐓庡缂佽鲸鎹囬獮妯兼嫚閸欏妫熼梻浣筋潐椤旀牠宕板Δ鍛仼?module 闂傚倷绀侀幖顐βㄩ埀顒傜磼鏉堛劍绀€閻?
            String newModule = String.format(
                    "      <module fileurl=\"file://$PROJECT_DIR$/%s\" filepath=\"$PROJECT_DIR$/%s\" />",
                    moduleEntry, moduleEntry);

            // 闂?</modules> 婵犵數鍋為崹鍫曞蓟閵娾晩鏁勯柛娑卞枟濞呯娀鏌ｅΟ娆惧殭缂佺姷鏁婚弻鐔兼倻濡櫣浠撮悗?
            String newContent = content.replace("    </modules>", newModule + "\n    </modules>");

            Files.writeString(modulesXml, newContent);
            QinLogger.info("[iml]   Registered module in modules.xml: " + moduleEntry);

        } catch (Exception e) {
            QinLogger.error("[iml]   Failed to update modules.xml: " + e.getMessage());
        }
    }

    /**
     * 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅灏扮紒缁樼洴瀵爼骞嬮鐐插缂傚倷绶￠悡鍫ュ窗濡ゅ啰鐭夐柟鐑橆殕閺呮煡鏌涢妷銏℃珔闁诲骏缍佸娲倻閳哄倹鐝﹂梺鍝ュУ閻楁粎妲?
     */
    private static String detectSourceDir(Path projectPath) {
        // 婵犵數鍋炲娆撳触鐎ｎ喗鏅梻浣告啞钃辩紒瀣灱閻忓姊洪柅鐐茶嫰婢ь噣妫佹径鎰€甸柨婵嗛娴滅偤鏌涢妸銉ｅ仮闁?Maven 缂傚倸鍊搁崐鐑芥倿閿曞倸绠板┑鐘崇閸?
        Path mavenSrc = projectPath.resolve(DEFAULT_SOURCE_DIR);
        if (Files.exists(mavenSrc)) {
            return DEFAULT_SOURCE_DIR;
        }
        // 闂傚倷鑳堕…鍫㈡崲濡ゅ懎鏄ラ柡宥庡弾閺佸啴鏌ｉ幘鍐差唫婵炴垯鍨归悞鍨亜閹哄棗浜鹃梻鍥ь樀閹鏁愭惔鈥愁潾闂佸搫妫楀Λ婵嬪蓟濞戞鏃€鎷呴崷顓фФ缂傚倷绶￠崰鎾诲礉閹存繍鍤?
        Path simpleSrc = projectPath.resolve("src");
        if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
            return "src";
        }
        // 濠电姷鏁搁崑娑欏緞閸ヮ剙绀堟繝闈涙川閻濊埖鎱ㄥ璇蹭壕閻庤娲樺畝鎼佸箖瑜斿畷濂稿閵忕姷鍘撴繝鐢靛仜椤曨厽鍒婄€电绶ら柛褎顨嗛崑澶嬬節婵犲倻澧涢柛搴″閵囧嫰寮崶褌姹楃紓?
        return null;
    }

    /**
     * 闂傚倷绀侀幖顐ょ矓閻戞枻缍栧璺猴功閺嗐倕霉閿濆牊顏犻悘蹇曟暬閺岋繝宕熼銈囶唺缂傚倸鐗嗛…鐑藉蓟濞戙垹绠ｉ柨婵嗘－娴犫晛鈹戦悙鎻掔骇濠殿垯绮欐俊鐢稿箛閺夊灝宓嗛梺瀹犳濡骞冩担铏圭＝濞达絽鎼牎濡炪們鍔屽Λ娑㈡嚍闁稁鏁嬮柍褜鍓熼獮鍡楊吋閸℃ê顎撻梺缁樺灦椤洭鎮伴妷鈺傚仭?
     * 婵犵數鍋為幐濠氭嚌妤ｅ喚鏁勯柛娑欑暘閳? D:/project/subhuti-java/build/classes ->
     * D:/project/subhuti-java/src/main/java
     */
    private static String computeSourcePath(String classPath) {
        try {
            // 闂?build/classes 闂傚倷绀侀幖顐⒚洪敂閿亾缁楁稑鍟伴弳锕傛煕濞嗗浚妲洪柍缁樻閺岋綁骞嬮悜鍡欏姺闂佺粯鐗犳禍鍫曞蓟閿濆鏁婇柡鍕箰閸樷€斥攽閻愭彃绾у┑顖欑矙婵?
            Path classDir = Paths.get(classPath);

            // 闂傚倷绀侀幉锛勫枈瀹ュ鍨傜€规洖娲ㄧ粻鏃堟煕瀹€鈧崑娑氱不閵夆晜鐓欐い鏍ф閸熶即宕㈤幒鎾茬箚闁绘劦浜滈埀顒佹礀閿曘垽宕￠悘鑽ゅ劋鐎靛ジ寮堕幋婵嗘暏闂備線娼荤€靛矂宕㈤悾宀€鐜绘俊銈呭暞閸犳劙鏌ｅΔ鈧悧濠勭矚鐠恒劎纾奸弶鍫涘妿閹冲洨鈧娲滄繛鈧€规洏鍔戦、姗€鎮㈠畡鏉款棐 build 闂傚倷鐒﹂惇褰掑礉瀹€鈧埀顒佸嚬閸ｏ絽鐣烽崫鍕庢棃宕ㄩ鐓庡婵＄偑鍊栧Λ渚€宕戦幇顔剧煋闁秆勵殕閻?
            Path current = classDir;
            while (current != null && !current.getFileName().toString().equals("build")) {
                current = current.getParent();
            }

            if (current != null && current.getParent() != null) {
                Path projectRoot = current.getParent();

                // 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅宕岄柡?src/main/java
                Path mavenSrc = projectRoot.resolve(DEFAULT_SOURCE_DIR);
                if (Files.exists(mavenSrc)) {
                    return mavenSrc.toString().replace("\\", "/");
                }

                // 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅宕岄柡?src
                Path simpleSrc = projectRoot.resolve("src");
                if (Files.exists(simpleSrc) && Files.isDirectory(simpleSrc)) {
                    return simpleSrc.toString().replace("\\", "/");
                }
            }
        } catch (Exception e) {
            // 闂傚倸顭崑鍕洪妸鈺佺柧妞ゆ劧绠戝Ч?
        }
        return null;
    }

    /**
     * 婵犵數鍎戠徊钘壝归崒鐐茬獥闁哄稁鍘旈崶顒佸殤妞ゆ帒鍊婚敍婵嬫⒑閸涘﹦绠撻悗姘煎枛琚?sourceFolder 闂?.iml 闂傚倷绀侀幖顐﹀磹缁嬫５娲晲閸涱亝鐎婚梺闈涚箞閸婃洜绮堥崘顔界厱婵炴垵宕楣冩煕?
     * 闂備浇顕х换鎰崲閹邦儵娑橆煥閸繂鎯炲┑鐘诧工閻楀﹪鍩涢幒鎾变簻闁规崘娉涙禒锕傛煕婵炲灝鈧繈寮?<content url="..." /> 闂備礁鎼ˇ閬嶅磿閹版澘绀堟慨姗嗗墰閺嗭箓鏌涘▎蹇ｆШ闁崇粯妫冮弻娑樷攽閸℃浠奸梺浼欑畱閻栧ジ寮?sourceFolder 闂傚倷鐒﹂惇褰掑礉瀹€鈧埀顒佸嚬閸撶喖骞冩ィ鍐炬晢闁告洦鍋呴悗顒勬⒑閸濆嫷妲归柛銊ャ偢瀹曞搫鐣濋埀顒勫焵?
     */
    private static String fixMissingSourceFolder(String imlContent, Path projectPath) {
        try {
            // 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅灏扮紒缁樼洴瀵爼骞嬮鐐插缂傚倷绶￠悡鍫ュ窗濡ゅ啰鐭夐柟鐑橆殕閺呮煡鏌涢妷銏℃珔闁诲骏缍佸娲倻閳哄倹鐝﹂梺鍝ュУ閻楁粎妲?
            String sourceDir = detectSourceDir(projectPath);
            if (sourceDir == null) {
                QinLogger.info("[iml]   Could not detect source directory, skipping sourceFolder repair");
                return imlContent;
            }

            // 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅宕岄柡宀嬬磿娴狅妇鎷犻幓鎺戭潥婵犵鈧啿绾ч柟顔煎€搁悾鐑藉Ψ閳哄倹娅嗛梺鍏间航閸庢盯鏁冮崒娑氬幘婵犻潧鍊搁ˇ顕€鏌囬娑楃箚妞ゆ劧绲剧亸锔锯偓瑙勬磸閸庨潧鐣锋總绋款潊闁挎稑瀚▓?content 闂傚倷绀侀幖顐ょ矓閺夋嚚娲Ω閳哄﹥鏅?
            java.util.regex.Pattern selfClosingPattern = java.util.regex.Pattern.compile(
                    "<content\\s+url=\"[^\"]*\"\\s*/>");
            java.util.regex.Matcher matcher = selfClosingPattern.matcher(imlContent);

            if (matcher.find()) {
                // 闂傚倷鑳堕幊鎾绘倶濮樿泛纾块柟鎯版閺勩儳鈧厜鍋撻柛鏇ㄥ墮濞堟粌顪冮妶鍛闁绘锕弫宥夊籍閸喓鍘卞┑顔斤供閸撴稒鏅堕姀锛勭?content 闂傚倷绀侀幖顐ょ矓閺夋嚚娲Ω閳哄﹥鏅㈤梺缁樺灱婵倝寮查鍌楀亾閸忓浜鹃梺鍛婃处閸忔稓鍒掗鐐寸厽闁绘柨鎲＄壕濠氭煟閹虹偟鐣遍棁澶嬬箾閹存瑥鐏╃紒鐘插级閵囧嫰顢曢銏犵哗缂備讲鍋撻悗锝庡枟閸婄敻鏌ｉ悢鍝勵暭婵犫偓娴煎瓨鐓涢柛鈽嗗幘缁夘剟鏌曢崶銊ュ濠殿喒鍋撻梺缁橆焽閺佹悂銆?
                String originalTag = matcher.group();
                int urlStart = originalTag.indexOf("url=\"") + 5;
                int urlEnd = originalTag.indexOf("\"", urlStart);
                String url = originalTag.substring(urlStart, urlEnd);

                // 闂傚倷绀侀幖顐︻敄閸涱垪鍋撳鐓庡缂佽鲸鎹囬獮姗€宕瑰☉妯瑰闂佽崵鍠撴晶妤€锕㈡导瀛樼厸闁糕槅鍘剧粔顕€鏌?content 闂傚倷绀侀幖顐ょ矓閺夋嚚娲Ω閳哄﹥鏅?
                StringBuilder newContent = new StringBuilder();
                newContent.append("<content url=\"").append(url).append("\">\n");
                newContent.append("      <sourceFolder url=\"file://$MODULE_DIR$/")
                        .append(sourceDir).append("\" isTestSource=\"false\" />\n");

                // 濠电姷顣藉Σ鍛村磻閳ь剟鏌涚€ｎ偅宕岄柡宀嬬磿娴狅妇鎷犻幓鎺戭潥婵犵鈧啿绾ч柟顔煎€搁悾鐑藉Ψ閳哄倹娅嗛梺鍏间航閸庢娊鎮鹃崘娴嬫斀闁绘劕鐡ㄧ紞鎴炪亜閹存繄澧﹂柟顖欑窔椤㈡盯鎮欓弶鎴濆婵＄偑鍊栧Λ渚€宕戦幇顔剧煋?
                Path testDir = projectPath.resolve("src/test/java");
                if (Files.exists(testDir)) {
                    newContent.append(
                            "      <sourceFolder url=\"file://$MODULE_DIR$/src/test/java\" isTestSource=\"true\" />\n");
                }

                // 濠电姷鏁搁崕鎴犵礊閳ь剚銇勯弴鍡楀閸欏繘鏌ｉ幇顒佹儓缂佺姵濞婇弻鐔兼倻濮楀棙鐣烽梺缁樼箘閸犳牠寮婚敐澶婄厸濠电姴鍊归悘鍫㈢磽?
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
