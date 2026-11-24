package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.unique.WhirlwindAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.Whirlwind;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;

public class ManifestationAction extends AbstractGameAction {
    public int amount;
    private boolean freeToPlayOnce = false;
    private DamageInfo.DamageType damageType;
    private AbstractPlayer p;
    private int energyOnUse = -1;

    public ManifestationAction(AbstractPlayer p, int amount, DamageInfo.DamageType damageType , boolean freeToPlayOnce , int energyOnUse) {
        this.p = p;
        this.amount = amount;
        this.damageType = DamageInfo.DamageType.NORMAL;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        boolean hasBoot = false;

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }

        if (this.p.hasRelic("Boot") && effect >= 5) {
            hasBoot = true;
            effect = 5;
        }

        if (this.p.hasPower("Vigor")){
            effect += this.p.getPower("Vigor").amount;
        }

        if (effect >= 0) {
            for(int i = 0; i < amount; ++i) {
                this.addToBot(new SFXAction("ATTACK_HEAVY"));
                this.addToBot(new VFXAction(this.p, new CleaveEffect(), 0.0F));
                this.addToBot(new DamageAllEnemiesAction(this.p, effect, this.damageType, AttackEffect.FIRE));
                if (hasBoot){
                    this.p.getRelic("Boot").flash();
                    this.addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this.p.getRelic("Boot")));
                }
            }

            if (!this.freeToPlayOnce) {
                this.p.energy.use(EnergyPanel.totalCount);
            }
        }

        this.isDone = true;
    }
}
