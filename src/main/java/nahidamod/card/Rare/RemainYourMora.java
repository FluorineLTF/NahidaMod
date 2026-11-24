package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class RemainYourMora extends NahidaCard {
    public static final String ID = ModHelper.makePath("RemainYourMora");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/RemainYourMora.png";
    private static final int COST = 1;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public RemainYourMora() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.RARE, CardTarget.ENEMY, 0);
        this.damage = this.baseDamage = 5;
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
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_DIAGONAL));
                    m.damage(info);
                    int gold = m.lastDamageTaken;
                    if (gold > 0) {
                        p.gainGold(gold);
                        for (int i = 0; i < Math.min(gold, 20); i++) {
                            AbstractDungeon.effectList.add(new GainPennyEffect(p, m.hb.cX, m.hb.cY, p.hb.cX, p.hb.cY, true));
                        }
                    }
                }
                this.isDone = true;
            }
        });
    }
}
