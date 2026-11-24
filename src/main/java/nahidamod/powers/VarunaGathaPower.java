package nahidamod.powers;

import com.megacrit.cardcrawl.actions.common.UpgradeRandomCardAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;
import nahidamod.interfaces.OnInspirationBurstListener;

public class VarunaGathaPower extends AbstractPower implements OnInspirationBurstListener {
    public static final String POWER_ID = ModHelper.makePath("VarunaGathaPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME;
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private boolean upgradeAll;

    public VarunaGathaPower(AbstractCreature owner, boolean upgradeAll) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.upgradeAll = upgradeAll;
        this.amount = -1;
        this.type = PowerType.BUFF;
        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example84.png");
        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example32.png");
        this.updateDescription();
    }

    public void setUpgradeAll(boolean all) {
        if (all) {
            this.upgradeAll = true;
            this.updateDescription();
        }
    }

    @Override
    public void onInspirationBurst() {
        this.flash();
        if (this.upgradeAll) {
            this.addToBot(new ArmamentsAction(true));
        } else {
            this.addToBot(new ArmamentsAction(false));
        }
    }

    @Override
    public void updateDescription() {
        this.description = this.upgradeAll ? DESCRIPTIONS[1] : DESCRIPTIONS[0];
    }
}
