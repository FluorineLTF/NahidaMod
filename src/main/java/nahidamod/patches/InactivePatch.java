package nahidamod.patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.InactiveHelper;
import nahidamod.helpers.ModHelper;

public class InactivePatch {

    public static final Color INACTIVE_GLOW_COLOR = new Color(1.0F, 0.15F, 0.15F, 0.25F);
    public static final Color DEFAULT_GLOW_COLOR = new Color(0.2F, 0.9F, 1.0F, 0.25F);

    private static UIStrings uiStrings;

    private static String[] getText() {
        if (uiStrings == null) {
            uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("InactiveUI"));
        }
        return uiStrings != null ? uiStrings.TEXT : new String[]{" NL nahidamod:失活 ", " 。", "这张牌已失活，暂时无法打出。"};
    }

    @SpirePatch(clz = AbstractCard.class, method = "canUse")
    public static class CanUsePatch {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> Prefix(AbstractCard __instance, AbstractPlayer p, AbstractMonster m) {
            if (InactiveHelper.getInactive(__instance) > 0) {
                __instance.cantUseMessage = getText()[2];
                return SpireReturn.Return(false);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "triggerWhenDrawn")
    public static class DrawDecayPatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance) {
            InactiveHelper.reduceInactiveByDraw(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class CopyInactivePatch {
        @SpirePostfixPatch
        public static AbstractCard Postfix(AbstractCard __result, AbstractCard __instance) {
            int val = InactiveHelper.getInactive(__instance);
            if (val > 0) {
                InactiveField.inactives.set(__result, val);
                __result.initializeDescription();
            }
            return __result;
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "initializeDescription")
    public static class DescriptionPatch {
        private static String savedRawDescription = null;

        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            int val = InactiveHelper.getInactive(__instance);
            if (val > 0) {
                String[] text = getText();
                savedRawDescription = __instance.rawDescription;
                __instance.rawDescription = savedRawDescription + text[0] + val + text[1];
            } else {
                savedRawDescription = null;
            }
        }

        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance) {
            if (savedRawDescription != null) {
                __instance.rawDescription = savedRawDescription;
                savedRawDescription = null;
            }
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "glowCheck")
    public static class RedGlowPatch {
        @SpirePrefixPatch
        public static void Prefix(CardGroup __instance) {
            if (__instance.type == CardGroup.CardGroupType.HAND) {
                for (AbstractCard c : __instance.group) {
                    if (c.glowColor != null && c.glowColor.equals(INACTIVE_GLOW_COLOR)) {
                        c.glowColor = DEFAULT_GLOW_COLOR.cpy();
                    }
                }
            }
        }

        @SpirePostfixPatch
        public static void Postfix(CardGroup __instance) {
            if (__instance.type == CardGroup.CardGroupType.HAND) {
                for (AbstractCard c : __instance.group) {
                    if (InactiveHelper.getInactive(c) > 0) {
                        c.glowColor = INACTIVE_GLOW_COLOR.cpy();
                        c.beginGlowing();
                    }
                }
            }
        }
    }
}
