### Сборка:  
`mvn package`

### Запуск:  
`java -jar bank-1.jar`

### База:
Файловая h2 в `${user.home}/database.mv.db` (inmemory для тестов)  
Пересоздается при каждом запуске.  
При старте проекта в базе автоматически создаются 1000 аккаунтов с идентификаторами от 1 до 1000 и с балансом 100000.

### Юнит тесты:  
Покрытие 94% без игнорирования моделей и т.п.

Во всех тестах используется база h2 inmemory.

#### Тестирование контроллеров:

[1) AccountControllerSuccessTest](https://github.com/YRashid/bank/blob/master/src/test/java/ru/rashid/bank/controller/AccountControllerSuccessTest.java)
1) успешное создание аккаунта

[2) AccountControllerValidationTest](https://github.com/YRashid/bank/blob/master/src/test/java/ru/rashid/bank/controller/AccountControllerValidationTest.java) 
1) аккаунт с существующим id нельзя создать 
2) баланс не может быть меньше 0

[3) TransferMoneyControllerSuccessTest](https://github.com/YRashid/bank/blob/master/src/test/java/ru/rashid/bank/controller/TransferMoneyControllerSuccessTest.java)
1) Перевод между двумя аккаунтами
2) Последовательный перевод по кругу между тремя аккаунтами
3) Допустимость нулевого баланса после перевода

[4) TransferMoneyControllerValidationTest](https://github.com/YRashid/bank/blob/master/src/test/java/ru/rashid/bank/controller/TransferMoneyControllerValidationTest.java)
1) Недостаточно денег на балансе для перевода
2) Аккаунт не существует
3) Перевод самому себе запрещен
4) Сумма перевода должна быть положительной
5) Сумма перевода не может быть равна 0

[5) FullLifeCycleControllerTest](https://github.com/YRashid/bank/blob/master/src/test/java/ru/rashid/bank/controller/FullLifeCycleControllerTest.java)
1) Пошаговое создание аккаунтов, перевод между созданными аккаунтами и проверка баланса

#### Тестирование TransferMoneyService:  
[1) ConcurrentTransfersTest](https://github.com/YRashid/bank/blob/master/src/test/java/ru/rashid/bank/service/ConcurrentTransfersTest.java)
1) Проверка корректности работы TransferMoneyService в многопоточной среде. 101 поток переводит со счета A на B и еще 101 поток переводит со счета B на A.


### Нагрузочное тестирование:
Скачать [jmeter-5.3](jmeter.apache.org)  
#### Тест forward_and_backward_100_rps
Выполнить в консоли:   
`/.../apache-jmeter-5.3/bin/jmeter -n -t /.../bank/jmeter_test/forward_and_backward_100_rps.jmx`  
Тест [forward_and_backward.jmx](https://github.com/YRashid/bank/blob/master/jmeter_test/forward_and_backward_100_rps.jmx) переводит с аккаунта 14 на аккаунт 24 и обратно. Баланс в итоге может отличаться от изначального, т.к. количество переводов в одну сторону может получиться больше чем в обратную.  
Тест дает нагрузку примерно 100 запросов в секунду в течении 60 секунд.  
В итоге получим, что выполнено ~6066 запросов и 0% ошибок.  

#### Тест forward_and_backward_balance_correctness
Выполнить в консоли:   
`/.../apache-jmeter-5.3/bin/jmeter -n -t /.../bank/jmeter_test/forward_and_backward_balance_correctness.jmx`  
Тест [forward_and_backward.jmx](https://github.com/YRashid/bank/blob/master/jmeter_test/forward_and_backward_balance_correctness.jmx) переводит с аккаунта 19 на аккаунт 29 и обратно. Запускается 10000 запросов в одну сторону и 10000 запросов в другую.  
В итоге получим, что выполнено 20000 запросов за ~18c. и 0% ошибок.
Баланс пользователей 19 и 29 остался таким же, каким был до теста.

### Описание api
Swagger доступен по адресу: `http://localhost:8080/swagger-ui.html#/`
Порт: 8080