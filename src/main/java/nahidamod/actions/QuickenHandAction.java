package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import nahidamod.helpers.InactiveHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.interfaces.OnQuickenCard;

import java.util.ArrayList;

public class QuickenHandAction extends AbstractGameAction {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("QuickenUI"));
    private static final String[] TEXT = uiStrings != null ? uiStrings.TEXT : new String[]{"激化"};
    private static final UIStrings noTargetUiStrings = CardCrawlGame.languagePack.getUIString(ModHelper.makePath("QuickenNoTargetUI"));
    private static final String TEXT_NO_TARGET = noTargetUiStrings != null && noTargetUiStrings.TEXT != null && noTargetUiStrings.TEXT.length > 0
            ? noTargetUiStrings.TEXT[0] : "我不需要激化卡牌";

    private final AbstractPlayer p;
    private final boolean allHand;
    private final ArrayList<AbstractCard> cannotQuicken = new ArrayList<>();

    public QuickenHandAction(int amount) {
        this(amount, false);
    }

    public QuickenHandAction(boolean allHand) {
        this(0, allHand);
    }

    private QuickenHandAction(int amount, boolean allHand) {
        this.p = AbstractDungeon.player;
        this.amount = amount;
        this.allHand = allHand;
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

            if (this.allHand) {
                ArrayList<AbstractCard> snapshot = new ArrayList<>(this.p.hand.group);
                for (AbstractCard c : snapshot) {
                    InactiveHelper.quickenCard(c);
                }
                this.isDone = true;
                return;
            }

            ArrayList<AbstractCard> valid = new ArrayList<>();
            this.cannotQuicken.clear();
            for (AbstractCard c : this.p.hand.group) {
                if (InactiveHelper.getInactive(c) > 0
                        || c instanceof OnQuickenCard
                        || c.type == AbstractCard.CardType.ATTACK) {
                    valid.add(c);
                } else {
                    this.cannotQuicken.add(c);
                }
            }

            if (valid.isEmpty()) {
                AbstractDungeon.effectList.add(new ThoughtBubble(this.p.dialogX, this.p.dialogY, 3.0F, TEXT_NO_TARGET, true));
                this.isDone = true;
                return;
            }

            if (valid.size() <= this.amount) {
                for (AbstractCard c : valid) {
                    InactiveHelper.quickenCard(c);
                }
                this.isDone = true;
                return;
            }

            for (AbstractCard c : this.cannotQuicken) {
                this.p.hand.group.remove(c);
            }
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, false, false, false, false, false);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
                InactiveHelper.quickenCard(c);
                this.p.hand.addToTop(c);
            }
            if (!this.cannotQuicken.isEmpty()) {
                this.p.hand.group.addAll(this.cannotQuicken);
                this.cannotQuicken.clear();
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
