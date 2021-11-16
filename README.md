# MultiAgentSystems

## Задание 1
16.11.2021

Есть n агентов. Каждый из них загадывает какое-то число. Нужно посчитать среднее арифметическое (топология не задана).

## Решение задания 1
### Описание решения
Идея алгоритма заключается в следующем: нам дан неориентированный
связный граф(вершины - это агенты, ребра - это каналы связи между агентами).
Вначале агенты генерируют случайные числа. Каждому агенту известны свои соседи, свой id и загаданное число.
Также агентам известно общее число агентов.

Начинается процесс голосования. Результатом голосования является выбор главного агента, который будет
запускать процесс подсчета среднего арифметического и отдавать его в центр по окончании процесса.

После того, как голосование окончено и выбран главный агент, начинается сама фаза подсчета. Главный
агент отправляет всем своим соседям сообщение с перформативом REQUEST, которое сигнализирует о том,
чтобы агент начал процесс вычисления и результат передал тому агенту, который вызвал его.
Те агенты, которые получили это сообщение, делают тоже самое и так начинается ловинообразная передача
этих сообщений по всему графу. Каждый агент, который сделал такие вызову к своим соседям, ожидает от них
ответа. Ответом может быть 2 вида сообщений: либо с перформативом INFORM, которое содержит пару числе (sum, n),
либо же REFUSE, которое сигнализирует о том, что агент заблокирован другим агентом и не будет ничего
передавать.

По окончании данного алгоритма главный агент получит пару чисел (sum, n), на основе которой он получит результат
sum / n.

#### Голосование
У каждого агента имеется состояние. Оно бывает типа "-1", "0", "1" и "2". Изначально все агенты имеют состояние "-1"
Для того, чтобы начать процесс вычисления, агенты должны договориться с тем, кто будет главным инициатором вычислений и будет отправлять результат в центр. Для этого проводится голосование.

Каждый агент хранит пару значений (degV, id), где degV - это число соседей. Главным агентом будем выбирать того агента, для которого эти значения максимальны(очевидно, что коллизий быть не может)

На 1-м такте каждый агент посылает своим соседям пару (degV, id). Принимая сообщения от других соседей, он сравнивает их со своей парой (degV, id), и если его пара меньше чем та, которую ему отослали, то он сохраняет максимальную пару (degV_max, id_max), котороую он получил от соседей.

Условно говоря, на 1-м такте происходит следующее:
1. Отправить всем соседям (degV, id)
2. Создать пару (degV_max, id_max) = max ((degV, id), (degV1, id1), (degV2, id2), ..., (degVk, idk)), где (degVk, idk) - это пара, полученная от соседа k

Далее, на каждом следующем такте каждый агент делает тоже самое, что и на 1-м, только в качестве (degV, id) выступает обновленное значение максимальной полученной пары на предыдущих тактах, т.е. обновленная (degV_max, id_max)
1. Отправить всем соседям (degV_max, id_max)
2. Обновить пару (degV_max, id_max) = max ((degV_max, id_max), (degV1, id1), (degV2, id2), ..., (degVk, idk)), где (degVk, idk) - это пара, полученная от соседа k

Теоретически, агент с максимальной парой (degV, id) передаст через своих соседей и далее по графу до самого последнего агента за количество ребер, которые стоят между ним и самой дальней от него вершиной. Очевидно, что это число не больше числа вершин.
Поэтому если провести ровно n тактов, то в графе каждая вершина сохранит значение максимальной пары и будет определен главный агент просто по сопоставлению со своей изначальной парой значений, т.е. isMain = (degV, id) == (degV_max, id_max)

После окончания всех тактов голосование оканчивается, все агенты переходят в состояние "0".

#### Процесс вычисления
Главный агент отсылает сообщения типа REQUEST всем своим соседям. После этого меняет свое состояние на "1".
Каждый из других агентов в состоянии "0" делает следующее:
- ничего не делает, пока не получит хотя бы 1 сообщение
- если сообщение получено и оно типа REQUEST, то агент запоминает того, от кого он его получил и переходит в состояние "1". При этом он точно также посылает всем соседям REQUEST.

