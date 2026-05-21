# Herramientas utilizadas

Para realizar el análisis del proyecto se utilizaron tres herramientas principales: **SonarQube for IDE**, **VisualVM** y el **analizador incluido en IntelliJ IDEA**.

## SonarQube for IDE

SonarQube for IDE es un complemento que permite analizar el código fuente directamente desde el entorno de desarrollo. Su función principal es detectar problemas de calidad, seguridad y mantenibilidad.

Durante el análisis del proyecto, esta herramienta se utilizó para identificar:

- Código duplicado.
- Métodos demasiado largos.
- Variables con nombres poco descriptivos.
- Código innecesario o difícil de mantener.
- Posibles malas prácticas de programación.

## VisualVM

VisualVM es una herramienta de monitoreo para aplicaciones Java. Permite observar el comportamiento de una aplicación mientras se está ejecutando.

En este proyecto se utilizó para analizar:

- Consumo de memoria.
- Uso de CPU.
- Carga de clases.
- Comportamiento general de la aplicación.
- Posibles cuellos de botella durante la ejecución.

VisualVM fue importante porque permitió observar el comportamiento real de la aplicación y no solo el código escrito.

## Analizador incluido en IntelliJ IDEA

IntelliJ IDEA incluye herramientas internas de inspección de código. Estas permiten detectar errores, advertencias y oportunidades de mejora.

Se utilizó para revisar:

- Imports no utilizados.
- Variables innecesarias.
- Métodos que podían simplificarse.
- Código redundante.
- Recomendaciones de refactorización.
- Posibles errores de estilo o estructura.

## Justificación del uso de herramientas

El uso de estas herramientas permitió realizar un análisis más completo, combinando revisión estática del código con análisis en tiempo de ejecución. De esta manera, fue posible detectar problemas tanto en la estructura del código como en el comportamiento del programa.