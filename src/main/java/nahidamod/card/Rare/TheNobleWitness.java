package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.ModHelper;
import nahidamod.interfaces.OnQuickenCard;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class TheNobleWitness extends NahidaCard implements OnQuickenCard {
    public static final String ID = ModHelper.makePath("TheNobleWitness");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TheNobleWitness.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    private int quickenDrawBonus = 0;

    public TheNobleWitness() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.RARE, CardTarget.SELF, 0);
        this.magicNumber = this.baseMagicNumber = 1;
        this.selfRetain = true;
        this.hasUPGRADE_DESCRIPTION = true;
    }

    @Override
    public void onQuicken() {
        this.quickenDrawBonus += 1;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainEnergyAction(this.magicNumber));
        if (this.quickenDrawBonus > 0) {
            this.addToBot(new DrawCardAction(p, this.quickenDrawBonus));
            this.quickenDrawBonus = 0;
        }
    }
}
