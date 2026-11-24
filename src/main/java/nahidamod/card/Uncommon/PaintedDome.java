package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import java.util.Iterator;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class PaintedDome extends NahidaCard {
    public static final String ID = ModHelper.makePath("PaintedDome");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TestPicture.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = EXAMPLE_CARD;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    int[] temp = new int[1];

    public PaintedDome() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0);
        this.magicNumber = this.baseMagicNumber = 5;
        this.exhaust = true;
    }
    @Override
    public void applyPowers() {
        super.applyPowers();
        Iterator<AbstractCard> var2 = AbstractDungeon.actionManager.cardsPlayedThisTurn.iterator();
        int count = 0;
        while(var2.hasNext()) {
            AbstractCard c = (AbstractCard)var2.next();
            if (c.type == CardType.ATTACK||c.type == CardType.SKILL||c.type == CardType.POWER) {
                ++count;
            }
        }
        temp[0]=count;
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[0] + count;
        if (count == 1) {
            this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[1];
        } else {
            this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[2];
        }

        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if((temp[0]/this.magicNumber) > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new WisdomPower(p, (temp[0] / this.magicNumber)), (temp[0] / this.magicNumber)));
        }
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.initializeDescription();
    }
}