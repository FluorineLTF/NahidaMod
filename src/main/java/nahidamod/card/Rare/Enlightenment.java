package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.EnlightenmentPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Enlightenment extends NahidaCard {
    public static final String ID = ModHelper.makePath("Enlightenment");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/Enlightenment.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Enlightenment() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.RARE, CardTarget.SELF, 0);
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
        this.addToBot(new ApplyPowerAction(p, p, new EnlightenmentPower(p)));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractCard c : p.hand.group) {
                    EnlightenmentPower.reducePowerCardCost(c);
                }
                for (AbstractCard c : p.drawPile.group) {
                    EnlightenmentPower.reducePowerCardCost(c);
                }
                for (AbstractCard c : p.discardPile.group) {
                    EnlightenmentPower.reducePowerCardCost(c);
                }
                for (AbstractCard c : p.exhaustPile.group) {
                    EnlightenmentPower.reducePowerCardCost(c);
                }
                p.hand.glowCheck();
                this.isDone = true;
            }
        });
    }
}
