package com.qin.debug;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.qin.debug.lsp.QinLspStartupProbe;
import com.qin.debug.run.QinLegacyRunConfigurations;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.*;
import java.util.*;

// 闂傚倷绀侀幉锛勬暜閳哄懎纾婚柛鏇ㄥ灠缁犳牠鏌￠崶銉ョ仾闁哄拋鍓氶幈銊ヮ潨閸℃ぞ绨婚梺瀹狀嚙缁绘﹢寮?qin-cli 闂傚倷鐒﹂惇褰掑礉瀹€鈧埀顒佺煯閸楁娊宕洪埀顒併亜閹哄秶鍔嶉柣銊﹀灴閺屸剝鎷呴棃娑掑亾濡ゅ懎鏋佹い鏇楀亾妤犵偞鐗楅幏鍛村川婵犲簼鐢?import static com.qin.constants.QinConstants.*;

/**
 * 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳閻戞ê顕遍柡澶嬪灱琚濇俊鐐€栭悧妤呮嚌閹规劦鏉介梻鍌欒兌閸庣敻寮插鍫濆瀭闁汇垻顭堢粻姘熆閼搁潧濮囬梻?
 * 闂傚倷鑳堕崢褔銆冩惔銏㈩洸婵犲﹤瀚崣蹇涙煃鏉炴壆瀵兼繛鎴欏灩閻掑灚銇勯幒鍡椾壕闂?Qin 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳閻戞ê顕遍悗鐢殿焾缁侊箓姊洪崗鍏煎€愭繛浣冲洤鐓曢柡鍐ㄧ墛閸?sync
 * 闂傚倷娴囬妴鈧柛瀣尰閵囧嫰寮介妸褉妲堥梺?Monorepo闂傚倷鐒︾€笛呯矙閹烘挾鈹嶆繛宸簻閸氳绻濇繝鍌滃缂佲偓婢舵劖鐓忓┑鐘茬箳閻ｈ鲸銇勯妷銉﹀枠闁哄瞼鍠撻幏鐘绘嚑椤掑偆鍞瑰┑鐘媰閸愵€呪偓娈垮枛閻忔艾顕ラ崟顒傜瘈闁告洦鍓氶崯銏犫攽閿涘嫬浜奸柛濠冩礀閿曘垽宕￠悘?
 */
public class DebugStartup implements ProjectActivity {
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
        QinLegacyRunConfigurations.remove(project);
        ApplicationManager.getApplication().invokeLater(() -> QinLspStartupProbe.log(project, Paths.get(basePath)));

        if (QinWorkspaceSdkDefaults.hasQinSdkContext(Paths.get(basePath))) {
            ApplicationManager.getApplication().invokeLater(() -> QinStartupSdkConfiguration.configure(project));
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

    /**
     * 闂傚倷绀侀幉锟犳偡閿曞倸鍨傜憸鐗堝笧瀹撲線鏌涢妷顔煎缂佺姰鍎甸弻宥堫檨闁告挾鍠庨?Qin 婵犵绱曢崑鎴﹀磹閺囩儑鑰块柛妤冧紳?
     * 婵犵數鍋犻幓顏嗙礊閳ь剚绻涙径瀣鐎?qin-cli 闂?LocalProjectResolver
     */
    public static List<Path> discoverQinProjects(Path ideaProjectDir) {
        return com.qin.core.LocalProjectResolver.scanAllProjects(ideaProjectDir.toString());
    }


}
