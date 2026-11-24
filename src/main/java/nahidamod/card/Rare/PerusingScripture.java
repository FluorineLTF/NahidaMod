package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class PerusingScripture extends NahidaCard {
    public static final String ID = ModHelper.makePath("PerusingScripture");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/PerusingScripture.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public PerusingScripture() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.RARE, CardTarget.ALL_ENEMY, 0);
        this.damage = this.baseDamage = 18;
        this.magicNumber = this.baseMagicNumber = 1;
        this.isMultiDamage = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(6);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower wp = p.getPower(WisdomPower.POWER_ID);
                if (wp != null && wp.amount >= 1) {
                    wp.reducePower(1);
                    wp.updateDescription();
                    if (wp.amount <= 0) {
                        p.powers.remove(wp);
                    }
                    this.addToTop(new DamageAllEnemiesAction(p, PerusingScripture.this.multiDamage, PerusingScripture.this.damageTypeForTurn, AttackEffect.SLASH_HORIZONTAL));
                    if (AbstractDungeon.getCurrRoom() != null && AbstractDungeon.getCurrRoom().monsters != null) {
                        for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
                            if (!mo.isDeadOrEscaped()) {
                                this.addToBot(new ApplyPowerAction(mo, p, new WeakPower(mo, PerusingScripture.this.magicNumber, false), PerusingScripture.this.magicNumber));
                            }
                        }
                    }
                }
                this.isDone = true;
            }
        });
    }
}
