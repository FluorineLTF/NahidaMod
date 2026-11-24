package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.ProlificPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Prolific extends NahidaCard {
    public static final String ID = ModHelper.makePath("Prolific");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/Prolific.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Prolific() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.POWER, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.SELF, 0);
        this.magicNumber = this.baseMagicNumber = 1;
        this.hasUPGRADE_DESCRIPTION = true;
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
        this.addToBot(new ApplyPowerAction(p, p, new ProlificPower(p, this.magicNumber), this.magicNumber));
    }
}
