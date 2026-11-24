package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.card.Special.Die;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.CandleOfLifePower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class VitalCandle extends NahidaCard {
    public static final String ID = ModHelper.makePath("VitalCandle");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/VitalCandle.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public VitalCandle() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.POWER, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.SELF, 0);
        this.hasUPGRADE_DESCRIPTION = true;
        this.cardsToPreview = new Die();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.isInnate = true;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new CandleOfLifePower(p)));
        this.addToBot(new MakeTempCardInDrawPileAction(new Die(), 1, true, false));
    }
}
