package nahidamod.modcore;

import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.*;
import nahidamod.Characters.MyCharacter;
import com.badlogic.gdx.graphics.Color;
import nahidamod.card.Basic.Akara;
import nahidamod.card.Basic.Defend;
import nahidamod.card.Basic.Strike;
import nahidamod.card.Common.*;
import nahidamod.card.Common.NoRespect;
import nahidamod.card.Rare.*;
import nahidamod.card.Special.*;
import nahidamod.card.Basic.ForbiddenKnowledge;
import nahidamod.card.Uncommon.*;
import nahidamod.card.Wish.Memory;
import nahidamod.card.Wish.Surprise;
import nahidamod.card.Wish.Sweet;
import nahidamod.helpers.ForgetHelper;
import nahidamod.helpers.NextBattleManager;
import nahidamod.potions.WisdomPotion;
import nahidamod.powers.InspirationPower;
import nahidamod.relics.*;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.nio.charset.StandardCharsets;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;
import static nahidamod.Characters.MyCharacter.Enums.MY_CHARACTER;

@SpireInitializer
public class NahidaMod implements EditCardsSubscriber, EditStringsSubscriber, EditCharactersSubscriber, EditKeywordsSubscriber, EditRelicsSubscriber, PostInitializeSubscriber, OnStartBattleSubscriber, PostBattleSubscriber {
    @Override
    public void receiveEditCharacters() {
        // 向basemod注册人物
        BaseMod.addCharacter(new MyCharacter(CardCrawlGame.playerName), MY_CHARACTER_BUTTON, MY_CHARACTER_PORTRAIT, MY_CHARACTER);
    }

    private static final String MY_CHARACTER_BUTTON = "NahidaResources/images/char/Character_Button.png";
    // 人物选择界面的立绘
    private static final String MY_CHARACTER_PORTRAIT = "NahidaResources/images/char/Nahida.png";
    // 攻击牌的背景（小尺寸）
    private static final String BG_ATTACK_512 = "NahidaResources/images/512/bg_attack_512.png";
    // 能力牌的背景（小尺寸）
    private static final String BG_POWER_512 = "NahidaResources/images/512/bg_power_512.png";
    // 技能牌的背景（小尺寸）
    private static final String BG_SKILL_512 = "NahidaResources/images/512/bg_skill_512.png";
    // 在卡牌和遗物描述中的能量图标
    private static final String SMALL_ORB = "NahidaResources/images/char/small_orb.png";
    // 攻击牌的背景（大尺寸）
    private static final String BG_ATTACK_1024 = "NahidaResources/images/1024/bg_attack.png";
    // 能力牌的背景（大尺寸）
    private static final String BG_POWER_1024 = "NahidaResources/images/1024/bg_power.png";
    // 技能牌的背景（大尺寸）
    private static final String BG_SKILL_1024 = "NahidaResources/images/1024/bg_skill.png";
    // 在卡牌预览界面的能量图标
    private static final String BIG_ORB = "NahidaResources/images/char/card_orb.png";
    // 小尺寸的能量图标（战斗中，牌堆预览）
    private static final String ENEYGY_ORB = "NahidaResources/images/char/cost_orb.png";
    public static final Color MY_COLOR = new Color(1.0f, 1.0f, 1.0f, 1.0F);

    public NahidaMod() {
        BaseMod.subscribe(this);
        BaseMod.addColor(EXAMPLE_CARD, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, MY_COLOR, BG_ATTACK_512, BG_SKILL_512, BG_POWER_512, ENEYGY_ORB, BG_ATTACK_1024, BG_SKILL_1024, BG_POWER_1024, BIG_ORB, SMALL_ORB);
    }


