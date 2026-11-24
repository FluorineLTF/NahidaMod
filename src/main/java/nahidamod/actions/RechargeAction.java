package nahidamod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import nahidamod.helpers.InactiveHelper;

public class RechargeAction extends AbstractGameAction {
    private final AbstractCard card;

    public RechargeAction(AbstractCard card) {
        this.card = card;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        if (this.card != null) {
            InactiveHelper.addInactive(this.card, 1);
            this.addToBot(new DiscardToHandAction(this.card));
        }
        this.isDone = true;
    }
}
