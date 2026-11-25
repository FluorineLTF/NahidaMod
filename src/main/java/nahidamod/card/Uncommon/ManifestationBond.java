package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.InspirationPower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class ManifestationBond extends NahidaCard {
    public static final String ID = ModHelper.makePath("ManifestationBond");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/ManifestationBond.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public ManifestationBond() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.ALL_ENEMY, 0);
        this.damage = this.baseDamage = 8;
        this.isMultiDamage = true;
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
        AbstractPower inspiration = p.getPower(InspirationPower.POWER_ID);
        int raw = inspiration != null ? Math.max(0, inspiration.amount) : 0;
        if (raw > 0) {
            this.addToBot(new RemoveSpecificPowerAction(p, p, InspirationPower.POWER_ID));
        }

        int x = Math.max(1, raw);
        for (int i = 0; i < x; i++) {
            this.addToBot(new DamageAllEnemiesAction(
                    p,
                    this.multiDamage,
                    this.damageTypeForTurn,
                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
            ));
        }

        int refund = 6 - x;
        if (refund > 0) {
            this.addToBot(new ApplyPowerAction(p, p, new InspirationPower(p, refund), refund));
        }
    }
}
