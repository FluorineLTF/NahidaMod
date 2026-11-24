package nahidamod.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;

public class EnlightenmentPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("EnlightenmentPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public EnlightenmentPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example84.png");
        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example32.png");
        this.updateDescription();
    }

    public static void reducePowerCardCost(AbstractCard c) {
        if (c != null && c.type == AbstractCard.CardType.POWER && c.cost > 0) {
            c.cost = 0;
            c.costForTurn = 0;
            c.isCostModified = true;
        }
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        reducePowerCardCost(card);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        if (AbstractDungeon.player != null) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                reducePowerCardCost(c);
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }
}
