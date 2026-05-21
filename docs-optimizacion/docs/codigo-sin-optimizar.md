# Código sin optimizar

En esta sección se documentarán las partes del código original que presentaban oportunidades de mejora. Estas partes fueron detectadas mediante el uso de SonarQube for IDE, VisualVM y el analizador incluido en IntelliJ IDEA.

## Criterios para seleccionar código sin optimizar

Se consideró como código sin optimizar aquel que presentaba alguno de los siguientes problemas:

- Código repetido.
- Métodos demasiado extensos.
- Variables con nombres poco claros.
- Uso innecesario de estructuras complejas.
- Creación excesiva de objetos.
- Operaciones redundantes.
- Código muerto o sin uso.
- Mala separación de responsabilidades.

## Ejemplo 1: Código repetido

### Problema

El código repetido ocurre cuando una misma lógica aparece en varias partes del programa. Esto dificulta el mantenimiento, ya que cualquier cambio debe realizarse en varios lugares.

### Código sin optimizar

```java
// Aquí se colocará el código original del proyecto
```

### Consecuencia

El código duplicado aumenta el tamaño del proyecto, dificulta la lectura y puede provocar errores si una parte se modifica y otra queda sin actualizar.

---

## Ejemplo 2: Método demasiado largo

### Problema

Un método demasiado largo puede ser difícil de entender, probar y mantener. Además, suele mezclar varias responsabilidades dentro de una misma función.

### Código sin optimizar

```java
// Aquí se colocará el método original antes de optimizar
```

### Consecuencia

Cuando un método realiza demasiadas tareas, se vuelve más complicado encontrar errores y aplicar cambios.

---

## Ejemplo 3: Variables poco descriptivas

### Problema

El uso de nombres de variables poco claros dificulta la comprensión del código.

### Código sin optimizar

```java
// Ejemplo:
String a;
int x;
boolean b;
```

### Consecuencia

Aunque el programa funcione correctamente, el código pierde claridad y se vuelve más difícil de mantener.

---

## Ejemplo 4: Código muerto

### Problema

El código muerto es aquel que existe en el proyecto pero no se utiliza o nunca se ejecuta.

### Código sin optimizar

```java
// Aquí se colocará código no utilizado encontrado en el proyecto
```

### Consecuencia

El código muerto ocupa espacio, genera confusión y puede hacer que el proyecto parezca más complejo de lo necesario.