package nahidamod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;

public class AgnihotraSutraPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("AgnihotraSutraPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public AgnihotraSutraPower(AbstractCreature owner, int strengthAmount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(1, strengthAmount);
        this.type = PowerType.BUFF;
        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example84.png");
        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example32.png");
        this.updateDescription();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        final int strGain = this.amount;
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower wp = owner.getPower(WisdomPower.POWER_ID);
                if (wp != null && wp.amount > 0) {
                    AgnihotraSutraPower.this.flash();
                    wp.reducePower(1);
                    wp.updateDescription();
                    if (wp.amount <= 0) {
                        owner.powers.remove(wp);
                    }
                    this.addToTop(new ApplyPowerAction(owner, owner, new StrengthPower(owner, strGain), strGain));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
