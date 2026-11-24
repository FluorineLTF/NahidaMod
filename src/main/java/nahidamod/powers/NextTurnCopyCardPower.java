package nahidamod.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;

import java.util.ArrayList;

public class NextTurnCopyCardPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("NextTurnCopyCardPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private final ArrayList<AbstractCard> queuedCards = new ArrayList<>();

    public NextTurnCopyCardPower(AbstractCreature owner, AbstractCard cardCopy) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example84.png");
        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example32.png");
        addCard(cardCopy);
    }

    public void addCard(AbstractCard cardCopy) {
        if (cardCopy != null) {
            queuedCards.add(cardCopy);
        }
        this.amount = queuedCards.size();
        this.updateDescription();
    }

    @Override
    public void atStartOfTurnPostDraw() {
        this.flash();
        for (AbstractCard c : queuedCards) {
            this.addToBot(new MakeTempCardInHandAction(c.makeStatEquivalentCopy(), 1));
        }
        queuedCards.clear();
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
