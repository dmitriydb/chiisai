**Компактный сериализатор Java-классов**

## Пример использования
### Сериализация
Запись в файл:
```java
MyCoolObject myCoolObject = new MyCoolObject();
new Chiisai()
    .shrink(myCoolObject)
    .andWriteTo(new PrintStream("somefile"));
```

В массив байт:
```java
MyCoolObject myCoolObject = new MyCoolObject();
byte[] bytes = new Chiisai()
    .shrink(myCoolObject)
    .toBytes();
```

### Десериализация
Из файла:
```java
...
MyCoolObject myCoolObjectAfterDeserializing = 
(MyCoolObject) new Chiisai().
    readAndUnshrink(new FileInputStream("..."), MyCoolObject.class);
```

Вариант для тестирования:
```java
...
MyCoolObject myCoolObjectAfterDeserializing = (MyCoolObject) new Chiisai()
    .shrink(myCoolObject)
    .andUnshrink();
```

## Формат сериализации

Сериализованный объект ::= {Сериализованное поле1 ... Сериализованное полеN}

Сериализованное поле ::= (Сериализованный объект | Значение поля)

Значение поля ::= Дескриптор (4 бита) | Длина содержимого N (8 бит) | Содержимое (N бит)

Дескриптор ::= INT | LONG | ... | OBJECT | ARRAY | NULL | OBJECT_REFERENCE...
