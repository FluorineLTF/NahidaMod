package nahidamod.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;

public class WorldForgetMeAction_Abandon extends AbstractGameAction {
    private AbstractPlayer p;
    private ArrayList<AbstractCard> cards = new ArrayList<>();


    public WorldForgetMeAction_Abandon() {
        this.p = AbstractDungeon.player;
        this.duration = Settings.ACTION_DUR_MED;
        this.actionType = ActionType.EXHAUST;
    }
    @Override
    public void update() {
        if (p.discardPile.isEmpty()) {
            this.isDone = true;
            return;
        }
        for (AbstractCard c : p.discardPile.group) {
            cards.add(c);
        }
        for (int i = cards.size() - 1; i >= 0; i--) {
            exhaustCard(cards.get(i));
        }
        this.isDone = true;
    }


    private void exhaustCard (AbstractCard c){
        c.target_x = MathUtils.random((float)Settings.WIDTH * 0.1F, (float)Settings.WIDTH * 0.9F);
        c.target_y = MathUtils.random((float)Settings.HEIGHT * 0.8F, (float)Settings.HEIGHT * 0.2F);//创建卡牌随机位置
        AbstractDungeon.topLevelEffectsQueue.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(),c.target_x, c.target_y));//展示卡牌
        this.p.discardPile.moveToExhaustPile(c);
        this.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
    }

}