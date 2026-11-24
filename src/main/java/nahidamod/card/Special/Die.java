package nahidamod.card.Special;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.purple.FollowUp;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import com.megacrit.cardcrawl.relics.OrangePellets;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;

public class Die extends NahidaCard {
    public static final String ID = ModHelper.makePath(Die.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/ForbiddenKnowledge.png";
    private static final int COST = -2;
    private static final CardType TYPE = CardType.CURSE;
    private static final CardColor COLOR = CardColor.CURSE;
    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public Die() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 3);
        this.magicNumber = 99999;
        this.baseMagicNumber = 99999;
    }

    public void upgrade() {
        super.upgrade();
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.dontTriggerOnUseCard) {
//            int[] num = {0,0};
//            boolean[] has = {false,false};
//            for (AbstractPower power : AbstractDungeon.player.powers) {
//                if (power instanceof IntangiblePlayerPower){
//                    num[0] = power.amount;
//                    this.addToBot(new RemoveSpecificPowerAction(p,p,power));
//                    has[0] = true;
//                }
//                if (power instanceof BufferPower){
//                    num[1] = power.amount;
//                    this.addToBot(new RemoveSpecificPowerAction(p,p,power));
//                    has[1] = true;
//                }
//            }
//            this.addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, this.magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.FIRE));

            this.addToBot(new VFXAction(new LightningEffect(p.hb.cX,p.hb.cY)));
            this.addToBot(new LoseHPAction(p,p, this.magicNumber));
//            if (has[0] && !p.isDead) {
//                this.addToBot(new ApplyPowerAction(p, p, new IntangiblePlayerPower(p, num[0]), num[0]));//返还无实体，如果玩家存活
//            }
//            if (has[1] && !p.isDead) {
//                this.addToBot(new ApplyPowerAction(p, p, new BufferPower(p, num[1]), num[1]));//返还缓冲，如果玩家存活
//            }
        }

    }

    public void triggerOnEndOfTurnForPlayingCard() {
        if (this.inactives == 0) {
            this.dontTriggerOnUseCard = true;
            AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
        }
    }

//    @Override
//    public void changeInactive() {
//        super.changeInactive();
//        //为失活数归零时更新描述
//        if (this.inactives == 0) {
//            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
//            this.initializeDescription();
//        }
//    }

    public void updateDescription() {
        if (this.inactives <= 0) {
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
        } else {
            this.rawDescription = CARD_STRINGS.DESCRIPTION;
            this.rawDescription = this.rawDescription + TEXT[0] + inactives + TEXT[2];
        }
        this.initializeDescription();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (this.inactives > 0){
            this.cantUseMessage = TEXT[1];
        }else {
            this.cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];//优化不能打出时的文本框
        }

        return super.canUse(p, m);
    }
}
