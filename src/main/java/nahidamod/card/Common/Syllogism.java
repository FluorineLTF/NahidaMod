package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Syllogism extends NahidaCard {
    public static final String ID = ModHelper.makePath("Syllogism");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/Syllogism.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Syllogism() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.COMMON, CardTarget.ENEMY, 0);
        this.damage = this.baseDamage = 3;
        this.magicNumber = this.baseMagicNumber = 3;
    }

    private int getWisdomBonus() {
        if (AbstractDungeon.player == null) {
            return 0;
        }
        AbstractPower p = AbstractDungeon.player.getPower(WisdomPower.POWER_ID);
        return p != null ? Math.max(0, p.amount) : 0;
    }

    @Override
    public void applyPowers() {
        int realBase = this.baseDamage;
        this.baseDamage += getWisdomBonus();
        super.applyPowers();
        this.baseDamage = realBase;
        this.isDamageModified = this.damage != this.baseDamage;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int realBase = this.baseDamage;
        this.baseDamage += getWisdomBonus();
        super.calculateCardDamage(mo);
        this.baseDamage = realBase;
        this.isDamageModified = this.damage != this.baseDamage;
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
        for (int i = 0; i < this.magicNumber; i++) {
            this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
        }
    }
}
