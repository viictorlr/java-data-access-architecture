# 🗄️ Hibernate & JDBC DAO Manager (Java Data Access Layer)

Este proyecto es una demostración práctica y avanzada del diseño de una **Capa de Acceso a Datos (DAL)** en Java. Su objetivo principal es resolver la persistencia de un sistema de gestión de viajes, rutas y clubes, implementando el **patrón DAO (Data Access Object)** bajo dos enfoques tecnológicos distintos: **JDBC nativo** para el control fino a bajo nivel y **Hibernate ORM** para la persistencia orientada a objetos.

---

## 🚀 Características Arquitectónicas

El núcleo del proyecto destaca por una estructura limpia, desacoplada y escalable, aplicando patrones de diseño estándar de la industria:

* **Patrón de Arquitectura DAO:** Abstracción total de la lógica de acceso a datos mediante interfaces, permitiendo cambiar la tecnología subyacente sin afectar al flujo principal de la aplicación.
* **DAOManager (Factory Pattern):** Centralización de la creación de instancias utilizando **Singleton con inicialización perezosa (Lazy Initialization)**, optimizando el uso de memoria en el ciclo de vida de la aplicación.
* **Control Extensivo de Transacciones:** Implementación de transacciones robustas con operaciones de `Commit` y `Rollback` manuales en JDBC para asegurar la atomicidad de los datos.
* **Optimización de Consultas (Lazy Loading):** Manejo eficiente de relaciones complejas (*Many-to-One* y *One-to-Many*) utilizando `Hibernate.initialize` para evitar excepciones de inicialización diferida (`LazyInitializationException`).

---

## 🛠️ Tecnologías y Herramientas

* **Lenguaje principal:** Java
* **Persistencia Avanzada / ORM:** [Hibernate Framework](https://hibernate.org/) (Mapeo de entidades, gestión del contexto de persistencia y consultas HQL).
* **Acceso Nativo:** JDBC (Java Database Connectivity) para la gestión directa de conexiones y transacciones.
* **Motor de Base de Datos:** MySQL / MariaDB (Gestionado a través de phpMyAdmin).
* **Patrones de Diseño:** DAO, Singleton, Factory Method.

---

## 📁 Estructura del Paquete DAO

El componente de acceso a datos se organiza de la siguiente manera:
* `GestorConnexions`: Centraliza y asegura la apertura y cierre controlado de conexiones físicas de JDBC.
* `SessionFactoryUtil`: Inicializador estático encargado de construir el `SessionFactory` de Hibernate una única vez para maximizar el rendimiento.
* `DAOManager`: Punto de acceso global y unificado para consumir las operaciones de las entidades (`Ruta`, `Club`, `Viatger`).
* `ClubDAOJDBC`: Implementación transaccional con SQL nativo para la entidad Club.
* `RutaDAOHibernate` & `ViatgerDAOHibernate`: Gestión avanzada de persistencia con Hibernate.
