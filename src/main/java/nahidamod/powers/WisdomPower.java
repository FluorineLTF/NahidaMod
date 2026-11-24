package nahidamod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;
import nahidamod.interfaces.OnGainWisdomListener;

import java.util.ArrayList;

public class WisdomPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("WisdomPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public WisdomPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.min(999, amount);
        this.type = PowerType.BUFF;
        this.canGoNegative = false;

        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Wisdom_p.png");
        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Wisdom.png");
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            notifyGainWisdom(this.amount);
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount = Math.min(999, this.amount + stackAmount);
        if (stackAmount > 0) {
            notifyGainWisdom(stackAmount);
        }
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        this.updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount) {
        this.fontScale = 8.0F;
        this.amount -= reduceAmount;
        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        this.updateDescription();
    }

    private void notifyGainWisdom(int gained) {
        if (this.owner == null) {
            return;
        }
        ArrayList<AbstractPower> snapshot = new ArrayList<>(this.owner.powers);
        for (AbstractPower p : snapshot) {
            if (p instanceof OnGainWisdomListener) {
                ((OnGainWisdomListener) p).onGainWisdom(gained);
            }
        }
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + Math.max(0, this.amount) + DESCRIPTIONS[1];
    }
}
