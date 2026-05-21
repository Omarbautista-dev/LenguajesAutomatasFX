# Análisis realizado

El análisis del proyecto se realizó utilizando herramientas de inspección estática y monitoreo en tiempo de ejecución. El objetivo fue identificar problemas de calidad, rendimiento y mantenibilidad.

## Análisis con SonarQube for IDE

SonarQube for IDE permitió revisar el código fuente directamente desde el IDE. La herramienta marcó advertencias relacionadas con posibles problemas de mantenimiento.

### Aspectos revisados

- Complejidad del código.
- Código duplicado.
- Nombres de variables y métodos.
- Fragmentos innecesarios.
- Posibles malas prácticas.

### Resultado esperado

A partir del análisis, se identificaron fragmentos de código que podían simplificarse o reorganizarse para mejorar la calidad general del proyecto.

---

## Análisis con IntelliJ IDEA

El analizador incluido en IntelliJ IDEA permitió encontrar advertencias dentro del código del proyecto.

### Aspectos revisados

- Imports innecesarios.
- Variables no utilizadas.
- Métodos que podían simplificarse.
- Recomendaciones de estilo.
- Posibles errores de estructura.

### Resultado esperado

El análisis ayudó a limpiar el código y mejorar su presentación, eliminando elementos innecesarios.

---

## Análisis con VisualVM

VisualVM se utilizó para observar el comportamiento de la aplicación durante la ejecución.

### Aspectos revisados

- Uso de memoria.
- Uso de CPU.
- Comportamiento de la aplicación al ejecutar sus funciones.
- Carga de clases.
- Posibles problemas de rendimiento.

### Resultado esperado

VisualVM permitió verificar si la aplicación consumía recursos de manera excesiva y si existían comportamientos anormales durante la ejecución.

---

## Interpretación general

El análisis permitió comprobar que la optimización no solo consiste en hacer que el programa funcione, sino en lograr que el código sea más claro, eficiente y fácil de mantener.