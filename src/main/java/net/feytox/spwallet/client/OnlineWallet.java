package net.feytox.spwallet.client;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import ua.valeriishymchuk.jsp.interfaces.wallet.IWallet;
import ua.valeriishymchuk.jsp.interfaces.wallet.IWalletInfo;
import ua.valeriishymchuk.jsp.wallet.Wallet;
import ua.valeriishymchuk.jsp.wallet.WalletKey;

import javax.annotation.Nullable;
import java.util.concurrent.*;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class OnlineWallet {
    private static CompletableFuture<IWalletInfo> currentBalance = null;
    private static Integer lastBalance = null;

    public static void initCommand() {
        ClientCommandManager.DISPATCHER.register(literal("spwallet")
                .then(literal("balance")
                        .then(literal("get")
                                .executes(context -> {
                                    Integer balance = getBalance();
                                    if (balance != null) {
                                        sendFormattedText("spwallet.get.success", balance);
                                    } else {
                                        sendTranslatableText("spwallet.get.fail");
                                    }
                                    return 1;
                                })
                                .then(argument("card id", StringArgumentType.string())
                                        .then(argument("card token", StringArgumentType.greedyString())
                                                .executes(context -> {
                                                    String[] inputSplitted = context.getInput().split(" ");
                                                    String cardId = inputSplitted[inputSplitted.length-2];
                                                    String cardToken = inputSplitted[inputSplitted.length-1];
                                                    Integer balance = getBalance(cardId, cardToken);
                                                    if (balance != null) {
                                                        sendFormattedText("spwallet.get.success", balance);
                                                    } else {
                                                        sendTranslatableText("spwallet.get.fail");
                                                    }
                                                    return 1;
                                                }))))));
    }

    public static void reloadBalance() {
        currentBalance = getWalletInfo(SPwalletConfig.cardId, SPwalletConfig.cardToken);
    }

    @Nullable
    public static Integer getCurrentBalance() {
        if (currentBalance != null && currentBalance.isDone()) {
            IWalletInfo walletInfo = currentBalance.join();
            if (walletInfo != null) {
                lastBalance = walletInfo.getBalance();
            }
        }
        if (lastBalance != null) {
            return lastBalance;
        }
        return null;
    }

    @Nullable
    public static Integer getBalance(String cardId, String cardToken) {
        CompletableFuture<IWalletInfo> futureInfo = getWalletInfo(cardId, cardToken);
        if (futureInfo != null) {
            IWalletInfo walletInfo = futureInfo.join();
            if (walletInfo != null) {
                return walletInfo.getBalance();
            }
        }
        return null;
    }

    @Nullable
    public static Integer getBalance() {
        return getBalance(SPwalletConfig.cardId, SPwalletConfig.cardToken);
    }

    @Nullable
    public static CompletableFuture<IWalletInfo> getWalletInfo(String cardId, String cardToken) {
        try {
            new WalletKey(cardId, cardToken);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return null;
        }

        WalletKey key = new WalletKey(cardId, cardToken);
        IWallet wallet = new Wallet(key);

        return wallet.getWalletInfo().exceptionally(e -> {
            e.printStackTrace();
            return null;
        });

    }

    private static void sendFormattedText(String key, Object formatObj) {
        sendMessage(new LiteralText(I18n.translate(key, formatObj)));
    }

    private static void sendTranslatableText(String key) {
        sendMessage(new TranslatableText(key));
    }

    private static void sendMessage(Text message) {
        MinecraftClient.getInstance().player.sendMessage(message, false);
    }
}
