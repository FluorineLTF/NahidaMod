package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.powers.NextTurnCopyCardPower;

public class WhispersLingerAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("WhispersLingerUI"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"选择一张弃牌堆中的牌消耗，并在下回合开始时将其复制品加入手牌。"};

    private final AbstractPlayer p;

    public WhispersLingerAction() {
        this.p = AbstractDungeon.player;
        this.actionType = ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (this.p == null || this.p.discardPile.isEmpty()) {
                this.isDone = true;
                return;
            }
            if (this.p.discardPile.size() == 1) {
                processCard(this.p.discardPile.getTopCard());
                this.isDone = true;
                return;
            }
            AbstractDungeon.gridSelectScreen.open(this.p.discardPile, 1, TEXT[0], false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
                c.unhover();
                processCard(c);
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            this.isDone = true;
        }
        this.tickDuration();
    }

    private void processCard(AbstractCard c) {
        AbstractCard copy = c.makeStatEquivalentCopy();
        this.p.discardPile.moveToExhaustPile(c);
        AbstractPower existing = this.p.getPower(NextTurnCopyCardPower.POWER_ID);
        if (existing instanceof NextTurnCopyCardPower) {
            ((NextTurnCopyCardPower) existing).addCard(copy);
            existing.flash();
        } else {
            this.addToBot(new ApplyPowerAction(this.p, this.p, new NextTurnCopyCardPower(this.p, copy), 1));
        }
    }
}
