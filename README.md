# DSL
The Dent Scripting Language

```
types
bool -> true / false
int -> 64 bit
char -> utf 16 char
double ->  64 bit float
list -> linkedlist? of thing
string -> just a linked list of chars with a special literal format
```


```
var a = 3
a = 3.0
var b = "A"
b += "C"
print(a,b) > 3.0 AC
var n = 3
var m = 5
print(n/m) > 0
print(n: double / m) > .6
print(2 ** 5) > 32
```

```
var a = "abc"
print(a[1]) > "b"
var c = "123${a+1}456"
print(c) > "123abc1456"
```

```
var list = [1,2,"A","blue"]
print(list[0]) > 1
print(list[-1]) > "blue"
print(list[1:3]) > [2,"A"]
print(list[:-1]) > [1,2,"A"]
print(list[::2]) > [1,"A"]
list += 3
print(list) > [1,2,"A","blue",3]
list[-1] = 5
list.add(3)
list.remove(0)
```

```
print(true && true || false && !true ? "yes" : "no" ) > "yes"
print(true ^ false) > true
```

```
var a = null
var b = 5
print(a ?? 3) > 3
print(b ?? "hey") > 5
var list = [1,["a","b"],null]
print(list[1][1]) > "b"
print(list.get(1).get(1)) > "b"
print(list[2]?[0]) > null
print(list.get(2)?.get(0)) > null
```

```
var a = (3,"5")
print(a[0]) > 3
print(a[1]) > "5"
a[0] = 3 > Doesn't work
(var x, var y) = a
print(x) > 3
```

```
var list = [1,2,3,4,5]
list.map(x => x+5).filter(it < 8).reduce(a,b => a+b) > i dont know some number u can figure it out
```

```
var a = 3
if(a <= 3)
{
  print(a)
}
else
{
  print(1)
} > 3

for(var i=0;i<10;i++) print(i)

var list = [1,2,3,4,5]
for(var i in list) print(i)
```

```
fun addOne(x) => x+1
above line is equal to var addOne = x => x+1
fun addTwo(x) {
  return x+2
}
fun typed(int x) {
  return x
}
type is optional and is only checked at runtime

print(addTwo(addOne(1))) > 4
```

```
$() means interpret as bash
^val^ is used to put in values (technically a special char but who cares)
I know its a weird choice but all the normal ones have meaning in bash

var dir = $(pwd)
print(dir) > "/home/tristan" or whatever

var files = $(ls)
for(var file in files.words()) print file

for(var file in $(ls -l).lines()) print file

fun echo(x) => $(echo ^x^)

echo(3) > "3"
all bash output is a string

fun printChar() => $(echo \^)
escape ^ with backslash
printChar() > "^"

