package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.actions.RechargeAction;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.InspirationPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class BeyondKlesha extends NahidaCard {
    public static final String ID = ModHelper.makePath("BeyondKlesha");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/BeyondKlesha.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public BeyondKlesha() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.COMMON, CardTarget.SELF, 0);
        this.magicNumber = this.baseMagicNumber = 3;
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
        this.addToBot(new ScryAction(this.magicNumber));
        this.addToBot(new ApplyPowerAction(p, p, new InspirationPower(p, 1), 1));
        this.addToBot(new RechargeAction(this));
    }
}
