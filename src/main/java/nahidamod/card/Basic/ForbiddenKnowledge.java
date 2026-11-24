package nahidamod.card.Basic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.InactiveHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import java.util.ArrayList;

public class ForbiddenKnowledge extends NahidaCard {
    public static final String ID = ModHelper.makePath("ForbiddenKnowledge");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/ForbiddenKnowledge.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public ForbiddenKnowledge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.CURSE, CardColor.CURSE, CardRarity.CURSE, CardTarget.SELF, 0);
        this.magicNumber = this.baseMagicNumber = 2;
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new GainEnergyAction(this.magicNumber));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ArrayList<AbstractCard> hand = p.hand.group;
                if (!hand.isEmpty()) {
                    AbstractCard target = hand.get(AbstractDungeon.cardRandomRng.random(hand.size() - 1));
                    InactiveHelper.addInactive(target, 1);
                }
                this.isDone = true;
            }
        });
        this.addToBot(new MakeTempCardInDiscardAction(this.makeStatEquivalentCopy(), 1));
    }
}
