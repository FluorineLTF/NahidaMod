package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.colorless.MasterOfStrategy;
import com.megacrit.cardcrawl.cards.green.Acrobatics;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.PressurePointEffect;
import nahidamod.actions.RechargeAction;
import nahidamod.actions.ReturnCardToDrawPileAction;
import nahidamod.actions.TriggerNahidaMarksAction;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.NahidaMarkPower;
import nahidamod.powers.VarunaGathaPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Cycle extends NahidaCard {
    public static final String ID = ModHelper.makePath(Cycle.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TestPicture.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = EXAMPLE_CARD;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public Cycle() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0);
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        this.addToBot(new DrawCardAction(p, this.magicNumber));
        this.addToBot(new ReturnCardToDrawPileAction(1,false,true,false));
        this.addToBot(new DrawCardAction(p, this.magicNumber));

        this.addToBot(new RechargeAction(this));
    }
    @Override
    public void upgrade() {
        // 升级时不改变效果
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.initializeDescription();
        }
    }
}
