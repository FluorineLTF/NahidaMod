package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.ModHelper;

import java.util.ArrayList;

public class SurpriseAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("SurpriseUI"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"选择 ", " 张抽牌堆中的牌加入手牌（本回合耗能为0）。"};

    private final AbstractPlayer p;

    public SurpriseAction(int amount) {
        this.p = AbstractDungeon.player;
        this.amount = amount;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p == null || this.p.drawPile.isEmpty() || this.amount <= 0) {
                this.isDone = true;
                return;
            }
            if (this.p.drawPile.size() <= this.amount) {
                ArrayList<AbstractCard> snapshot = new ArrayList<>(this.p.drawPile.group);
                for (AbstractCard c : snapshot) {
                    moveToHandFree(c);
                }
                this.p.hand.refreshHandLayout();
                this.isDone = true;
                return;
            }
            CardGroup tmp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.p.drawPile.group) {
                tmp.addToRandomSpot(c);
            }
            AbstractDungeon.gridSelectScreen.open(tmp, this.amount, TEXT[0] + this.amount + TEXT[1], false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.unhover();
                moveToHandFree(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.p.hand.refreshHandLayout();
            this.p.hand.glowCheck();
            this.isDone = true;
        }
        this.tickDuration();
    }

    private void moveToHandFree(AbstractCard c) {
        if (this.p.hand.size() >= basemod.BaseMod.MAX_HAND_SIZE) {
            this.p.drawPile.moveToDiscardPile(c);
            this.p.createHandIsFullDialog();
        } else {
            c.setCostForTurn(0);
            this.p.drawPile.moveToHand(c, this.p.drawPile);
        }
    }
}
