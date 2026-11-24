package nahidamod.card.Rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.helpers.ForgetHelper;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import java.util.ArrayList;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class WorldForgetMe extends NahidaCard {
    public static final String ID = ModHelper.makePath("WorldForgetMe");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/WorldForgetMe.png";
    private static final int COST = 3;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public WorldForgetMe() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.ATTACK, EXAMPLE_CARD, CardRarity.RARE, CardTarget.ENEMY, 0);
        this.damage = this.baseDamage = 24;
        this.exhaust = true;
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(8);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                ArrayList<AbstractPower> debuffs = new ArrayList<>();
                for (AbstractPower pow : p.powers) {
                    if (pow.type == AbstractPower.PowerType.DEBUFF) {
                        debuffs.add(pow);
                    }
                }
                for (AbstractPower debuff : debuffs) {
                    this.addToTop(new RemoveSpecificPowerAction(p, p, debuff.ID));
                }

                exhaustStatusAndCurse(p.hand);
                exhaustStatusAndCurse(p.drawPile);
                exhaustStatusAndCurse(p.discardPile);
                p.hand.refreshHandLayout();
                p.hand.glowCheck();
                this.isDone = true;
            }

            private void exhaustStatusAndCurse(CardGroup group) {
                ArrayList<AbstractCard> targets = new ArrayList<>();
                for (AbstractCard c : group.group) {
                    if (c.type == CardType.STATUS || c.type == CardType.CURSE) {
                        targets.add(c);
                    }
                }
                for (AbstractCard c : targets) {
                    group.moveToExhaustPile(c);
                }
            }
        });
        ForgetHelper.markCardForForget(this);
    }
}
