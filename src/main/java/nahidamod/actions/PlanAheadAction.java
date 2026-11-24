package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.NextBattleManager;

public class PlanAheadAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("PlanAheadUI"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"选择一张牌使其在下一场战斗获得固有"};

    private final AbstractPlayer p;

    public PlanAheadAction() {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p == null || this.p.hand.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (this.p.hand.size() == 1) {
                AbstractCard c = this.p.hand.getTopCard();
                NextBattleManager.INSTANCE.addNextBattleInnate(c.uuid);
                c.superFlash();
                this.isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false, false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                NextBattleManager.INSTANCE.addNextBattleInnate(c.uuid);
                c.superFlash();
                this.p.hand.addToTop(c);
            }
            this.p.hand.refreshHandLayout();
            this.p.hand.glowCheck();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
            this.isDone = true;
        }
        this.tickDuration();
    }
}
