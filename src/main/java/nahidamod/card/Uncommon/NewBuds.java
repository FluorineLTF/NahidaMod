package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import nahidamod.helpers.DendroCoreHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class NewBuds extends NahidaCard {
    public static final String ID = ModHelper.makePath("NewBuds");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/NewBuds.png";
    private static final int COST = -1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public NewBuds() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.SELF, 0);
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
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }
        if (this.upgraded) {
            effect += 1;
        }
        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }
        if (effect > 0) {
            DendroCoreHelper.spawnDendroCore(effect);
        }
    }
}
