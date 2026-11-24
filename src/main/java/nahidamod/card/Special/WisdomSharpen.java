package nahidamod.card.Special;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import nahidamod.actions.RechargeAction;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import java.util.ArrayList;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class WisdomSharpen extends NahidaCard {
    public static final String ID = ModHelper.makePath(WisdomSharpen.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TestPicture.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = EXAMPLE_CARD;
    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.SELF;

    public WisdomSharpen() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0);
        this.hasUPGRADE_DESCRIPTION = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.updateDescription();
        }
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int amount = 0;
        for (AbstractPower power : p.powers){
            if (power instanceof WisdomPower){
                amount += power.amount;
                break;
            }
        }
        if (amount > 0 && this.magicNumber > 0){
            int sum = amount * this.magicNumber;
            this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p,sum),sum));
            this.addToBot(new ApplyPowerAction(p, p, new LoseStrengthPower(p,sum),sum));
        }else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F,CARD_STRINGS.EXTENDED_DESCRIPTION[1], true));//对话说明，看情况加
        }
//        this.addToBot(new ActedAction(1));
        this.addToTop(new RechargeAction(this));

    }

    public void setX (int amount) {
        this.magicNumber = amount;
        this.baseMagicNumber = this.magicNumber;
        this.rawDescription = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
        this.initializeDescription();
    }

    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        card.baseMagicNumber = this.baseMagicNumber;
        card.magicNumber = this.magicNumber;
        card.description = (ArrayList)this.description.clone();
        return card;
    }

    public void updateDescription() {
        if (this.magicNumber > 0) {
            this.rawDescription = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
            if (this.inactives > 0) {
                this.rawDescription = this.rawDescription + TEXT[0] + inactives + TEXT[2];
            }
            this.initializeDescription();
        }else {
            super.updateDescription();
        }
    }

}