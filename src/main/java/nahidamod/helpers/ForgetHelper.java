package nahidamod.helpers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.UUID;

public class ForgetHelper {
    private static final LinkedHashSet<UUID> pendingForgetUuids = new LinkedHashSet<>();

    public static void clear() {
        pendingForgetUuids.clear();
    }

    /**
     * 仅当该牌真实存在于主卡组中时才登记“遗忘”，局内生成的临时复制品只消耗、不触发战后奖励。
     */
    public static void markCardForForget(AbstractCard card) {
        if (card == null || AbstractDungeon.player == null || AbstractDungeon.player.masterDeck == null) {
            return;
        }
        for (AbstractCard deckCard : AbstractDungeon.player.masterDeck.group) {
            if (deckCard.uuid.equals(card.uuid)) {
                pendingForgetUuids.add(card.uuid);
                return;
            }
        }
    }

    public static void onPostBattle(AbstractRoom room) {
        if (pendingForgetUuids.isEmpty() || AbstractDungeon.player == null || room == null) {
            pendingForgetUuids.clear();
            return;
        }

        ArrayList<AbstractCard> toRemove = new ArrayList<>();
        for (UUID uuid : pendingForgetUuids) {
            for (AbstractCard deckCard : AbstractDungeon.player.masterDeck.group) {
                if (deckCard.uuid.equals(uuid)) {
                    toRemove.add(deckCard);
                    break;
                }
            }
        }

        for (AbstractCard removed : toRemove) {
            AbstractDungeon.player.masterDeck.removeCard(removed);
            RewardItem reward = new RewardItem();
            reward.cards.clear();
            reward.cards.add(removed.makeStatEquivalentCopy());
            room.rewards.add(reward);
        }

        pendingForgetUuids.clear();
    }
}
