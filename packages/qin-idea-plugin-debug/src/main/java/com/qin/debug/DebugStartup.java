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
                currentVersion = QinProjectSdkSelection.sdkVersion(currentSdk);
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

            QinProjectSdkSelection.Selection bestSelection =
                    QinProjectSdkSelection.selectConfiguredJdk(desiredVersion);

            if (bestSelection != null) {
                final com.intellij.openapi.projectRoots.Sdk sdkToSet = bestSelection.sdk();
                final String sdkName = sdkToSet.getName();
                int selectedVersion = bestSelection.version();
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
                com.intellij.openapi.projectRoots.Sdk javaHomeSdk = QinProjectSdkSelection.registerJavaHomeJdk();
                if (javaHomeSdk != null) {
                    QinProjectSdkPersistence.applyAndPersist(project, rootManager, javaHomeSdk);
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

}
