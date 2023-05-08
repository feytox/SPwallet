package ru.feytox.spwallet.spapi;

import blue.endless.jankson.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import ru.feytox.spwallet.SPwalletClient;
import ru.feytox.spwallet.config.ModConfig;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class OnlineWallet {
    private static CompletableFuture<Integer> currentBalance = null;
    public static boolean isBadResponse = false;
    public static boolean isBadAPI = false;

    public static void initCommand() {
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> dispatcher.register(literal("spwallet")
                .then(literal("balance")
                        .then(literal("get")
                                .executes(context -> {
                                    CompletableFuture.runAsync(() -> {
                                        ModConfig config = ModConfig.get();
                                        Integer balance = getBalance(config.cardId, config.cardToken);
                                        if (balance != null) {
                                            sendFormattedText("spwallet.get.success", balance);
                                        } else {
                                            if (isBadAPI) sendTranslatableText("spwallet.get.bad_api");
                                            else sendTranslatableText("spwallet.get.fail");
                                        }
                                    });
                                    return 1;
                                })
                                .then(argument("card id", StringArgumentType.string())
                                        .then(argument("card token", StringArgumentType.greedyString())
                                                .executes(context -> {
                                                    CompletableFuture.runAsync(() -> {
                                                        String[] inputSplitted = context.getInput().split(" ");
                                                        String cardId = inputSplitted[inputSplitted.length-2];
                                                        String cardToken = inputSplitted[inputSplitted.length-1];
                                                        Integer balance = getBalance(cardId, cardToken);
                                                        if (balance != null) {
                                                            sendFormattedText("spwallet.get.success", balance);
                                                        } else {
                                                            if (isBadAPI) sendTranslatableText("spwallet.get.bad_api");
                                                            else sendTranslatableText("spwallet.get.fail");
                                                        }
                                                    });

                                                    return 1;
                                                }))))))));
    }

    public static void reloadBalance() {
        currentBalance = CompletableFuture.supplyAsync(() -> getBalance(ModConfig.get().cardId, ModConfig.get().cardToken));
    }

    public static int getCurrentBalance2() {
        Integer balance = getCurrentBalance();
        return balance != null ? balance : -621;
    }

    @Nullable
    private static Integer getCurrentBalance() {
        Integer balance = null;
        ModConfig config = ModConfig.get();

        if (currentBalance != null && currentBalance.isDone()) {
            try {
                balance = currentBalance.get();
            } catch (ExecutionException | InterruptedException ignored) {
            }

            if (balance != null) {
                config.cachedBalance = balance;
                ModConfig.save();
            }
        }

        return config.cachedBalance;
    }

    @Nullable
    private static Integer getBalance(String cardId, String cardToken) {
        String response = getCardData(cardId, cardToken);
        try {
            JsonElement root = JsonParser.parseString(response);
            Integer result = root.getAsJsonObject().get("balance").getAsInt();
            isBadResponse = false;
            isBadAPI = false;
            return result;
        } catch (Exception e) {
            SPwalletClient.LOGGER.error("Ошибка чтения ответа от SPworlds API.");
            if (response.contains("<html><head><meta name=\"robots\" content=\"noindex, noarchive\" /><style>.gorizontal-vertikal")) {
                SPwalletClient.LOGGER.error("Проблема со стороны SPworlds API - срабатывает 'защита от ддос'. Подождите, пока защита сама собой деактивируется");
                isBadAPI = true;
            } else {
                SPwalletClient.LOGGER.error(response);
                isBadAPI = false;
            }
            isBadResponse = true;
            return null;
        }
    }

    private static String getCardData(String cardId, String cardToken) {
        String notEncoded = cardId + ":" + cardToken;
        String headerValue = Base64.getEncoder().encodeToString(notEncoded.getBytes());

        return request("Bearer " + headerValue);
    }

    private static String request(String headerValue) {
        List<String> responseList = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://spworlds.ru/api/public/card"))
                .setHeader("Authorization", headerValue)
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseList::add)
                .join();

        return String.join("", responseList);
    }

    private static void sendFormattedText(String key, Object formatObj) {
        sendMessage(Text.literal(I18n.translate(key, formatObj)));
    }

    private static void sendTranslatableText(String key) {
        sendMessage(Text.translatable(key));
    }

    private static void sendMessage(Text message) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        player.sendMessage(message, false);
    }
}
