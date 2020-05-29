# DSL
The Dent Scripting Language

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
list.map(x -> x+5).filter(it < 8).reduce(a,b -> a+b) > i dont know some number u can figure it out
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
fun addTwo(x) {
  return x+2
}

print(addTwo(addOne(1))) > 4
```

