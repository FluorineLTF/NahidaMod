package nahidamod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 被激化的攻击牌在下一次打出前获得额外伤害。
 * 加成在伤害计算时临时写入 baseDamage，计算完成后立即还原，
 * 因此不会影响卡牌的原始数值或永久升级数据。
 */
public class QuickenAttackBonusPatch {
    @SpirePatch(clz = AbstractCard.class, method = SpirePatch.CLASS)
    public static class QuickenBonusField {
        public static SpireField<Integer> quickenDamageBonus = new SpireField<>(() -> 0);
    }

    private static final ThreadLocal<Integer> ORIGINAL_BASE_DAMAGE = new ThreadLocal<>();

    public static void addBonus(AbstractCard card, int amount) {
        if (card == null || amount == 0) {
            return;
        }
        Integer current = QuickenBonusField.quickenDamageBonus.get(card);
        int base = current == null ? 0 : Math.max(0, current);
        QuickenBonusField.quickenDamageBonus.set(card, Math.max(0, base + amount));
    }

    public static int getBonus(AbstractCard card) {
        if (card == null) {
            return 0;
        }
        Integer bonus = QuickenBonusField.quickenDamageBonus.get(card);
        return bonus == null ? 0 : Math.max(0, bonus);
    }

    public static void resetBonus(AbstractCard card) {
        if (card != null) {
            QuickenBonusField.quickenDamageBonus.set(card, 0);
        }
    }

    private static void applyTemporaryBonus(AbstractCard card) {
        if (card == null || getBonus(card) <= 0 || ORIGINAL_BASE_DAMAGE.get() != null) {
            return;
        }
        ORIGINAL_BASE_DAMAGE.set(card.baseDamage);
        card.baseDamage += getBonus(card);
    }

    private static void restoreBaseDamage(AbstractCard card) {
        Integer original = ORIGINAL_BASE_DAMAGE.get();
        if (original == null) {
            return;
        }
        card.baseDamage = original;
        card.isDamageModified = card.damage != card.baseDamage;
        ORIGINAL_BASE_DAMAGE.remove();
    }

    @SpirePatch(clz = AbstractCard.class, method = "applyPowers")
    public static class ApplyPowersPatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance) {
            applyTemporaryBonus(__instance);
        }

        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance) {
            restoreBaseDamage(__instance);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage")
    public static class CalculateCardDamagePatch {
        @SpirePrefixPatch
        public static void Prefix(AbstractCard __instance, AbstractMonster __0) {
            applyTemporaryBonus(__instance);
        }

        @SpirePostfixPatch
        public static void Postfix(AbstractCard __instance, AbstractMonster __0) {
            restoreBaseDamage(__instance);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "useCard")
    public static class ResetAfterUsePatch {
        @SpirePostfixPatch
        public static void Postfix(AbstractPlayer __instance, AbstractCard c, AbstractMonster m, int energyOnUse) {
            if (getBonus(c) > 0) {
                resetBonus(c);
            }
        }
    }
}
