package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import nahidamod.helpers.DendroCoreHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Paranoia extends NahidaCard {
    public static final String ID = ModHelper.makePath("Paranoia");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/Paranoia.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Paranoia() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.COMMON, CardTarget.ENEMY, 0);
        this.damage = this.baseDamage = 7;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(3);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().monsters != null) {
            for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (!mo.isDeadOrEscaped() && mo.hasPower(WeakPower.POWER_ID)) {
                    this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
                    break;
                }
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        if (m != null && m.hasPower(WeakPower.POWER_ID)) {
            DendroCoreHelper.spawnDendroCore(1);
        }
    }
}
