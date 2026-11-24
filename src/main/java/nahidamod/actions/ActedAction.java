package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.unique.DualWieldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.DualWield;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import java.util.ArrayList;
import java.util.Iterator;

public class ActedAction extends AbstractGameAction {
    public static final String UI_ID = ModHelper.makePath("ActedAction");
    private static final UIStrings ui_ID = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT=ui_ID.TEXT;
    private AbstractPlayer p;
    private boolean isRandom;
    private boolean anyNumber;
    private boolean canPickZero;
    public static int numActed;
    private ArrayList<AbstractCard> cards = new ArrayList<>();
    private ArrayList<AbstractCard> dontNeed = new ArrayList<>();

    public ActedAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero) {
        this.anyNumber = anyNumber;
        this.p = AbstractDungeon.player;
        this.canPickZero = canPickZero;
        this.isRandom = isRandom;
        this.amount = amount;
        this.duration = this.startDuration = Settings.ACTION_DUR_FAST;
    }
    public ActedAction(int amount) {
        this(amount, false, false, false);
    }
    public ActedAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom, boolean anyNumber) {
        this(amount, isRandom, anyNumber);
        this.target = target;
        this.source = source;
    }
    public ActedAction(AbstractCreature target, AbstractCreature source, int amount, boolean isRandom) {
        this(amount, isRandom, false, false);
        this.target = target;
        this.source = source;
    }
    public ActedAction(int amount, boolean isRandom, boolean anyNumber, boolean canPickZero, float duration) {
        this(amount, isRandom, anyNumber, canPickZero);
        this.duration = this.startDuration = duration;
    }
    public ActedAction(int amount, boolean canPickZero) {
        this(amount, false, false, canPickZero);
    }

    public ActedAction(int amount, boolean isRandom, boolean anyNumber) {
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
//            if (this.p.hand.isEmpty()) {
//                AbstractDungeon.actionManager.addToBottom(new TalkAction(true, TEXT[1], 2.4F, 2.4F));
//                this.isDone = true;
//                return;
//            }

//            int i;
//            if (!this.anyNumber && this.p.hand.size() < this.amount) {
//                this.amount = this.p.hand.size();
//                numActed = this.amount;
//                i = this.p.hand.size();
//
//                for(int j = 0; j < i; ++j) {
//                    if (this.p.hand.getNCardFromTop(j) instanceof NahidaCard) {
//                        NahidaCard c = (NahidaCard) this.p.hand.getNCardFromTop(j);
//                        c.changeInactive();
//                    }
//                }
//                this.tickDuration();
//                return;
//            }

//            ArrayList<NahidaCard> cards = new ArrayList<>();

            for (AbstractCard c : this.p.hand.group) {
                if (c instanceof NahidaCard && ((NahidaCard) c).inactives != 0) {
                    cards.add((NahidaCard) c);
                }else {
                    dontNeed.add(c);
                }
            }

            if (cards.isEmpty()) {
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[1], true));//对话说明，看情况加
                this.isDone = true;
                return;
            }

            if (!anyNumber && cards.size() <= this.amount) {
                for (AbstractCard c : cards) {
                    ((NahidaCard)c).changeInactive();
                }
                this.tickDuration();
                this.isDone = true;
                return;
            }



            if (this.isRandom) {
                for (int i = 0; i < amount ; i++) {//随机激化
                    NahidaCard c = (NahidaCard) cards.get(AbstractDungeon.miscRng.random(0, cards.size() - 1));
                    c.changeInactive();
                    cards.remove(c);
                }
                this.tickDuration();
                this.isDone = true;
                return;
            }

            numActed = this.amount;
            this.p.hand.group.removeAll(this.dontNeed);
            AbstractDungeon.handCardSelectScreen.open(TEXT[0], this.amount, this.anyNumber, this.canPickZero, false, false, true);
            this.tickDuration();
            return;
        }

        if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
            AbstractCard c;
            for(Iterator var1 = AbstractDungeon.handCardSelectScreen.selectedCards.group.iterator(); var1.hasNext(); AbstractDungeon.player.hand.addToTop(c))  {
                c = (AbstractCard) var1.next();
                if (c instanceof NahidaCard) {
                    NahidaCard c2 = (NahidaCard) c;
                    c2.changeInactive();
                }
            }
            this.returnCards();
            AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
            AbstractDungeon.handCardSelectScreen.selectedCards.group.clear();
        }

        this.tickDuration();
    }

    private void returnCards() {
        Iterator var1 = this.dontNeed.iterator();

        while(var1.hasNext()) {
            AbstractCard c = (AbstractCard)var1.next();
            this.p.hand.addToTop(c);
        }

        this.p.hand.refreshHandLayout();
    }

}
