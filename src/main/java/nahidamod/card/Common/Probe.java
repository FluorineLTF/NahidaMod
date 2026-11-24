package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Probe extends NahidaCard {
    public static final String ID = ModHelper.makePath("Probe");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/Probe.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Probe() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.COMMON, CardTarget.ENEMY, 0);
        this.damage = this.baseDamage = 4;
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
        final DamageInfo info = new DamageInfo(p, this.damage, this.damageTypeForTurn);
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (m != null && !m.isDeadOrEscaped()) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_HORIZONTAL));
                    m.damage(info);
                    if (m.lastDamageTaken > 0) {
                        this.addToTop(new DrawCardAction(p, 1));
                    }
                }
                this.isDone = true;
            }
        });
    }
}
