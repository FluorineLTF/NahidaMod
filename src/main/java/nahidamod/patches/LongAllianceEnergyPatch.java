package nahidamod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import nahidamod.powers.LongAlliancePower;

@SpirePatch(clz = EnergyManager.class, method = "recharge")
public class LongAllianceEnergyPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix(EnergyManager __instance) {
        if (AbstractDungeon.player != null
                && AbstractDungeon.player.hasPower(LongAlliancePower.POWER_ID)
                && !AbstractDungeon.player.hasRelic("Ice Cream")) {
            if (EnergyPanel.totalCount > 0) {
                AbstractDungeon.player.getPower(LongAlliancePower.POWER_ID).flash();
            }
            EnergyPanel.addEnergy(__instance.energy);
            AbstractDungeon.actionManager.updateEnergyGain(__instance.energy);
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
