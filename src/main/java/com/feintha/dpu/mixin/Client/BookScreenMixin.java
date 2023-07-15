package com.feintha.dpu.mixin.Client;

import com.feintha.dpu.iface.IBookContentsMixin;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BookScreen.class)
public abstract class BookScreenMixin {
    @Shadow private BookScreen.Contents contents;
    @Shadow private int pageIndex;

    @Shadow protected abstract void goToNextPage();

    @Shadow @Final public static Identifier BOOK_TEXTURE;
    @Unique
    ItemStack thisBook = ItemStack.EMPTY;
    @Inject(method="<init>(Lnet/minecraft/client/gui/screen/ingame/BookScreen$Contents;)V", at=@At("TAIL"))
    private void initMixin(BookScreen.Contents pageProvider, CallbackInfo ci){
        try {
            IBookContentsMixin iM = (IBookContentsMixin) pageProvider;
            thisBook = iM.datapackUtils$getStack();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    JsonObject cachedPageJson = null;

    @Inject(method="goToNextPage", at=@At("TAIL"))
    void goToNextPageMixin(CallbackInfo ci) {
        recacheJson();
    }
    @Inject(method="goToPreviousPage", at=@At("TAIL"))
    void goToPrevPageMixin(CallbackInfo ci) {
        recacheJson();
    }
    @Inject(method="setPage", at=@At("TAIL"))
    void goToPrevPageMixin(int index, CallbackInfoReturnable<Boolean> cir) {
        recacheJson();
    }

    private void recacheJson() {
        NbtList nbtList = thisBook.getOrCreateNbt().getList("pages", NbtElement.STRING_TYPE).copy();
        cachedPageJson = JsonHelper.deserialize(nbtList.getString(pageIndex));
        mBookBgTexture = Identifier.tryParse(JsonHelper.getString(cachedPageJson, "background", BOOK_TEXTURE.toString()));
        String bgS = JsonHelper.getString(cachedPageJson, "foreground", null);
        if (bgS != null) {
            mBookFgTexture = Identifier.tryParse(bgS);
        } else {
            mBookFgTexture = null;
        }
        hasCustomPage = mBookBgTexture != BOOK_TEXTURE || bgS != null;
    }
    @Unique
    boolean hasCustomPage = false;
    @Unique
    public Identifier mBookBgTexture = null;
    public Identifier mBookFgTexture = null;
    @Inject(method="render", at=@At("HEAD"))
    void mixinRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){}
    @WrapWithCondition(method="render", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    boolean redirectBookTextureMixin(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height){
        if (cachedPageJson == null) {recacheJson();}
        if (hasCustomPage) {
            Identifier id = new Identifier(mBookBgTexture.getNamespace(), (mBookBgTexture.getPath().endsWith(".png") ? mBookBgTexture.getPath() : mBookBgTexture.getPath() + ".png"));
            instance.drawTexture(id, x, y, u, v, width, height);
            return false;
        }
        return true;
    }
    @Inject(method="render", at=@At("TAIL"))
    void renderEndMixin(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){

        if (hasCustomPage) {

            if (mBookFgTexture != null) {
                Identifier fgId = new Identifier(mBookFgTexture.getNamespace(), (mBookFgTexture.getPath().endsWith(".png") ? mBookFgTexture.getPath() : mBookFgTexture.getPath() + ".png"));
                int i = (((BookScreen)(Object)this).width - 192) / 2;
                context.drawTexture(fgId, i, 2, 0, 0, 192, 192);
            }
        }
    }
    @Mixin(BookScreen.WritableBookContents.class)
    public static class WritableBookContentMixin implements IBookContentsMixin{
        public ItemStack stack = ItemStack.EMPTY;
        @Inject(method="<init>", at=@At("TAIL"))
        private void getPagesMixin(ItemStack stack, CallbackInfo ci){
            this.stack = stack;
        }

        @Override
        public ItemStack datapackUtils$getStack() {
            return stack;
        }
    }
    @Mixin(BookScreen.WrittenBookContents.class)
    public static class WrittenBookContentMixin implements IBookContentsMixin {
        public ItemStack stack = ItemStack.EMPTY;
        @Inject(method="<init>", at=@At("TAIL"))
        private void getPagesMixin(ItemStack stack, CallbackInfo ci){
            this.stack = stack;
        }

        @Override
        public ItemStack datapackUtils$getStack() {
            return stack;
        }
    }

}
