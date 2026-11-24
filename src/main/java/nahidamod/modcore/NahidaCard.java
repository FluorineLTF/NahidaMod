package nahidamod.modcore;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.StrengthPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.cardManip.CardFlashVfx;
import nahidamod.actions.ReturnEToHandAction;
import nahidamod.actions.OrmosAction;
import nahidamod.helpers.ModHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class NahidaCard extends CustomCard {
    public static final String UI_ID = ModHelper.makePath("NahidaCard");
    private static final UIStrings ui_ID = CardCrawlGame.languagePack.getUIString(UI_ID);
    public static final String[] TEXT = ui_ID.TEXT;
    public int inactives;//设置int变量失活数
    private boolean active;//设置boolean变量active，来判断失活数是否为0
    public boolean hasUPGRADE_DESCRIPTION;
    private static InputStream fileStream;

    public NahidaCard(String id, String name, String imgPath, int cost, String description, CardType type, CardColor color, CardRarity rarity, CardTarget target, int inactives) {
        super(id, name, loadCard_png(id), cost, description, type, color, rarity, target);
        this.inactives = inactives;
        //如果卡牌有升级后描述，需要把这个设置为true
        this.hasUPGRADE_DESCRIPTION = false;
        CardStrings desc = CardCrawlGame.languagePack.getCardStrings(id);
        if (inactives > 0) {
            this.rawDescription = desc.DESCRIPTION + TEXT[0] + this.inactives + TEXT[2];
        }
        // 根据 inactives 的初始值设置 actives
        updateActive();
//        triggerOnGlowCheck();
//        try {
//            fileStream.close();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
    }

    public void updateActive() {
        active = inactives <= 0;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public void triggerWhenDrawn() {
        changeInactive();
    }

    public void upgrade() {
    }

    private static String loadCard_png (String name) {
        String finalName = name.split(":")[1];
        fileStream = NahidaCard.class.getClassLoader().getResourceAsStream("NahidaResources/images/cards/" + finalName + ".png");
        if(fileStream != null){
            return "NahidaResources/images/cards/" + finalName + ".png";
        }else {
            return "NahidaResources/images/cards/TestPicture.png";
        }
    }

    //激化方法
    public void changeInactive() {
        if (this.inactives != 0) {
            this.inactives -= 1;
            this.superFlash();
            //将E返回手牌
            this.addToBot(new ReturnEToHandAction());
            //触发喧繁之港行动
            this.addToBot(new OrmosAction());
        }
        triggerOnGlowCheck();
        updateDescription();
        updateActive();
    }

    public void addInactive() {
        if (this.inactives == -1) {
            this.inactives = 1;
        }else {
            this.inactives += 1;
        }
        this.flashVfx = new CardFlashVfx(this, Color.RED.cpy(), true);
        triggerOnGlowCheck();
        updateDescription();
        updateActive();
    }


    public void updateDescription() {
        CardStrings desc = CardCrawlGame.languagePack.getCardStrings(cardID);

        if (this.upgraded && this.hasUPGRADE_DESCRIPTION) {
            this.rawDescription = desc.UPGRADE_DESCRIPTION;
        } else {
            this.rawDescription = desc.DESCRIPTION;
        }
        this.initializeDescription();

        if (inactives > 0) {
            this.rawDescription = this.rawDescription + TEXT[0] + inactives + TEXT[2];
            this.initializeDescription();
        }
    }


/*
    //充能方法的实现
    public AbstractCard makeStatEquivalentCopy(boolean isRecharge) {
        AbstractCard c = super.makeStatEquivalentCopy();

        //只有这个是true以及卡牌是NahidaCard同时为真才会返回失活卡牌
        if (c instanceof NahidaCard && isRecharge) {
            NahidaCard n = (NahidaCard) c;
            n.inactives = 1;
            n.updateDescription();
            n.updateActive();
            return n;
        }
        return c;
    }
*/

    @Override//如果失活，则canUse直接输出false
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
//        if (!active) {
        if (inactives > 0) {
            this.initializeDescription();
            this.cantUseMessage = TEXT[1];
//            this.isGlowing = true;
            return false;
        } else {
            if (this.type == CardType.STATUS && this.costForTurn < -1 && !AbstractDungeon.player.hasRelic("Medical Kit")) {
                return false;
            } else if (this.type == CardType.CURSE && this.costForTurn < -1 && !AbstractDungeon.player.hasRelic("Blue Candle")) {
                return false;
            } else {
                return this.cardPlayable(m) && this.hasEnoughEnergy();
            }
        }
    }

    //makeCopy重写
//    @Override
//    public AbstractCard makeCopy() {
//        try {
//            NahidaCard card = this.getClass().newInstance();
//            if (this.addInactive && (card.inactives < this.inactives)) {
//                card.inactives = this.inactives;
//            }else if (!this.addInactive && (card.inactives > this.inactives)){
//                card.inactives = this.inactives;
//            }
//            card.updateActive();
//            card.updateDescription();
//            return card;
//        } catch (IllegalAccessException | InstantiationException var2) {
//            throw new RuntimeException("BaseMod failed to auto-generate makeCopy for card: " + this.cardID);
//        }
//    }


    @Override
    public AbstractCard makeStatEquivalentCopy() {
        NahidaCard card = (NahidaCard) super.makeStatEquivalentCopy();
        card.inactives = this.inactives;
        card.updateDescription();
        card.triggerOnGlowCheck();
        return card;
    }

    @Override
    public void triggerOnGlowCheck() {
        super.triggerOnGlowCheck();
        if (AbstractDungeon.currMapNode == null || !(AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT)) {
            return;
        }
        if (this.inactives > 0 && !this.isGlowing){
            this.isGlowing = true;
        }
        if (this.inactives > 0) {
            this.glowColor = Color.RED.cpy();
        }else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }

    }

    @Override
    public void update() {
        super.update();
        triggerOnGlowCheck();
    }

}