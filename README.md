# Tienda_Barrio

Aplicación de escritorio desarrollada en Java con Maven, interfaz gráfica Swing y conexión a MySQL para administrar productos y categorías de una tienda.

## Requisitos
- Java JDK.
- Apache NetBeans o un entorno compatible con Maven.
- XAMPP con MySQL.

## Pasos para ejecutar el proyecto
1. Descargar o clonar este repositorio.
2. Abrir XAMPP e iniciar el servicio MySQL.
3. Abrir phpMyAdmin e importar el archivo `tienda.sql` para crear la base de datos y las tablas, si está incluido en el repositorio.
4. Verificar la configuración de conexión a MySQL en la clase `Conexion.java`.
5. Abrir el proyecto en NetBeans y esperar a que Maven descargue las dependencias.
6. Ejecutar la clase `Tienda_Barrio.java` para abrir la aplicación.

## Funcionalidades
- Consultar productos y categorías.
- Registrar productos.
- Editar productos existentes.
- Eliminar productos.
- Guardar los cambios en MySQL.

## Persistencia
Los datos se almacenan en la base de datos MySQL llamada `tienda`.

Ejercicio 6. Prueba de persistencia y reflexión

a) Registro, actualización, eliminación y persistencia

Para comprobar la persistencia, se deben registrar al menos cinco productos desde la interfaz, actualizar uno y eliminar otro. Después se cierra la aplicación y se vuelve a ejecutar para verificar que los cambios se mantengan.

La persistencia se consigue porque los datos se guardan en MySQL y no únicamente en la memoria de la aplicación. La evidencia correspondiente es una captura de la ventana después de reiniciar el programa, mostrando los productos y los cambios realizados.

b) Verificación de los datos en MySQL

Se puede comprobar la información almacenada ejecutando la siguiente consulta:

SELECT
    p.id_producto,
    p.nombre,
    p.precio,
    p.cantidad,
    c.nombre AS categoria
FROM producto p
INNER JOIN categoria c
    ON p.id_categoria = c.id_categoria
ORDER BY p.id_producto;

Los resultados deben coincidir con los datos que muestra la ventana de inventario. La consulta permite verificar el nombre, precio, cantidad y categoría de cada producto.

c) Eliminación de una categoría con productos asociados

Cuando se intenta eliminar una categoría que tiene productos asociados, MySQL impide la operación si la clave foránea utiliza el comportamiento predeterminado de restricción (RESTRICT o NO ACTION). Esto evita que queden productos relacionados con una categoría inexistente.

En el modelo Java, cada objeto Producto contiene un objeto Categoria, que representa la categoría a la que pertenece. Esta relación refleja la dependencia entre ambos elementos. La clave foránea en MySQL mantiene la integridad de esa relación en la base de datos.

El resultado puede variar si la clave foránea se configuró con una regla como ON DELETE CASCADE, que permite eliminar también los productos asociados.

d) Ventajas de la separación por paquetes y cambio a PostgreSQL

Separar el proyecto en los paquetes conexion, model, dao y vista permite organizar las responsabilidades y facilitar el mantenimiento. conexion administra el acceso a la base de datos; model representa las entidades; dao contiene las operaciones sobre los datos; y vista se encarga de la interfaz gráfica. Esta estructura facilita localizar errores y modificar una parte del sistema sin afectar innecesariamente las demás.

Si se cambiara MySQL por PostgreSQL, sería necesario agregar el controlador JDBC de PostgreSQL, modificar la URL de conexión y ajustar las credenciales. También podrían requerirse cambios en algunas consultas SQL o tipos de datos. Los modelos y buena parte de la interfaz podrían conservarse si se mantiene la misma estructura de datos y las mismas operaciones.

Estructura del proyecto

Tienda_Barrio/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/
                └── tienda/
                    ├── Tienda_Barrio.java
                    ├── conexion/
                    │   └── Conexion.java
                    ├── model/
                    │   ├── Categoria.java
                    │   └── Producto.java
                    ├── dao/
                    │   ├── CategoriaDAO.java
                    │   └── ProductoDAO.java
                    └── vista/
                        └── VentanaInventario.java
