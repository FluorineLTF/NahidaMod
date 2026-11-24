package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class MazeWanderer extends NahidaCard {
    public static final String ID = ModHelper.makePath("MazeWanderer");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/MazeWanderer.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public MazeWanderer() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.RARE, CardTarget.SELF, 0);
        this.magicNumber = this.baseMagicNumber = 3;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractPower wp = p.getPower(WisdomPower.POWER_ID);
        int wisdom = wp != null ? Math.max(0, wp.amount) : 0;
        if (wisdom > 0) {
            this.addToBot(new GainEnergyAction(wisdom));
        }
        this.addToBot(new DrawCardAction(p, this.magicNumber));
    }
}
