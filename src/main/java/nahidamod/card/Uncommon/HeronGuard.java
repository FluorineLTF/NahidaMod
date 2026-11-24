package nahidamod.card.Uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import nahidamod.card.Special.BorderSandBarrier;
import nahidamod.helpers.ModHelper;
import nahidamod.helpers.TextureLoader;
import nahidamod.modcore.NahidaCard;

import static nahidamod.Characters.MyCharacter.Enums.EXAMPLE_CARD;

public class HeronGuard extends NahidaCard {
    public static final String ID = ModHelper.makePath("HeronGuard");
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = CARD_STRINGS.NAME;
    private static final String IMG_PATH = "NahidaResources/images/cards/HeronGuard.png";
    private static final int COST = 2;
    private static final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;

    public HeronGuard() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.POWER, EXAMPLE_CARD, CardRarity.UNCOMMON, CardTarget.SELF, 0);
        this.hasUPGRADE_DESCRIPTION = true;
        this.cardsToPreview = new BorderSandBarrier();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(1);
            AbstractCard preview = new BorderSandBarrier();
            preview.upgrade();
            this.cardsToPreview = preview;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        final boolean isUpgraded = this.upgraded;
        AbstractCard barrier = new BorderSandBarrier();
        if (isUpgraded) {
            barrier.upgrade();
        }
        this.addToBot(new MakeTempCardInHandAction(barrier, 1));
        this.addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                p.powers.add(new com.megacrit.cardcrawl.powers.AbstractPower() {
                    {
                        this.name = HeronGuard.this.name;
                        this.ID = ModHelper.makePath("HeronGuardTurnPower");
                        this.owner = p;
                        this.type = PowerType.BUFF;
                        this.amount = isUpgraded ? 1 : 0;
                        this.img = TextureLoader.getTexture("NahidaResources/images/powers/Example84.png");
                        this.region128 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example84.png");
                        this.region48 = TextureLoader.getPowerRegion("NahidaResources/images/powers/Example32.png");
                        this.updateDescription();
                    }

                    @Override
                    public void atStartOfTurnPostDraw() {
                        this.flash();
                        AbstractCard c = new BorderSandBarrier();
                        if (this.amount > 0) {
                            c.upgrade();
                        }
                        this.addToBot(new MakeTempCardInHandAction(c, 1));
                    }

                    @Override
                    public void updateDescription() {
                        this.description = "每回合开始时，向手牌中加入 1 张边沙聚垒障。";
                    }
                });
                this.isDone = true;
            }
        });
    }
}
