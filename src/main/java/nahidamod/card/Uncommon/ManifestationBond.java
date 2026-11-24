package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.WisdomPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class ManifestationBond extends NahidaCard {
    public static final String ID = ModHelper.makePath("ManifestationBond");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/ManifestationBond.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public ManifestationBond() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY, 0);
        this.damage = this.baseDamage = 8;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(4);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                AbstractPower wp = p.getPower(WisdomPower.POWER_ID);
                int x = wp != null ? Math.max(0, wp.amount) : 0;
                if (x > 0) {
                    wp.reducePower(x);
                    wp.updateDescription();
                    if (wp.amount <= 0) {
                        p.powers.remove(wp);
                    }
                    for (int i = 0; i < x; i++) {
                        this.addToTop(new DamageAllEnemiesAction(
                                p,
                                DamageInfo.createDamageMatrix(ManifestationBond.this.damage, true),
                                ManifestationBond.this.damageTypeForTurn,
                                AttackEffect.SLASH_HORIZONTAL
                        ));
                    }
                }
                int refund = Math.max(0, 3 - x);
                if (refund > 0) {
                    this.addToBot(new ApplyPowerAction(p, p, new WisdomPower(p, refund), refund));
                }
                this.isDone = true;
            }
        });
    }
}
