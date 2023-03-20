## EN

Mod made specifically for #SP servers. It will add a Diamond Ore counter (currency on #SP servers) to the inventory and to the storage GUI. You can choose 2 modes: Show the counter on a special plate under the inventory or display the counter directly in the inventory. You can also adjust the position of all counters in the config (relative to the center).

## RU

Мод сделан специально для серверов #СП. Он добавит в инвентарь и в GUI хранилищ счетчик Алмазной Руды (валюта на серверах #СП). Вы можете выбрать 2 режима: Показывать счетчик на специальной плашке под инвентарем или отображать счетчик  прямо в инвентаре. Вы так-же можете настроить положение всех плашек и счетчиков в конфиге (относительно центра).

Если вы игрок #СП, то вы можете добавить в конфиг API токен и айди карты, что позволит вам видеть ваш баланс прямо в инвентаре. Также вы можете вывести баланс прямо в чат, воспользовавшись командой `/spwallet balance get <card id> <card token>` или просто `/spwallet balance get`, чтобы взять данные из конфига.

<details>
<summary>Как получить API токен и айди карты</summary>

1. Зайдите на сервер СП
2. Зайдите на сайт СП в "Кошелёк"
3. Выберите нужную карту и нажмите на значок "Поделиться"
4. Нажмите "Сгенерировать новый API токен" и, прочитав открывшуюся информацию, нажмите "ОК"
5. В течение минуты на СП в чат вам отправят API токен и айди карты, которые вам нужно поочерёдно скопировать, наведясь в чате и нажав ЛКМ, а после чего вставить в конфиг #SP Wallet в списке модов. 

</details>

<details>
<summary>Баланс карты стал красного цвета?</summary>

Это означает то, что при получении баланса карты от SPworlds API. Чтобы выяснить причину, используйте пункты ниже:

1. Попробуйте `/spwallet balance get`. Если ошибка связана с "ддос защитой" - просто подождите (возможно, долго).
2. Перепроверьте API токен и айди карты, вписанные в конфиг.
3. Если ничего из вышеперечисленного не помогло, то посмотрите полную ошибку через консоль/логи и напишите issue на GitHub мода или обратитесь в наш [Discord](https://discord.gg/U23C6ewP2X).

</details>

## Создатели

![This is an image](https://visage.surgeplay.com/bust/128/09196327-ac27-43f4-8f47-87859b8423be)
#### [Feytox](https://github.com/feytox) - Программист, соавтор.
![This is an image](https://visage.surgeplay.com/bust/128/cdb909c2-499f-409c-8723-27c5b6effc20)
#### [DearFox](https://github.com/DearFox) - Автор идеи и дизайнер.

## Discord

https://discord.gg/U23C6ewP2X

##### Специально для [#СП](https://spworlds.ru/)
