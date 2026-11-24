package nahidamod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.GamblingChipAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;
import nahidamod.interfaces.OnInspirationBurstListener;

import java.util.ArrayList;

public class InspirationPower extends AbstractPower {
    public static final String POWER_ID = ModHelper.makePath("InspirationPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public static final int BURST_THRESHOLD = 6;
    public static int totalGainedThisCombat = 0;

    public InspirationPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, amount);
        this.type = PowerType.BUFF;
        this.canGoNegative = false;

        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example84.png");
        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example32.png");
        this.updateDescription();
    }

    @Override
    public void onInitialApplication() {
        if (this.amount > 0) {
            totalGainedThisCombat += this.amount;
            notifyPaintedDome(this.amount);
        }
        checkBurst();
    }

    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        if (stackAmount > 0) {
            totalGainedThisCombat += stackAmount;
            notifyPaintedDome(stackAmount);
        }
        this.amount += stackAmount;
        checkBurst();
    }

    private void notifyPaintedDome(int gainedAmount) {
        if (this.owner == null) {
            return;
        }
        AbstractPower paintedDome = this.owner.getPower(PaintedDomePower.POWER_ID);
        if (paintedDome instanceof PaintedDomePower) {
            ((PaintedDomePower) paintedDome).onGainInspiration(gainedAmount);
        }
    }

    private void checkBurst() {
        if (this.amount >= BURST_THRESHOLD) {
            int bursts = this.amount / BURST_THRESHOLD;
            this.amount %= BURST_THRESHOLD;
            this.flash();

            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new WisdomPower(this.owner, bursts), bursts));
            this.addToBot(new DrawCardAction(this.owner, bursts));
            this.addToBot(new GamblingChipAction(this.owner, true));

            if (this.owner != null) {
                ArrayList<AbstractPower> snapshot = new ArrayList<>(this.owner.powers);
                for (int i = 0; i < bursts; i++) {
                    for (AbstractPower p : snapshot) {
                        if (p instanceof OnInspirationBurstListener) {
                            ((OnInspirationBurstListener) p).onInspirationBurst();
                        }
                    }
                }
            }
        }

        if (this.amount <= 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        }
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        int remaining = Math.max(0, BURST_THRESHOLD - this.amount);
        this.description = DESCRIPTIONS[0] + remaining + DESCRIPTIONS[1];
    }
}
