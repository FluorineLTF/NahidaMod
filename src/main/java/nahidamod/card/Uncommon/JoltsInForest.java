package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.helpers.DendroCoreHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.DendroCorePower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class JoltsInForest extends NahidaCard {
    public static final String ID = ModHelper.makePath("JoltsInForest");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/JoltsInForest.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public JoltsInForest() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.ENEMY, 0);
        this.damage = this.baseDamage = 9;
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
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int cores = DendroCoreHelper.consumeAllDendroCores();
                int hyperbloomDmg = (int) Math.round(DendroCorePower.BASE_DAMAGE * 1.5);
                for (int i = 0; i < cores; i++) {
                    this.addToBot(new DamageAction(m, new DamageInfo(p, hyperbloomDmg, DamageInfo.DamageType.THORNS), AttackEffect.LIGHTNING));
                }
                this.isDone = true;
            }
        });
    }
}