Поведение агента в состоянии "1":
- ожидает ответа от всех своих соседей, которым послал сообщение REQUEST. Причем ожидает именно сообщений типа INFORM или REFUSE.
- если сосед передал сообщение с перформативом INFORM, то агент парсит это сообщение(оно имеет вид пары (req_sum, req_n)), и прибавляет к своей (sum, n) = (sum + req_sum, n + req_n)
- если получено сообщение с перформативом REFUSE, то считается, что сосед ответил, но пара чисел (sum, n) не обновляется
- если получает сообщение от других агентов типа REQUEST, то мгновенно отвечает им REFUSE.
- после того, как все соседи ответили сообщениями с перформативами INFORM или REFUSE, агент к полученной паре (sum, n) добавляет свое загаданное число и 1, т.е. (sum, n) = (sum + number, n + 1). После этого отправляет эту пару в сообщении типа INFORM своему агенту (если агент является главным, то результат (sum, n) отправляется в центр) и переходит в состояние "2"

Поведение агента в состоянии "2":
- ничего не делает, пока не получит хотя бы 1 сообщение
- если сообщение получено, то вне зависимости от типа сообщения, агент отправляет сразу же ответ REFUSE

Формально можно доказать то, что алгоритм:
1. Конечен
2. Корректен

### Оценка решения
m - число ребер (число линий связи)

n - число вершин (агентов)

Оценки приведены для 2-х случаев(лучший и худший), в зависимости от топологии.

| Случай | Память | Кол-во сообщений локально | Кол-во операций | Время(такты) | Кол-во ребер | Кол-во сообщений в центр |
| ------ | ------ | ------------------------- | --------------- | ------------ | ------------ | ------------------------ |
| Лучший | O(m)   | O(m * n + m)              |   4m            | O(1)         |не меньше n-1 | 1
| Худший | O(m)   | O(m * n + m)              |   4m            | 3*n+3 = O(n) |не меньше n-1 | 1

## Инструкция к коду
### Замечания по установке и запуску проекта с JADE

[Инструкция к установке](https://www.math.spbu.ru/user/gran/PP/JADE.pdf)

В приведенном решении есть файлы [build.gradle](https://github.com/gitmskhl/MultiAgentSystems/blob/main/Task1/build.gradle), в котором прописаны определенные версии для самого Gradle. Они могут быть не совместимы с установленными у Вас версиями SDK. Поэтому этот файл, если будет ошибка запуска, следует обновить под доступные у Вас версии.

### Замечения к коду
В файле [App.java](https://github.com/gitmskhl/MultiAgentSystems/blob/main/Task1/src/main/java/ru/spbu/mas/App.java) есть несколько методов инициализации, при выборе каждого из которых следует внимательно следить за параметром [APP.AGENT_NUMBERS](https://github.com/gitmskhl/MultiAgentSystems/blob/8182327325ce9ab84fa31071287ef665cc2a33a6/Task1/src/main/java/ru/spbu/mas/App.java#L12): 
- В случае выбора этого метода [initStar](https://github.com/gitmskhl/MultiAgentSystems/blob/8182327325ce9ab84fa31071287ef665cc2a33a6/Task1/src/main/java/ru/spbu/mas/App.java#L27) параметр APP.AGENT_NUMBERS может быть любым
- В случае выбора этого метода [initTop1](https://github.com/gitmskhl/MultiAgentSystems/blob/8182327325ce9ab84fa31071287ef665cc2a33a6/Task1/src/main/java/ru/spbu/mas/App.java#L42) параметр APP.AGENT_NUMBERS должен быть равен 5
- В случае выбора этого метода [initTop2](https://github.com/gitmskhl/MultiAgentSystems/blob/8182327325ce9ab84fa31071287ef665cc2a33a6/Task1/src/main/java/ru/spbu/mas/App.java#L60) параметр APP.AGENT_NUMBERS должен быть равен 11

Все эти тонкости связаны просто с тем, что инициализация сделана достаточно коряво. Ее можно сделать нормальной, но это к заданию отношения не имеет.