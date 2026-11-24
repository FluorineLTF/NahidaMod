package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.InactiveHelper;
import nahidamod.helpers.ModHelper;

import java.util.ArrayList;

public class QuickenDrawPileAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("QuickenDrawUI"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"选择", "张抽牌堆中的牌进行激化。", "激化抽牌堆中的牌"};

    private final AbstractPlayer p;

    public QuickenDrawPileAction(int amount) {
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
                    InactiveHelper.quickenCard(c);
                }
                this.isDone = true;
                return;
            }
            CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard c : this.p.drawPile.group) {
                temp.addToTop(c);
            }
            AbstractDungeon.gridSelectScreen.open(temp, this.amount, TEXT[0] + this.amount + TEXT[1], false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            ArrayList<AbstractCard> selected = new ArrayList<>(AbstractDungeon.gridSelectScreen.selectedCards);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            for (AbstractCard c : selected) {
                c.unhover();
                InactiveHelper.quickenCard(c);
            }
            this.isDone = true;
        }
        this.tickDuration();
    }
}
