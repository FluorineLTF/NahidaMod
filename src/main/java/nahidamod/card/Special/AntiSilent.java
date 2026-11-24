package nahidamod.card.Special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.HeavyBlade;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.SilencePower;

public class AntiSilent extends NahidaCard {
    public static final String ID = ModHelper.makePath(AntiSilent.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/ForbiddenKnowledge.png";
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public AntiSilent() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 1);
        this.exhaust = true;
    }

    public void upgrade() {
        this.isInnate = true;
        super.upgrade();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower silence = AbstractDungeon.player.getPower("NahidaMod:SilencePower");
        if (silence != null) {
            this.addToBot(new ApplyPowerAction(p, p, new SilencePower(p,-1), -1));
        }
        //this.addToBot(new ApplyPowerAction(p, p, new SilencePower(p,-1), -1));
    }
}
