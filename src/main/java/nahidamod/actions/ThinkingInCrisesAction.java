package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MayhemPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

public class ThinkingInCrisesAction extends AbstractGameAction {
    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final AbstractMonster targetMonster;

    public ThinkingInCrisesAction(AbstractMonster m) {
        this.duration = 0.0F;
        this.actionType = ActionType.WAIT;
        this.targetMonster = m;
    }

    public void update() {
        if (this.targetMonster != null && this.targetMonster.getIntentBaseDmg() >= 0) {
            if (AbstractDungeon.player.drawPile.isEmpty()){
                this.addToTop(new ThinkingInCrisesAction(this.targetMonster));
                this.addToTop(new EmptyDeckShuffleAction());
                this.isDone = true;
                return;
            }

            AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();
            if (card instanceof NahidaCard && ((NahidaCard) card).inactives > 0){
                AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, "我抽牌堆的顶部牌带有 #r失活 ，不能打出！", true));//对话说明，看情况加，暂时硬编码
                this.isDone = true;
                return;
            }
            this.addToBot(new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), false));
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }

        this.isDone = true;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("OpeningAction");
        TEXT = uiStrings.TEXT;
    }
}
