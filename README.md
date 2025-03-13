# EasyDate
## Overview
A simple java library to numerize and find dates for natural language.

## Maven 
Add following to pom.xml
```
<dependency>
    <groupId>io.github.nicechester</groupId>
    <artifactId>easy-date</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>
```
You may need to add snapshot repo to settings.xml as following
```
<repository>
  <name>Central Portal Snapshots</name>
  <id>central-portal-snapshots</id>
  <url>https://central.sonatype.com/repository/maven-snapshots/</url>
  <releases>
      <enabled>false</enabled>
  </releases>
  <snapshots>
      <enabled>true</enabled>
  </snapshots>
</repository>
```

## Usage
### Numerizer
Supported words
```
System.out.println(Numerizer.suppoerted_words());
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
String[] ds = {"March tenth", "February second", "December twenty fifth", "Fourth July",
        "today", "tomorrow", "yesterday", "next week", "next month"};
for (String d : ds) {
    System.out.println(FindDate.parse(d));
}
```
results
```
2025-03-10
2025-02-02
2025-12-25
2025-07-04
2025-03-12
2025-03-13
2025-03-11
2025-03-19
2025-04-12
```

Datize
```
String source = "Do you have tickets for March tenth, June twenty first, January 1, 2025-12-25 for two adults and 1 kid?";
System.out.println(FindDate.datize(source));
```
results
```
Do you have tickets for 2025-03-10, 2025-06-21, 2025-01-01, 2025-12-25 for 2 adults and 1 kid?
```

Find dates
```
String source = "Do you have tickets for March tenth, June twenty first, January 1, 2025-12-25 for two adults and 1 kid?";
System.out.println(FindDate.findDates(source));
```
results (`List<LocalDate>`)
```
[2025-03-10, 2025-06-21, 2025-01-01, 2025-12-25]
```

Formatter
```
LocalDate date = LocalDate.parse("March 10", FindDate.getFormatter());
System.out.println(date);
```
results
```
2025-03-10
```