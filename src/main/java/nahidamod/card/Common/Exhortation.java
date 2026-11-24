package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Exhortation extends NahidaCard {
    public static final String ID = ModHelper.makePath("Exhortation");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/Exhortation.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Exhortation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.COMMON, CardTarget.SELF, 0);
        this.block = this.baseBlock = 8;
        this.magicNumber = this.baseMagicNumber = 2;
        this.shuffleBackIntoDrawPile = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBlock(2);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainBlockAction(p, p, this.block));
        if (this.magicNumber <= 1) {
            this.baseMagicNumber = 0;
            this.magicNumber = 0;
            this.shuffleBackIntoDrawPile = false;
            this.exhaust = true;
        } else {
            this.baseMagicNumber -= 1;
            this.magicNumber = this.baseMagicNumber;
            this.shuffleBackIntoDrawPile = true;
            this.exhaust = false;
        }
    }
}
