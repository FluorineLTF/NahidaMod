package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import nahidamod.actions.RemoveMasterDeckCardAction;
import nahidamod.helpers.ForgetHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class LeavesReturnToRoot extends NahidaCard {
    public static final String ID = ModHelper.makePath("LeavesReturnToRoot");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/LeavesReturnToRoot.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public LeavesReturnToRoot() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.RARE, CardTarget.ENEMY, 0);
        this.damage = this.baseDamage = 12;
        this.exhaust = true;
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
        final DamageInfo info = new DamageInfo(p, this.damage, this.damageTypeForTurn);
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (m != null && !m.isDeadOrEscaped()) {
                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, AttackEffect.SLASH_HEAVY));
                    m.damage(info);
                    if ((m.isDying || m.currentHealth <= 0) && !m.halfDead && !m.hasPower("Minion")) {
                        this.addToBot(new RemoveMasterDeckCardAction());
                    }
                }
                this.isDone = true;
            }
        });
        ForgetHelper.markCardForForget(this);
    }
}
