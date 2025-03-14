# EasyDate
## Overview
A simple java library to numerize and find dates for natural language.

## Maven 
Add following to pom.xml
```
<dependency>
    <groupId>io.github.nicechester</groupId>
    <artifactId>easy-date</artifactId>
    <version>0.4</version>
</dependency>
```

## Usage
### Numerizer
Supported words
```
System.out.println(Numerizer.suppoertedWords());
```
results
```
[one, two, three, four, five, six, seven, eight, nine, ten, eleven, twelve, thirteen, fourteen, fifteen, sixteen, seventeen, eighteen, nineteen, twenty, first, second, third, fourth, fifth, sixth, seventh, eighth, ninth, tenth, eleventh, twelfth, thirteenth, fourteenth, fifteenth, sixteenth, seventeenth, eighteenth, nineteenth, twentieth, twenty first, twenty second, twenty third, twenty fourth, twenty fifth, twenty sixth, twenty seventh, twenty eighth, twenty ninth, thirtieth, thirty first]
```

Numerize text
```
String source = "Do you have tickets for March tenth, June twenty first, 2025-12-25 for two adults and 1 kid?";
String numerized = Numerizer.numerize(source);
System.out.println(source);
System.out.println(numerized);
```
results
```
Do you have tickets for March tenth, June twenty first, 2025-12-25 for two adults and 1 kid?
Do you have tickets for March 10, June 21, 2025-12-25 for 2 adults and 1 kid?
```

### Find Date
Parse text dates
```
String[] ds = {"New year day", "Martin Luther King Junior day", "Thanksgiving", "Christmas",
        "March tenth", "February second", "December twenty fifth", "Fourth July",
        "today", "tomorrow", "yesterday", "next week", "next month"};
for (String d : ds) {
    System.out.println(d + ": " + FindDate.parse(d));
}
```
results
```
New year day: 2025-01-01
Martin Luther King Junior day: 2025-01-20
Thanksgiving: 2025-11-20
Christmas: 2025-12-25
March tenth: 2025-03-10
February second: 2025-02-02
December twenty fifth: 2025-12-25
Fourth July: 2025-07-04
today: 2025-03-14
tomorrow: 2025-03-15
yesterday: 2025-03-13
next week: 2025-03-21
next month: 2025-04-14
```

Datize
```
String source = "Do you have tix on tomorrow, april fifth, june twenty fifth, fourth of july and christmas with two adults and five kids?";
System.out.println(FindDate.datize(source));
```
results
```
Do you have tix on 2025-03-15, 2025-04-05, 2025-06-25, 2025-07-04 and 2025-12-25 with 2 adults and 5 kids?
```

Find dates
```
String source = "Do you have tickets for March tenth, June twenty first, first January, Fourth of July,"
        + "Thanksgiving, Christmas day, 2025-12-31, today and tomorrow for two adults and 1 kid?";
List<LocalDate> dates = FindDate.findDates(source);
System.out.println(dates);
```
results (`List<LocalDate>`)
```
[2025-03-10, 2025-06-21, 2025-01-01, 2025-07-04, 2025-11-20, 2025-12-25, 2025-12-31, 2025-03-14, 2025-03-15]
```

Supported holidays
```
System.out.println(FindDate.supportedHolidays());
```
results
```
[juneteenth day, thanksgiving day, presidents day, juneteenth, columbus day, martin luther king junior day, christmas, veterans day, martin luther king day, memorial day, independence day, new year day, thanksgiving, christmas day, labor day]
```
