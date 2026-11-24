package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.VarunaGathaPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class VarunaGatha extends NahidaCard {
    public static final String ID = ModHelper.makePath("VarunaGatha");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/VarunaGatha.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public VarunaGatha() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.POWER, EXAMPLE_CARD, CardRarity.RARE, CardTarget.SELF, 0);
        this.hasUPGRADE_DESCRIPTION = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        final boolean isUpgraded = this.upgraded;
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower existing = p.getPower(VarunaGathaPower.POWER_ID);
                if (existing instanceof VarunaGathaPower) {
                    ((VarunaGathaPower) existing).setUpgradeAll(isUpgraded);
                    existing.flash();
                } else {
                    this.addToTop(new ApplyPowerAction(p, p, new VarunaGathaPower(p, isUpgraded)));
                }
                this.isDone = true;
            }
        });
    }
}
