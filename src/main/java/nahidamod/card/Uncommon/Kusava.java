package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.actions.RechargeAction;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class Kusava extends NahidaCard {
    public static final String ID = ModHelper.makePath("Kusava");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TestPicture.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = EXAMPLE_CARD;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Kusava() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, -1);
        this.damage = this.baseDamage = 8;
        this.magicNumber = this.baseMagicNumber = 8;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new DamageAction(m, new DamageInfo(p,this.damage,this.damageTypeForTurn)));
        //初始化伤害
        if (this.upgraded) {
            this.damage = this.baseDamage = 10;
        } else {
            this.damage = this.baseDamage = 8;
        }
        //调用充能方法
        this.addToTop(new RechargeAction(this));
    }

    //实现无限激化
    @Override
    public void changeInactive() {
        super.changeInactive();
        // 每当触发 actedActionEffect 时，伤害增加M点
        this.addToBot(new ModifyDamageAction(this.uuid, this.magicNumber));
        // 更新卡牌描述
        this.updateDescription();

        //激化完成后，如果失活为0，则改为-1，实现无限激化
        if (this.inactives == 0){
            this.inactives = -1;
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2); // 将该卡牌的伤害提高2点。
            this.upgradeMagicNumber(4); // 升级时增加的伤害
            this.initializeDescription();
        }
    }
}
