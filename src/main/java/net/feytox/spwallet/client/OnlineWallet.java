package net.feytox.spwallet.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import javax.annotation.Nullable;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.literal;

public class OnlineWallet {
    // 61954fa9-6b80-4621-b19c-2df6e9b98b10
    // 4wczPNLpxWvpBS9hQO+ApQkZn4nsb3xA
    public static Integer currentBalance = null;

    public static void initCommand() {
        ClientCommandManager.DISPATCHER.register(literal("spwallet")
                .then(literal("balance")
                        .then(literal("get")
                                .executes(context -> {
                                    Integer balance = getBalance(SPwalletConfig.cardId, SPwalletConfig.cardToken);
                                    if (balance != null) {
                                        sendFormattedText("spwallet.get.success", balance);
                                    } else {
                                        sendTranslatableText("spwallet.get.fail");
                                    }
                                    return 1;
                                })
                                .then(argument("card id", StringArgumentType.greedyString())
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
        currentBalance = getBalance(SPwalletConfig.cardId, SPwalletConfig.cardToken);
    }

    @Nullable
    public static Integer getBalance(String cardId, String cardToken) {
        try {
            String response = getCardData(cardId, cardToken);
            JsonElement root = JsonParser.parseString(response);
            return root.getAsJsonObject().get("balance").getAsInt();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getCardData(String cardId, String cardToken) {
        String notEncoded = cardId + ":" + cardToken;
        String headerValue = Base64.getEncoder().encodeToString(notEncoded.getBytes());

        return request("https://spworlds.ru/api/public/card", "Authorization", "Bearer " + headerValue);
    }

    private static String request(String url, String headerName, String headerValue) {
        List<String> responseList = new ArrayList<>();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .setHeader(headerName, headerValue)
                .build();
        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseList::add)
                .join();

        return String.join("", responseList);
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
