package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import nahidamod.helpers.ModHelper;

public class RemoveMasterDeckCardAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("RemoveDeckCardUI"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"选择一张牌从你的卡组中永久移除。"};

    public RemoveMasterDeckCardAction() {
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (AbstractDungeon.player == null) {
                this.isDone = true;
                return;
            }
            CardGroup purgeable = AbstractDungeon.player.masterDeck.getPurgeableCards();
            if (purgeable.isEmpty()) {
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(purgeable, 1, TEXT[0], false, false, false, true);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(c, Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F));
                AbstractDungeon.player.masterDeck.removeCard(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.isDone = true;
        }
        this.tickDuration();
    }
}