    public static void initialize() {
        new NahidaMod();
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new Defend());
        BaseMod.addCard(new Akara());
        BaseMod.addCard(new WiseLordForm());
        BaseMod.addCard(new RemainYourMora());
        BaseMod.addCard(new SeeThrough());
        BaseMod.addCard(new LongAlliance());
        BaseMod.addCard(new PaintedDome());
        BaseMod.addCard(new PerusingScripture());
        BaseMod.addCard(new SurasthanaFantasia());
        BaseMod.addCard(new Paranoia());
        BaseMod.addCard(new BeyondKlesha());
        BaseMod.addCard(new ForbiddenKnowledge());
        BaseMod.addCard(new JustPunishment());
        BaseMod.addCard(new E());
        BaseMod.addCard(new Kusava());
        BaseMod.addCard(new UndisturbedSlumber());
        BaseMod.addCard(new MelodyOfRebirth());
        BaseMod.addCard(new RitualDuel());
        BaseMod.addCard(new Reverie());
        BaseMod.addCard(new EvergreenWish());
        BaseMod.addCard(new SomeoneCallMyName());
        BaseMod.addCard(new AgnihotraSutra());
        BaseMod.addCard(new TheNobleWitness());
        BaseMod.addCard(new VitalCandle());
        BaseMod.addCard(new Die());
        BaseMod.addCard(new Wish());
        BaseMod.addCard(new Sweet());
        BaseMod.addCard(new Memory());
        BaseMod.addCard(new Surprise());
        BaseMod.addCard(new LeavesReturnToRoot());
        BaseMod.addCard(new Ormos());
        BaseMod.addCard(new BurningOutpost());
        BaseMod.addCard(new LeavesAndPetals());
        BaseMod.addCard(new VimanaAgam());
        BaseMod.addCard(new VarunaGatha());
        BaseMod.addCard(new Cycle());
        BaseMod.addCard(new MazeWanderer());
        BaseMod.addCard(new WhispersLinger());
        BaseMod.addCard(new TenderBranch());
        BaseMod.addCard(new WorldForgetMe());
        BaseMod.addCard(new WeavingDreams());
        BaseMod.addCard(new Manifestation());
        BaseMod.addCard(new InitialWish());
        BaseMod.addCard(new NeverForget());
        BaseMod.addCard(new GracefulContemplation());
        BaseMod.addCard(new HeronGuard());
        BaseMod.addCard(new RapidMetabolism());
        BaseMod.addCard(new SanctuaryOfExiles());
        BaseMod.addCard(new BorderSandBarrier());
        BaseMod.addCard(new Contemplation());
        BaseMod.addCard(new SelfExploitation());
        BaseMod.addCard(new SlumberingWoods());
        BaseMod.addCard(new ManifestationBond());
        BaseMod.addCard(new EndlessBubbles());
        BaseMod.addCard(new SongOfThousandNight());
        BaseMod.addCard(new WhirlingFoliage());
        BaseMod.addCard(new NoRespect());
        BaseMod.addCard(new GuiltByAssociation());
        BaseMod.addCard(new Q());
        BaseMod.addCard(new AkashaStirring());
        BaseMod.addCard(new SortThoughts());
        BaseMod.addCard(new JoltsInForest());
        BaseMod.addCard(new Prolific());
        BaseMod.addCard(new Sprout());
        BaseMod.addCard(new NewBuds());
        BaseMod.addCard(new SevenDomainsLotus());
        BaseMod.addCard(new RagingLotus());
        BaseMod.addCard(new OptimizePlan());
        BaseMod.addCard(new Brainstorm());
        BaseMod.addCard(new InitialWisdomWish());
        BaseMod.addCard(new CollectiveWisdom());
        BaseMod.addCard(new CriticalHit());
        BaseMod.addCard(new GildedDash());
        BaseMod.addCard(new PureThought());
        BaseMod.addCard(new Syllogism());
        BaseMod.addCard(new Rigorous());
        BaseMod.addCard(new Probe());
        BaseMod.addCard(new Exhortation());
        BaseMod.addCard(new Reflection());


//        new AutoAdd("NahidaMod").packageFilter(SurasthanaFantasia.class).setDefaultSeen(true).cards();
    }

//    private void autoAddCard (Class c){
//
//        URL location;
//        try {
//            location = c.getProtectionDomain().getCodeSource().getLocation();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//
//        File file;
//        try {
//            file = new File(location.toURI());
//        }catch (Exception e){
//            e.printStackTrace();
//            return;
//        }
//
//        try (JarFile jarFile = new JarFile(file)){
//            Enumeration<JarEntry> entries = jarFile.entries();
//            while(entries.hasMoreElements()){
//                JarEntry entry = entries.nextElement();
//
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();

        String json = Gdx.files.internal("NahidaResources/localization/ZHS/keywords.json")
                .readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                // 这个id要全小写
                BaseMod.addKeyword("nahidamod", keyword.NAMES[0], keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new BlossomingWhiteBranch(), EXAMPLE_CARD);
        BaseMod.addRelicToCustomPool(new Jinn(), EXAMPLE_CARD);
        BaseMod.addRelicToCustomPool(new ApotheosisOfLuminescence(), EXAMPLE_CARD);
        BaseMod.addRelicToCustomPool(new Heart(), EXAMPLE_CARD);
    }

    public void receiveEditStrings() {//本地化位置，目前只有中文
        BaseMod.loadCustomStringsFile(CardStrings.class, "NahidaResources/localization/ZHS/cards.json"); // 加载相应语言的卡牌本地化内容。
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "NahidaResources/localization/ZHS/characters.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "NahidaResources/localization/ZHS/relics.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "NahidaResources/localization/ZHS/powers.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, "NahidaResources/localization/ZHS/ui.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, "NahidaResources/localization/ZHS/potions.json");
    }


    @Override
    public void receivePostInitialize() {
        BaseMod.addSaveField("NahidaMod:NextBattleData", NextBattleManager.INSTANCE);
        addPotion();
    }

    @Override
    public void receiveOnBattleStart(AbstractRoom room) {
        InspirationPower.totalGainedThisCombat = 0;
        ForgetHelper.clear();
        NextBattleManager.INSTANCE.applyAndClear();
    }

    @Override
    public void receivePostBattle(AbstractRoom room) {
        ForgetHelper.onPostBattle(room);
    }

    public void addPotion () {
        BaseMod.addPotion(WisdomPotion.class, Color.FIREBRICK, Color.GRAY, Color.TAN, "NahidaMod:WisdomPotion", MY_CHARACTER);//参数没有搞明白
    }
}
