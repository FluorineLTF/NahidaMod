package nahidamod.card.Special;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.InactiveHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

public class Die extends NahidaCard {
    public static final String ID = ModHelper.makePath("Die");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/Die.png";
    private static final int COST = -2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Die() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE, 3);
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        if (AbstractDungeon.player != null
                && AbstractDungeon.player.hand.contains(this)
                && InactiveHelper.getInactive(this) != 0) {
            this.superFlash();
            AbstractDungeon.actionManager.addToBottom(
                    new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 99999)
            );
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public AbstractCard makeCopy() {
        return new Die();
    }
}
