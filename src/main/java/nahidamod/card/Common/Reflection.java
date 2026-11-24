package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.actions.QuickenDiscardAction;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Reflection extends NahidaCard {
    public static final String ID = ModHelper.makePath("Reflection");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/Reflection.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Reflection() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.COMMON, CardTarget.SELF, 0);
        this.block = this.baseBlock = 8;
        this.magicNumber = this.baseMagicNumber = 2;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(3);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
        this.addToBot(new QuickenDiscardAction(this.magicNumber));
    }
}
