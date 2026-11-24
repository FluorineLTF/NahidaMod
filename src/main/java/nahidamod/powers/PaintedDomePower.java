package nahidamod.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;
import nahidamod.interfaces.OnInspirationBurstListener;

public class PaintedDomePower extends AbstractPower implements OnInspirationBurstListener {
    public static final String POWER_ID = ModHelper.makePath("PaintedDomePower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public PaintedDomePower(AbstractCreature owner, int blockPerInspiration) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(0, blockPerInspiration);
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

    public void onGainInspiration(int gained) {
        if (gained > 0 && this.amount > 0) {
            this.flash();
            this.addToBot(new GainBlockAction(this.owner, this.owner, gained * this.amount));
        }
    }

    @Override
    public void onInspirationBurst() {
        // 灵感爆发本身会额外增加 1 点智慧，此处不重复加灵感格挡
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
