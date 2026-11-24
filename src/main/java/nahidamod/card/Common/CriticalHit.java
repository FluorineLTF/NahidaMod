package nahidamod.card.Common;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.red.HeavyBlade;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import nahidamod.actions.ReturnCardToDrawPileAction;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.LeavesAndPetalsPower;
import nahidamod.powers.WisdomPower;

import java.util.Iterator;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class CriticalHit extends NahidaCard {
    public static final String ID = ModHelper.makePath(CriticalHit.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TestPicture.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = EXAMPLE_CARD;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private int temp = 0;

    public CriticalHit() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0);
        this.damage = this.baseDamage = 14;
        this.magicNumber = this.baseMagicNumber = 3;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));

    }

    public void applyPowers() {
        super.applyPowers();

        AbstractPower power = null;
        for (AbstractPower p : AbstractDungeon.player.powers) {
            if (p instanceof WisdomPower){
                power = p;
                if (p.amount <= 0 || temp == p.amount){
                    return;
                }
                temp = power.amount;
            }
        }

        if (power != null) {
            this.damage = this.baseDamage = 14;
            this.upgradeDamage(((power.amount) * this.magicNumber));
//            this.damage += power.amount * this.magicNumber;
        }else {
            temp = 0;
            this.damage = this.baseDamage = 14;
        }
        this.updateDescription();

    }
    @Override
    public void upgrade() {
        // 升级时不改变效果
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
            this.upgradeMagicNumber(2);
            this.updateDescription();
            if (AbstractDungeon.currMapNode == null){
                return;
            }
            if ((AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)) {
                this.applyPowers();
            }
            this.baseDamage = 14;
            temp = 0;
        }
    }
}
