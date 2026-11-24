package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class ConsciousnessDetach extends NahidaCard {
    public static final String ID = ModHelper.makePath("ConsciousnessDetach");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/ConsciousnessDetach.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public ConsciousnessDetach() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.SKILL, EXAMPLE_CARD, CardRarity.RARE, CardTarget.ENEMY, 0);
        this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (m != null && !m.isDeadOrEscaped()) {
                    AbstractPower weak = m.getPower(WeakPower.POWER_ID);
                    int weakAmt = weak != null ? Math.max(0, weak.amount) : 0;
                    if (weakAmt > 0) {
                        this.addToTop(new ApplyPowerAction(m, p, new StrengthPower(m, -weakAmt), -weakAmt));
                    }
                }
                this.isDone = true;
            }
        });
    }
}
