package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import nahidamod.card.Special.AntiSilent;
import nahidamod.card.Special.BorderSandBarrier;
import nahidamod.helpers.ModHelper;
import nahidamod.modcore.NahidaCard;
import nahidamod.powers.GracefulContemplationPower;
import nahidamod.powers.SilencePower;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class GracefulContemplation extends NahidaCard {
    public static final String ID = ModHelper.makePath(GracefulContemplation.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/TestPicture.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = EXAMPLE_CARD;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    public GracefulContemplation() {
        // 为了命名规范修改了变量名。这些参数具体的作用见下方
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET, 0);
        //在卡牌界面可以看见净善的遐歌的预览
        this.cardsToPreview = new AntiSilent();
        if(this.upgraded){this.cardsToPreview.upgrade();}
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName(); // 卡牌名字变为绿色并添加“+”，且标为升级过的卡牌，之后不能再升级。
        }
    }



    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        this.addToBot(new ApplyPowerAction(p, p, new GracefulContemplationPower(p,1), 1));
        this.addToBot(new ApplyPowerAction(p, p, new SilencePower(p,1), 1));
        NahidaCard card = new AntiSilent();
        if (this.upgraded) {
            card.upgrade();
        }
        this.addToBot(new MakeTempCardInHandAction(card,1,false));
    }
}