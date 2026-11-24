package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class EndlessBubbles extends NahidaCard {
    public static final String ID = ModHelper.makePath("EndlessBubbles");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/EndlessBubbles.png";
    private static final int COST = -1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public EndlessBubbles() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.ENEMY, 0);
        this.damage = this.baseDamage = 5;
        this.magicNumber = this.baseMagicNumber = 1;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (p.hasRelic("Chemical X")) {
            effect += 2;
            p.getRelic("Chemical X").flash();
        }
        AbstractPower wp = p.getPower(WisdomPower.POWER_ID);
        int wisdomBonus = wp != null ? Math.max(0, wp.amount) : 0;
        int totalHits = Math.max(0, effect + wisdomBonus);

        if (!this.freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }

        for (int i = 0; i < totalHits; i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            this.addToBot(new ApplyPowerAction(m, p, new WeakPower(m, this.magicNumber, false), this.magicNumber));
        }
    }
}
