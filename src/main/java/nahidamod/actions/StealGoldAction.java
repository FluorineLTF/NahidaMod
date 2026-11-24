package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.city.Mugger;
import com.megacrit.cardcrawl.monsters.exordium.Looter;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

import java.lang.reflect.Field;


public class StealGoldAction extends AbstractGameAction {

    private DamageInfo info;

    public StealGoldAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = AbstractGameAction.ActionType.DAMAGE;
        this.startDuration = Settings.ACTION_DUR_FAST;
        this.duration = this.startDuration;
    }

    @Override
    public void update() {
        if (this.shouldCancelAction()) {
            this.isDone = true;
        } else {
            this.tickDuration();
            if (this.isDone) {
                AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_DIAGONAL, false));
                this.target.damage(this.info);
                if (this.target.lastDamageTaken > 0) {
                    AbstractDungeon.player.gainGold(this.target.lastDamageTaken);
                    for(int i = 0; i < this.target.lastDamageTaken; ++i) {
                        AbstractDungeon.effectList.add(new GainPennyEffect(this.source, this.target.hb.cX, this.target.hb.cY, this.source.hb.cX, this.source.hb.cY, true));
                    }
                    if (this.target.hb != null) {
                        this.addToTop(new VFXAction(new WallopEffect(this.target.lastDamageTaken, this.target.hb.cX, this.target.hb.cY)));
                    }
                }
//
//                Looter looter = new Looter(x(this.target.drawX),y(this.target.drawY));
//                if (this.target.equals(looter)){
//                    looter = (Looter) this.target;
//                    Class c = looter.getClass();
//                    try {
//                        Field f = c.getDeclaredField("stolenGold");
//                        f.setAccessible(true);
//
//                    } catch (NoSuchFieldException e) {
//                        throw new RuntimeException(e);
//                    }
//                }



                if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                    AbstractDungeon.actionManager.clearPostCombatActions();
                } else {
                    this.addToTop(new WaitAction(0.1F));
                }
            }
        }
    }
//
//    private float x (float x) {
//        return (x - (float)Settings.WIDTH * 0.75F )/Settings.xScale;
//    }
//    private float y (float y) {
//        return (y - AbstractDungeon.floorY)/Settings.yScale;
//    }
}