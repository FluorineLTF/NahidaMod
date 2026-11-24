package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.purple.FlurryOfBlows;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ReturnEToHandAction extends AbstractGameAction {

    @Override
    public void update() {
        // 遍历弃牌堆，查找ID为 "NahidaMod:E" 的卡牌
        for (AbstractCard card : AbstractDungeon.player.discardPile.group) {
            if (card.cardID.equals("NahidaMod:E")) {
                // 从弃牌堆移除并添加到手牌
                this.addToBot(new DiscardToHandAction(card));
            }
        }
        this.isDone = true;
    }}

