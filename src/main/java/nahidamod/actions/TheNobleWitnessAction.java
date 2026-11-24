package nahidamod.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.unique.MadnessAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import java.util.Iterator;

public class TheNobleWitnessAction extends AbstractGameAction {

    public static final String UI_ID = ModHelper.makePath("TheNobleWitnessAction");
    private static final UIStrings ui_ID = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT=ui_ID.TEXT;
    private AbstractPlayer p;
    private AbstractCard c;

    public TheNobleWitnessAction() {
        this.p = AbstractDungeon.player;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            if (this.p.hand.isEmpty()) {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(true, TEXT[1], 2.4F, 2.4F));
                this.isDone = true;
                return;
            }
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], 1, false, false, false, false, true);
            this.tickDuration();
            return;
        }
        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            for (Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator(); var1.hasNext(); AbstractDungeon.player.hand.addToTop(c)) {
                c = (AbstractCard) var1.next();
                if (c.cost > 0) {
                    c.cost = 0;
                    c.costForTurn = 0;
                    c.isCostModified = true;
                }
            }

            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
        }

        this.tickDuration();
    }
}
