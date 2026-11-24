package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.InactiveHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import java.util.ArrayList;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class OptimizePlan extends NahidaCard {
    public static final String ID = ModHelper.makePath("OptimizePlan");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/OptimizePlan.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public OptimizePlan() {
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
        final boolean isUpgraded = this.upgraded;
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractMonster sample = AbstractDungeon.getRandomMonster();
                ArrayList<AbstractCard> toDiscard = new ArrayList<>();
                for (AbstractCard c : p.hand.group) {
                    if (InactiveHelper.getInactive(c) > 0 || c.cost == -2 || !c.canUse(p, sample)) {
                        toDiscard.add(c);
                    }
                }
                for (AbstractCard c : toDiscard) {
                    p.hand.moveToDiscardPile(c);
                    c.triggerOnManualDiscard();
                }
                int drawCount = isUpgraded ? toDiscard.size() * 2 : toDiscard.size();
                if (drawCount > 0) {
                    this.addToTop(new DrawCardAction(p, drawCount));
                }
                p.hand.refreshHandLayout();
                p.hand.glowCheck();
                this.isDone = true;
            }
        });
    }
}
