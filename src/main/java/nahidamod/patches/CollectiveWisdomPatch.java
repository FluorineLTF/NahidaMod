package nahidamod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import nahidamod.powers.CollectiveWisdomPower;
import nahidamod.powers.WisdomPower;

public class CollectiveWisdomPatch {

    private static int getWisdom(AbstractPlayer p) {
        if (p == null) {
            return 0;
        }
        AbstractPower wp = p.getPower(WisdomPower.POWER_ID);
        return wp != null ? Math.max(0, wp.amount) : 0;
    }

    @SpirePatch(clz = AbstractCard.class, method = "hasEnoughEnergy")
    public static class HasEnoughEnergyPatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AbstractCard __instance) {
            AbstractPlayer p = AbstractDungeon.player;
            if (p != null
                    && p.hasPower(CollectiveWisdomPower.POWER_ID)
                    && __instance.costForTurn > 0
                    && !__instance.freeToPlayOnce
                    && !__instance.isInAutoplay
                    && EnergyPanel.totalCount < __instance.costForTurn) {
                int deficit = __instance.costForTurn - EnergyPanel.totalCount;
                if (getWisdom(p) >= deficit) {
                    return SpireReturn.Return(true);
                }
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class ConsumeWisdomOnUseCardPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractPlayer __instance, AbstractCard c, AbstractMonster monster, int energyOnUse) {
            if (c != null
                    && __instance.hasPower(CollectiveWisdomPower.POWER_ID)
                    && c.costForTurn > 0
                    && !c.freeToPlayOnce
                    && !c.isInAutoplay
                    && EnergyPanel.totalCount < c.costForTurn) {
                int deficit = c.costForTurn - EnergyPanel.totalCount;
                AbstractPower wp = __instance.getPower(WisdomPower.POWER_ID);
                if (wp != null && wp.amount >= deficit) {
                    wp.reducePower(deficit);
                    wp.updateDescription();
                    if (wp.amount <= 0) {
                        __instance.powers.remove(wp);
                    }
                    AbstractPower cw = __instance.getPower(CollectiveWisdomPower.POWER_ID);
                    if (cw != null) {
                        cw.flash();
                    }
                }
            }
        }
    }
}
