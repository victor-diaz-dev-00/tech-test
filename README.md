# GFT Technical Test - Price Service API

API REST para consultar precios aplicables de productos según fecha, producto y marca, diseñada bajo principios de alta configurabilidad, adaptabilidad al entorno y robustez en producción.

## 📋 Tabla de Contenidos

- [Requisitos](#-requisitos)
- [Tecnologías](#-tecnologías)
- [Ejecución Local](#-ejecución-local)
- [Endpoints API](#-endpoints-api)
- [Testing](#-testing)
- [🧠 Decisiones Técnicas y Arquitectura](#-decisiones-técnicas-y-arquitectura)

---

## 🔧 Requisitos

### Ejecución Local
- **Java 21** o superior
- **Maven 3.9+** (o usar el wrapper incluido `./mvnw` / `mvnw.cmd`)

---

## 🛠 Tecnologías

- **Spring Boot 3.x** - Framework principal
- **Java 21** - Lenguaje de programación
- **H2 Database** - Base de datos en memoria
- **RestAssured** - Automatización de pruebas funcionales E2E
- **SpringDoc OpenAPI** - Documentación Swagger

---

## 🚀 Ejecución Local

Para arrancar la aplicación en local:

### En sistemas basados en Unix (Linux / macOS)
```bash
./mvnw spring-boot:run
```

### En sistemas basados en Windows (PowerShell)
.\mvnw.cmd spring-boot:run

### Verificar que está funcionando
La aplicación estará disponible en: **http://localhost:8080**

---

## 📡 Endpoints API

### Consultar Precio Aplicable

**Endpoint:** `GET /prices`

**Parámetros:**
- `date` (requerido): Fecha de aplicación en formato ISO con time zone
- `productId` (requerido): Identificador del producto
- `brandId` (requerido): Identificador de la marca

**Ejemplo de petición:**
```bash
curl "http://localhost:8080/prices?date=2020-06-14T10:00:00+02:00&productId=35455&brandId=1"
```

### Documentación Swagger

Acceder a la interfaz Swagger UI:

```
URL: http://localhost:8080/swagger-ui.html
```

⚠️ Nota operativa: La documentación interactiva de la API permanece desactivada en el perfil live. En el resto de entornos se mantiene disponible para facilitar la validación y las pruebas durante el desarrollo. Esta decisión se toma por seguridad y control operativo: en live no queremos exponer la documentación ni la superficie exploratoria de la API, mientras que fuera de ese entorno sigue siendo útil para trabajar con el servicio.

## 🧪 Testing

### Ejecutar los tests unitarios

En entornos Unix:
```bash
./mvnw test
```

En entornos Windows:
```
.\mvnw.cmd test
```

Pruebas Funcionales Automatizadas (E2E)
Para validar el servicio con pruebas funcionales automáticas, el proyecto incluye una suite E2E con RestAssured que levanta la aplicación real y ejecuta las 5 casuísticas del enunciado sobre http://localhost:8080.

Para ejecutarlas de manera exclusiva:

En entornos Unix:
```
./mvnw test -Dtest=PriceApiE2ETest
```

En entornos Windows:
```
.\mvnw.cmd -Dtest=PriceApiE2ETest test```
```

Casuísticas E2E cubiertas:

✅ Precio aplicable para 2020-06-14T10:00:00Z

✅ Precio aplicable para 2020-06-14T16:00:00Z

✅ Precio aplicable para 2020-06-14T21:00:00Z

✅ Precio aplicable para 2020-06-15T10:00:00Z

✅ Conflicto por prioridad duplicada para brandId=2

✅ Validación de integridad: Error 404 cuando el **Producto** no existe en el maestro

✅ Validación de integridad: Error 404 cuando la **Marca** no existe en el maestro

✅ Validación de negocio: Error 404 cuando no existe **ninguna tarifa vigente** para la fecha consultada




## 🧠 Decisiones Técnicas y Arquitectura

Durante el desarrollo se han identificado algunos puntos que dependen del contexto funcional y de los requisitos no funcionales del sistema. Por ese motivo, las siguientes decisiones deberían validarse antes de cerrar una implementación definitiva.

### Integridad Referencial y Consistencia del Modelo de Datos

Aunque el enunciado de la prueba se centraba exclusivamente en la consulta de tarifas, se ha tomado la decisión de diseño de modelar e implementar de forma explícita las entidades **`Product`** y **`Brand`** con sus respectivas tablas en la base de datos.

* **El problema de los identificadores huérfanos:** Diseñar una tabla de precios (`PRICES`) que almacene `product_id` o `brand_id` como simples campos numéricos sin relación formal expone al sistema a inconsistencias graves (como asignar precios a productos o marcas que ni siquiera existen). Además, ante una consulta inválida, el sistema se limitaría a no devolver datos, enmascarando un error de origen.
* **Nuestra solución por consistencia:** Se han creado las tablas maestras correspondientes para garantizar la integridad referencial mediante claves foráneas (`FOREIGN KEY`). En lugar de fallar silenciosamente o devolver un conjunto vacío, el sistema está diseñado para **verificar activamente esta consistencia**. Si un consumidor intenta consultar el precio de una marca o producto inexistente en los maestros, la aplicación reacciona de forma robusta, validando la lógica de negocio y permitiendo gestionar el error de manera controlada y semántica en la API.
* **Nota de diseño sobre el alcance del modelo:** En un escenario real, la relación entre `Product` y `Brand` respondería a un contexto de negocio más complejo (por ejemplo, asumiendo que no todas las marcas comercializan todos los productos, lo que requeriría tablas de asociación intermedias o catálogos cruzados). Dado que el enunciado no especifica estas restricciones ni profundiza en el modelo maestro, se ha optado conscientemente por una relación directa y simplificada. Esto permite garantizar la integridad de los datos de la prueba de forma pragmática, evitando añadir sobreingeniería o suposiciones funcionales que excedan el propósito principal del ejercicio.

### Consulta en base de datos frente a filtrado en Java

Una de las decisiones principales es si conviene resolver la lógica de selección del precio aplicable directamente en la base de datos o recuperar un conjunto de resultados y aplicar el filtrado en Java.

La consulta en base de datos puede ser más eficiente cuando el volumen de datos es elevado, ya que evita transferir registros innecesarios a la aplicación. Sin embargo, implementar parte de la lógica en Java puede facilitar la legibilidad, el testeo y la evolución de las reglas de negocio si estas cambian con frecuencia.

Sin más información sobre el volumen esperado de datos, los índices disponibles, la frecuencia de consulta y la complejidad futura de las reglas, no hay una única opción claramente superior. Por ese motivo, se plantea una configuración mediante el parámetro `feature.query-mode`, que permite seleccionar la estrategia de consulta más adecuada para cada escenario.

* **`feature.query-mode=database-filter`**: Si el volumen de datos dentro de un rango de fechas es elevado, sería recomendable utilizar esta estrategia. Delega más trabajo en la base de datos, filtrando y ordenando los resultados hasta obtener directamente el único registro aplicable. De esta forma se reduce la cantidad de información transferida a la aplicación y se aprovecha mejor la capacidad de filtrado de la base de datos.
* **`feature.query-mode=java-filter`**: Si, por el contrario, el volumen de datos esperado es bajo y controlado, puede ser suficiente utilizar esta alternativa. Mantiene una consulta más simple y deja parte de la selección en la capa de aplicación, lo que puede facilitar la lectura, el mantenimiento y la evolución de la lógica mientras el coste de procesar los resultados siga siendo despreciable.

### Gestión de fechas, zonas horarias y divisas

También es importante definir con precisión cómo deben interpretarse las fechas. El uso de `LocalDateTime`, `OffsetDateTime` o `Instant` no es una decisión menor, especialmente si el sistema trabaja con distintas divisas.

La presencia del parámetro `currency` sugiere que pueden existir precios asociados a diferentes países o mercados. Esto implica que también podrían existir diferentes zonas horarias, por lo que conviene acordar una estrategia común para evitar ambigüedades en la interpretación de las fechas de inicio y fin de vigencia.

Como criterio general, sería recomendable normalizar internamente las fechas a un instante inequívoco y exponer en la API el formato que mejor represente el contrato funcional esperado. La decisión final debería tomarse con más contexto sobre el origen de los datos, los países soportados y la forma en la que los consumidores de la API enviarán las fechas.

### Responsabilidad sobre el dato

Otro punto relevante es conocer quién es responsable de escribir y mantener esta información. No es lo mismo trabajar en una arquitectura donde existe un único servicio propietario del dato que en un escenario donde varios sistemas pueden consultar o modificar directamente la base de datos.

Si existen múltiples puntos de escritura, guardar las fechas sin información de zona horaria puede generar interpretaciones distintas entre sistemas. Cada consumidor podría asumir que su hora local es la referencia común, introduciendo inconsistencias difíciles de detectar.

En cambio, si el acceso de escritura está centralizado en un único servicio, ese riesgo se reduce porque la normalización puede controlarse en un solo punto. A falta de más contexto, y dado que este servicio únicamente consulta la información, no se puede asegurar que sea el único punto de acceso al dato. Como hipótesis conservadora, conviene contemplar que puedan existir otros sistemas con capacidad de escritura. Por ello, sería más prudente persistir las fechas con una referencia temporal inequívoca, incluyendo zona horaria o normalización explícita a UTC.

### Swagger en `live`

La documentación interactiva de la API permanece desactivada en el perfil `live`. En el resto de entornos se mantiene disponible para facilitar la validación y las pruebas durante el desarrollo.

Esta decisión se toma por seguridad y control operativo: en `live` no queremos exponer la documentación ni la superficie exploratoria de la API, mientras que fuera de ese entorno sigue siendo útil para trabajar con el servicio.

---

## 👤 Autor

Proyecto realizado como parte del proceso de selección técnica de GFT.

---

## 📄 Licencia

Este proyecto es parte de una prueba técnica.