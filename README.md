## EN

Mod made specifically for #SP servers. It will add a Diamond Ore counter (currency on #SP servers) to the inventory and to the storage GUI. You can choose 2 modes: Show the counter on a special plate under the inventory or display the counter directly in the inventory. You can also adjust the position of all counters in the config (relative to the center).

## RU

Мод сделан специально для серверов #СП. Он добавит в инвентарь и в GUI хранилищ счетчик Алмазной Руды (валюта на серверах #СП). Вы можете выбрать 2 режима: Показывать счетчик на специальной плашке под инвентарем или отображать счетчик  прямо в инвентаре. Вы так-же можете настроить положение всех плашек и счетчиков в конфиге (относительно центра).

Если вы игрок #СП, то вы можете добавить в конфиг API токен и айди карты, что позволит вам видеть ваш баланс прямо в инвентаре. Также вы можете вывести баланс прямо в чат, воспользовавшись командой `/spwallet balance get <card id> <card token>` или просто `/spwallet balance get`, чтобы взять данные из конфига.

<details>
<summary>Не работает получение баланса карты?</summary>

1. Попробуйте `/spwallet balance get`. Если ошибки не выдало - внимательно перенастройте конфиг или сбросьте его.
2. Проверьте доступность API: https://spworlds.ru/api/public/card. Если появляется окно CloudFlare или возникает ошибка 500/502, то подождите, пока сайт починят.
3. Перепроверьте API токен и айди карты, вписанные в конфиг.
4. Если ничего из вышеперечисленного не помогло, пишите issue на GitHub мода.

</details>

<details>
<summary>После обновления плашки наехали друг на друга?</summary>

Если по какой-то причине мод не обновился должным образом, просто ресетните в конфиге данные пункты
![image](https://user-images.githubusercontent.com/73577753/181618013-e353b3df-c81b-408f-9e95-4c682e8d046b.png)

</details>

## Создатели

![This is an image](https://visage.surgeplay.com/bust/128/09196327-ac27-43f4-8f47-87859b8423be)
#### [Feytox](https://github.com/feytox) - Программист, соавтор. Всё закодил и пофиксил кучу багов.
![This is an image](https://visage.surgeplay.com/bust/128/cdb909c2-499f-409c-8723-27c5b6effc20)
#### [DearFox](https://github.com/DearFox) - Автор идеи и дизайнер. Старался сделать, чтобы было красиво, и находил кучу багов.

##### Специально для [#СП](https://spworlds.ru/)
