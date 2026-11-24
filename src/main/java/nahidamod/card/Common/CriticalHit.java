package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.InspirationPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class CriticalHit extends NahidaCard {
    public static final String ID = ModHelper.makePath("CriticalHit");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/CriticalHit.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public CriticalHit() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.COMMON, CardTarget.ENEMY, 0);
        this.damage = this.baseDamage = 12;
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void applyPowers() {
        int realBase = this.baseDamage;
        this.baseDamage += InspirationPower.totalGainedThisCombat * this.magicNumber;
        super.applyPowers();
        this.baseDamage = realBase;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = this.baseDamage;
        this.baseDamage += InspirationPower.totalGainedThisCombat * this.magicNumber;
        super.calculateCardDamage(mo);
        this.baseDamage = realBase;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
            this.upgradeMagicNumber(1);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    }
}
