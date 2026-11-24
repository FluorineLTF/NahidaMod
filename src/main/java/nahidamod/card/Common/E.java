package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.DiscardToHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.interfaces.OnAnyCardQuickenedListener;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.NahidaMarkPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class E extends NahidaCard implements OnAnyCardQuickenedListener {
    public static final String ID = ModHelper.makePath("E");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/E.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public E() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.COMMON, CardTarget.ENEMY, 0);
        this.magicNumber = this.baseMagicNumber = 1;
        this.hasUPGRADE_DESCRIPTION = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        final int hpLoss = this.upgraded ? 7 : 5;
        this.addToBot(new ApplyPowerAction(m, p, new NahidaMarkPower(m, this.magicNumber), this.magicNumber));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                    if (!mo.isDeadOrEscaped()) {
                        AbstractPower mark = mo.getPower(NahidaMarkPower.POWER_ID);
                        if (mark instanceof NahidaMarkPower) {
                            ((NahidaMarkPower) mark).triggerFixedLoss(hpLoss);
                        }
                    }
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void onAnyCardQuickened(AbstractCard quickenedCard) {
        if (AbstractDungeon.player != null && AbstractDungeon.player.discardPile.contains(this)) {
            this.addToBot(new DiscardToHandAction(this));
        }
    }
}
