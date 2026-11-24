package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import nahidamod.card.Special.EscalatingRage;
import nahidamod.card.Special.WisdomSharpen;
import nahidamod.modcore.NahidaCard;

import javax.smartcardio.Card;

public class AkashaPulsesAction extends AbstractGameAction {
    private boolean freeToPlayOnce;
    private AbstractPlayer p;
    private int energyOnUse;
    private NahidaCard card;

    public AkashaPulsesAction(AbstractPlayer p, NahidaCard card, boolean freeToPlayOnce, int energyOnUse) {
        this.p = p;
        this.freeToPlayOnce = freeToPlayOnce;
        this.duration = Settings.ACTION_DUR_XFAST;
        this.actionType = ActionType.SPECIAL;
        this.energyOnUse = energyOnUse;
        this.card = card ;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }

        if (this.p.hasRelic("Chemical X")) {
            effect += 2;
            this.p.getRelic("Chemical X").flash();
        }
        /*WisdomSharpen c = new WisdomSharpen();
        c.setX(effect);
        if (effect > 0 || AbstractDungeon.player.hasPower("MasterRealityPower")) {
            this.addToBot(new MakeTempCardInDrawPileAction(c, 1, true, true));
        }else {
            CardStrings desc = CardCrawlGame.languagePack.getCardStrings(c.cardID);
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, desc.EXTENDED_DESCRIPTION[2], true));//对话说明，看情况加
        }
        if (!this.freeToPlayOnce) {
            this.p.energy.use(EnergyPanel.totalCount);
        }*/

        this.addToBot(new MakeTempCardInDrawPileAction(card, effect, true, true));
        if (!this.freeToPlayOnce) {
            this.p.energy.use(EnergyPanel.totalCount);
        }

        this.isDone = true;
    }
}
