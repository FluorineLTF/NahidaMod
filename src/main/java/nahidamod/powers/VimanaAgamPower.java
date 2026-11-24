package nahidamod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.DendroCoreHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;
import nahidamod.interfaces.OnInspirationBurstListener;

public class VimanaAgamPower extends AbstractPower implements OnInspirationBurstListener {
    public static final String POWER_ID = ModHelper.makePath("VimanaAgamPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public VimanaAgamPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = Math.max(1, amount);
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
    public void onInspirationBurst() {
        this.flash();
        DendroCoreHelper.spawnDendroCore(this.amount);
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }
}
